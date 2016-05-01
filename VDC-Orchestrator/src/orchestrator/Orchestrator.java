package orchestrator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.heat.HeatApiClient;
import api.horizon.HorizonApiHandler;
import api.horizon.HorizonApiServer;
import api.keystone.KeystoneApiClient;
import api.nova.Flavor;
import api.nova.Host;
import api.nova.NovaApiClient;
import api.odl.OdlApiClient;
import conf.Conf;
import db.DataBase;
import db.NovaDB;
import tenant.Tenant;
import topology.Topology;
import utils.JsonParser;
import utils.SSHclient;

public class Orchestrator {

	public static void main(String[] args) {
		
		try {
			Conf.readConf();
			
			/*DataBase db = DataBase.getInstance();
			db.startDB();
		
			HorizonApiServer horizonapi = new HorizonApiServer(Conf.IP_Horizon,Integer.parseInt(Conf.Port_Horizon),0);
			horizonapi.setContext("/orchestrator/algorithms/vdc/", new HorizonApiHandler(), Conf.User_Horizon, Conf.Pass_Horizon);
			horizonapi.start();

			System.out.println("Server is listening...");*/

			/*InputStream is = null;
			try{
				System.out.println("Waiting for data");
				is = System.in;
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String line = null;
	            while ((line = br.readLine()) != null) {
	            	if(line.equals("quit"))
	            		break;
	                sb.append(line);
	                System.out.println("Line entered : " + line);
	            }
	            br.close();
	            String json = sb.toString();
	            
				System.out.println("Data readed");
				JsonParser parse = new JsonParser();
				Topology topology = new Topology();
				Map<String,Host> h = new Hashtable<String,Host>();
				parse.readTopology(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), topology, h);
			}catch(Exception e){
				e.printStackTrace();
			}*/
			/*JsonParser parser = new JsonParser();
			
			KeystoneApiClient keystoneapi = new KeystoneApiClient();
			String token = keystoneapi.getToken(Conf.IP_Keystone, Conf.User_Keystone,Conf. Pass_Keystone, "default");
			//String token = keystoneapi.getToken("http://localhost:5000", "admin", "admin", "default");
			System.out.println(token);

			List<Tenant> tenants = keystoneapi.getTenant(Conf.IP_Keystone, token);
			String id = "";
			for(Tenant aux : tenants){
				if(aux.getName().equals("admin"))
					id = aux.getId();
			}
			NovaApiClient novaapi = new NovaApiClient();*/
			
			/*ArrayList<Flavor> flavors = novaapi.getFlavors(Conf.IP_Nova, token, parser, id);
			
			System.out.println(flavors.toString());*/
			
			//Map<String,Host> hosts = novaapi.getHosts(Conf.IP_Nova, token, parser, id);
			//Map<String,Host> hosts = novaapi.getHosts("http://localhost:8774", token, parser, id);
			
			HeatApiClient heatapi = new HeatApiClient();
			heatapi.deployVDC("", "");
			
			/*NovaDB db = NovaDB.getInstance();
			db.startDB();
			ResultSet rs = db.queryDB();
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
			
			db.stopDB();*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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