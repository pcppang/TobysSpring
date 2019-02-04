package com.david.tobysspring.user.domain;

public enum Lvl {
	GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);
	
	private final int value;
	private final Lvl next;
	
	Lvl(int value, Lvl next) {
		this.value = value;
		this.next = next;
	}
	
	public int intValue() {
		return value;
	}
	
	public Lvl nextLvl() {
		return next;
	}
	
	public static Lvl valueOf(int value) {
		switch(value) {
			case 1: return BASIC;
			case 2: return SILVER;
			case 3: return GOLD;
			default: throw new AssertionError("Unknown value : " + value);
		}
	}
}
