package vdc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;

/** The VDC class represents a Virtual Data Center (VDC) instance requested by a tenant/user through the dashboard service of the DC infrastructure.
 * It is composed by a set of virtual nodes interconnected through a set of virtual links. At its turn, each virtual node is composed by a set of Virtual Machines (VMs).
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VDC {

	/** The ID of the tenant. */
	private String tenantID;
	
	/**  An ArryList representing the collection of virtual nodes requested within the VDC instance. */
	private List<VirtualNode> vnodes = new ArrayList<VirtualNode>();
	
	/**  An ArryList representing the collection of virtual links requested within the VDC instance. */
	private List<VirtualLink> vlinks = new ArrayList<VirtualLink>();
	
	/** Creates a new VDC object passing as a parameter the ID of the tenant requesting the VDC instance. It initially sets the collection of virtual nodes and links
	 * to empty lists.
	 * 
	 * @param TenantID - A String representing the ID of the tenant.
	 */
	public VDC(String tenantID){
		this.tenantID = tenantID;
		this.vnodes = new ArrayList<VirtualNode>(0);
		this.vlinks = new ArrayList<VirtualLink>(0);
	}

	/** Creates a new VDC object passing as a parameter the ID of the tenant requesting the VDC instance as well as the collections of virtual nodes and links
	 * requested within the VDC.
	 * 
	 * @param TenantID - A String representing the ID of the tenant.
	 * @param vnodes - An ArrayList&ltVirtualNode&gt containing the virtual nodes requested within the VDC.
	 * @param vlinks - An  ArrayList&ltVirtualLink&gt containing the virtual links requested within the VDC.
	 */
	public VDC(String tenantID, ArrayList<VirtualNode> vnodes, ArrayList<VirtualLink> vlinks){
		this.tenantID = tenantID;
		this.vnodes = vnodes;
		this.vlinks = vlinks;
	}
	
	/**
	 * Creates an empty instance of a VDC.
	 */
	public VDC(){
		
	}
	
	/** Obtains the ID of the tenant.
	 * 
	 * @return A string representing the ID of the tenant.
	 */
	public String getTenant(){
		return tenantID;
	}
	
	/** Updates the ID of the tenant.
	 * 
	 * @param tenantID - A string representing the new ID of the tenant.
	 */
	public void setTenant(String tenant_id){
		this.tenantID = tenant_id;
	}
	
	/** Updates the collection of virtual nodes of the VDC.
	 *
	 * @param vnodes - An ArrayList&ltirtualNode&gt containing the new collection of virtual nodes.
	 */
	public void addNodes(VirtualNode vnodes){
		this.vnodes.add(vnodes);
	}
	
	/** Updates the collection of virtual links of the VDC.
	 *
	 * @param vlinks - An ArrayList&ltirtualLink&gt containing the new collection of virtual links.
	 */
	public void addLinks(VirtualLink vlinks){
		this.vlinks.add(vlinks);
	}
	
	/**
	 * Obtain the virtual node identified by the name of the virtual node.
	 * @param id the name of the virtual node
	 * @return
	 */
	public VirtualNode getVNodeByName(String id){
		for(VirtualNode aux : vnodes)
			if(aux.getId().equals(id))
				return aux;
		return null;
	}
	
	/**
	 * Obtain the virtual node identified by a list position 
	 * @param i the position of the list
	 * @return
	 */
	public VirtualNode getVNodeByPos(int i){
		return vnodes.get(i);
	}
	
	/**
	 * Obtain the virtual link identified by a list position
	 * @param i the position of the list
	 * @return
	 */
	public VirtualLink getVLinkByPos(int i){
		return vlinks.get(i);
	}
	
	/**
	 * Get the virtual node id
	 * @param i the list position of the virtual node
	 * @return
	 */
	public String getVNodeID(int i){
		return vnodes.get(i).getId();
	}
	
	/**
	 * Get the virtual link id
	 * @param i the list position of the virtual link
	 * @return
	 */
	public String getVLinkID(int i){
		return vlinks.get(i).getID();
	}
	
	/**
	 * Obtain the number of virtual node in a VDC
	 * @return
	 */
	public int getSizeVNode(){
		return vnodes.size();
	}
	
	/**
	 * Obtain the number of virtual link in a VDC
	 * @return
	 */
	public int getSizeVLink(){
		return vlinks.size();
	}

	/**
	 * Performs an insert against the DB, more precisely, VDC table 
	 * @throws SQLException
	 */
	public void updateVdc() throws SQLException{
		DataBase db = DataBase.getInstance();
		db.newEntryDB("INSERT INTO vdc VALUES ('" + tenantID + "')");
	}
	
	/**
	 * Performs an insert or update against the DB, more precisely, virtual node table
	 * @param i the position in the list
	 * @param insert = If true insert else update
	 * @throws SQLException
	 */
	public void updateVnode(int i, boolean insert) throws SQLException{	
		DataBase db = DataBase.getInstance();
		if(insert){
			String id = vnodes.get(i).getId();
			String label = vnodes.get(i).getLabel();
			db.newEntryDB("INSERT INTO vnode VALUES ('" + id + "','" + label + "','" + tenantID + "')");
		}			
		else{
			PreparedStatement ps = db.prepareStatement("UPDATE vnode SET label = ? WHERE id= ?");
			ps.setString(1, vnodes.get(i).getLabel());
			ps.setString(2, vnodes.get(i).getId());
			ps.executeUpdate();
		}
		for(VirtualNode aux : vnodes)
			aux.assignIDtoVM();
		vnodes.get(i).updateVM();
	}
	
	/**
	 * Performs an insert or update against the DB, more precisely, virtual link table
	 * @param i the position in the list
	 * @param insert = If true insert else update
	 * @throws SQLException
	 */
	public ErrorCheck updateVlink(int i, boolean insert) throws SQLException{
		DataBase db = DataBase.getInstance();
		ErrorCheck ec = validateVLinkReferences(vlinks.get(i).getDestination(),vlinks.get(i).getSource());
		if(insert && ec.equals(ErrorCheck.ALL_OK)){
			String id = vlinks.get(i).getID();
			String bandwith = vlinks.get(i).getBandwith();
			String to = vlinks.get(i).getDestination();
			String from = vlinks.get(i).getSource();
			db.newEntryDB("INSERT INTO vlink VALUES ('" + id + "','" + bandwith + "','" + to + "','" +from + "')");
		}
		else if(!insert && ec.equals(ErrorCheck.ALL_OK)){
			PreparedStatement ps = db.prepareStatement("UPDATE vlink SET bandwith=?,fk_to=?,fk_from=? WHERE id=?");
			ps.setString(1, vlinks.get(i).getBandwith());
			ps.setString(2, vlinks.get(i).getDestination());
			ps.setString(3, vlinks.get(i).getSource());
			ps.setString(4, vlinks.get(i).getID());
			ps.executeUpdate();
		}
		return ec;
	}
	
	/**
	 * Check if the virtual link references of a virtual node exists
	 * @param to Reference of a destination virtual node
	 * @param from Reference of a source virtual node
	 * @return
	 */
	private ErrorCheck validateVLinkReferences(String to, String from){
		boolean retVal1 = false;
		boolean retVal2 = false;
		for( VirtualNode aux : vnodes ){
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
	 * Print the VDC structure defined
	 */
	public void printInfo(){
		System.out.println("tenant_id : " + tenantID);
		for(VirtualNode vn : vnodes){
			System.out.println("vnodes : " );
			vn.printInfo();
		}
		
		for(VirtualLink vl : vlinks){
			System.out.println("vlinks : ");
			vl.printInfo();
		}
	}
	
	/**
	 * Prepare a select statement against VDC table 
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareSelectVDC() throws SQLException{
		DataBase db = DataBase.getInstance();
		PreparedStatement ps = db.prepareStatement("SELECT tenantID FROM vdc WHERE tenantID = ?");
		ps.setString(1, tenantID);
		return ps;
	}
	
	/**
	 * Prepare a select statement against virtual node table
	 * @param i the list position of the virtual node
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareSelectVNode(int i) throws SQLException{
		VirtualNode aux = vnodes.get(i);
		aux.setID(tenantID+":"+aux.getId());
		DataBase db = DataBase.getInstance();
		PreparedStatement ps = db.prepareStatement("SELECT id FROM vnode WHERE id=?");
		ps.setString(1, aux.getId());
		return ps;
	}
	
	/**
	 * Prepare a select statement against virtual link table
	 * @param i the list position of the virtual link
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareSelectVLink(int i) throws SQLException{
		VirtualLink aux = vlinks.get(i);
		aux.setID(tenantID+":"+aux.getID());
		aux.setDestination(tenantID+":"+aux.getDestination());
		aux.setSource(tenantID+":"+aux.getSource());
		DataBase db = DataBase.getInstance();
		PreparedStatement ps = db.prepareStatement("SELECT id FROM vlink WHERE id=?");
		ps.setString(1, aux.getID());
		return ps;	
	}
	
	/**
	 * Check the vdc defined in order to find incomplete fields
	 * @return
	 */
	public ErrorCheck checkVDC(){
		if(tenantID.isEmpty() || tenantID == null){
			return ErrorCheck.VDC_NOT_COMPLETED;
		}	
		else{
			ErrorCheck ec;
			for(VirtualNode aux : vnodes){
				ec = aux.checkVNode();
				if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED) || ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
					return ec;
				}
			}
			for(VirtualLink aux : vlinks){
				ec = aux.checkVLink();
				if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
					return ec;
				}
			}
			return ErrorCheck.ALL_OK;
		}
	}
	
	/**
	 * Check if exist an equal tenantID at VDC table
	 * @param rs the result of the select
	 * @return
	 */
	public boolean checkRowDBVDC(ResultSet rs){
		try{
			while(rs.next()){
				if(rs.getString("tenantID").equals(tenantID))
					return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Check if exist an equal virtual node at virtual node table
	 * @param rs the result of the select
	 * @param i the list position of the virtual node
	 * @return
	 */
	public boolean checkRowDBVNode(ResultSet rs, int i){
		return vnodes.get(i).checkRowDBVNode(rs);
	}
	
	/**
	 * Check if exist an equal virtual link at virtual link table
	 * @param rs the result of the select
	 * @param i the list position of the virtual link
	 * @return
	 */
	public boolean checkRowDBVLink(ResultSet rs, int i){
		return vlinks.get(i).checkRow(rs);
	}
}
