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
	
	public void upgradeLvls() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			Boolean changed = null;
			
			if (user.getLvl() == Lvl.BASIC && user.getLogin() >= 50) {
				user.setLvl(Lvl.SILVER);
				changed = true;
			} else if (user.getLvl() == Lvl.SILVER && user.getRecommend() >= 30) {
				user.setLvl(Lvl.GOLD);
				changed = true;
			} else if (user.getLvl() == Lvl.GOLD) {
				changed = false;
			} else {
				changed = false;
			}
			
			if (changed) {
				userDao.update(user);
			}
		}
	}

	public void add(User user) {
		if (user.getLvl() == null) {
			user.setLvl(Lvl.BASIC);
		}
		userDao.add(user);
	}
}
