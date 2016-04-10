package vdc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;


public class VDC {

	private String tenantID;
	private List<vNodes> vnodes = new ArrayList<vNodes>();
	private List<vLinks> vlinks = new ArrayList<vLinks>();
	
	public void addTenant(String tenant_id){
		this.tenantID = tenant_id;
	}
	
	public void addNodes(vNodes vnodes){
		this.vnodes.add(vnodes);
	}
	
	public void addLinks(vLinks vlinks){
		this.vlinks.add(vlinks);
	}
	
	public vNodes getVnode(String id){
		for(vNodes aux : vnodes)
			if(aux.getId().equals(id))
				return aux;
		return null;
	}
	
	public int getNumElemVnode(){
		return vnodes.size();
	}
	
	public int getNumElemVlink(){
		return vlinks.size();
	}
	
	public String getTenant(){
		return tenantID;
	}

	/**
	 * Se realiza un insert contra la tabla vdc de la base de datos
	 * @throws SQLException
	 */
	public void updateVdc() throws SQLException{
		DataBase db = DataBase.getInstance();
		db.newEntryDB("INSERT INTO vdc VALUES ('" + tenantID + "')");
	}
	
	/**
	 * Se reliza una accion contra la tabla vnode de la base de datos
	 * @param i
	 * @param insert = Si true: insert, sino update
	 * @throws SQLException
	 */
	public void updateVnode(int i, boolean insert) throws SQLException{	
		DataBase db = DataBase.getInstance();
		if(insert){
			String id = vnodes.get(i).getId();
			String label = vnodes.get(i).getLabel();
			db.newEntryDB("INSERT INTO vnode VALUES ('" + id + "','" + label + "','" + tenantID + "')");
			//System.out.println("insert vnode");
		}			
		else{
			PreparedStatement ps = db.prepareStatement("UPDATE vnode SET label = ? WHERE id= ?");
			ps.setString(1, vnodes.get(i).getLabel());
			ps.setString(2, vnodes.get(i).getId());
			ps.executeUpdate();
			//System.out.println("update vnode");
		}
		for(vNodes aux : vnodes)
			aux.assignIDtoVM();
		vnodes.get(i).updateVM();
	}
	
	/**
	 * Se realiza una accion contra la tabla vlink de la base de datos
	 * @param i
	 * @param insert = Si true: insert, sino update
	 * @throws SQLException
	 */
	public ErrorCheck updateVlink(int i, boolean insert) throws SQLException{
		DataBase db = DataBase.getInstance();
		ErrorCheck ec = validateVnode(vlinks.get(i).getDestination(),vlinks.get(i).getSource());
		if(insert && ec.equals(ErrorCheck.ALL_OK)){
			String id = vlinks.get(i).getId();
			String bandwith = vlinks.get(i).getBandwith();
			String to = vlinks.get(i).getDestination();
			String from = vlinks.get(i).getSource();
			db.newEntryDB("INSERT INTO vlink VALUES ('" + id + "','" + bandwith + "','" + to + "','" +from + "')");
			//System.out.println("insert vlink");
		}
		else if(!insert && ec.equals(ErrorCheck.ALL_OK)){
			PreparedStatement ps = db.prepareStatement("UPDATE vlink SET bandwith=?,fk_to=?,fk_from=? WHERE id=?");
			ps.setString(1, vlinks.get(i).getBandwith());
			ps.setString(2, vlinks.get(i).getDestination());
			ps.setString(3, vlinks.get(i).getSource());
			ps.setString(4, vlinks.get(i).getId());
			ps.executeUpdate();
			//System.out.println("update vlink");
		}
		return ec;
	}
	
	private ErrorCheck validateVnode(String to, String from){
		boolean retVal1 = false;
		boolean retVal2 = false;
		for( vNodes aux : vnodes ){
			if(aux.getId().equals(to))
				retVal1 = true;
			else if(aux.getId().equals(from))
				retVal2 = true;
		}
		if(retVal1 && retVal2)
			return ErrorCheck.ALL_OK;
		else
			return ErrorCheck.VNODE_FROM_VLINK_WRONG;
	}
	
	/**
	 * Imprime la estructura vdc
	 */
	public void printInfo(){
		System.out.println("tenant_id : " + tenantID);
		for(vNodes vn : vnodes){
			System.out.println("vnodes : " );
			vn.printInfo();
		}
		
		for(vLinks vl : vlinks){
			System.out.println("vlinks : ");
			vl.printInfo();
		}
	}
	
	/**
	 * Se hace una query contra la tabla vdc de la base de datos 
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement entryCheckerDB_vdc() throws SQLException{
		DataBase db = DataBase.getInstance();
		PreparedStatement ps = db.prepareStatement("SELECT tenantID FROM vdc WHERE tenantID = ?");
		ps.setString(1, tenantID);
		return ps;
	}
	
	/**
	 * Se hace una query contra la tabla vnode de la base de datos
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement entryCheckerDB_vnode(int i) throws SQLException{
		DataBase db = DataBase.getInstance();
		PreparedStatement ps = db.prepareStatement("SELECT id FROM vnode WHERE id=?");
		ps.setString(1, vnodes.get(i).getId());
		return ps;
	}
	
	/**
	 * Se hace una query contra la tabla vlink de la base de datos
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement entryCheckerDB_vlink(int i) throws SQLException{
		DataBase db = DataBase.getInstance();
		PreparedStatement ps = db.prepareStatement("SELECT id FROM vlink WHERE id=?");
		ps.setString(1, vlinks.get(i).getId());
		return ps;	
	}
	
	/**
	 * Se hace un check del json informado una vez parseado a la estructura VDC
	 * @return
	 */
	public ErrorCheck checkVDC(){
		if(tenantID.isEmpty() || tenantID == null){
			return ErrorCheck.VDC_NOT_COMPLETED;
		}	
		else{
			ErrorCheck ec;
			for(vNodes aux : vnodes){
				ec = aux.check_vnode();
				if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED) || ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
					return ec;
				}
			}
			for(vLinks aux : vlinks){
				ec = aux.check_vlink();
				if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
					return ec;
				}
			}
			return ErrorCheck.ALL_OK;
		}
	}
	
	/**
	 * Comprueba si ya existe una entrada en la base de datos con el mismo id
	 * @param rs
	 * @return
	 */
	public boolean checkRow(ResultSet rs){
		try{
			while(rs.next()){
				if(rs.getString("tenantID").equals(tenantID))
					return false;
			}
		}catch(SQLException e){
			System.err.println(e);
		}
		return true;
	}
	
	/**
	 * Comprueba si ya existe una entrada en la base de datos con el mismo id
	 * @param rs
	 * @param i
	 * @return
	 */
	public boolean checkRow_vnode(ResultSet rs, int i){
		return vnodes.get(i).checkRow(rs);
	}
	
	/**
	 * Comprueba si ya existe una entrada en la base de datos con el mismo id
	 * @param rs
	 * @param i
	 * @return
	 */
	public boolean checkRow_vlink(ResultSet rs, int i){
		return vlinks.get(i).checkRow(rs);
	}
	
	/**
	 * Devuelve el id del vnode solicitado
	 * @param i
	 * @return
	 */
	public String getIdVnode(int i){
		return vnodes.get(i).getId();
	}
	
	/**
	 * Devuelve el id del vlink solicitado
	 * @param i
	 * @return
	 */
	public String getIdVlink(int i){
		return vlinks.get(i).getId();
	}
}
