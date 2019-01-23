package com.david.tobysspring.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.david.tobysspring.user.domain.User;

public class UserDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user) throws SQLException {
		this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES (?, ?, ?)", 
				user.getId(), user.getName(), user.getPassword());
	}
	
	public User get(String id) throws SQLException {
		return this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", new Object[] {id},
			new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					return user;
				}
			}
		);
	}
	
	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("DELETE FROM users WHERE 1=1");
	}
	
	public int getCount() throws SQLException{
		return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id", new Object[] {},
			new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					return user;
				}
			}
		);
	}	
}
