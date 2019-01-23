package com.david.tobysspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.david.tobysspring.user.domain.User;

public class UserDao {
	DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}
	
	public void add(final User user) throws SQLException {
		this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES (?, ?, ?)", 
				user.getId(), user.getName(), user.getPassword());
	}
	
	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE ID = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		User user = null;
		
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("ID"));
			user.setName(rs.getString("NAME"));
			user.setPassword(rs.getString("PASSWORD"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if (user == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return user;
	}
	
	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("DELETE FROM users WHERE 1=1");
	}
	
	public int getCount() throws SQLException{
		return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
	}	
}
