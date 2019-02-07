package com.david.tobysspring.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.david.tobysspring.user.dao.UserDao;
import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

public class UserServiceImpl implements UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	private UserDao userDao;
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void add(User user) {
		if (user.getLvl() == null) {
			user.setLvl(Lvl.BASIC);
		}
		userDao.add(user);
	}
	
	@Override
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
	
	protected void upgradeLvl(User user) {
		user.upgradeLvl();
		userDao.update(user);
		sendUpgradeEMail(user);
	}

	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLvl().name() + "로 업그레이드되었습니다.");
		
		this.mailSender.send(mailMessage);
	}
}
