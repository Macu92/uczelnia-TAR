<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.example.guestbook.model.Event"%>

<%-- //[START imports]--%>
<%@ page import="com.googlecode.objectify.Key"%>
<%@ page import="com.googlecode.objectify.ObjectifyService"%>
<%-- //[END imports]--%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>3 Col Portfolio - Start Bootstrap Template</title>

<!-- Bootstrap Core CSS -->
<link href="/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="/css/3-col-portfolio.css" rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>


<body>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<%
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
				if (user != null) {
					pageContext.setAttribute("user", user);
			%>

			<p class="navbar-brand">
				Hello, ${fn:escapeXml(user.nickname)}! (You can <a
					href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
					out</a>.)
			</p>
			<%
				} else {
			%>
			<p class="navbar-brand">
				Hello! <a
					href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
					in</a> to subscribe events.
			</p>
			<%
				}
				if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
			%>
			<ul class="nav navbar-nav">
				<li class="nav-item"><a class="nav-link" href="addEvent.jsp">Add
						event </a></li>
			</ul>
			<%
				}
			%>

		</div>
		<!-- /.container -->
	</nav>

	<!-- Page Content -->
	<div class="container">

		<!-- Page Header -->
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">Events</h1>
			</div>
		</div>
		<!-- /.row -->

		<%
			List<Event> events = ObjectifyService.ofy().load().type(Event.class).list();
			int i = 0;
			for (; i < events.size(); i++) {
				Event e = events.get(i);
				pageContext.setAttribute("event_title", e.title);
				pageContext.setAttribute("event_id", e.id);
				pageContext.setAttribute("event_content", e.content);
				pageContext.setAttribute("event_date", e.date);
				if (i % 3 == 0) {
		%>
		<!-- Projects Row -->
		<div class="row">

			<%
				}
			%>
			<div class="col-md-4  portfolio-item">
				<h3>${fn:escapeXml(event_title)}</h3>
				<p>${fn:escapeXml(event_content)}</p>
				<p>${fn:escapeXml(event_date)}</p>
				<a class="btn"
					href="/viewEvent.jsp?eventId=${fn:escapeXml(event_id)}">Check
					event</a>
				<%
					if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
				%>
				<a href="/delete?eventId=${fn:escapeXml(event_id)}">DELETE</a>
				<%
					}
				%>
			</div>
			<%
				if (i % 3 == 2) {
			%>
			<!-- Projects Row -->
		</div>
		<%
			}
			}
			if (i % 3 != 2) {
		%>
		<!-- Projects Row -->
	</div>
	<%
		}
	%>
	<!-- /.row -->

	<hr>

	<!-- Footer -->
	<footer>
		<div class="row">
			<div class="col-lg-12">
				<p>Copyright &copy; Your Website 2014</p>
			</div>
		</div>
		<!-- /.row -->
	</footer>

	</div>
	<!-- /.container -->

	<!-- jQuery -->
	<script src="/js/jquery.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="/js/bootstrap.min.js"></script>

</body>

</html>