package com.david.tobysspring.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.david.tobysspring.user.domain.User;

public abstract class UserDao {
	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = getConnection();
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES (?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = getConnection();
		
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
	
	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		NUserDao dao = new NUserDao();
		
		User user = new User();
		user.setId("whiteship");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + "조회 성공");
	}
}

// Oracle을 사용하는 N사의 UserDao
class NUserDao extends UserDao {
	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "spring", "book");
	
		return c;
	}
}

// MySql을 사용하는 D사의 UserDao
class DUserDao extends UserDao {
	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring", "book");
	
		return c;
	}
}
