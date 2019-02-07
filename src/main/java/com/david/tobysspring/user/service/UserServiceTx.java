package com.david.tobysspring.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.david.tobysspring.user.domain.User;

public class UserServiceTx implements UserService {
	UserService userService;
	PlatformTransactionManager transactionManager;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public void add(User user) {
		this.userService.add(user);
	}

	@Override
	public void upgradeLvls() {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			this.userService.upgradeLvls();
			
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}
}
