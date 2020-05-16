package it.engim.fp.registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO {
	
	public static User newUser(int id, String email, String firstName, String lastName, String role) {

		User newUser = newUser( email,  firstName,  lastName, role);

		newUser.setId(id);

		return newUser;
	}
	
	public static User newUser(String email, String firstName, String lastName, String role) {

		return newUser( email,  firstName,  lastName, toRole(role));
	
	}
	
	public static User newUser(int id, String email, String firstName, String lastName, UserRole role) {

		User newUser = newUser( email,  firstName,  lastName, role);

		newUser.setId(id);

		return newUser;
	}
	
	public static User newUser(String email, String firstName, String lastName, UserRole role) {

		User newUser = new User();

		newUser.setEmail(email);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setRole(role);
		return newUser;
	}

	public static UserRole toRole(String roletext) {

		switch (roletext) {
		case "ADMIN":
			return UserRole.ADMIN;

		case "TEACHER":
			return UserRole.TEACHER;

		case "STUDENT":
			return UserRole.STUDENT;

		default:
			throw new IllegalArgumentException("Unexpected value: " + roletext);
		}

	}

	public static User newUser(String email) {

		User newUser = new User();

		try {

			Connection currentCon = DBconnect.getConnection();
			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM users WHERE email = ?");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			newUser.setEmail(email);
			rs.next();
			newUser.setFirstName(rs.getString("firstname"));
			newUser.setLastName(rs.getString("lastname"));
			newUser.setRole(toRole(rs.getString("userrole")));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newUser;

	}
	
	public static User newUser(int id) {

		User newUser = new User();

		try {

			Connection currentCon = DBconnect.getConnection();
			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM users WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			newUser.setId(id);
			newUser.setId(id);
			newUser.setId(id);
			rs.next();
			newUser.setEmail(rs.getString("email"));
			newUser.setFirstName(rs.getString("firstname"));
			newUser.setLastName(rs.getString("lastname"));
			newUser.setRole(toRole(rs.getString("userrole")));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newUser;

	}

	public static void insertToDB(User user, String salt, String passwordhash) {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon
					.prepareStatement("INSERT INTO users (email, firstname, lastname, passwordhash, salt, userrole) VALUES(?,?,?,?,?,?)");
			
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getFirstName());
			stmt.setString(3, user.getLastName());
			stmt.setString(4, passwordhash);
			stmt.setString(5, salt);
			stmt.setString(6, user.getRole().toString());

			

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteFromDb(String email) {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("DELETE FROM users WHERE email = ?");
			stmt.setString(1, email);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFromDb(int id) {
		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("DELETE FROM users WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<User> getListFromDB() {
		LinkedList<User> list = new LinkedList<User>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM users");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				list.add(UserDAO.newUser(rs.getInt("ID"),rs.getString("email"), rs.getString("firstname"), rs.getString("lastname"),
						toRole(rs.getString("userrole"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	public static List<User> getTeachersListFromDB() {
		LinkedList<User> list = new LinkedList<User>();

		try {

			Connection currentCon = DBconnect.getConnection();

			PreparedStatement stmt = currentCon.prepareStatement("SELECT * FROM users WHERE userrole = 'TEACHER'");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				list.add(UserDAO.newUser(rs.getInt("ID"), rs.getString("email"), rs.getString("firstname"), rs.getString("lastname"),
						toRole(rs.getString("userrole"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	

}
