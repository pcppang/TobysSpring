package com.david.tobysspring.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.david.tobysspring.user.domain.User;

public class UserDaoTest {
    // JUnit에게 테스트 메소드임을 알려줌
    @Test
    // JUnit 테스트 메소드는 반드시 public으로 선언되어야 함
    public void addAndGet() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("whiteship");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));	
	}
    
    public static void main(String[] args) {
		JUnitCore.main("com.david.tobysspring.user.dao.UserDaoTest");
	}
}
