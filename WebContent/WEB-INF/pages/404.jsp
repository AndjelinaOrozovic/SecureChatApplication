<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Security-Policy" content="default-src 'self'">
<link href="styles/style.css" type="text/css" rel="stylesheet">
</head>
<script type="text/javascript">
    window.history.forward();
    function noBack() {
      window.history.forward();
    }
  </script>
<body onload="noBack();" onpageshow="if (event.persisted) noBack();">
	<h3>Stranica nije pronadjena</h3>
	<h3><%=session.getAttribute("notification")!=null?session.getAttribute("notification").toString():""%></h3>
<form method="POST" action="./ErrorController?action=error">
</form>
</body>
</html>


