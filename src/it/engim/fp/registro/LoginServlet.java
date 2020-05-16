package it.engim.fp.registro;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

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

				if (rs.next()) { 			//TODO error handling


					if (sha256(rs.getString("salt") + password).equals(rs.getString("passwordhash"))) { 			//TODO error handling

						User user = UserDAO.newUser(email);

						HttpSession session = request.getSession();
						if (request.getParameter("JSESSIONID") != null) {
							Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
							response.addCookie(userCookie);
						} else {
							String sessionId = session.getId();
							Cookie userCookie = new Cookie("JSESSIONID", sessionId);
							response.addCookie(userCookie);
						}

						session.setAttribute("user", user);
						List<User> teachers = UserDAO.getTeachersListFromDB();
						session.setAttribute("teachers", teachers);
						List<Class> classes = ClassDAO.getListFromDB();
						session.setAttribute("classes", classes);
						
						
						switch (user.getRole()) {
						case ADMIN:
							List<User> users = UserDAO.getListFromDB();
							session.setAttribute("users", users);

							break;

						default: //TODO error handling
							break;
						}
					
					}

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

		default:
			response.sendRedirect("index.jsp");

			break;
		}

	}

}
