package db;

import java.sql.Statement;

import person.Persona;
import vdc.VDC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

	private static final String DB_USER = "root";
	private static final String DB_PASS = "password";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/algorithms";
	
	private static final String JDBC_PATH = "com.mysql.jdbc.Driver";
	
	Connection c;
	Statement stmt;
	
	private static DataBase instance;
	
	public static DataBase getInstance(){
		if(instance == null)
			return instance = new DataBase();
		else
			return instance;
	}
	
	public void  initializeDB(){
		loadDriver();
		createConnection();
		createDB();
	}
	
	private void loadDriver(){
		try{
			Class.forName(JDBC_PATH);
			System.out.println("Driver loaded");
		}catch(ClassNotFoundException e){
			System.err.println("Unable to load database driver");
			System.err.println(e);
			System.exit(0);
		}
	}
	
	private void createConnection(){
		try{
			c = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			System.out.println("Connected to date base");
		}catch(SQLException e){
			System.err.println("Unable to connect with data base");
			System.err.println(e);
			System.exit(0);
		}
	}
	
	private void createDB(){
		System.out.println("Creating new DB...");
		try{
			stmt = c.createStatement();
			//stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS PEOPLE");
			startTable();
			System.out.println("DB created succesfully");
		}catch(Exception e){
			System.err.println("Can not create a statement");
			System.err.println(e);
			System.exit(1);
		}
		/*finally{
			try{
				stmt.close();
			}catch(SQLException e){
				System.err.println(e);
				System.exit(1);
			}
			try{
				c.close();
			}catch(SQLException e){
				System.err.println(e);
				System.exit(1);
			}
		}*/
	}
	
	public void startTable(){
		String sql = "CREATE TABLE IF NOT EXISTS Info " +
					 "(name VARCHAR(255), " +
					 "age INTEGER not NULL, " +
					 "location VARCHAR(255), " +
					 "PRIMARY KEY ( name ))";
		updateBD(sql);
	}
	
	public void addRow(String sql){
		//String sql = "INSERT INTO  " +
		//			 "VALUES ('" + p.getName() + "', " + p.getAge() + ", " + "'" + p.getLocation() + "')";
		
		updateBD(sql);
	}
	
	public String showDB(){
		String sql = "SELECT name,age,location FROM Info";
		String info = "";
		try{
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String location = rs.getString("location");
				info = info + "Name: " + name + " age: " + age + " location: " + location + "\n";
				System.out.println("Name: " + name + " age: " + age + " location: " + location);
			}
		}catch(SQLException e){
			System.err.println(e);
			System.exit(0);
		}
		return info;
	}
	public void updateBD(String sql){
		try{
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.err.println(e);
			System.exit(0);
		}
	}
}
