<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Log in</title>
	<link rel="stylesheet" type="text/css" href="css/bluebliss.css" />
	</head>
	<script>
	function preventBack(){window.history.forward();}
	setTimeout("preventBack()", 0);
	window.onload=function(){null};
	</script>
	<body>
		<h1>ChatApp</h1>
		<h2>Prijava na sistem</h2>
		<form method="POST" action="./LoginController?action=login">
		Korisni&#269;ko ime <br/> 
		<input type="text" name="username" id="username" required="required"/><br/>
		Lozinka <br/>
		<input type="password" name="password" id="password" required="required"/><br/><hr>
		<input type="submit" value="Prijavi me" name="submit"/><br/>
		<h3><%=session.getAttribute("notification")!=null?session.getAttribute("notification").toString():""%></h3>
		<br/><a href="./RegistrationController?action=registration">Kreiraj novi nalog &gt;&gt;&gt;</a>
		</form>
	</body>
</html>
