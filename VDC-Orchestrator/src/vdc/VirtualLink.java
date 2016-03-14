package vdc;

/** The VirtualLink class represents a virtual link requested within a VDC instance. It states the desired bit-rate between a (source, destination) pair of virtual nodes.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */

public class VirtualLink {
	
	/** The ID of the virtual link */
	private String vLinkID;
	
	/** The bit-rate requested by the virtual link. */
	private double bitrate;
	
	/** The source virtual node of the virtual link. */
	private VirtualNode source;
	
	/** The destination virtual node of the virtual link. */
	private VirtualNode destination;
	
	/** Creates a new VirtualLink object passing as parameters the id of the virtual link, the requested bit-rate and both source and destination virtual nodes for the virtual link.
	 *
	 * @param vLinkID - A String representing the ID of the virtual link.
	 * @param bitrate - A double stating the requested bit-rate.
	 * @param source - A VirtualNode object representing the source of the virtual link.
	 * @param destination - A VirtualNode object representing the destination of the virtual link.
	 */
	public VirtualLink(String vLinkID, double bitrate, VirtualNode source, VirtualNode destination) {
		this.vLinkID = vLinkID;
		this.bitrate = bitrate;
		this.source = source;
		this.destination = destination;
	}
	
	/** Obtains the ID of the virtual link.
	 *
	 * @return A String representing the ID of the virtual link. 
	 */
	public String getvLinkID() {
		return vLinkID;
	}
	
	/** Updates the ID of the virtual link.
	 *
	 * @param vLinkID - A String representing the new ID of the virtual link.
	 */
	public void setvLinkID(String vLinkID) {
		this.vLinkID = vLinkID;
	}
	
	/** Obtains the bit-rate requested by the virtual link.
	 *
	 * @return A double stating the bit-rate requested by the virtual link.
	 */
	public double getBitrate() {
		return bitrate;
	}
	
	/** Updates the bit-rate requested by the virtual link.
	 *
	 * @param bitrate - A double stating the new bit-rate of the virtual link.
	 */
	public void setBitrate(double bitrate) {
		this.bitrate = bitrate;
	}
	
	/** Obtains the source virtual node of the virtual link.
	 *
	 * @return A VirtualNode object representing the source of the virtual link.
	 */
	public VirtualNode getSource() {
		return source;
	}
	
	/** Updates the source virtual node of the virtual link.
	 *
	 * @param source - A VirtualNode object representing the new source of the virtual link.
	 */
	public void setSource(VirtualNode source) {
		this.source = source;
	}
	
	/** Obtains the destination virtual node of the virtual link.
	 *
	 * @return the destination
	 */
	public VirtualNode getDestination() {
		return destination;
	}
	
	/** Updates the destination virtual node of the virtual link.
	 *
	 * @param destination - A VirtualNode object representing the new destination of the virtual link.
	 */
	public void setDestination(VirtualNode destination) {
		this.destination = destination;
	}
}