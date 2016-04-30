package topology;

public class Link {
	
	private Node src;
	private Node dest;
	
	/**
	 * Creates an empty instance of a link
	 */
	public Link(){}
	
	/**
	 * Creates an instance of a link representing the source and destination
	 * @param src
	 * @param dest
	 */
	public Link(Node src, Node dest){
		this.src = src;
		this.dest = dest;
	}
	
	/**
	 * Set the source and destination of the link
	 * @param src
	 * @param dest
	 */
	public void setLink(Node src, Node dest){
		this.src = src;
		this.dest = dest;
	}
	
	/**
	 * Get the source of the link
	 * @return
	 */
	public String getSrcId(){
		return src.getId();
	}
	
	/**
	 * Get the destination of the link
	 * @return
	 */
	public String getDestId(){
		return dest.getId();
	}
}
