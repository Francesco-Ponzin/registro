package it.engim.fp.registro;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	private static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		switch (request.getParameter("action")) {
		case "login":
			try {

				String email = request.getParameter("email");
				String password = request.getParameter("password");

				Connection con = DBconnect.getConnection();
				PreparedStatement stmt = con.prepareStatement("select * from users where email=?");

				stmt.setString(1, email);
				ResultSet rs = stmt.executeQuery();

				HttpSession session = request.getSession();
				if (request.getParameter("JSESSIONID") != null) {
					Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
					response.addCookie(userCookie);
				} else {
					String sessionId = session.getId();
					Cookie userCookie = new Cookie("JSESSIONID", sessionId);
					response.addCookie(userCookie);
				}

				if (rs.next()) {

					if (sha256(rs.getString("salt") + password).equals(rs.getString("passwordhash"))) {

						User user = UserDAO.newUser(email);

						// setup session

						session.setAttribute("user", user);
						List<User> teachers = UserDAO.getTeachersListFromDB();
						session.setAttribute("teachers", teachers);
						List<Course> courses = CourseDAO.getListFromDB();
						session.setAttribute("courses", courses);

						switch (user.getRole()) {
						case ADMIN:
							List<User> users = UserDAO.getListFromDB();
							session.setAttribute("users", users);

							break;
						case TEACHER:
							List<Course> teachedCourses = CourseDAO.getTeacherCoursesFromDB(user.getId());
							session.setAttribute("teachedCourses", teachedCourses);
							break;
						case STUDENT:

							List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
							session.setAttribute("myVotes", myVotes);
							break;

						default:
							session.setAttribute("error", "Errore di DB, ruolo inesistente");

							break;
						}

						// end setup session
					} else {
						session.setAttribute("error", "Password errata");

					}

				} else {
					session.setAttribute("error", "Email sconosciuta");

				}

			}

			catch (Throwable theException) {
				System.out.println(theException);
			}

			finally {
				response.sendRedirect("index.jsp");
			}
			break;

		case "logout":

			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect("index.jsp");
			break;

		case "change":

			try {

				session = request.getSession();
				if (request.getParameter("JSESSIONID") != null) {
					Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
					response.addCookie(userCookie);
				} else {
					String sessionId = session.getId();
					Cookie userCookie = new Cookie("JSESSIONID", sessionId);
					response.addCookie(userCookie);
				}

				User user = (User) session.getAttribute("user");

				String email = user.getEmail();
				String password = request.getParameter("password");

				Connection con = DBconnect.getConnection();
				PreparedStatement stmt = con.prepareStatement("select * from users where email=?");

				stmt.setString(1, email);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) { // TODO error handling

					if (request.getParameter("newpassword").equals(request.getParameter("newpassword2"))) {

						if (sha256(rs.getString("salt") + password).equals(rs.getString("passwordhash"))) {

							Random RANDOM = new SecureRandom();
							String salt = Integer.toString(RANDOM.nextInt());
							UserDAO.updatePassword(user, salt, sha256(salt + request.getParameter("newpassword")));

						}else {
							session.setAttribute("error", "Password errata");
							response.sendRedirect("profile.jsp");
							return;
						}
					} else {
						session.setAttribute("error", "Errore di digitazione, le due password non coincidono");
						response.sendRedirect("profile.jsp");
						return;

					}

				} else {
					session.setAttribute("error", "Errore interno, utente non valido");

				}

			}

			catch (Throwable theException) {
				System.out.println(theException);
			}

			response.sendRedirect("index.jsp");

			break;

		default:
			response.sendRedirect("index.jsp");

			break;
		}

	}

}
