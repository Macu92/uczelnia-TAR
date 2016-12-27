package com.example.guestbook.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.guestbook.model.Event;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class DeleteEventServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("eventId");
		Event event = ObjectifyService.ofy().load().type(Event.class).id(Long.valueOf(id)).now();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.
		if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
			ObjectifyService.ofy().delete().entity(event).now();
			resp.sendRedirect("/");
		} else {
			resp.sendRedirect("viewEvent.jsp?eventId=" + id);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
