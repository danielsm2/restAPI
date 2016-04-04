package orchestrator;

import java.util.ArrayList;

import api.horizon.HorizonApiHandler;
import api.horizon.HorizonApiServer;
import api.keystone.KeystoneApiClient;
import api.nova.Flavor;
import api.nova.Host;
import api.nova.NovaApiClient;
import utils.JsonParser;
import utils.SSHclient;

public class Orchestrator {

	public static void main(String[] args) {
		
		try {
			/*HorizonApiServer horizonapi = new HorizonApiServer("localhost",16485,0);
			horizonapi.setContext("/orchestrator/algorithms/vdc/", new HorizonApiHandler(), "admin", "admin");
			horizonapi.start();*/
			
			/*JsonParser parser = new JsonParser();
			
			KeystoneApiClient keystoneapi = new KeystoneApiClient();
			String token = keystoneapi.getToken("http://localhost:5000", "admin", "admin", "default");
			
			System.out.println(token);
			
			NovaApiClient novaapi = new NovaApiClient();
			ArrayList<Flavor> flavors = novaapi.getFlavors("http://localhost:8774", token, parser);
			
			System.out.println(flavors.toString());
			
			ArrayList<Host> hosts = novaapi.getHosts("http://localhost:8774", token, parser);
			
			System.out.println(hosts.toString());*/
			
			SSHclient ssh = new SSHclient();
			
			ssh.connect("root", "localhost", 15978, "root");
			String output = ssh.ExecuteIfconfig();
			System.out.println(output);
			ssh.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
