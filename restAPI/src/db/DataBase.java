package db;

import java.sql.Statement;

import vdc.ErrorCheck;
import vdc.VDC;
import vdc.VMS;
import vdc.vLinks;
import vdc.vNodes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

	private static final String DB_USER = "root";
	private static final String DB_PASS = "password";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/algorithms";
	
	private static final String JDBC_PATH = "com.mysql.jdbc.Driver";
	
	private String queryVDC = "SELECT tenantID FROM vdc WHERE tenantID=?";
	private String queryVNODE = "SELECT * FROM vnode WHERE fk_vdc=?";
	private String queryVLINK = "SELECT * FROM vlink WHERE fk_to=? OR fk_from=?";
	private String queryVM = "SELECT * FROM vm WHERE fk_vnode=?";
	
	private String deleteVDC = "DELETE FROM vdc WHERE tenantID=?";
	private String deleteVNODE = "DELETE FROM vnode WHERE id=?";
	private String deleteVLINK = "DELETE FROM vlink WHERE fk_to=? OR fk_from=?";
	private String deleteVM = "DELETE FROM vm WHERE fk_vnode=?";


	
	Connection c;
	Statement stmt;
	PreparedStatement ps;
	
	private static DataBase instance;
	
	public static DataBase getInstance(){
		if(instance == null)
			return instance = new DataBase();
		else
			return instance;
	}
	
	public void  startDB(){
		loadDriver();
		createConnection();
		createDB();
	}
	
	public void stopDB(){
		/*try{
			c.close();
		}catch(SQLException e){
			System.err.println(e);
		}*/
	}
	
	private void loadDriver(){
		try{
			Class.forName(JDBC_PATH);
		}catch(ClassNotFoundException e){
			System.err.println("Unable to load database driver");
			System.err.println(e);
			System.exit(0);
		}
	}
	
	private void createConnection(){
		try{
			c = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
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
			//startTable();
			System.out.println("DB created succesfully");
		}catch(Exception e){
			System.err.println("Can not create a statement");
			System.err.println(e);
			System.exit(1);
		}
	}
	
	public ErrorCheck showDB(VDC vdc, String level, String foreignKey, String request) throws SQLException{
		if(level.equals("vdc")){
			ps = prepareStatement(queryVDC);
			ps.setString(1, foreignKey);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				if(request.equals("get"))
					vdc.addTenant(rs.getString("tenantID"));
				showDB(vdc, "vnode", foreignKey, request);
				if(request.equals("delete")){
					PreparedStatement aux = prepareStatement(deleteVDC);
					aux.setString(1, foreignKey);
					aux.executeUpdate();
				}
			}
			else{
				return ErrorCheck.TENANTID_NOT_FOUND;
			}
		}
		else if(level.equals("vnode")) {
			ps = prepareStatement(queryVNODE);
			ps.setString(1, foreignKey);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				if(request.equals("get"))
					vdc.addNodes(new vNodes(rs.getString("id"), rs.getString("label")));
				showDB(vdc, "vm", rs.getString("id"), request);
				showDB(vdc, "vlink", rs.getString("id"), request);
				if(request.equals("delete")){
					System.out.println("delete vnode");
					PreparedStatement aux = prepareStatement(deleteVNODE);
					aux.setString(1, rs.getString("id"));
					aux.executeUpdate();
				}
			}
		}
		else if(level.equals("vm")){
			ps = prepareStatement(queryVM);
			ps.setString(1, foreignKey);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				if(request.equals("get")){
					vNodes vnode = vdc.getVnode(foreignKey);
					vnode.addVM(new VMS(rs.getString("id"),rs.getString("label"), rs.getString("flavorID"), rs.getString("imageID")));
				}
				else if(request.equals("delete")){
					System.out.println("delete vm");
					PreparedStatement aux = prepareStatement(deleteVM);
					aux.setString(1, foreignKey);
					aux.executeUpdate();
				}
				
			}
		}
		else if(level.equals("vlink")){
			ps = prepareStatement(queryVLINK);
			ps.setString(1, foreignKey);
			ps.setString(2, foreignKey);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				if(request.equals("get"))
					vdc.addLinks(new vLinks(rs.getString("id"), rs.getString("bandwith"), rs.getString("fk_to"), rs.getString("fk_from")));
				else if(request.equals("delete")){
					System.out.println("delete vlink");
					PreparedStatement aux = prepareStatement(deleteVLINK);
					aux.setString(1, foreignKey);
					aux.setString(2, foreignKey);
					aux.executeUpdate();
				}		
			}
			return ErrorCheck.ALL_OK;
		}
		return ErrorCheck.ALL_OK;
	}
	
	public ErrorCheck getVnodeDB(/*VDC vdc*/){
		String sql = "SELECT id,label FROM vnode WHERE fk_vdc='";
		return ErrorCheck.ALL_OK;
	}
	
	public void newEntryDB(String sql){
		try{
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.err.println(e);
			System.exit(0);
		}
	}
	
	public ResultSet checkEntryDB(PreparedStatement ps){
		//startDB();
		try{
			ResultSet rs = ps.executeQuery();
			//stopDB();
			return rs;
		}catch(SQLException e){
			System.err.println(e);
			System.exit(0);
		}
		return null;
	}
	
	public PreparedStatement prepareStatement(String sql){
		try{
			return c.prepareStatement(sql);
		}catch(SQLException e){
			System.err.println(e);
			System.exit(1);
		}
		return null;
	}
}
