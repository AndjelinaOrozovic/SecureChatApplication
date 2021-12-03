<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta  http-equiv="Content-Security-Policy" content="default-src 'self'">
<title>Token check</title>
<link rel="stylesheet" type="text/css" href="css/bluebliss.css" />
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
<h2>Unos tokena</h2>
<form method="POST" action="./TokenController?action=token">
Token: 
<input type="number" name="token" id="token" required="required"/><br/><hr>
<input type="submit" value="Prijavi me" name="submit"/><br/>
<h3><%=session.getAttribute("notification")!=null?session.getAttribute("notification").toString():""%></h3>
</form>
</body>
</html>