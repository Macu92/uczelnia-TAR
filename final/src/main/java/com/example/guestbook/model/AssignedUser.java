package com.example.guestbook.model;

import com.google.appengine.api.users.User;

public class AssignedUser {

	int hours;
	User user;
	boolean notofiacted;
	
	public AssignedUser() {
	}
	public AssignedUser(int hours, User user) {
		super();
		this.hours = hours;
		this.user = user;
		this.notofiacted=false;
	}
	
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isNotofiacted() {
		return notofiacted;
	}
	public void setNotofiacted(boolean notofiacted) {
		this.notofiacted = notofiacted;
	}	
}
