package com.example.guestbook.servlets;
import java.util.logging.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.guestbook.model.AssignedUser;
import com.example.guestbook.model.Event;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.Period;
import com.googlecode.objectify.ObjectifyService;

public class MailCronServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MailCronServlet.class.getName());
	  
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Event> events = ObjectifyService.ofy().load().type(Event.class).list();
		for(Event e : events){
			for(AssignedUser u:e.assignees){
				log.info("User: "+u.getUser().getEmail()+" is notited:"+u.isNotofiacted());
				if(checkDate(u.getHours(), new DateTime(e.date))&&!u.isNotofiacted()){
					u.setNotofiacted(sendSimpleMessage(u.getUser(),e.getTitle()+" is COMING",e.getTitle()+" is COMING"));
				}
			}
			ObjectifyService.ofy().save().entity(e).now();
		}

	}

	private boolean sendSimpleMessage(User u, String message, String subject) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);


		System.out.println("Message sending");
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("ziniewiczmaciej@gmail.com", "GOOGLE APP ENGINE"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail(), u.getNickname()));
			msg.setText(message);
			msg.setSubject(subject);
			Transport.send(msg);
			return true;
		} catch (AddressException e) {
			log.info(e.getLocalizedMessage());
		} catch (MessagingException e) {
			log.info(e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			log.info(e.getLocalizedMessage());
		}
		return false;
	}

	private boolean checkDate(int hours, DateTime date) {
		DateTime startTime = DateTime.now();
		Period p = new Period(startTime, date);
		int hoursbetween = p.getHours();
		if (hoursbetween <= hours) {
			return true;
		}
		return false;
	}

}
