package com.example.guestbook.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.guestbook.model.AssignedUser;
import com.example.guestbook.model.Event;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class UserSignOutByAdminServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("eventId");
		String userId = req.getParameter("userId");
		Event event = ObjectifyService.ofy().load().type(Event.class).id(Long.valueOf(id)).now();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.
		if (userService.isUserLoggedIn() && user != null && userService.isUserAdmin()) {
			for(AssignedUser au : event.assignees){
						if(au.getUser().getUserId().equals(userId)){
							event.deleteAssignee(au.getUser());
						}
			}
		}
		ObjectifyService.ofy().save().entity(event).now();
		event = ObjectifyService.ofy().load().type(Event.class).id(Long.valueOf(id)).now();
		resp.sendRedirect("viewEvent.jsp?eventId=" + id);
	}

}
