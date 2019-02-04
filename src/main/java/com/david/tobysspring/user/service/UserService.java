package com.david.tobysspring.user.service;

import java.util.List;

import com.david.tobysspring.user.dao.UserDao;
import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

public class UserService {
	UserDao userDao;
	
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
			case BASIC: return (user.getLogin() >= 50);
			case SILVER: return (user.getRecommend() >= 30);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level : " + currentLvl);
		}
	}
	
	private void upgradeLvl(User user) {
		if (user.getLvl() == Lvl.BASIC) {
			user.setLvl(Lvl.SILVER);
		} else if (user.getLvl() == Lvl.SILVER) {
			user.setLvl(Lvl.GOLD);
		}
		
		userDao.update(user);
	}
}
