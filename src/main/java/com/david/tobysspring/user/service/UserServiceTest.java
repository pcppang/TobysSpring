package com.david.tobysspring.user.service;

import static com.david.tobysspring.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.david.tobysspring.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.david.tobysspring.user.dao.UserDao;
import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired UserServiceImpl userServiceImpl;
	@Autowired UserDao userDao;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender mailSender;
	@Autowired ApplicationContext context;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("bumjin", "박범진", "p1", "bumjin@test.com", Lvl.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("joytouch", "강명성", "p2", "joytouch@test.com", Lvl.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("erwins", "신승한", "p3", "erwins@test.com", Lvl.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
			new User("madnite1", "이상호", "p4", "madnite1@test.com", Lvl.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("green", "오민", "p5", "green@test.com", Lvl.GOLD, 100, 100)
		);
	}

	/**
	 * 처음 사용자를 추가할 경우 등급이 BASIC 등급으로 조정되는지에 대한 테스트
	 */
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLvl = users.get(4);
		User userWithoutLvl = users.get(0);
		userWithoutLvl.setLvl(null);
		
		userServiceImpl.add(userWithLvl);
		userServiceImpl.add(userWithoutLvl);
		
		User userWithLvlRead = userDao.get(userWithLvl.getId());
		User userWithoutLvlRead = userDao.get(userWithoutLvl.getId());
		
		checkLvl(userWithLvlRead, Lvl.GOLD);
		checkLvl(userWithoutLvlRead, Lvl.BASIC);
	}
	
	/**
	 * 레벨 업그레이드 확인 테스트
	 */
	@Test
	public void upgradeLvls() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLvls();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLvl(updated.get(0), "joytouch", Lvl.SILVER);
		checkUserAndLvl(updated.get(1), "madnite1", Lvl.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}
	
	private void checkUserAndLvl(User updated, String expectedId, Lvl expectedLvl) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLvl(), is(expectedLvl));
	}
	
	@Test
	public void mockUpgradeLvls() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLvls();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLvl(), is(Lvl.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLvl(), is(Lvl.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}

	/**
	 * 작업 도중 예외 발생 시 이전 작업이 롤백되는지에 대한 테스트
	 */
	static class TestUserService extends UserServiceImpl {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		@Override
		protected void upgradeLvl(User user) {
			if (user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			super.upgradeLvl(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	@Test
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(userDao);
		testUserService.setMailSender(mailSender);
		
		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();
		
		userDao.deleteAll();
		for  (User user : users) {
			userDao.add(user);
		}
		
		try {
			txUserService.upgradeLvls();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLvlUpgraded(users.get(1), false);
	}
	
	/**
	 * 메일 발송 내부 클래스
	 */
	static class DummyMailSender implements MailSender {
		@Override
		public void send(SimpleMailMessage simpleMessage) throws MailException {
		}

		@Override
		public void send(SimpleMailMessage... simpleMessages) throws MailException {
		}
	}
	
	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();
		
		public List<String> getRequests() {
			return requests;
		}
		
		@Override
		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		@Override
		public void send(SimpleMailMessage... mailMessage) throws MailException {
		}
	}
	
	/**
	 * UserDao 테스트 대역
	 */
	static class MockUserDao implements UserDao {
		private List<User> users;
		private List<User> updated = new ArrayList<User>();
		
		private MockUserDao() {
		}
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated() {
			return this.updated;
		}
		
		// Stub
		@Override
		public List<User> getAll() {
			return this.users;
		}
		
		// Mock Object
		@Override
		public void update(User user) {
			updated.add(user);
		}

		@Override
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 레벨 확인 메서드
	 */
	private void checkLvl(User user, Lvl expectedLvl) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLvl(), is(expectedLvl));
	}
	
	private void checkLvlUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLvl(), is(user.getLvl().nextLvl()));
		} else {
			assertThat(userUpdate.getLvl(), is(user.getLvl()));
		}
	}
}
