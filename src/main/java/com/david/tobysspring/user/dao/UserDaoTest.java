package com.david.tobysspring.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.david.tobysspring.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@DirtiesContext
public class UserDaoTest {
	@Autowired
	UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {		
		DataSource dataSource = new SingleConnectionDataSource(
				"jdbc:oracle:thin:@localhost:1521:xe", "springbook_test", "test", true);
		dao.setDataSource(dataSource);
        this.user1 = new User("gyumee", "박성철", "springno1");
    	this.user2 = new User("leegw700", "이길원", "springno2");
    	this.user3 = new User("bumjin", "박범진", "springno3");
	}
	
    @Test
    public void addAndGet() throws SQLException {        
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
