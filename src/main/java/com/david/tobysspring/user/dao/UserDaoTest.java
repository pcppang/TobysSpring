package com.david.tobysspring.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import com.david.tobysspring.user.domain.User;


public class UserDaoTest {
	@Autowired
	private UserDao dao;
	
	@Autowired
	private DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {		
		dao = new UserDaoJdbc();
		
		this.dataSource = new SingleConnectionDataSource("jdbc:oracle:thin:@localhost:1521:xe", "springbook_test", "test", true);
		((UserDaoJdbc) dao).setDataSource(dataSource);
		
        this.user1 = new User("gyumee", "박성철", "springno1");
    	this.user2 = new User("leegw700", "이길원", "springno2");
    	this.user3 = new User("bumjin", "박범진", "springno3");
	}
	
    @Test
    public void addAndGet() {        
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
    public void count() {
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
    public void getUserFailure() {
    	dao.deleteAll();
    	assertThat(dao.getCount(), is(0));
    	
    	dao.get("unknown_id");
    }
    
    @Test(expected=DuplicateKeyException.class)
    public void duplicateKey() {
    	dao.deleteAll();
    	
    	dao.add(user1);
    	dao.add(user1);
    }
    
    @Test(expected=DuplicateKeyException.class)
    public void sqlExceptionTranslate() {
    	dao.deleteAll();
    	
    	try {
    		dao.add(user1);
    		dao.add(user1);
    	} catch (DuplicateKeyException ex) {
    		SQLException sqlEx = (SQLException)ex.getRootCause();
    		SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
    		
    		throw set.translate(null, null, sqlEx);
    	}
    }
    
    @Test
    public void getAllUsers() {
    	dao.deleteAll();
    	List<User> users0 = dao.getAll();
    	assertThat(users0.size(), is(0));
    	
    	dao.add(user1); // gyumee
    	List<User> users1 = dao.getAll();
    	assertThat(users1.size(), is(1));
    	checkSameUser(user1, users1.get(0));
    	
    	dao.add(user2); // leegw700
    	List<User> users2 = dao.getAll();
    	assertThat(users2.size(), is(2));
    	checkSameUser(user1, users2.get(0));
    	checkSameUser(user2, users2.get(1));
    	
    	dao.add(user3); // bumjin
    	List<User> users3 = dao.getAll();
    	assertThat(users3.size(), is(3));
    	checkSameUser(user3, users3.get(0));
    	checkSameUser(user1, users3.get(1));
    	checkSameUser(user2, users3.get(2));
    }
    
    private void checkSameUser(User user1, User user2) {
    	assertThat(user1.getId(), is(user2.getId()));
    	assertThat(user1.getName(), is(user2.getName()));
    	assertThat(user1.getPassword(), is(user2.getPassword()));
    }
}
