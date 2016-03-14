package vdc;

import java.util.ArrayList;

/** The VDC class represents a Virtual Data Center (VDC) instance requested by a tenant/user through the dashboard service of the DC infrastructure.
 * It is composed by a set of virtual nodes interconnected through a set of virtual links. At its turn, each virtual node is composed by a set of Virtual Machines (VMs).
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VDC {
	
	/** The ID of the tenant. */
	private String TenantID;
	
	/**  An ArryList representing the collection of virtual nodes requested within the VDC instance. */
	private ArrayList<VirtualNode> vnodes;
	
	/**  An ArryList representing the collection of virtual links requested within the VDC instance. */
	private ArrayList<VirtualLink> vlinks;
	
	/** Creates a new VDC object passing as a parameter the ID of the tenant requesting the VDC instance. It initially sets the collection of virtual nodes and links
	 * to empty lists.
	 * 
	 * @param TenantID - A String representing the ID of the tenant.
	 */
	public VDC(String TenantID){
		this.TenantID = TenantID;
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
	public VDC(String TenantID, ArrayList<VirtualNode> vnodes, ArrayList<VirtualLink> vlinks){
		this.TenantID = TenantID;
		this.vnodes = vnodes;
		this.vlinks = vlinks;
	}
	
	/** Obtains the ID of the tenant.
	 * 
	 * @return A string representing the ID of the tenant.
	 */
	public String getTenantID() {
		return TenantID;
	}

	/** Updates the ID of the tenant.
	 * 
	 * @param tenantID - A string representing the new ID of the tenant.
	 */
	public void setTenantID(String tenantID) {
		TenantID = tenantID;
	}

	/** Obtains the collection of virtual nodes of the VDC.
	 *
	 * @return An ArrayList&ltVirtualNode&gt containing the virtual nodes requested within the VDC.
	 */
	public ArrayList<VirtualNode> getVnodes() {
		return vnodes;
	}

	/** Updates the collection of virtual nodes of the VDC.
	 *
	 * @param vnodes - An ArrayList&ltirtualNode&gt containing the new collection of virtual nodes.
	 */
	public void setVnodes(ArrayList<VirtualNode> vnodes) {
		this.vnodes = vnodes;
	}

	/** Adds a new virtual node to the virtual node collection of the VDC.
	 * 
	 * @param vnode - The virtual node to be added to the collection.
	 */
	public void addVirtualNode(VirtualNode vnode) {
		vnodes.add(vnode);
	}
	
	/** Obtains the virtual node at the specified position.
	 * 
	 * @param index - Integer stating the position of the virtual node to return.
	 * @return The virtual node at the specified position in the list.
	 */
	public VirtualNode getVirtualNode(int index) {
		return vnodes.get(index);
	}
	
	/** Eliminates the virtual node at the specified position.
	 * 
	 * @param index - Integer stating the position of the virtual node to be eliminated.
	 */
	public void removeVirtualNode(int index) {
		vnodes.remove(index);
	}
	
	/** Replaces the virtual node at the specified position in the list with the specified virtual node.
	 * 
	 * @param index - Integer stating the position of the virtual node to be replaced.
	 * @param vnode - The virtual node to be stored in the specified position.
	 */
	public void replaceVirtualNode(int index, VirtualNode vnode) {
		vnodes.set(index, vnode);
	}
	
	/** Obtains the collection of virtual links of the VDC.
	 *
	 * @return An ArrayList&ltVirtualLink&gt containing the virtual links requested within the VDC.
	 */
	public ArrayList<VirtualLink> getVlinks() {
		return vlinks;
	}

	/** Updates the collection of virtual links of the VDC.
	 *
	 * @param vlinks - An ArrayList&ltirtualLink&gt containing the new collection of virtual links.
	 */
	public void setVlinks(ArrayList<VirtualLink> vlinks) {
		this.vlinks = vlinks;
	}
	
	/** Adds a new virtual link to the virtual link collection of the VDC.
	 * 
	 * @param vlink - The virtual link to be added to the collection.
	 */
	public void addVirtualLink(VirtualLink vlink) {
		vlinks.add(vlink);
	}
	
	/** Obtains the virtual link at the specified position.
	 * 
	 * @param index - Integer stating the position of the virtual link to return.
	 * @return The virtual link at the specified position in the list.
	 */
	public VirtualLink getVirtualLink(int index) {
		return vlinks.get(index);
	}
	
	/** Eliminates the virtual link at the specified position.
	 * 
	 * @param index - Integer stating the position of the virtual link to be eliminated.
	 */
	public void removeVirtualLink(int index) {
		vlinks.remove(index);
	}
	
	/** Replaces the virtual link at the specified position in the list with the specified virtual link.
	 * 
	 * @param index - Integer stating the position of the virtual link to be replaced.
	 * @param vlink - The virtual link to be stored in the specified position.
	 */
	public void replaceVirtualLink(int index, VirtualLink vlink) {
		vlinks.set(index, vlink);
	}
}