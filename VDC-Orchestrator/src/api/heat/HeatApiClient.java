package api.heat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import db.DataBase;
import utils.JsonParser;
import vdc.VDC;
import vdc.VirtualMachine;
import vdc.VirtualNode;

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
			
			VDC vdc = db.getLocalVDC(tenantID);
			List<JsonElement> je = new ArrayList<JsonElement>();
			JsonObject resources= new JsonObject();

			je.add(createNetwork("test_network_1","OS::Neutron::Net"));
			je.add(createSubnet("test_network_1","test_subnet_1","10.11.12.0/24", "OS::Neutron::Subnet"));
			resources.add("test_network_1", je.get(0));
			resources.add("test_subnet_1", je.get(1));
			
			for(int i = 0; i < vdc.getSizeVNode(); ++i){
				VirtualNode vn = vdc.getVNodeByPos(i);
				for(int j = 0; j < vn.getSizeVirtualMachine(); ++j){
					VirtualMachine vm = vn.getVirtualMachine(j);
					je.add(createHost("host" + i + j, vm.getImage(), vm.getFlavorName(),"nova","OS::Nova::Server"));
					resources.add("host" + i +j, je.get(je.size()-1));
				}
			}
			
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
			else{
				System.err.println(code + " " + http.getResponseMessage());
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
