package vdc;

/** The VM class represents a virtual machine requested within a virtual node. Each VM is characterized by the requested computer resources (virtual cpus, memory and disk)
 * in the form of a OpenStack Nova flavor as well as the image of the operating system.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VM {
	
	/** The ID of the virtual machine. */
	private String vmID;
	
	/** The name of the requested flavor. */
	private String flavor;
	
	/** The amount of requested memory. */
	private double memory;
	
	/** The amount of requested disk. */
	private double disk;
	
	/** The number of requested cpu cores. */
	private int cpus;
	
	/** The name of the image of the operating system. */
	private String image;
	
	/** Creates a new VM object passing as parameters the ID of the virtual machine, the flavor, the requested resources (memory, disk and cpus) and the operating system image.
	 * 
	 * @param vmID - A String representing the ID of the VM.
	 * @param flavor - A String representing the name of the flavor.
	 * @param memory - A double stating the amount of requested memory resource.
	 * @param disk - A double stating the amount of requested disk resource.
	 * @param cpus - A integer stating the number of requested cpu cores.
	 * @param image - A String representing the name of the operating system image.
	 */
	public VM(String vmID, String flavor, double memory, double disk, int cpus, String image) {
		this.vmID = vmID;
		this.flavor = flavor;
		this.memory = memory;
		this.disk = disk;
		this.cpus = cpus;
		this.image = image;
	}

	/**
	 * Obtains the ID of the VM.
	 *
	 * @return A string representing the ID of the VM.
	 */
	public String getVmID() {
		return vmID;
	}

	/**
	 * Updates the ID of the VM.
	 *
	 * @param vmID - A String representing the new ID of the VM.
	 */
	public void setVmID(String vmID) {
		this.vmID = vmID;
	}

	/**
	 * Obtains the flavor of the VM.
	 *
	 * @return A String representing the name of the flavor.
	 */
	public String getFlavor() {
		return flavor;
	}

	/**
	 * Updates the flavor of the VM.
	 *
	 * @param flavor - A String representing the name of the new flavor.
	 */
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	/**
	 * Obtains the amount of memory requested by the VM.
	 *
	 * @return A double stating the amount of requested memory resource.
	 */
	public double getMemory() {
		return memory;
	}

	/**
	 * Updates the amount of memory requested by the VM.
	 *
	 * @param memory -  A double stating the new amount of requested memory resource.
	 */
	public void setMemory(double memory) {
		this.memory = memory;
	}

	/**
	 * Obtains the amount of disk requested by the VM.
	 *
	 * @return A double stating the amount of requested disk resource.
	 */
	public double getDisk() {
		return disk;
	}

	/**
	 * Updates the amount of disk requested by the VM.
	 *
	 * @param disk - A double stating the new amount of requested disk resource.
	 */
	public void setDisk(double disk) {
		this.disk = disk;
	}

	/**
	 * Obtains the number of cpu cores requested by the VM.
	 *
	 * @return An integer stating the number of requested cpu cores.
	 */
	public int getCpus() {
		return cpus;
	}

	/**
	 * Updates the number of cpu cores requested by the VM.
	 *
	 * @param cpus - An integer stating the new number of requested cpu cores.
	 */
	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	/**
	 * Obtains the name of the image of the operating system of the VM.
	 *
	 * @return A String representing the name of the image.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Updates the image of the operating system of the VM.
	 *
	 * @param image - A String representing the name of the new image.
	 */
	public void setImage(String image) {
		this.image = image;
	}
}