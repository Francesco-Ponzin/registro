package it.engim.fp.registro;

import java.sql.*;

public class DBconnect {
	   static Connection con;
	   static String url;
	         
	   public static Connection getConnection()
	   {
	     
	      try
	      {
	    	  DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
	         String url = "jdbc:mysql://localhost:3306/registro?characterEncoding=latin1&serverTimezone=Europe/Rome"; 
	        // Class.forName("com.mysql.jdbc.Driver");
	         
	         try
	         {            	
	            con = DriverManager.getConnection(url,"engim","engim"); 
	         }
	         catch (SQLException ex)
	         {
	            ex.printStackTrace();
	         }
	      }

	      catch(Exception e)
	      {
	         System.err.println(e);
	      }

	   return con;
	}
}
