package vdc;

import java.util.ArrayList;

/** The VirtualNode class represents a virtual node requested within a VDC instance. It is composed by a collection of Virtual Machines (VMs)
 * that are collocated onto the same physical rack at the DC infrastructure.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VirtualNode {
	
	/** The ID of the virtual node. */
	private String vNodeID;
	
	/** A string representing the label (i.e. name) given to the virtual node. */
	private String vNodeLabel;
	
	/**  An ArryList representing the collection of virtual machines (VMs) requested within the virtual node. */
	private ArrayList<VM> vms;

	/** Creates a new VirtualNode object passing as parameters the ID of the virtual node and its label. It initially sets the collection of vms to an empty list.
	 *
	 * @param vNodeID - A String representing the ID of the virtual node.
	 * @param vNodeLabel - A String representing the label of the virtual node.
	 */
	public VirtualNode(String vNodeID, String vNodeLabel) {
		this.vNodeID = vNodeID;
		this.vNodeLabel = vNodeLabel;
		this.vms = new ArrayList<VM>(0);
	}
	
	/** Creates a new VirtualNode object passing as parameters the ID of the virtual node and its label and the collection of VMs requested within the virtual node.
	 *
	 * @param vNodeID - A String representing the ID of the virtual node.
	 * @param vNodeLabel - A String representing the label of the virtual node.
	 * @param vms - An ArrayList&ltVM&gt containing the VMs requested within the virtual node.
	 */
	public VirtualNode(String vNodeID, String vNodeLabel, ArrayList<VM> vms) {
		this.vNodeID = vNodeID;
		this.vNodeLabel = vNodeLabel;
		this.vms = vms;
	}

	/** Obtains the ID of the virtual node.
	 *
	 * @return A String representing the ID of the virtual node.
	 */
	public String getvNodeID() {
		return vNodeID;
	}

	/** Updates the ID of the virtual node.
	 *
	 * @param vNodeID - A String representing the new ID of the virtual node.
	 */
	public void setvNodeID(String vNodeID) {
		this.vNodeID = vNodeID;
	}

	/** Obtains the label of the virtual node.
	 *
	 * @return A String representing the label of the virtual node.
	 */
	public String getvNodeLabel() {
		return vNodeLabel;
	}

	/** Updates the label of the virtual node.
	 *
	 * @param vNodeLabel - A String representing the new label of the virtual node.
	 */
	public void setvNodeLabel(String vNodeLabel) {
		this.vNodeLabel = vNodeLabel;
	}

	/** Obtains the collection of VMs of the virtual node.
	 *
	 * @return An ArrayList&ltVM&gt containing the VMs requested within the virtual node.
	 */
	public ArrayList<VM> getVMs() {
		return vms;
	}

	/** Updates the collection of VMs of the virtual node.
	 *
	 * @param vms - An ArrayList&ltVM&gt containing the new collection of VMs.
	 */
	public void setVMs(ArrayList<VM> vms) {
		this.vms = vms;
	}
	
	/** Adds a new VM to the VM collection of the virtual node.
	 * 
	 * @param vm - The VM to be added to the collection.
	 */
	public void addVM(VM vm) {
		vms.add(vm);
	}
	
	/** Obtains the VM at the specified position.
	 * 
	 * @param index - Integer stating the position of the VM to return.
	 * @return The VM at the specified position in the list.
	 */
	public VM getVM(int index) {
		return vms.get(index);
	}
	
	/** Eliminates the VM at the specified position.
	 * 
	 * @param index - Integer stating the position of the VM to be eliminated.
	 */
	public void removeVM(int index) {
		vms.remove(index);
	}
	
	/** Replaces the VM at the specified position in the list with the specified VM.
	 * 
	 * @param index - Integer stating the position of the VM to be replaced.
	 * @param vm - The VM to be stored in the specified position.
	 */
	public void replaceVM(int index, VM vm) {
		vms.set(index, vm);
	}
}