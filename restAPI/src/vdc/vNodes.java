package vdc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;

public class vNodes {
	

	private String id;
	private String label;
	private List<VMS> vms = new ArrayList<VMS>();
	private DataBase db;
	private PreparedStatement ps;
	
	public vNodes(String id, String label){
		this.id = id;
		this.label = label;
	}
	
	public void addVM(VMS vm){
		vms.add(vm);
	}
	
	/*public String getInfoLinks(){
		Iterator<String> it = vNodes.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() + " : " + vNodes.get(it.next());  
		}
		return result;
	}*/
	
	public void printInfo(){
		System.out.println("id : " + id + " label : " + label);
		System.out.println("vms : " );
		for(VMS v : vms){
			v.printInfo();
		}
	}

	public String getInfo(boolean insert){
		String updateVnode = label;
		String insertVnode = id + "','" + label;
		if (insert)
			return insertVnode;
		return updateVnode;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void updateVM() throws SQLException{
		//db.startDB();
		db = DataBase.getInstance();
		ResultSet rs;
		for(VMS aux : vms){
			ps = db.prepareStatement("SELECT id FROM vm WHERE id = ?");
			ps.setString(1, aux.getId());
			rs = db.checkEntryDB(ps); 
			if(aux.checkRow(rs)){
			    String id = aux.getId();
				String label = aux.getLabel();
				String flavor = aux.getFlavor();
				String image = aux.getImage();
				db.newEntryDB("INSERT INTO vm VALUES ('" + id + "','" + label + "','" + flavor + "','" + image + "','" + this.id + "')");
				System.out.println("insert vm");
			}
			else{
				ps = db.prepareStatement("UPDATE vm SET label=?,flavorID=?,imageID=? WHERE id=?");
				ps.setString(1, aux.getLabel());
				ps.setString(2, aux.getFlavor());
				ps.setString(3, aux.getImage());
				ps.setString(4, aux.getId());
				ps.executeUpdate();
				System.out.println("update vm");
			}
		}
		//db.stopDB();
	}
	
	public String getId(){
		return id;
	}
	
	public ErrorCheck check_vnode(){
		if(id.isEmpty() || label.isEmpty())
			return ErrorCheck.VNODE_NOT_COMPLETED;
		ErrorCheck ec;
		for(VMS aux : vms){
			ec = aux.check_vm();
			if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
				return ec;
			}
		}
		return ErrorCheck.ALL_OK;
	}
	
	public boolean checkRow(ResultSet rs){
		try{
			while(rs.next()){
				if(rs.getString("id").equals(id))
					return false;
			}
		}catch(SQLException e){
			System.err.println(e);
		}
		return true;
	}
}
