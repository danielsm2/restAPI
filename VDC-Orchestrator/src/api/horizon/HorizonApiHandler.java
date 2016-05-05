package api.horizon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import api.heat.HeatApiClient;
import api.keystone.KeystoneApiClient;
import api.nova.Flavor;
import api.nova.Host;
import api.nova.NovaApiClient;
import api.odl.OdlApiClient;
import conf.Conf;
import db.DataBase;
import db.DecodifyMessage;
import db.NovaDB;
import tenant.Tenant;
import topology.Topology;
import utils.JsonParser;
import utils.SSHclient;
import vdc.ErrorCheck;
import vdc.VDC;

public class HorizonApiHandler implements HttpHandler{
	
	private DecodifyMessage dm = new DecodifyMessage();
	private VDC vdc;
	private DataBase db = DataBase.getInstance();
	NovaDB dbnova = NovaDB.getInstance();
	private JsonParser parser = new JsonParser();

	HeatApiClient hac = HeatApiClient.getInstance();
	KeystoneApiClient kac = KeystoneApiClient.getInstance();
	NovaApiClient nac = NovaApiClient.getInstance();
	OdlApiClient oac = OdlApiClient.getInstance();

	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		String request = e.getRequestMethod();
		String response;
		
		if(request.equals("POST")){
			
			InputStream is = e.getRequestBody();
			@SuppressWarnings("resource")
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
			List<String> content = e.getRequestHeaders().get("Content-type");
			try{
				if(content != null && content.get(0).equals("application/json")){
					vdc = parser.fromJson(message);
					String token = kac.getToken(Conf.IP_Keystone, Conf.User_Keystone,Conf. Pass_Keystone, "default");
					System.out.println(token);
					if(db.existVDC(vdc.getTenant())){
						ErrorCheck res = hac.deployTopology(Conf.IP_Heat, vdc.getTenant(), token, "PUT");
						if(res == ErrorCheck.ALL_OK)
							resRequest(db.deleteVDC(vdc.getTenant()), e, "DELETE", "");
						else
							resRequest(res, e, "POST", "");
					}
					else{
						ErrorCheck ec = dm.startParse(vdc);
						if(!ec.equals(ErrorCheck.ALL_OK))
							resRequest(ec,e,"POST","");
						
						/*List<Tenant> tenants = kac.getTenant(Conf.IP_Keystone, token);
						String id = "";
						for(Tenant aux : tenants){
							if(aux.getName().equals("admin"))
								id = aux.getId();
						}
						
						ArrayList<Flavor> flavors = nac.getFlavors(Conf.IP_Nova, token, parser, id);
						
						System.out.println(flavors.toString());
						
						Map<String,Host> hosts = nac.getHosts(Conf.IP_Nova, token, parser, id);
						
						dbnova.startDB();
						ResultSet rs = dbnova.queryDB();
						Host aux;
						SSHclient ssh = new SSHclient();
						
						while(rs.next()){
							if(hosts.containsKey(rs.getString("host"))){
								aux = hosts.get(rs.getString("host"));
								ssh.connect(Conf.User_Compute,rs.getString("host_ip"),22,Conf.Pass_Compute);					
								String[] output = ssh.ExecuteIfconfig();
								Map<String,String> mac = checkMac(output, aux);
								for(Entry<String, String> set : mac.entrySet()){
									if(!set.getKey().equals("127.0.0.1") && !set.getKey().equals(rs.getString("host_ip")))
											aux.setMac(set.getValue());
								}
							}
							ssh.disconnect();
						}
						for(Entry<String, Host> host : hosts.entrySet()){
							  System.out.println("Host name=" + host.getKey() + ", Host_info " + host.getValue().toString());
						}
						
						Topology topology = new Topology();
						OdlApiClient odlApi = new OdlApiClient();
						odlApi.getResources(topology,hosts);
						
						dbnova.stopDB();*/
						
						ErrorCheck res = hac.deployTopology(Conf.IP_Heat, vdc.getTenant(), token, "POST");
						resRequest(res, e, "POST", "");
					}
				}
				else{
					resRequest(ErrorCheck.BAD_CONTENT_TYPE,e,"POST","");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.close();
		}
		else if(request.equals("DELETE")){
			String query = e.getRequestURI().getQuery();

			try {
				String tenant = checkQuery(query);
				if(!tenant.isEmpty()){
					if(db.existVDC(tenant)){
						String token = kac.getToken(Conf.IP_Keystone, Conf.User_Keystone,Conf. Pass_Keystone, "default");
						System.out.println(token);
						ErrorCheck res = hac.deployTopology(Conf.IP_Heat, tenant, token, "DELETE");
						if(res == ErrorCheck.ALL_OK)
							resRequest(db.deleteVDC(vdc.getTenant()), e, "DELETE", "");
					}
					else{
						resRequest(ErrorCheck.TENANTID_NOT_FOUND,e,"DELETE","");
					}
				}
				else
					resRequest(ErrorCheck.TENANTID_REQUIRED,e,"DELETE","");
				
				e.close();
			} catch (SQLException e1) {
				response = "ERROR";
				e.sendResponseHeaders(500, response.length());
				OutputStream os = e.getResponseBody();
				os.write(response.getBytes());

				os.close();
				e.close();
				e1.printStackTrace();
			}
		}
		else if(request.equals("GET")){
			vdc = new VDC();
			String query = e.getRequestURI().getQuery();
			try {
				String tenant = checkQuery(query);
				if(!tenant.isEmpty()){
					ErrorCheck ec;
					if(db.checkLocalVDC(tenant)){
						vdc = db.getLocalVDC(tenant);
						ec = ErrorCheck.ALL_OK; 
					}
					else{
						 ec = db.showDB(vdc, "vdc", tenant, "get");
						 db.saveVDC(vdc);
					}
					response = parser.toJson(vdc);
					resRequest(ec,e,"GET",response);
				}
				else
					resRequest(ErrorCheck.TENANTID_REQUIRED,e,"GET","");
				
				e.close();
			} catch (SQLException e1) {
				OutputStream os = e.getResponseBody();
				response = "ERROR";
				e.sendResponseHeaders(500, response.length());
				os.write(response.getBytes());
				
				os.close();
				e.close();
				e1.printStackTrace();
			}
		}
		else {
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			OutputStream os = e.getResponseBody();
			response = "Unsupported method. Server only accepts POST,DELETE and GET operations";
			e.sendResponseHeaders(400, response.length());
			os.write(response.getBytes());
			
			os.close();
			e.close();
		}
	}	
	
	/**
	 * Recoge el parametro tenantid
	 * @param query
	 * @return
	 */
	private String checkQuery(String query){
		if(query != null){
			String[] param = query.split("=");
			if(param[0].equals("tenantID")){
				return param[1];
			}
		}
		return "";
	}
	
	private void resRequest(ErrorCheck ec, HttpExchange e, String method, String getResponse) throws IOException, SQLException{
		String response;
		Headers header = e.getResponseHeaders();
		if(ec.equals(ErrorCheck.ALL_OK) && method.equals("GET"))
			header.add("Content-Type","application/json");
		else		
			header.add("Content-Type","text/plain");
			
		if(ec.equals(ErrorCheck.ALL_OK)){
			if(method.equals("POST")){
				response = "The VDC given has been registered";
				db.saveVDC(vdc);
				e.sendResponseHeaders(201, response.length());
			}
			else if(method.equals("DELETE")){
				response = "Delete operation has been processed";
				e.sendResponseHeaders(200, response.length());
			}
			else{
				response = getResponse;
				e.sendResponseHeaders(200, response.length());
			}
			
		}
		else if(ec.equals(ErrorCheck.VDC_NOT_COMPLETED)){
			response = "Incorrect content. Tenant id has to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. All virtual link fields have to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. All virtual machina fields have to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. All virtual node fields have to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VNODE_FROM_VLINK_WRONG)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. Some of the virtual link fields are not referencing properly to virtual node fields";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.TENANTID_NOT_FOUND)){
			response = "Incorrect content. Tenant id given is not found";
			e.sendResponseHeaders(404, response.length());
		}
		else if(ec.equals(ErrorCheck.TENANTID_REQUIRED)){
			response = "To correctly proceed with request '" + method + "', tenant id has to be informed";	
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.BAD_CONTENT_TYPE)){
			response = "Incorrect content type. The server only accepts json files from body performing POST operations";
			e.sendResponseHeaders(400, response.length());
		}
		else{
			response = "Unexpected error. Restart system.";
			e.sendResponseHeaders(500, response.length());
		}
		
		OutputStream os = e.getResponseBody();
		os.write(response.getBytes());
		
		os.close();
	}
	
	private static Map<String,String> checkMac(String[] output, Host host){
		Map<String,String> setMac = new HashMap<String,String>();
		String mac = "";
		String ip = "";
		String[] res;
		for (int i = 0; i < output.length; ++i){
			if(output[i].contains("HW")){
				res = output[i].split(" ");
				for(int j = 0; j < res.length; ++j){
					if(res[j].contains("HW")){
						mac = res[j+1];
					}
				}
			}
			else if(output[i].contains("inet") && !output[i].contains("inet6")){
				res = output[i].split(" ");
				for(int j = 0; j < res.length; ++j){
					if(res[j].contains("inet")){
						ip = res[11].split(":")[1];
						setMac.put(ip, mac);
						mac = ip = "";
					}
				}
			}
		}
		return setMac;
	}
}