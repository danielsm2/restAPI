package topology;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private String id;
	private List<Node> link_in = new ArrayList<Node>();
	private List<Node> link_out = new ArrayList<Node>();
	
	/**
	 * Create an empty instance of a node
	 */
	public Node(){}
	
	/**
	 * Creates an instance of a node represented by an ID
	 * @param id - the ID of the node
	 */
	public Node(String id){
		this.id = id;
	}
	
	/**
	 * Set the ID of the node
	 * @param id - ID of the node
	 */
	public void setNode(String id){
		this.id = id;
	}
		
	/**
	 * Get the ID of the node
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Get the links size of the node. Input and output size.
	 * @return
	 */
	public Integer getNumLinks(){
		return link_in.size() + link_out.size();
	}
	
	/**
	 * Set a input link of the node
	 * @param node - Node representing an input link
	 */
	public void addLink_in(Node node){
		link_in.add(node);
	}
	
	/**
	 * Set a output link of the node
	 * @param node - Node representing an output link
	 */
	public void addLink_out(Node node){
		link_out.add(node);
	}
}
