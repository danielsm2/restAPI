package vdc;

import java.sql.ResultSet;
import java.sql.SQLException;

/** The VM class represents a virtual machine requested within a virtual node. Each VM is characterized by the requested computer resources (virtual cpus, memory and disk)
 * in the form of a OpenStack Nova flavor as well as the image of the operating system.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VirtualMachine {

	/** The ID of the virtual machine. */
    private transient String id;
    
	private String label;
	
	/** The name of the requested flavor. */
	private String flavorID;
	
	/** The name of the image of the operating system. */
	private String imageID;
	
	/** The amount of requested memory. */
    private transient String memory;
    
	/** The amount of requested disk. */
	private transient String disk;
	
	/** The number of requested cpu cores. */
	private transient String cpus;
	
	public VirtualMachine(String label, String flavor, String image){
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
		System.out.println(" label : " + label + " flavor : " + flavorID + " image : " + imageID);
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
