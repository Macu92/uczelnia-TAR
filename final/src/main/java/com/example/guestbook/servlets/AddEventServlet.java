package com.example.guestbook.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.guestbook.model.Event;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class AddEventServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.

		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String date = req.getParameter("date");
		String hours = req.getParameter("hours");
		DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		if (user == null) {
			resp.sendRedirect("/");
		}

		Event newEvent;
		try {
			newEvent = new Event(user.getEmail(), user.getUserId(), title, content, format.parse(date),Integer.valueOf(hours));
			ObjectifyService.ofy().save().entity(newEvent).now();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		List<Event> greetings = ObjectifyService.ofy().load().type(Event.class).list(); 
			
		resp.sendRedirect("/");
	}
	
}
