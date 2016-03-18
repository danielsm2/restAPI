package vdc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VMS {

    private String id;
	private String label;
	private String flavorID;
	private String imageID;
	
	public VMS(String id, String label, String flavor, String image){
		this.id = id;
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
		if(id.isEmpty() || label.isEmpty() || flavorID.isEmpty() || imageID.isEmpty()){
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
	
	public void update(){
		
	}
}
