<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="it.engim.fp.registro.*, java.util.*"%>
<!DOCTYPE html>
<%
	User user = (User) session.getAttribute("user");
	String errorMessage = session.getAttribute("error") == null ? null : session.getAttribute("error").toString();

%>
<html>
<head>
<meta charset="UTF-8">
<title>Profile</title>
</head>
<body>

	<%
		if (errorMessage != null) {
	%>
	<div class="error"><%=errorMessage%></div>
	<%
		session.setAttribute("error", null);
	}
	%>

	<%
		if (user == null) {
	%>
	Devi prima fare il <a href="login.jsp">Login</a>
	<%
		} else {
	%>
	
	
		<form action="LoginServlet" method="POST" class="login">

		Inserisci password <input type="password" name="password">
		Inserisci nuova password <input type="password" name="newpassword">
		Ripeti nuova password <input type="password" name="newpassword2">
		
		 <input
			type="hidden" name="action" value="change"> <input
			type="submit" value="submit">

	</form>
	
	
	
	
	<%
		}
	%>

</body>
</html>