package com.david.tobysspring.user.service;

import com.david.tobysspring.user.domain.User;

public interface UserService {
	void add(User user);
	void upgradeLvls();
}