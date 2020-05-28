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
 * Servlet implementation class VoteServlet
 */
@WebServlet("/VoteServlet")
public class VoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VoteServlet() {
		super();
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

		try {
			Vote vote;
			switch (request.getParameter("action")) {
			case "subscribe":
				if (user.getRole() == UserRole.STUDENT) {
					Course course = CourseDAO.newCourse(Integer.parseInt(request.getParameter("course")));
					VoteDAO.insertToDB(VoteDAO.newVote(course, user, 0, VoteStatus.VOID));
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} else {
					session.setAttribute("error",
							"Autorizzazione negata, solo gli studenti possono isriversi a un corso");
				}

				break;
			case "resign":
				vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.STUDENT && vote.getStatus() == VoteStatus.VOID
						&& vote.getStudent().getId() == user.getId()) {
					VoteDAO.deleteFromDb(vote.getId());
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} else {
					session.setAttribute("error", "Autorizzazione negata, non puoi abbandonare questo corso");
					if (user.getRole() == UserRole.STUDENT) {
						List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
						session.setAttribute("myVotes", myVotes);
					}
				}

				break;
			case "accept":
				vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.STUDENT && vote.getStatus() == VoteStatus.ASSIGNED
						&& vote.getStudent().getId() == user.getId()) {
					VoteDAO.accept(vote.getId());
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} else {
					session.setAttribute("error", "Autorizzazione negata");
					if (user.getRole() == UserRole.STUDENT) {
						List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
						session.setAttribute("myVotes", myVotes);
					}
				}
				break;
			case "decline":
				vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.STUDENT && vote.getStatus() == VoteStatus.ASSIGNED
						&& vote.getStudent().getId() == user.getId()) {
					VoteDAO.decline(vote.getId());
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} else {
					session.setAttribute("error", "Autorizzazione negata");
					if (user.getRole() == UserRole.STUDENT) {
						List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
						session.setAttribute("myVotes", myVotes);
					}
				}
				break;
			case "assign":
				vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.TEACHER
						&& (vote.getStatus() == VoteStatus.VOID || vote.getStatus() == VoteStatus.DECLINED)
						&& vote.getCourse().getTeacher().getId() == user.getId()) {
					VoteDAO.assign(vote.getId(), Integer.parseInt(request.getParameter("assigned")));
					List<Course> teachedCourses = CourseDAO.getTeacherCoursesFromDB(user.getId());
					session.setAttribute("teachedCourses", teachedCourses);
				} else {
					session.setAttribute("error", "Autorizzazione negata");
					if (user.getRole() == UserRole.TEACHER) {
						List<Course> teachedCourses = CourseDAO.getTeacherCoursesFromDB(user.getId());
						session.setAttribute("teachedCourses", teachedCourses);
					}
				}
				break;

			default:
				break;
			}

		} catch (DAOException DAOError) {
			session.setAttribute("error", DAOError);
			try {
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
				}
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception theException) {
			System.out.println(theException);
		} finally {
		}

		response.sendRedirect("index.jsp");
	}

}
