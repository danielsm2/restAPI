package orchestrator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.horizon.HorizonApiHandler;
import api.horizon.HorizonApiServer;
import api.keystone.KeystoneApiClient;
import api.nova.Flavor;
import api.nova.Host;
import api.nova.NovaApiClient;
import db.NovaDB;
import utils.JsonParser;
import utils.SSHclient;

public class Orchestrator {

	public static void main(String[] args) {
		
		try {
			/*HorizonApiServer horizonapi = new HorizonApiServer("localhost",16485,0);
			horizonapi.setContext("/orchestrator/algorithms/vdc/", new HorizonApiHandler(), "admin", "admin");
			horizonapi.start();*/
			
			JsonParser parser = new JsonParser();
			
			KeystoneApiClient keystoneapi = new KeystoneApiClient();
			String token = keystoneapi.getToken("http://172.26.37.249:5000", "admin", "cosign", "default");
			
			//System.out.println(token);
			
			NovaApiClient novaapi = new NovaApiClient();
			/*ArrayList<Flavor> flavors = novaapi.getFlavors("http://172.26.37.249:8774", token, parser);
			
			System.out.println(flavors.toString());*/
			
			//ArrayList<Host> hosts = novaapi.getHosts("http://localhost:8774", token, parser);
			Map<String,Host> hosts = novaapi.getHosts("http://172.26.37.249:8774", token, parser);
			NovaDB db = NovaDB.getInstance();
			db.startDB();
			ResultSet rs = db.queryDB();
			Host aux;
			SSHclient ssh = new SSHclient();
			
			while(rs.next()){
				if(hosts.containsKey(rs.getString("host"))){
					aux = hosts.get(rs.getString("host"));
					ssh.connect("stack",rs.getString("host_ip"),22,"stack");
					String output = ssh.ExecuteIfconfig();
					Map<String,String> mac = checkMac(output, aux);
					for(Entry<String, String> set : mac.entrySet()){
						if(!set.getKey().equals("127.0.0.1") && !set.getKey().equals(rs.getString("host_ip")))
								aux.setMac(set.getValue());
					}
					//System.out.println(output);
					//aux.setMac(rs.getString("host_ip"));
				}
				ssh.disconnect();
			}
			for(Entry<String, Host> host : hosts.entrySet()){
				  System.out.println("Host name=" + host.getKey() + ", Host_info " + host.getValue().toString());
			}
			db.stopDB();
			
			//System.out.println(hosts.toString());
			
			/*SSHclient ssh = new SSHclient();
			
			ssh.connect("apages", "84.88.32.238", 15978,);
			String output = ssh.ExecuteIfconfig();
			System.out.println(output);
			ssh.disconnect();*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String,String> checkMac(String output, Host host){
		/*Pattern pattern = Pattern.compile("*HW*");
		Matcher m = pattern.matcher(output);
		if(m.find()){
			System.out.print(m.group());
		}*/
		Map<String,String> setMac = new HashMap<String,String>();
		String res[] = output.split(" ");
		String mac = "";
		String ip = "";
		for (int i = 0; i < res.length; ++i){
			if(res[i].contains("HW"))
				mac = res[i+1];
			else if(res[i].contains("inet") && !res[i].contains("inet6")){
				System.out.println("salida: "+res[i]);
				ip = res[i+1];
				ip = ip.split(":")[1];
				setMac.put(ip, mac);
				mac = ip = "";
			}
		}
		return setMac;
	}
}