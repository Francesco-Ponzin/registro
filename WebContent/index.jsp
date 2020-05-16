<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="it.engim.fp.registro.*, java.util.*"%>
<!DOCTYPE html>


<%
	User user = (User) session.getAttribute("user");
List<User> users = (List<User>) session.getAttribute("users");
List<User> teachers = (List<User>) session.getAttribute("teachers");
List<it.engim.fp.registro.Class> classes = (List<it.engim.fp.registro.Class>) session.getAttribute("classes");
%>


<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
		if (user == null) {
	%>

	<h2>Login</h2>
	<form action="LoginServlet" method="POST">
		Inserisci email <input type="email" name="email"><br>
		Inserisci password <input type="password" name="password"> <input
			type="hidden" name="action" value="login"> <input
			type="submit" value="submit">

	</form>



	<%
		} else {
	%>

	<h2>
		Benvenuto
		<%=user.getFirstName() + " " + user.getLastName()%>

	</h2>
	<%=user.getRole()%>

	<form action="LoginServlet" method="POST">
		<input type="hidden" name="action" value="logout"> <input
			type="submit" value="logout">

	</form>



	<%
		if (user.getRole() == UserRole.ADMIN) {
	%>


	<table>


		<tr>
			<th>Nome</th>
			<th>Cognome</th>
			<th>Email</th>
			<th>Ruolo</th>
			<th></th>
		</tr>

		<%
			for (User u : users) {
		%>
		<tr>

			<td><%=u.getFirstName()%></td>
			<td><%=u.getLastName()%></td>
			<td><%=u.getEmail()%></td>
			<td><%=u.getRole()%></td>
			<td>
				<form action="UserServlet" method="POST">
					<input type="hidden" name="action" value="delete"> <input
						type="hidden" name="email" value="<%=u.getEmail()%>"> <input
						type="submit" value="delete">

				</form>
			</td>
		</tr>


		<%
			}
		%>


		<tr>
			<form action="UserServlet" method="POST">
				<td><input type="text" name="firstname"></td>
				<td><input type="text" name="lastname"></td>
				<td><input type="email" name="email"></td>
				<td><select name="role">
						<option value="ADMIN">Admin</option>
						<option value="STUDENT">Student</option>
						<option value="TEACHER">Teacher</option>

				</select></td> <input type="hidden" name="password" value="password">
				<td><input type="hidden" name="action" value="create">
					<input type="submit" value="insert"></td>
			</form>
		</tr>

	</table>

	<table>


		<tr>
			<th>Nome</th>
			<th>Descrizione</th>
			<th>CFU</th>
			<th>Professore</th>
			<th></th>
		</tr>

		<%
			for (it.engim.fp.registro.Class c : classes) {
		%>
		<tr>

			<td><%=c.getName()%></td>
			<td><%=c.getDescription()%></td>
			<td><%=c.getCfu()%></td>
			<td><%=c.getTeacher().getFirstName() + " " + c.getTeacher().getLastName()%></td>
			<td>
				<form action="ClassServlet" method="POST">
					<input type="hidden" name="action" value="delete"> <input
						type="hidden" name="id" value="<%=c.getId()%>"> <input
						type="submit" value="delete">

				</form>
			</td>
		</tr>


		<%
			}
		%>


		<tr>
			<form action="ClassServlet" method="POST">
				<td><input type="text" name="name"></td>
				<td><input type="text" name="description"></td>
				<td><input type="number" name="CFU" max="30" min="1" value="5"></td>
				<td><select name="teacher">
						<%
							for (User u : teachers) {
						%>
						<option value="<%= u.getId() %>"><%= u.getEmail() %>+<%=u.getFirstName() + " " + u.getLastName()%></option>
						<%
							}
						%>

				</select></td> <input type="hidden" name="password" value="password">
				<td><input type="hidden" name="action" value="create">
					<input type="submit" value="insert"></td>
			</form>
		</tr>

	</table>







	<%
		} // admin logged end
	%>



	<%
		} //all logged end
	%>

</body>
</html>