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
package com.example.guestbook;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

/**
 * Form Handling Servlet Most of the action for this sample is in
 * webapp/guestbook.jsp, which displays the {@link Greeting}'s. This servlet has
 * one method {@link #doPost(<#HttpServletRequest req#>, <#HttpServletResponse
 * resp#>)} which takes the form data and saves it.
 */
public class SignGuestbookServlet extends HttpServlet {

	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Greeting greeting;

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // Find out who the user is.

		String guestbookName = req.getParameter("guestbookName");
		String content = req.getParameter("content");
		if (user != null) {
			greeting = new Greeting(guestbookName, content, user.getUserId(), user.getEmail());
		} else {
			greeting = new Greeting(guestbookName, content);
		}

		// Use Objectify to save the greeting and now() is used to make the call
		// synchronously as we
		// will immediately get a new page using redirect and we want the data
		// to be present.
		ObjectifyService.ofy().save().entity(greeting).now();

		resp.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Greeting greeting;
		UserService userService = UserServiceFactory.getUserService();
		List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class) 
				.order("-date") // Most recent first - date is indexed.
				.list();

		String guestbookName = req.getParameter("guestbookName");
		Key<Guestbook> theBook = Key.create(Guestbook.class, guestbookName);
		if(userService.isUserLoggedIn()&&userService.isUserAdmin()){
			 String id = req.getParameter("greet_id");
			 greeting =  ObjectifyService.ofy().load().key(Key.create(theBook,Greeting.class, greetings.get(0).id)).now();
			 ObjectifyService.ofy().delete().entity(greeting);
		}
		resp.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Result<Greeting> greeting;
		UserService userService = UserServiceFactory.getUserService();
		
		if(userService.isUserLoggedIn()&&userService.isUserAdmin()){
			 String id = req.getParameter("greet_id");
			 greeting = ObjectifyService.ofy().load().type(Greeting.class).id(Long.valueOf(id));
			 ObjectifyService.ofy().delete().entity(greeting);
		}
	}
}

// [END all]
