package api.heat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import api.network.NetworkApiHandler;
import db.DataBase;
import utils.JsonParser;
import vdc.ErrorCheck;
import vdc.VDC;
import vdc.VirtualMachine;
import vdc.VirtualNode;

public class HeatApiClient {

	Gson gson = new Gson();
	DataBase db = DataBase.getInstance();
	HttpURLConnection http = null;
	private String sqlStatement;

	private NetworkApiHandler nar = NetworkApiHandler.getInstance();

	private static HeatApiClient instance;
	
	public static HeatApiClient getInstance(){
		if(instance == null)
			return instance = new HeatApiClient();
		else
			return instance;
	}
	
	private String buildJson(VDC vdc){
		List<JsonElement> je = new ArrayList<JsonElement>();
		JsonObject resources= new JsonObject();
		je.add(createNetwork("test_network_1","OS::Neutron::Net"));
		je.add(createSubnet("test_network_1","test_subnet_1",nar.getFirstFreeIP(), "OS::Neutron::Subnet"));
		resources.add("test_network_1", je.get(0));
		resources.add("test_subnet_1", je.get(1));
		
		for(int i = 0; i < vdc.getSizeVNode(); ++i){
			VirtualNode vn = vdc.getVNodeByPos(i);
			for(int j = 0; j < vn.getSizeVirtualMachine(); ++j){
				//Port port = new Port(new Network_id("test_network_1"), "port" + i + j, new Network_id("test_subnet_1"));
				VirtualMachine vm = vn.getVirtualMachine(j);
				je.add(createHost("host" + i + j, vm.getImage(), vm.getFlavorName(),"nova:ubuntu-devstack","OS::Nova::Server", "port" + i + j));
				resources.add("host" + i +j, je.get(je.size()-1));
				je.add(createPort("OS::Neutron::Port", "test_network_1", "test_subnet_1"));
				resources.add("port"+i+j, je.get(je.size()-1));
			}
		}
		
		Template template = new Template("2015-10-15", "simple template to test heat commands", resources);
		
		HeatTemplate heatTemplate = new HeatTemplate("test", template);
		
		return gson.toJson(heatTemplate,heatTemplate.getClass());
	}
	
	private void writeOutputStream (HttpURLConnection http, String json) throws IOException{
		http.setRequestProperty("Content-Lenght", Integer.toString(json.getBytes().length));
		http.setDoOutput(true);
		
		DataOutputStream dos = new DataOutputStream(http.getOutputStream());
		dos.writeBytes(json);
		dos.close();
	}
	public ErrorCheck deployTopology(String heatURL, String tenantID, String token, String type, VDC vdc){		
		try{
			if(type == "DELETE"){
				URL url = new URL(db.getStack(tenantID));
				http = (HttpURLConnection) url.openConnection();
				http.setRequestMethod("DELETE");
				http.setRequestProperty("X-Auth-Token", token);
				http.connect();
			}
			else{
				if(type == "POST"){
					URL url = new URL("http://" + heatURL + ":8004/v1/" + tenantID + "/stacks");
					http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("POST");
					}
				else if(type == "PUT"){
					URL url = new URL(db.getStack(tenantID));
					http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("PUT");
				}
				http.setRequestProperty("Content-Type", "application/json");
				http.setRequestProperty("X-Auth-Token", token);
				
				String json = buildJson(vdc);
				System.out.println(json);
				
				writeOutputStream(http, json);
			}
						
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_CREATED){
				System.out.println("Deployed topology");
				JsonParser jp = new JsonParser();
				List<String> stackInfo = jp.getStackID(http.getInputStream());
				sqlStatement = "INSERT INTO stacks values ('" + stackInfo.get(0) + "','" + stackInfo.get(1) + "','" + vdc.getTenant() + "')";
				//saveStack();
				return ErrorCheck.ALL_OK;
			}
			else if(code == HttpURLConnection.HTTP_ACCEPTED){
				System.out.println("Updated topology");
				return ErrorCheck.ALL_OK;
			}
			else if(code == HttpURLConnection.HTTP_NO_CONTENT){
				System.out.println("Deleted from OpenStack");
				return ErrorCheck.ALL_OK;
			}
			else{
				//TODO devolver el codigo correcto segun 'code'
				System.err.println(code + " " + http.getResponseMessage());
				return ErrorCheck.BAD_CONTENT_TYPE;
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private JsonElement createNetwork(String name, String type){
		Properties p = new Properties(name);
		Element e = new Element(type,p);
		return gson.toJsonTree(e);
	}
	
	private JsonElement createSubnet(String network, String name, String ip, String type){
		Network_id ni = new Network_id(network);
		Properties p = new Properties(ni,name,ip, "null", "true");
		Element e = new Element(type, p);
		return gson.toJsonTree(e);
	}
	
	private JsonElement createHost(String name, String image, String flavor, String availability_zone, String type, String port){
		Properties p = new Properties(name, image, flavor,availability_zone, port);
		Element e = new Element(type, p);
		return gson.toJsonTree(e);
	}
	
	private JsonElement createPort(String type, String network, String subnet){
		Properties p = new Properties(new Network_id(network), new Subnet_id(new Network_id(subnet)));
		Element e = new Element(type, p);
		return gson.toJsonTree(e);
		
	}
	
	public void saveStack(){
		db.newEntryDB(sqlStatement);
	}
}
