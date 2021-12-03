<%@page import="org.unibl.etf.model.Message"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.unibl.etf.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="messageBean" type="org.unibl.etf.beans.MessageBean" scope="session"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Security-Policy" content="default-src 'self'">
<link href="styles/style.css" type="text/css" rel="stylesheet">
<title>Poruke</title>
</head>
<body>
	<h1>ChatApp</h1>
	<form method="POST" action="./MessageController?action=sendMessage">
	<a href="./MessageController?action=logout">Odjava sa sistema</a>
	<hr>
	<p>Poruke</p>
	<hr>
	<h3><%=session.getAttribute("receiver")!=null?session.getAttribute("receiver").toString():"" %></h3>
	<hr>
	<h3>Poruke: <% 
					ArrayList<String> ls = (ArrayList<String>)session.getAttribute("messagesForUser");
					for(String s: ls) {
						out.println("<p>" + s + "</p><br/>");
					}
					
				%></h3>
	<textarea required="required" id="messageForSending" required="required" name="messageForSending" rows="4" cols="50"></textarea>
	<input type="submit" value="Posalji" name="submit"/><br/>
	</form>
</body>
</html>