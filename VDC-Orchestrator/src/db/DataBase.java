package db;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import conf.Conf;
import vdc.ErrorCheck;
import vdc.VDC;
import vdc.VirtualMachine;
import vdc.VirtualLink;
import vdc.VirtualNode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/algorithms";
	
	private static final String JDBC_PATH = "com.mysql.jdbc.Driver";
	
	private String queryVDC = "SELECT tenantID FROM vdc WHERE tenantID=?";
	private String queryVNODE = "SELECT * FROM vnode WHERE fk_vdc=?";
	private String queryVLINK = "SELECT * FROM vlink WHERE fk_to=? OR fk_from=?";
	private String queryVM = "SELECT * FROM vm WHERE fk_vnode=?";
	private String queryStack = "SELECT url FROM stacks WHERE fk_vdc=?";
	
	private String deleteVDC = "DELETE FROM vdc WHERE tenantID=?";
	//private String deleteVNODE = "DELETE FROM vnode WHERE id=?";
	//private String deleteVLINK = "DELETE FROM vlink WHERE fk_to=? OR fk_from=?";
	//private String deleteVM = "DELETE FROM vm WHERE fk_vnode=?";

	private Map<String,VDC> setVDC = new HashMap<String,VDC>();

	private String currentTenant;
	
	Connection c;
	Statement stmt;
	PreparedStatement ps;
	
	private static DataBase instance;
	
	/**
	 * DataBase singleton  
	 * @return
	 */
	public static DataBase getInstance(){
		if(instance == null)
			return instance = new DataBase();
		else
			return instance;
	}
	
	/**
	 * The database pass to be running
	 */
	public void  startDB(){
		loadDriver();
		createDB();
		createConnection();
		try {
			stmt = c.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the database
	 */
	public void stopDB(){
		try{
			c.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Load mysql driver 
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
	 * Creates a database connection
	 */
	private void createConnection(){
		try{
			c = DriverManager.getConnection(DB_URL,Conf.User_BD_Horizon,Conf.Pass_BD_Horizon);
		}catch(SQLException e){
			System.err.println("Unable to connect with data base");
			System.err.println(e);
			System.exit(0);
		}
	}
	
	/**
	 * Creates a database if it does not exist
	 */
	private void createDB(){ //TODO
		try{
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/" ,Conf.User_BD_Horizon,Conf.Pass_BD_Horizon);
			Statement aux = c.createStatement();
			ResultSet rs = aux.executeQuery("SHOW DATABASES LIKE 'algorithms'");
			if(!rs.next()){
				System.out.println("Creating new DB tables...");
				Conf conf = new Conf();
				List<String> createTables = conf.readDBFile();
				for(String table : createTables)
					aux.executeUpdate(table);
				System.out.println("DB created succesfully");
			}
			
		}catch(Exception e){
			System.err.println("Can not create a statement");
			System.err.println(e);
			System.exit(1);
		}
	}
	
	public boolean existVDC(String tenantID) throws SQLException{
		ps = prepareStatement(queryVDC);
		ps.setString(1, tenantID);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			return true;
		return false;
	}
	
	public ErrorCheck deleteVDC(String tenantID) throws SQLException{
		PreparedStatement aux = prepareStatement(deleteVDC);
		aux.setString(1, tenantID);
		if(aux.executeUpdate() != 0){
			deleteLocalVDC(tenantID);
			return ErrorCheck.ALL_OK;
		}
		else
			return ErrorCheck.NOTHING_TO_DELETE;
	}
	
	/**
	 * Hace un volcado de la base de datos a través del campo informado tenantID
	 * @param vdc : objeto que se construira si se hace un GET
	 * @param level : nivel dentro del arbol
	 * @param foreignKey : busqueda a traves de la base de datos
	 * @param request : tipo de peticion(get o delete)
	 * @return
	 * @throws SQLException
	 */
	public ErrorCheck showDB(VDC vdc, String level, String foreignKey, String request) throws SQLException{
		if(level.equals("vdc")){
			ps = prepareStatement(queryVDC);
			ps.setString(1, foreignKey);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				if(request.equals("get"))
					vdc.setTenant(rs.getString("tenantID"));
				showDB(vdc, "vnode", foreignKey, request);
				/*if(request.equals("delete")){
					PreparedStatement aux = prepareStatement(deleteVDC);
					aux.setString(1, foreignKey);
					aux.executeUpdate();
				}*/
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
					vdc.addNodes(new VirtualNode(rs.getString("id").split(":")[1], rs.getString("label")));
				showDB(vdc, "vm", rs.getString("id"), request);
				showDB(vdc, "vlink", rs.getString("id"), request);
				/*if(request.equals("delete")){
					PreparedStatement aux = prepareStatement(deleteVNODE);
					aux.setString(1, rs.getString("id"));
					aux.executeUpdate();
				}*/
			}
		}
		else if(level.equals("vm")){
			ps = prepareStatement(queryVM);
			ps.setString(1, foreignKey);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				if(request.equals("get")){
					VirtualNode vnode = vdc.getVNodeByName(foreignKey.split(":")[1]);
					vnode.addVirtualMachine(new VirtualMachine(rs.getString("label"), rs.getString("flavorID"), rs.getString("flavorName"),rs.getString("imageID")));
				}
				/*else if(request.equals("delete")){
					PreparedStatement aux = prepareStatement(deleteVM);
					aux.setString(1, foreignKey);
					aux.executeUpdate();
				}*/
				
			}
		}
		else if(level.equals("vlink")){
			ps = prepareStatement(queryVLINK);
			ps.setString(1, foreignKey);
			ps.setString(2, foreignKey);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				if(request.equals("get") && !checkID(rs.getString("id").split(":")[1], vdc))
					vdc.addLinks(new VirtualLink(rs.getString("id").split(":")[1], rs.getString("bandwith"), rs.getString("fk_to").split(":")[1], rs.getString("fk_from").split(":")[1]));
				/*else if(request.equals("delete")){
					PreparedStatement aux = prepareStatement(deleteVLINK);
					aux.setString(1, foreignKey);
					aux.setString(2, foreignKey);
					aux.executeUpdate();
				}*/		
			}
			return ErrorCheck.ALL_OK;
		}
		return ErrorCheck.ALL_OK;
	}
	
	private boolean checkID(String id, VDC vdc){
		for(int i = 0; i < vdc.getSizeVLink(); ++i){
			if(vdc.getVLinkID(i).equals(id))
				return true;
		}
		return false;
	}
	
	/**
	 * Se encarga de los inserts, nueva entrada contra base de datos
	 * @param sql
	 */
	public void newEntryDB(String sql){
		try{
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Devuelve el resultado de realizar una query contra la base de datos
	 * @param ps
	 * @return
	 */
	public ResultSet queryDB(PreparedStatement ps){
		try{
			ResultSet rs = ps.executeQuery();
			return rs;
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * Devuelve una instancia de PreparedStatement con conexión con la base de datos
	 * @param sql
	 * @return
	 */
	public PreparedStatement prepareStatement(String sql){
		try{
			return c.prepareStatement(sql);
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	public void saveVDC(VDC vdc){
		currentTenant = vdc.getTenant();
		if(vdc.getVNodeID(0).contains(":")){
			for(int i = 0; i < vdc.getSizeVLink(); ++i){
				VirtualLink aux = vdc.getVLinkByPos(i);
				aux.setID(aux.getID().split(":")[1]);
			}
			
			for(int i = 0; i < vdc.getSizeVNode(); ++i){
				VirtualNode aux = vdc.getVNodeByPos(i);
				aux.setID(aux.getId().split(":")[1]);
				for(int j = 0; j < aux.getSizeVirtualMachine(); ++j){
					VirtualMachine vm = aux.getVirtualMachine(j);
					vm.setId(vm.getId().split(":")[1]);
				}
			}
		}
		
		setVDC.put(vdc.getTenant(), vdc);
	}
	
	public void rollbackVDC(VDC vdc) throws SQLException{
		if(setVDC.containsKey(vdc.getTenant())){
			DecodifyMessage dm = new DecodifyMessage();
			dm.startParse(setVDC.get(vdc.getTenant()));
		}
		else
			showDB(new VDC(), "vdc", vdc.getTenant(), "delete");
	}
	
	public boolean checkLocalVDC(String tenant){
		/*for(Entry<String, VDC> aux : setVDC.entrySet()){
			System.out.println("tenant: " + aux.getKey());
			aux.getValue().printInfo();
		}*/
		return setVDC.containsKey(tenant);
	}
	
	public VDC getLocalVDC(String tenant){
		return setVDC.get(tenant);
	}
	
	public void deleteLocalVDC(String tenant){
		if(setVDC.containsKey(tenant))
			setVDC.remove(tenant);
	}
	
	public String getCurrentTenant(){
		return currentTenant;
	}
	
	public String getStack(String tenantID) throws SQLException{
		ps = prepareStatement(queryStack);
		ps.setString(1, tenantID);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			return rs.getString("url");
		}
		return null;
	}
}
