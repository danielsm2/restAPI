package topology;

public class Switch extends Node{
	
	public Switch(){}
	
	public Switch(String id){
		super(id);
	}
	
	public Switch(String id, Node src, Node dest){
		super(id,src,dest); 
	}
}
