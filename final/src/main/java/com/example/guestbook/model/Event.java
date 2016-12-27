package com.example.guestbook.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Event {

	@Id
	public Long id;

	public String author_email;
	public String author_id;
	public String title;
	public String content;
	public Set<AssignedUser> assignees;
	public int defaultNotificationHours;
	@Index
	public Date date;

	public Event() {
		this.assignees = new HashSet<AssignedUser>();
	}

	public Event(String author_email, String author_id, String title, String content, Date date, int hours) {
		this();
		this.author_email = author_email;
		this.author_id = author_id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.defaultNotificationHours = hours;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor_email() {
		return author_email;
	}

	public void setAuthor_email(String author_email) {
		this.author_email = author_email;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<AssignedUser> getAssignees() {
		return assignees;
	}

	public void setAssignees(Set<AssignedUser> assignees) {
		this.assignees = assignees;
	}

	public void addAssigne(User user, Integer hours) {
		if(hours!=null){
			this.assignees.add(new AssignedUser(hours, user));	
		}else{
			this.assignees.add(new AssignedUser(this.defaultNotificationHours, user));
		}
		
	}

	public void deleteAssignee(User user) {
		if (isAssignee(user)) {
			AssignedUser toDelete = null;
			for (AssignedUser u : assignees) {
				if (u.getUser().equals(user)) {
					toDelete = u;
				}
			}
			assignees.remove(toDelete);
		}
	}

	public boolean isAssignee(User user) {
		for (AssignedUser u : assignees) {
			if (u.getUser().equals(user)) {
				return true;
			}
		}
		return false;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
