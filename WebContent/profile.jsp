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
	<title>Profile settings</title>
	<link rel="stylesheet" href="style.css">
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

	<div class="login">
		<h2>Cambia password</h2>
		<form action="LoginServlet" method="POST" class="login">

			<div><label for="password">Inserisci password</label><input type="password" name="password"></div>
			<div><label for="newpassword">Inserisci nuova password</label><input type="password" name="newpassword">
			</div>
			<div><label for="newpassword2">Ripeti nuova password</label><input type="password" name="newpassword2">
			</div>
			<input type="hidden" name="action" value="change">
			<input type="submit" value="submit">

		</form>
		<a href="index.jsp"><button>indietro</button></a>
	</div>


	<%
		}
	%>

</body>

</html>