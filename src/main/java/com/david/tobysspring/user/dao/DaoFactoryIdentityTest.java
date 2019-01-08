package com.david.tobysspring.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DaoFactoryIdentityTest {
	public static void main(String[] args) {
		DaoFactory factory = new DaoFactory();
		
		UserDao dao1 = factory.userDao();
		UserDao dao2 = factory.userDao();
		
		System.out.println("ObjectFactory");
		System.out.println("ObjectFactory dao1: " + dao1);
		System.out.println("ObjectFactory dao2: " + dao2);
		System.out.println("dao1 == dao2 : " + (dao1==dao2));
		
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		UserDao dao3 = context.getBean("userDao", UserDao.class);
		UserDao dao4 = context.getBean("userDao", UserDao.class);
		
		System.out.println("ApplicationContext");
		System.out.println("ApplicationContext dao3: " + dao3);
		System.out.println("ApplicationContext dao4: " + dao4);
		System.out.println("dao3 == dao4 : " + (dao3==dao4));
	}
}
