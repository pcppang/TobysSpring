package com.david.tobysspring.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class DaoFactory {
	@Bean // 오브젝트 생성을 담담하는 IoC용 메소드라는 표시
	public UserDao userDao() {
		return new UserDao(connectionMaker());
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
