package topology;

public class Node {

	private String id;
	private Integer nlink;
	
	public Node(){}
	
	public Node(String id){
		this.id = id;
		this.nlink = 0;
	}
	
	public void setNode(String id){
		this.id = id;
	}
		
	public String getId(){
		return id;
	}
	
	public Integer getnLink(){
		return nlink;
	}
	
	public void addLink(){
		++nlink;
	}
}
