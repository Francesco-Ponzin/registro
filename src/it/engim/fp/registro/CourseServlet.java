package it.engim.fp.registro;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation course ClassServlet
 */
@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CourseServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

		
		
		
		try {
			List<Course> courses;

			switch (request.getParameter("action")) {
			case "create":
				
				if (user == null || user.getRole() != UserRole.ADMIN) {
					session.setAttribute("error", "Autorizzazione negata, solo gli amministratori possono creare un corso");
					response.sendRedirect("index.jsp");
					return;
				}

				CourseDAO.insertToDB(
						CourseDAO.newCourse(request.getParameter("name"), request.getParameter("description"), Integer.parseInt(request.getParameter("CFU")), UserDAO.newUser(Integer.parseInt(request.getParameter("teacher")))));

				courses = CourseDAO.getListFromDB();
				session.setAttribute("courses", courses);
				break;
			case "delete":
				
				if (user == null || user.getRole() != UserRole.ADMIN) {
					session.setAttribute("error", "Autorizzazione negata, solo gli amministratori possono eliminare un corso");
					response.sendRedirect("index.jsp");
					return;
				}

				CourseDAO.deleteFromDb(Integer.parseInt(request.getParameter("id")));
				courses = CourseDAO.getListFromDB();
				session.setAttribute("courses", courses);
				break;
			default:
				break;
			}

		}catch (DAOException DAOError) {
			session.setAttribute("error", DAOError);

		} catch (Exception theException) {
			System.out.println(theException);
		} finally {
		}

		response.sendRedirect("index.jsp");
	}

}
