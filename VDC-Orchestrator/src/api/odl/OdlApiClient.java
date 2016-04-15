package api.odl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Map;

import api.nova.Host;
import topology.Topology;
import utils.JsonParser;

public class OdlApiClient {
	HttpURLConnection t;
	JsonParser parser = new JsonParser();
	
	public void getResources(Topology topology, Map<String, Host> hosts){
			try {
				URL url = new URL("http://localhost:8181/restconf/operational/network-topology:network-topology");
				t = (HttpURLConnection) url.openConnection();
				t.setRequestMethod("GET");
				Decoder d = Base64.getDecoder();
				byte[] decode = d.decode("admin:admin".getBytes());
				t.setRequestProperty("Authorization", decode.toString());
				
				int code = t.getResponseCode();
				
				if(code == HttpURLConnection.HTTP_OK){
					parser.readTopology(t.getInputStream(), topology, hosts);
				}
				else{
					System.out.println(code +" " + t.getResponseMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
