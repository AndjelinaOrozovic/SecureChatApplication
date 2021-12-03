<%@page import="org.unibl.etf.model.User"%>
<%@page import="org.unibl.etf.beans.UserBean"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="userBean" type="org.unibl.etf.beans.UserBean" scope="session"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Security-Policy" content="default-src 'self'">
<link href="styles/style.css" type="text/css" rel="stylesheet">
<title>Kontakti</title>
</head>
<body>
	<h1>ChatApp</h1>
	<a href="./LoginController?action=logout">Odjava sa sistema</a>
	<hr/>
	<h2>Korisnici</h2>
	<%
		for(User user:userBean.getAll()) {
			for(User loggedUser:UserBean.loggedUsers) {
				if(user.equals(loggedUser)) {
					if(!user.getName().equals(session.getAttribute("userName"))) {
					out.println("<a href=" + "./MessageController?action=showMessages&receiver=" + user.getUsername() + ">" +  user.getUsername() + "&gt;&gt;&gt;</a>" + "<br/>");
					out.println("<hr/>" + "<br/>");
					}
				}
			}
		}
		
	%>
</body>
</html>