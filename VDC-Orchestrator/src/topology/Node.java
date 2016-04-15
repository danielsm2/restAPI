package topology;

public class Node {

	private String id;
	private Link link;
	
	public Node(){}
	
	public Node(String id){
		this.id = id;
	}
	
	public Node(String id, Node src, Node dest){
		this.id = id;
		this.link = new Link(src, dest); 
	}
	
	public void setNode(String id){
		this.id = id;
	}
		
	public String getId(){
		return id;
	}
	
	public Link getLink(){
		return link;
	}
}
