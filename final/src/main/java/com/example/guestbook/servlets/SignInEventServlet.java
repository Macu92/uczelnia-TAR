/**
 * Copyright 2014-2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//[START all]
package com.example.guestbook.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.guestbook.Greeting;
import com.example.guestbook.model.Event;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Form Handling Servlet Most of the action for this sample is in
 * webapp/guestbook.jsp, which displays the {@link Greeting}'s. This servlet has
 * one method {@link #doPost(<#HttpServletRequest req#>, <#HttpServletResponse
 * resp#>)} which takes the form data and saves it.
 */
public class SignInEventServlet extends HttpServlet {

	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("eventId");
		Event event = ObjectifyService.ofy().load().type(Event.class).id(Long.valueOf(id)).now();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.
		if (userService.isUserLoggedIn() && user != null && !event.isAssignee(user)) {
			event.addAssigne(user,24);
		}else if(userService.isUserLoggedIn() && user != null && event.isAssignee(user)){
			event.deleteAssignee(user);
		}
		ObjectifyService.ofy().save().entity(event).now();
		event = ObjectifyService.ofy().load().type(Event.class).id(Long.valueOf(id)).now();
		resp.sendRedirect("viewEvent.jsp?eventId=" + id);
	}
}

// [END all]
