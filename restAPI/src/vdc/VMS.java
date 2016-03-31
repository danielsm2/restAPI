package vdc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VMS {

    private transient String id;
	private String label;
	private String flavorID;
	private String imageID;
    private transient String memory;
	private transient String disk;
	private transient String cpus;
	
	public VMS(String label, String flavor, String image){
		this.label = label;
		this.flavorID = flavor;
		this.imageID = image;
	}
	
	public String getId(){
		return id;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getFlavor(){
		return flavorID;
	}
	
	public String getImage(){
		return imageID;
	}
	/*public String getInfoMachine(){
		Iterator<String> it = VMS.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() +" : " +VMS.get(it.next());
		}
		return result;
	}*/
	
	public void printInfo(){
		System.out.println("id : " + id + " label : " + label + " flavor : " + flavorID + " image : " + imageID);
	}
	
	public ErrorCheck check_vm(){
		if(label.isEmpty() || flavorID.isEmpty() || imageID.isEmpty() ||
				label == null || flavorID == null || imageID == null){
			return ErrorCheck.VM_NOT_COMPLETED;
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
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setCPU(String cpu){
		this.cpus = cpu;
	}
	
	public void setMem(String memory){
		this.memory = memory;
	}
	
	public void setDisk(String disk){
		this.disk = disk;
	}
}
