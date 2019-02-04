package com.david.tobysspring.user.service;

import java.util.List;

import com.david.tobysspring.user.dao.UserDao;
import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

public class UserService {
	UserDao userDao;
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void add(User user) {
		if (user.getLvl() == null) {
			user.setLvl(Lvl.BASIC);
		}
		userDao.add(user);
	}
	
	public void upgradeLvls() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLvl(user)) {
				upgradeLvl(user);
			}
		}
	}

	private boolean canUpgradeLvl(User user) {
		Lvl currentLvl = user.getLvl();
		switch(currentLvl) {
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level : " + currentLvl);
		}
	}
	
	private void upgradeLvl(User user) {
		user.upgradeLvl();
		userDao.update(user);
	}
}
