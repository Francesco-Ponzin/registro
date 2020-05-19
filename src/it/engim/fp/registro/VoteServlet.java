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

			switch (request.getParameter("action")) {
			case "subscribe":
				if (user.getRole() == UserRole.STUDENT) {
					Course course = CourseDAO.newCourse(Integer.parseInt(request.getParameter("course")));
				//	VoteDAO.insertToDB(VoteDAO.newVote(course, user, 0, VoteStatus.VOID)); //TODO find bug
					VoteDAO.insertToDB(Integer.parseInt(request.getParameter("course")), user.getId());
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} // TODO manage error

				break;
			case "resign":
				if (user.getRole() == UserRole.STUDENT) {
				//	Course course = CourseDAO.newCourse(Integer.parseInt(request.getParameter("course")));
				//	VoteDAO.insertToDB(VoteDAO.newVote(course, user, 0, VoteStatus.VOID)); //TODO find bug
					VoteDAO.deleteFromDb(Integer.parseInt(request.getParameter("vote")));
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} // TODO manage error

				break;
			case "accept":
				Vote vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.STUDENT && vote.getStatus() == VoteStatus.ASSIGNED) {
					VoteDAO.accept(vote.getId());
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} // TODO manage error
				break;
			case "decline":
				vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.STUDENT && vote.getStatus() == VoteStatus.ASSIGNED) {
					VoteDAO.decline(vote.getId());
					List<Vote> myVotes = VoteDAO.getStudentVotesFromDB(user.getId());
					session.setAttribute("myVotes", myVotes);
				} // TODO manage error
				break;
			case "assign":
				vote = VoteDAO.newVote(Integer.parseInt(request.getParameter("vote")));
				if (user.getRole() == UserRole.TEACHER
						&& (vote.getStatus() == VoteStatus.VOID || vote.getStatus() == VoteStatus.DECLINED)) {
					VoteDAO.assign(vote.getId(), Integer.parseInt(request.getParameter("assigned")));
					List<Course> teachedCourses = CourseDAO.getTeacherCoursesFromDB(user.getId());
					session.setAttribute("teachedCourses", teachedCourses);
				} // TODO manage error
				break;

			default:
				break;
			}

		} catch (Exception theException) {
			System.out.println(theException);
		} finally {
		}

		response.sendRedirect("index.jsp");
	}

}
