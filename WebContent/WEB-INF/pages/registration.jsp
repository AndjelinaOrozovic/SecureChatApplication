<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Security-Policy" content="default-src 'self'">
<link href="styles/style.css" type="text/css" rel="stylesheet">
<title>Kreiranje novog naloga</title>
</head>
<script>
if(window.history.replaceState) {
	window.history.replaceState(null, null, window.location.href);
}
function preventBack(){window.history.forward();}
setTimeout("preventBack()", 0);
window.onload=function(){null};
</script>
<body>
	<h1>ChatApp</h1>
	<h2>Registracija</h2>
	<form method="POST" action="./RegistrationController?action=registration">
	Ime<br/> 
	<input type="text" name="name" id="name" required="required"/><br/>
	Prezime<br/> 
	<input type="text" name="surname" id="surname" required="required"/><br/>
	Korisni&#269;ko ime<br/> 
	<input type="text" name="username" id="username" required="required"/><br/>
	Email<br/> 
	<input type="email" name="email" id="email" required="required"/><br /> 
	Lozinka<br/>
	<input type="password" pattern=".{8,}" name="password" id="password" required="required" required title="8 characters minimum"/><br/>
	Potvrda lozinke<br/>
	<input type="password" pattern=".{8,}" name="passwordConfirm" id="passwordConfirm" required="required" required title="8 characters minimum"/><br/><hr> 
	<input type="submit" value="Registruj nalog" name="submit" required="required"/><br/>
	<h3><%=session.getAttribute("notification")!=null?session.getAttribute("notification").toString():""%></h3><br/>  
	<a href="./LoginController?action=login">Prijava na sistem &gt;&gt;&gt;</a>
	</form>
</body>
</html>