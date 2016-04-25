package topology;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private String id;
	private List<Node> link_in = new ArrayList<Node>();
	private List<Node> link_out = new ArrayList<Node>();
	
	public Node(){}
	
	public Node(String id){
		this.id = id;
	}
	
	public void setNode(String id){
		this.id = id;
	}
		
	public String getId(){
		return id;
	}
	
	public Integer getNumLinks(){
		return link_in.size() + link_out.size();
	}
	
	public void addLink_in(Node node){
		link_in.add(node);
	}
	
	public void addLink_out(Node node){
		link_out.add(node);
	}
}
