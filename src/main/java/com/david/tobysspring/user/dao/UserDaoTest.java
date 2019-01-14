package com.david.tobysspring.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import com.david.tobysspring.user.domain.User;

public class UserDaoTest {
	private UserDao dao;
	
	@Before
	public void setUp() {
		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
        this.dao = context.getBean("userDao", UserDao.class);
	}
	
    @Test
    public void addAndGet() throws SQLException {        
		User user1 = new User("gyumee", "박성철", "springno1");
		User user2 = new User("leegw700", "이길원", "springno2");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
        
        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));
		
		User userGet1 = dao.get(user1.getId());
		assertThat(userGet1.getName(), is(user1.getName()));
		assertThat(userGet1.getPassword(), is(user1.getPassword()));
		
		User userGet2 = dao.get(user2.getId());
		assertThat(userGet2.getName(), is(user2.getName()));
		assertThat(userGet2.getPassword(), is(user2.getPassword()));
	}
    
    @Test
    public void count() throws SQLException {
    	User user1 = new User("gyumee", "박성철", "springno1");
    	User user2 = new User("leegw700", "이길원", "springno2");
    	User user3 = new User("bumjin", "박범진", "springno3");

    	dao.deleteAll();
    	assertThat(dao.getCount(), is(0));
    	
    	dao.add(user1);
    	assertThat(dao.getCount(), is(1)); 
    	
    	dao.add(user2);
    	assertThat(dao.getCount(), is(2)); 
    	
    	dao.add(user3);
    	assertThat(dao.getCount(), is(3)); 
    }
    
    @Test(expected=EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
    	dao.deleteAll();
    	assertThat(dao.getCount(), is(0));
    	
    	dao.get("unknown_id");
    }
}
