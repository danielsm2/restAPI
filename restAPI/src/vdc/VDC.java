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
	private PreparedStatement ps;
	private DataBase db = DataBase.getInstance();
	
	public void addTenant(String tenant_id){
		this.tenantID = tenant_id;
	}
	
	public void addNodes(vNodes vnodes){
		this.vnodes.add(vnodes);
	}
	
	public void addLinks(vLinks vlinks){
		boolean introduce = true;
		for(vLinks aux : this.vlinks){
			if(vlinks.getId().equals(aux.getId()))
				introduce = false;	
		}
		if(introduce)
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

	public void updateVdc() throws SQLException{
		//db.startDB();
		db.newEntryDB("INSERT INTO vdc VALUES ('" + tenantID + "')");
		//db.stopDB();
	}
	
	public void updateVnode(int i, boolean insert) throws SQLException{	
		//db.startDB();
		if(insert){
			String id = vnodes.get(i).getId();
			String label = vnodes.get(i).getLabel();
			db.newEntryDB("INSERT INTO vnode VALUES ('" + id + "','" + label + "','" + tenantID + "')");
			System.out.println("insert vnode");
		}			
		else{
			ps = db.prepareStatement("UPDATE vnode SET label = ? WHERE id= ?");
			ps.setString(1, vnodes.get(i).getLabel());
			ps.setString(2, vnodes.get(i).getId());
			ps.executeUpdate();
			System.out.println("update vnode");
		}
		vnodes.get(i).updateVM();
		//db.stopDB();
	}
	
	public void updateVlink(int i, boolean insert) throws SQLException{
		//db.startDB();
		if(insert){
			String id = vlinks.get(i).getId();
			String bandwith = vlinks.get(i).getBandwith();
			String to = vlinks.get(i).getDestination();
			String from = vlinks.get(i).getSource();
			db.newEntryDB("INSERT INTO vlink VALUES ('" + id + "','" + bandwith + "','" + to + "','" +from + "')");
			System.out.println("insert vlink");
		}
		else{
			ps = db.prepareStatement("UPDATE vlink SET bandwith=?,fk_to=?,fk_from=? WHERE id=?");
			ps.setString(1, vlinks.get(i).getBandwith());
			ps.setString(2, vlinks.get(i).getDestination());
			ps.setString(3, vlinks.get(i).getSource());
			ps.setString(4, vlinks.get(i).getId());
			ps.executeUpdate();
			System.out.println("update vlink");
		}
		//db.stopDB();
	}
	
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
	
	public PreparedStatement entryCheckerDB_vdc() throws SQLException{
		ps = db.prepareStatement("SELECT tenantID FROM vdc WHERE tenantID = ?");
		ps.setString(1, tenantID);
		return ps;
	}
	
	public PreparedStatement entryCheckerDB_vnode(int i) throws SQLException{
		ps = db.prepareStatement("SELECT id FROM vnode WHERE id=?");
		ps.setString(1, vnodes.get(i).getId());
		return ps;
	}
	
	public PreparedStatement entryCheckerDB_vlink(int i) throws SQLException{
		ps = db.prepareStatement("SELECT id FROM vlink WHERE id=?");
		ps.setString(1, vlinks.get(i).getId());
		return ps;	
	}
	
	public ErrorCheck checkVDC(){
		if(tenantID.isEmpty()){
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
	
	public boolean checkRow_vnode(ResultSet rs, int i){
		return vnodes.get(i).checkRow(rs);
	}
	
	public boolean checkRow_vlink(ResultSet rs, int i){
		return vlinks.get(i).checkRow(rs);
	}
	
	public String getIdVnode(int i){
		return vnodes.get(i).getId();
	}
	
	public String getIdVlink(int i){
		return vlinks.get(i).getId();
	}
	
}
