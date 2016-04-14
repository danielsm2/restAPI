package topology;

import java.util.ArrayList;
import java.util.List;

public class Topology {

	List<Node> topology = new ArrayList<Node>();
	
	public void addNode(Node node){
		topology.add(node);
	}
	
	public List<Node> getTopology(){
		return topology;
	}
}
