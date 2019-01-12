package com.david.tobysspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.david.tobysspring.user.domain.User;

public class UserDao {
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES (?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("ID"));
		user.setName(rs.getString("NAME"));
		user.setPassword(rs.getString("PASSWORD"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}
	
	public void deleteAll() throws SQLException {
	    Connection c = dataSource.getConnection();

	    PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE 1=1");
	    ps.executeUpdate();

	    ps.close();
	    c.close();
	}
	
	public int getCount() throws SQLException {
	    Connection c = dataSource.getConnection();

	    PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users");

	    ResultSet rs = ps.executeQuery();
	    rs.next();

	    int count = rs.getInt(1);

	    rs.close();
	    ps.close();
	    c.close();

	    return count;
	}
}
