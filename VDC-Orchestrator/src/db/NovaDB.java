package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import conf.Conf;

public class NovaDB extends DataBase{
	
	private static final String DB_URL = "jdbc:mysql://" + Conf.IP_Nova +":3306/nova";
	
	Connection c;
	Statement stmt;
	PreparedStatement ps;
	
	private static NovaDB instance;
	
	public static NovaDB getInstance(){
		if(instance == null)
			return instance = new NovaDB();
		else
			return instance;
	}
	
	/**
	 * Crear una conexión con la base de datos
	 */
	private void createConnection(){
		try{
			c = DriverManager.getConnection(DB_URL,Conf.User_BD_Nova,Conf.Pass_BD_Nova);
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
