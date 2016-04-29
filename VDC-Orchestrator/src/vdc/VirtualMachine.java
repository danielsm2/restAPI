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
	
	private String flavorName;
	
	/** The name of the image of the operating system. */
	private String imageID;
	
	/** The amount of requested memory. */
    private transient String memory;
    
	/** The amount of requested disk. */
	private transient String disk;
	
	/** The number of requested cpu cores. */
	private transient String cpus;
	
	/**
	 * Creates a new virtual machine instance
	 * @param label - the label of the virtual machine
	 * @param flavorID - the flavor ID of the virtual machine
	 * @param flavorName - the flavor name of the virtual machine
	 * @param image -  the imagine related to the virtual machine
	 */
	public VirtualMachine(String label, String flavorID, String flavorName, String image){
		this.label = label;
		this.flavorID = flavorID;
		this.flavorName = flavorName;
		this.imageID = image;
	}
	
	/**
	 * Set the ID of the virtual machine
	 * @param id - the new ID of the virtual machine
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/**
	 * Set the CPU of the virtual machine
	 * @param cpu - the new cpu value of the virtual machine
	 */
	public void setCPU(String cpu){
		this.cpus = cpu;
	}
	
	/**
	 * Set the Mem of the virtual machine 
	 * @param memory - the new mem value of the virtual machine
	 */
	public void setMem(String memory){
		this.memory = memory;
	}
	
	/**
	 * Set the disk of the virtual machine
	 * @param disk - the new disk of the virtual machine
	 */
	public void setDisk(String disk){
		this.disk = disk;
	}
	
	/**
	 * Get the ID related to the virtual machine
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Get the label related to the virtual machine
	 * @return
	 */
	public String getLabel(){
		return label;
	}
	
	/**
	 * Get the flavor ID related to the virtual machine
	 * @return
	 */
	public String getFlavorID(){
		return flavorID;
	}
	
	/**
	 * Get the image related to the virtual machine
	 * @return
	 */
	public String getImage(){
		return imageID;
	}
	
	/**
	 * Get the flavor name related to the virtual machine
	 * @return
	 */
	public String getFlavorName(){
		return flavorName;
	}
	/*public String getInfoMachine(){
		Iterator<String> it = VMS.keySet().iterator();
		String result = "";
		while(it.hasNext()){
			result = result + "\n" + it.next() +" : " +VMS.get(it.next());
		}
		return result;
	}*/
	
	/**
	 * Print the virtual machine information
	 */
	public void printInfo(){
		System.out.println(" label : " + label + " flavorID : " + flavorID + "flavorName: " + flavorName + " image : " + imageID);
	}
	
	/**
	 * Check the information given in order to find incomplete fields
	 * @return
	 */
	public ErrorCheck checkVM(){
		if(label.isEmpty() || flavorID.isEmpty() || flavorName.isEmpty() || imageID.isEmpty() ||
				label == null || flavorID == null || imageID == null){
			return ErrorCheck.VM_NOT_COMPLETED;
		}
		return ErrorCheck.ALL_OK;
	}
	
	/**
	 * Check if the current virtual machine instance is referenced at DB
	 * @param rs - the result of the select
	 * @return
	 */
	public boolean checkRowDBVM(ResultSet rs){
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