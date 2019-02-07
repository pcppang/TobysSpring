package com.david.tobysspring.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

public class UserDaoJdbc implements UserDao {
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			user.setLvl(Lvl.valueOf(rs.getInt("lvl")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		}
	};
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void add(final User user) throws DataAccessException {
		this.jdbcTemplate.update("INSERT INTO users(id, name, password, email, lvl, login, recommend) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)", 
				user.getId(), user.getName(), user.getPassword(), user.getEmail(),
				user.getLvl().intValue(), user.getLogin(), user.getRecommend());
	}
	
	@Override
	public User get(String id) throws DataAccessException {
		return this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", 
			new Object[] {id}, this.userMapper
		);
	}
	
	@Override
	public void deleteAll() throws DataAccessException {
		this.jdbcTemplate.update("DELETE FROM users WHERE 1=1");
	}
	
	@Override
	public int getCount() throws DataAccessException {
		return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
	}

	@Override
	public List<User> getAll() throws DataAccessException {
		return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id", 
			new Object[] {}, this.userMapper
		);
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(
			"UPDATE users SET name = ?, password = ?, email = ?, lvl = ?, "
			+ "login = ?, recommend = ? where id = ?", 
			user.getName(), user.getPassword(), user.getEmail(), user.getLvl().intValue(), 
			user.getLogin(), user.getRecommend(), user.getId()
		);
	}	
}
