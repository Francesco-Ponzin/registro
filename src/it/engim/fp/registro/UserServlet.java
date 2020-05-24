package it.engim.fp.registro;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
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
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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

		HttpSession session = request.getSession();
		if (request.getParameter("JSESSIONID") != null) {
			Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
			response.addCookie(userCookie);
		} else {
			String sessionId = session.getId();
			Cookie userCookie = new Cookie("JSESSIONID", sessionId);
			response.addCookie(userCookie);
		}

		User user = (User) session.getAttribute("user");
		if (user == null || user.getRole() != UserRole.ADMIN) {

			session.setAttribute("error", "Autorizzazione negata, azione riservata agli amministratori");
			response.sendRedirect("index.jsp");
			return;
		}

		try {
			List<User> users;

			switch (request.getParameter("action")) {
			case "create":
				Random RANDOM = new SecureRandom();
				String salt = Integer.toString(RANDOM.nextInt());
				UserDAO.insertToDB(
						UserDAO.newUser(request.getParameter("email"), request.getParameter("firstname"),
								request.getParameter("lastname"), request.getParameter("role")),
						salt, sha256(salt + request.getParameter("password")));
				users = UserDAO.getListFromDB();
				session.setAttribute("users", users);
				List<User> teachers = UserDAO.getTeachersListFromDB();
				session.setAttribute("teachers", teachers);
				break;
			case "delete":

				UserDAO.deleteFromDb(request.getParameter("email"));
				users = UserDAO.getListFromDB();
				session.setAttribute("users", users);
				teachers = UserDAO.getTeachersListFromDB();
				session.setAttribute("teachers", teachers);
				break;
			default:
				break;
			}

		} catch (DAOException DAOError) {
			session.setAttribute("error", DAOError);

		} catch (Exception theException) {
			System.out.println(theException.getMessage());

		} finally {
			response.sendRedirect("index.jsp");
		}

	}

}
