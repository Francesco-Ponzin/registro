package it.engim.fp.registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ClassDAO {
	public static Class newClass(int id, String name, String description, int cfu, User teacher) {
		if (teacher.getRole() == UserRole.TEACHER) {
			Class newClass = newClass(name, description, cfu, teacher);
			newClass.setId(id);
			return newClass;
		}
		return null;
	}

	public static Class newClass(String name, String description, int cfu, User teacher) {

		if (teacher.getRole() == UserRole.TEACHER) {
			Class newClass = new Class();
			newClass.setName(name);
			newClass.setDescription(description);
			newClass.setCfu(cfu);
			newClass.setTeacher(teacher);

			return newClass;
		}
		return null; // TODO throw exception

	}

	public static Class newClass(int id) {

		Class newClass = new Class();

		try {

			Connection currentCon = DBconnect.getConnection();
			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM class WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			newClass.setId(id);
			rs.next();
			newClass.setName(rs.getString("name"));

			newClass.setDescription(rs.getString("description"));
			newClass.setCfu(rs.getInt("CFU"));
			newClass.setTeacher(UserDAO.newUser(id));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newClass;

	}

	public static void insertToDB(Class toinsert) {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon
					.prepareStatement("INSERT INTO classes (name, description, CFU, teacher) VALUES(?,?,?,?)");

			stmt.setString(1, toinsert.getName());
			stmt.setString(2, toinsert.getDescription());
			stmt.setInt(3, toinsert.getCfu());
			stmt.setInt(4, toinsert.getTeacher().getId());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteFromDb(int id) {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("DELETE FROM classes WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Class> getListFromDB() {
		LinkedList<Class> list = new LinkedList<Class>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM classes");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				list.add(ClassDAO.newClass(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("cfu"), UserDAO.newUser(rs.getInt("teacher"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	
	public static List<Class> getTeacherClassesFromDB(int teacherId) {
		LinkedList<Class> list = new LinkedList<Class>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM classes WHERE teacher = ?");
			stmt.setInt(1, teacherId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(ClassDAO.newClass(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("cfu"), UserDAO.newUser(rs.getInt("teacher"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
