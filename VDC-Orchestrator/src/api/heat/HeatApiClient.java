package api.heat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import db.DataBase;
import utils.JsonParser;

public class HeatApiClient {

	Gson gson = new Gson();
	DataBase db = DataBase.getInstance();

	public void deployVDC(String heatURL, String tenantID, String token){
		HttpURLConnection http = null;
		
		try{
			URL url = new URL(heatURL + "/v1/" + tenantID + "/stacks");
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
			http.setRequestProperty("X-Auth-Token", token);
			
			JsonElement je1 = createNetwork("test_network_1","OS::Neutron::Net");
			JsonElement je2 = createSubnet("test_network_1","test_subnet_1","10.11.12.0/24", "OS::Neutron::Subnet");
			JsonElement je3 = createHost("host1", "80671221-3749-42d6-88b2-f1389d88a9c3", "m1.nano","nova","OS::Nova::Server");
			JsonElement je4 = createHost("host2", "80671221-3749-42d6-88b2-f1389d88a9c3", "m1.nano","nova","OS::Nova::Server");
			
			JsonObject resources= new JsonObject();
			resources.add("test_network_1", je1);
			resources.add("test_subnet_1", je2);
			resources.add("host1", je3);
			resources.add("host2", je4);
			
			Template template = new Template("2015-10-15", "simple template to test heat commands", resources);
			
			HeatTemplate heatTemplate = new HeatTemplate("test", template);
			
			String json = gson.toJson(heatTemplate,heatTemplate.getClass());
			System.out.println(json);
			
			http.setRequestProperty("Content-Lenght", Integer.toString(json.getBytes().length));
			http.setDoOutput(true);
			
			DataOutputStream dos = new DataOutputStream(http.getOutputStream());
			dos.writeBytes(json);
			dos.close();
			
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_CREATED){
				System.out.println("Ok");
				JsonParser jp = new JsonParser();
				List<String> stackInfo = jp.getStackID(http.getInputStream());
				String sql = "INSERT INTO stacks values ('" + stackInfo.get(0) + "','" + stackInfo.get(1) + "','" + db.getCurrentTenant() + "')";
				//saveStack(sql);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private JsonElement createNetwork(String name, String type){
		Properties p1 = new Properties(name);
		Element e1 = new Element(type,p1);
		return gson.toJsonTree(e1);
	}
	
	private JsonElement createSubnet(String network, String name, String ip, String type){
		Network_id ni1 = new Network_id(network);
		Properties p2 = new Properties(ni1,name,ip);
		Element e2 = new Element(type, p2);
		return gson.toJsonTree(e2);
	}
	
	private JsonElement createHost(String name, String image, String flavor, String availability_zone, String type){
		Properties p3 = new Properties(name, image, flavor,availability_zone);
		Element e3 = new Element(type, p3);
		return gson.toJsonTree(e3);
	}
	
	private void saveStack(String sql){
		db.newEntryDB(sql);
	}
}
