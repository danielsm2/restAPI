package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NovaDB {

	private static final String DB_USER = "root";
	private static final String DB_PASS = "cosign";
	private static final String DB_URL = "jdbc:mysql://172.26.37.249:3306/nova";
	
	private static final String JDBC_PATH = "com.mysql.jdbc.Driver";
	
	Connection c;
	Statement stmt;
	PreparedStatement ps;
	
	private static NovaDB instance;
	
	/**
	 * Singleton de la clase DataBase 
	 * @return
	 */
	public static NovaDB getInstance(){
		if(instance == null)
			return instance = new NovaDB();
		else
			return instance;
	}
	
	/**
	 * La base de datos pasa a estar en estado running
	 */
	public void  startDB(){
		loadDriver();
		createConnection();
		//createDB();
		try {
			stmt = c.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * La base de datos pasa a estar parada
	 */
	public void stopDB(){
		try{
			c.close();
		}catch(SQLException e){
			System.err.println(e);
		}
	}
	
	/**
	 * Cargar el driver de mysl
	 */
	private void loadDriver(){
		try{
			Class.forName(JDBC_PATH);
		}catch(ClassNotFoundException e){
			System.err.println("Unable to load database driver");
			System.err.println(e);
			System.exit(0);
		}
	}
	
	/**
	 * Crear una conexión con la base de datos
	 */
	private void createConnection(){
		try{
			c = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
		}catch(SQLException e){
			System.err.println("Unable to connect with data base");
			System.err.println(e);
			System.exit(0);
		}
	}
	
	public ResultSet queryDB(){
		try{
			return stmt.executeQuery("SELECT host,host_ip FROM compute_nodes");
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
}
