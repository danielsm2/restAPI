package api.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import utils.JsonParser;

public class NetworkApiHandler {

	private static NetworkApiHandler instance = null;
	private List<Boolean> ips = new ArrayList<Boolean>(256);
	
	public static NetworkApiHandler getInstance(){
		if(instance == null)
			return instance = new NetworkApiHandler();
		else
			return instance;
	}
	
	@SuppressWarnings("static-access")
	public void updateSubnets(String networkURL){
		URL url;
		HttpURLConnection connection;
		try {
			url = new URL("http://" + networkURL +":9696/v2.0/subnets");
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			
			int code = connection.getResponseCode();
			
			if(code == connection.HTTP_OK){
				BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line = null;
				
				while((line = bf.readLine()) != null){
					sb.append(line);
				}
				
				String response = sb.toString(); 
				JsonParser parser = new JsonParser();
				parser.getIps(new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8)), ips);
			}
			else{
				System.out.println("Error: " + connection.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFirstFreeIP(){
		return "10.0."+ips.indexOf(false)+"0/24";
	}
}
