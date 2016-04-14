package topology;

public class Link {
	
	private Node src;
	private Node dest;
	
	public Link(){}
	
	public Link(Node src, Node dest){
		this.src = src;
		this.dest = dest;
	}
	
	public void setLink(Node src, Node dest){
		this.src = src;
		this.dest = dest;
	}
	
	public String getSrcId(){
		return src.getId();
	}
	
	public String getDestId(){
		return dest.getId();
	}
}
