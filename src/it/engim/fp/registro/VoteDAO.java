package it.engim.fp.registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class VoteDAO {
	
	public static VoteStatus toStatus(String statustext) {

		switch (statustext) {
		case "VOID":
			return VoteStatus.VOID;

		case "ASSIGNED":
			return VoteStatus.ASSIGNED;

		case "ACCEPTED":
			return VoteStatus.ACCEPTED;
			
		case "DECLINED":
			return VoteStatus.DECLINED;
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + statustext);
		}

	}

	public static Vote newVote( int id, Course course, User student,int vote, VoteStatus status) {

		Vote newVote = newVote(course, student, vote, status);
		newVote.setId(id);
			return newVote;

	}

	public static Vote newVote( Course course, User student,int vote, VoteStatus status) {


			Vote newVote = new Vote();
			newVote.setCourse(course);
			newVote.setStudent(student);
			newVote.setVote(vote);
			newVote.setStatus(status);

			return newVote;


	}

	public static Vote newVote(int id) throws DAOException {

		Vote newVote = new Vote();

		try {

			Connection currentCon = DBconnect.getConnection();
			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM votes WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			newVote.setId(id);
			rs.next();
			
			
			newVote.setCourse(CourseDAO.newCourse(rs.getInt("course")));
			newVote.setStudent(UserDAO.newUser(rs.getInt("student")));
			newVote.setVote(rs.getInt("vote"));
			newVote.setStatus(toStatus(rs.getString("status")));
		

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newVote;

	}

	public static void insertToDB(Vote toinsert) throws DAOException {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon
					.prepareStatement("INSERT INTO votes (course, student, vote, status) VALUES(?,?,?,?)");

			stmt.setInt(1, toinsert.getCourse().getId());
			stmt.setInt(2, toinsert.getStudent().getId());
			stmt.setInt(3, toinsert.getVote());
			stmt.setString(4, toinsert.getStatus().toString());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Impossibile iscriversi al corso, probabile duplicato");

		}
	}
	
	

	public static void deleteFromDb(int id) {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("DELETE FROM votes WHERE id = ? AND status = ?");
			stmt.setInt(1, id);
			stmt.setString(2, "VOID");
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Vote> getListFromDB() throws DAOException {
		LinkedList<Vote> list = new LinkedList<Vote>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM votes");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				list.add(VoteDAO.newVote(rs.getInt("id"),CourseDAO.newCourse(rs.getInt("course")), UserDAO.newUser(rs.getInt("student")), rs.getInt("vote"),toStatus(rs.getString("status"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}

	public static List<Vote> getCourseVotesFromDB(int courseId) throws DAOException {
		LinkedList<Vote> list = new LinkedList<Vote>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM votes WHERE course = ?");
			stmt.setInt(1, courseId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Vote newVote = VoteDAO.newVote(rs.getInt("id"),CourseDAO.newCourse(rs.getInt("course")), UserDAO.newUser(rs.getInt("student")), rs.getInt("vote"),toStatus(rs.getString("status")));
				list.add(newVote);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<Vote> getStudentVotesFromDB(int studentId) throws DAOException {
		LinkedList<Vote> list = new LinkedList<Vote>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM votes WHERE student = ?");
			stmt.setInt(1, studentId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Vote newVote = VoteDAO.newVote(rs.getInt("id"),CourseDAO.newCourse(rs.getInt("course")), UserDAO.newUser(rs.getInt("student")), rs.getInt("vote"),toStatus(rs.getString("status")));
				list.add(newVote);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void assign(int voteId, int vote) throws DAOException {
		
		if (vote < 1 || vote > 30) {
			throw new DAOException("Voto non valido");

		}
		
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("UPDATE votes  SET vote = ?, status= ? WHERE id = ? ");
			stmt.setInt(1, vote);
			stmt.setString(2, "ASSIGNED");
			stmt.setInt(3, voteId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void accept(int voteId) {
		
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("UPDATE votes SET status= ? WHERE id = ? ");
			stmt.setString(1, "ACCEPTED");
			stmt.setInt(2, voteId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void decline(int voteId) {
		
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("UPDATE votes SET status= ? WHERE id = ? ");
			stmt.setString(1, "DECLINED");
			stmt.setInt(2, voteId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
