<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="it.engim.fp.registro.*, java.util.*"%>
<!DOCTYPE html>


<%
	User user = (User) session.getAttribute("user");
List<User> users = (List<User>) session.getAttribute("users");
List<User> teachers = (List<User>) session.getAttribute("teachers");
List<Course> courses = (List<Course>) session.getAttribute("courses");
List<Vote> myVotes = (List<Vote>) session.getAttribute("myVotes");
List<Course> teachedCourses = (List<Course>) session.getAttribute("teachedCourses");
String errorMessage = session.getAttribute("error") == null ? null : session.getAttribute("error").toString();
%>


<html>

<head>
	<meta charset="UTF-8">
	<title>Registro Elettronico</title>
	<link rel="stylesheet" href="style.css">
</head>

<body>

	<header>
		<h1>Engim - Tecnico di Sviluppo Software</h1>
	</header>
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
	<div class="login">
		<h2>Login</h2>
		<form action="LoginServlet" method="POST" class="login">
			<label for="email">Inserisci email</label> <input type="email" name="email"><br>
			<label for="password">Inserisci password</label> <input type="password" name="password"> <input
				type="hidden" name="action" value="login">
			<input type="submit" value="submit">

		</form>
	</div>


	<%
		} else {
	%>

	<div class="welcome">
		<h2>
			Welcome
			<%=user.getFirstName() + " " + user.getLastName()%>

		</h2>


		<form action="LoginServlet" method="POST" class="logout">
			<a href="profile.jsp">profilo</a>
			<input type="hidden" name="action" value="logout"> <input type="submit" value="logout">
		</form>

	</div>

	<%
		if (user.getRole() == UserRole.ADMIN) {
	%>

	<h2>Elenco utenti</h2>
	<div class="table">
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
						<input type="hidden" name="action" value="delete"> <input type="hidden" name="email"
							value="<%=u.getEmail()%>"> <input type="submit" value="delete">

					</form>
				</td>
			</tr>


			<%
			}
		%>




		</table>
		<div class="formbox">
			<h3>aggiungi un utente</h3>
			<form action="UserServlet" method="POST">
				<input type="hidden" name="action" value="create">
				<div><label for="firstname">Nome:</label><input type="text" name="firstname"></div>
				<div><label for="lastname">Cognome:</label><input type="text" name="lastname"></div>
				<div><label for="email">Mail:</label><input type="email" name="email"></div>
				<div><label for="password">Password:</label><input type="text" name="password" value="cambiami"></div>
				<label for="role">Ruolo:</label><select name="role">
					<option value="ADMIN">Admin</option>
					<option value="STUDENT">Student</option>
					<option value="TEACHER">Teacher</option>
				</select>

				<input type="submit" value="aggiungi">
			</form>
		</div>
	</div>

	<h2>Elenco corsi</h2>
	<div class="table">
		<table>


			<tr>
				<th>Nome</th>
				<th>Descrizione</th>
				<th>CFU</th>
				<th>Professore</th>
				<th></th>
			</tr>

			<%
			for (Course c : courses) {
		%>
			<tr>

				<td><%=c.getName()%></td>
				<td><%=c.getDescription()%></td>
				<td><%=c.getCfu()%></td>
				<td><%=c.getTeacher().getFirstName() + " " + c.getTeacher().getLastName()%></td>
				<td>
					<form action="CourseServlet" method="POST">
						<input type="hidden" name="action" value="delete"> <input type="hidden" name="id"
							value="<%=c.getId()%>"> <input type="submit" value="delete">

					</form>
				</td>
			</tr>


			<%
			}
		%>




		</table>
		<div class="formbox">
			<h3>aggiungi un corso</h3>

			<div>
				<form action="CourseServlet" method="POST">
					<div><label for="name">Nome corso:</label><input type="text" name="name"></div>
					<div><label for="CFU">Crediti formativi:</label><input type="number" name="CFU" max="30" min="1"
							value="5"></div>
					<div><label for="description">Descrizione:</label>
						<textarea name="description"></textarea>
					</div>
					<label for="teacher">Docente:</label><select name="teacher">
						<%	for (User u : teachers) {	%>
						<option value="<%=u.getId()%>"><%=u.getFirstName() + " " + u.getLastName()%></option>
						<%	} %>
					</select> <input type="hidden" name="action" value="create"> <input type="submit" value="insert">
				</form>
			</div>


		</div>


		<%	} else if (user.getRole() == UserRole.TEACHER) {    // admin logged end
			%>

		<h2>I miei corsi:</h2>

		<ul class="courselist">


			<%
			for (Course c : teachedCourses) {
		%>
			<li>

				<details>
					<summary><%=c.getName()%></summary>
					<%=c.getDescription()%>
					<p>CFU: <%=c.getCfu()%></p>

				</details>


				<table>

					<%
					for (Vote v : c.getVotes()) {
				%>
					<tr>
						<td><%=v.getStudent().getFirstName() + " " + v.getStudent().getLastName()%></td>
						<td>
							<%
							if (v.getStatus() == VoteStatus.VOID) {
						%> N.A. <%
							} else {
						%> <%=v.getVote()%> <%
 	}
 %>


						</td>
						<td>
							<%
							if (v.getStatus() == VoteStatus.VOID || v.getStatus() == VoteStatus.DECLINED) {
						%>
							<form action="VoteServlet" method="POST">
								<input type="hidden" name="action" value="assign">
								<input type="number" name="assigned" value="18">
								<input type="hidden" name="vote" value="<%=v.getId()%>">
								<input type="submit" value="Assegna">
							</form>
							<%
 	}else if (v.getStatus() == VoteStatus.ACCEPTED){ 
 %>
<span class="accepted">Accettato</span>
							<% } %>
						</td>




					</tr>

					<%
					}
				%>
				</table>



			</li>




			<%
			}
		%>
		</ul>
		<%
		} else if (user.getRole() == UserRole.STUDENT) {     // teacher logged end
	%>


		<h2>I miei corsi</h2>
		<table>

			<%
		for (Vote v : myVotes) {
	%>
			<tr>
				<td><%=v.getCourse().getName()%></td>
				<td><%=v.getCourse().getTeacher().getLastName()%></td>
				<td>
					<%
				if (v.getStatus() == VoteStatus.VOID) {
			%> N.A. <%
				} else {
			%> <%=v.getVote()%> <%
 }
%>


				</td>
				<td>
					<%
				if (v.getStatus() == VoteStatus.ASSIGNED) {
			%>
					<form action="VoteServlet" method="POST">
						<input type="hidden" name="action" value="accept"> <input type="hidden" name="vote"
							value="<%=v.getId()%>"> <input type="submit" value="accetta">
					</form>

					<form action="VoteServlet" method="POST">
						<input type="hidden" name="action" value="decline"> <input type="hidden" name="vote"
							value="<%=v.getId()%>"> <input type="submit" value="rifiuta">
					</form> <%
			 } else if (v.getStatus() == VoteStatus.VOID) {
		 %>


					<form action="VoteServlet" method="POST">
						<input type="hidden" name="action" value="resign"> <input type="hidden" name="vote"
							value="<%=v.getId()%>"> <input type="submit" value="Ritirati">
					</form>
					<%
 } else if (v.getStatus() == VoteStatus.DECLINED) {
%> <span class="declined">Rifiutato</span>
					<%
 } else if (v.getStatus() == VoteStatus.ACCEPTED) {
%> <span class="accepted">Definitivo</span>


					<%
 }
%>


				</td>


			</tr>

			<%
		}
	%>
		</table>


		<h2>Elenco corsi</h2>

		<table>


			<tr>
				<th>Nome</th>
				<th>Descrizione</th>
				<th>CFU</th>
				<th>Professore</th>
				<th></th>
			</tr>

			<%
			for (Course c : courses) {
		%>
			<tr>

				<td><%=c.getName()%></td>
				<td><%=c.getName()%></td>
				<td><%=c.getDescription()%></td>
				<td><%=c.getCfu()%></td>
				<td><%=c.getTeacher().getFirstName() + " " + c.getTeacher().getLastName()%></td>
				<td>
					<form action="VoteServlet" method="POST">
						<input type="hidden" name="action" value="subscribe"> <input type="hidden" name="course"
							value="<%=c.getId()%>"> <input type="submit" value="Iscriviti">
					</form>
				</td>
			</tr>

			<%
			}
		%>



		</table>




		<%
		} // student logged end
	%>

		<%
		} //all logged end
	%>

</body>

</html>