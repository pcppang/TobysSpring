package com.david.tobysspring.user.service;

import static com.david.tobysspring.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.david.tobysspring.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.david.tobysspring.user.dao.UserDao;
import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired UserService userService;
	@Autowired UserDao userDao;
	@Autowired PlatformTransactionManager transactionManager;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("bumjin", "박범진", "p1", Lvl.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("joytouch", "강명성", "p2", Lvl.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("erwins", "신승한", "p3", Lvl.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
			new User("madnite1", "이상호", "p4", Lvl.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("green", "오민", "p5", Lvl.GOLD, 100, 100)
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
		
		userService.add(userWithLvl);
		userService.add(userWithoutLvl);
		
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
		userDao.deleteAll();
		
		for (User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLvls();
		
		checkLvlUpgraded(users.get(0), false);
		checkLvlUpgraded(users.get(1), true);
		checkLvlUpgraded(users.get(2), false);
		checkLvlUpgraded(users.get(3), true);
		checkLvlUpgraded(users.get(4), false);
	}
	
	/**
	 * 작업 도중 예외 발생 시 이전 작업이 롤백되는지에 대한 테스트
	 */
	static class TestUserService extends UserService {
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
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(userDao);
		testUserService.setTransactionManager(transactionManager);
		
		userDao.deleteAll();
		for  (User user : users) {
			userDao.add(user);
		}
		
		try {
			testUserService.upgradeLvls();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLvlUpgraded(users.get(1), false);
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
