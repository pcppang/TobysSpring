package com.david.tobysspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import com.david.tobysspring.user.domain.User;

public class UserDao {
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES (?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE ID = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		// User는 null로 초기
		User user = null;
		
		// 쿼리 결과가 있을 경우만 User 오브젝트 생성 후 값을 넣어준다.
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("ID"));
			user.setName(rs.getString("NAME"));
			user.setPassword(rs.getString("PASSWORD"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		// 결과가 없다면 User는 계속 null일 것이다.
		if (user == null) {
			// 예외를 던져준다.
			throw new EmptyResultDataAccessException(1);
		}
		
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
