package com.david.tobysspring.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.david.tobysspring.user.domain.Lvl;
import com.david.tobysspring.user.domain.User;

public class UserTest {
	User user;
	
	@Before
	public void setUp() {
		user = new User();
	}
	
	@Test()
	public void upgradeLvl() {
		Lvl[] lvls = Lvl.values();
		for (Lvl lvl : lvls) {
			if (lvl.nextLvl() == null) {
				continue;
			}
			
			user.setLvl(lvl);
			user.upgradeLvl();
			assertThat(user.getLvl(), is(lvl.nextLvl()));
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void cannotUpgradeLvl() {
		Lvl[] lvls = Lvl.values();
		for (Lvl lvl : lvls) {
			if (lvl.nextLvl() != null) {
				continue;
			}
			
			user.setLvl(lvl);
			user.upgradeLvl();
		}
	}
}
