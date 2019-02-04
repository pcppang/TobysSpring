package com.david.tobysspring.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.david.tobysspring.user.dao.UserDao;
import com.david.tobysspring.user.dao.UserDaoJdbc;
import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	@Autowired UserService userService;
	@Autowired UserDao userDao;
	@Autowired private DataSource dataSource;
	
	List<User> users;
	
	@Before
	public void setUp() {
		this.dataSource = new SingleConnectionDataSource("jdbc:oracle:thin:@localhost:1521:xe", "springbook_test", "test", true);
		((UserDaoJdbc) userDao).setDataSource(dataSource);
		
		users = Arrays.asList(
			new User("bumjin", "박범진", "p1", Lvl.BASIC, 49, 0),
			new User("joytouch", "강명성", "p2", Lvl.BASIC, 50, 0),
			new User("erwins", "신승한", "p3", Lvl.SILVER, 60, 29),
			new User("madnite1", "이상호", "p4", Lvl.SILVER, 60, 30),
			new User("green", "오민", "p5", Lvl.GOLD, 100, 100)
		);
	}
	
	@Test
	public void upgradeLvls() {
		userDao.deleteAll();
		
		for (User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLvls();
		
		checkLvl(users.get(0), Lvl.BASIC);
		checkLvl(users.get(1), Lvl.SILVER);
		checkLvl(users.get(2), Lvl.SILVER);
		checkLvl(users.get(3), Lvl.GOLD);
		checkLvl(users.get(4), Lvl.GOLD);
	}
	
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

	private void checkLvl(User user, Lvl expectedLvl) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLvl(), is(expectedLvl));
	}
}
