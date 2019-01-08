package com.david.tobysspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.david.tobysspring.user.domain.User;

public class UserDao {
	// 초기에 설정하면 사용 중에는 바뀌지 않는 읽기 전용 인스턴스 변수
	private ConnectionMaker connectionMaker;
	
	// 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수
	// 심각한 문제가 발생한다.
	private Connection c;
    private User user;

	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = connectionMaker.makeConnection();
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES (?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
		this.c = connectionMaker.makeConnection();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		this.user = new User();
        this.user.setId(rs.getString("ID"));
        this.user.setName(rs.getString("NAME"));
        this.user.setPassword(rs.getString("PASSWORD"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}
}
