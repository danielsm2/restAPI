package api.odl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Map;

import api.nova.Host;
import conf.Conf;
import topology.Topology;
import utils.JsonParser;

public class OdlApiClient {
	HttpURLConnection t;
	JsonParser parser = new JsonParser();
	private static OdlApiClient instance;
	
	public static OdlApiClient getInstance(){
		if(instance == null)
			return instance = new OdlApiClient();
		else 
			return instance;
	}
	
	public void getResources(Topology topology, Map<String, Host> hosts){
			try {
				URL url = new URL("http://" + Conf.IP_ODL + ":8181/restconf/operational/network-topology:network-topology");
				t = (HttpURLConnection) url.openConnection();
				t.setRequestMethod("GET");
				Encoder enc = Base64.getEncoder();
				String auth = Conf.User_ODL + ":" + Conf.User_ODL;
				//byte[] decode = d.decode("admin:admin".getBytes());
				t.setRequestProperty("Authorization", "Basic "+new String(enc.encode(auth.getBytes())));
				System.out.println("Basic "+new String(enc.encode(auth.getBytes())));
				
				int code = t.getResponseCode();
				BufferedReader br = new BufferedReader(new InputStreamReader(t.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line = null;
				
				while((line = br.readLine()) != null)
					sb.append(line);
				
				br.close();
				String json = sb.toString();
				
				if(code == HttpURLConnection.HTTP_OK){
					parser.readTopology(new ByteArrayInputStream(json.getBytes()), topology, hosts);
				}
				else{
					System.out.println(code +" " + t.getResponseMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
