package topology;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private String id;
	//private Integer nlink;
	private List<Link> links = new ArrayList<Link>();
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
	
	public Integer getnLink(){
		return links.size();
	}
	
	public void addLink(Link link){
		links.add(link);
	}
}
