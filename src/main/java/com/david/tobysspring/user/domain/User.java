package com.david.tobysspring.user.domain;

public class User {
	String id;
	String name;
	String password;
	Lvl lvl;
	int login;
	int recommend;
	
	public User() {
	}

	public User(String id, String name, String password, Lvl lvl, int login, int recommend) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.lvl = lvl;
		this.login = login;
		this.recommend = recommend;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Lvl getLvl() {
		return lvl;
	}

	public void setLvl(Lvl lvl) {
		this.lvl = lvl;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	
	public void upgradeLvl() {
		Lvl nextLvl = this.lvl.nextLvl();
		if (nextLvl == null) {
			throw new IllegalStateException(this.lvl + "은 업그레이드가 불가능합니다.");
		} else {
			this.lvl = nextLvl;
		}
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", lvl=" + lvl + ", login=" + login
				+ ", recommend=" + recommend + "]";
	}
}
