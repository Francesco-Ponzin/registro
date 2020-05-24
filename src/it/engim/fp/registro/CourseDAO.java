package it.engim.fp.registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CourseDAO {
	public static Course newCourse(int id, String name, String description, int cfu, User teacher) {
		if (teacher.getRole() == UserRole.TEACHER) {
			Course newClass = newCourse(name, description, cfu, teacher);
			newClass.setId(id);
			return newClass;
		}
		return null;
	}

	public static Course newCourse(String name, String description, int cfu, User teacher) {

		if (teacher.getRole() == UserRole.TEACHER) {
			Course newClass = new Course();
			newClass.setName(name);
			newClass.setDescription(description);
			newClass.setCfu(cfu);
			newClass.setTeacher(teacher);

			return newClass;
		}
		return null; // TODO throw exception

	}

	public static Course newCourse(int id) throws DAOException {

		Course newClass = new Course();

		try {

			Connection currentCon = DBconnect.getConnection();
			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM courses WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			newClass.setId(id);
			rs.next();
			newClass.setName(rs.getString("name"));

			newClass.setDescription(rs.getString("description"));
			newClass.setCfu(rs.getInt("CFU"));
			newClass.setTeacher(UserDAO.newUser(rs.getInt("teacher")));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newClass;

	}

	public static void insertToDB(Course toinsert) throws DAOException {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon
					.prepareStatement("INSERT INTO courses (name, description, CFU, teacher) VALUES(?,?,?,?)");

			stmt.setString(1, toinsert.getName());
			stmt.setString(2, toinsert.getDescription());
			stmt.setInt(3, toinsert.getCfu());
			stmt.setInt(4, toinsert.getTeacher().getId());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Impossibile inserire il corso, probabile duplicato");
		}
	}

	public static void deleteFromDb(int id) throws DAOException {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("DELETE FROM courses WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Impossibile eliminare il corso");

		}
	}

	public static List<Course> getListFromDB() throws DAOException {
		LinkedList<Course> list = new LinkedList<Course>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM courses");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				list.add(CourseDAO.newCourse(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("cfu"), UserDAO.newUser(rs.getInt("teacher"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	
	public static List<Course> getTeacherCoursesFromDB(int teacherId) throws DAOException {
		LinkedList<Course> list = new LinkedList<Course>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM courses WHERE teacher = ?");
			stmt.setInt(1, teacherId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				
				Course newCourse = CourseDAO.newCourse(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("cfu"), UserDAO.newUser(rs.getInt("teacher")));
				newCourse.setVotes(VoteDAO.getCourseVotesFromDB(newCourse.getId())); 
				list.add(newCourse);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
