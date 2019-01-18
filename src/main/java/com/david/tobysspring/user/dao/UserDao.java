package com.david.tobysspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import com.david.tobysspring.user.domain.User;

public class UserDao {
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		
		try { 
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}			
			}
		}		
	}

	public void add(final User user) throws SQLException {
		class AddAllStatement implements StatementStrategy {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES (?, ?, ?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		}
		
		StatementStrategy st = new AddAllStatement();
		jdbcContextWithStatementStrategy(st);
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
		class DeleteAllStatement implements StatementStrategy {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE 1=1");
				return ps;
			}
		}
		
		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(st);
	}
	
	public int getCount() throws SQLException{
		Connection c = null;		
		PreparedStatement ps = null;		
		ResultSet rs = null;
		
		int count = 0;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("SELECT COUNT(*) FROM users");		
			
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}

			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return count;
	}

}
