package orchestrator;

import java.util.ArrayList;

import api.horizon.HorizonApiHandler;
import api.horizon.HorizonApiServer;
import api.keystone.KeystoneApiClient;
import api.nova.Flavor;
import api.nova.NovaApiClient;
import utils.JsonParser;

public class Orchestrator {

	public static void main(String[] args) {
		
		try {
			/*HorizonApiServer horizonapi = new HorizonApiServer("localhost",16485,0);
			horizonapi.setContext("/orchestrator/algorithms/vdc/", new HorizonApiHandler(), "admin", "admin");
			horizonapi.start();*/
			
			JsonParser parser = new JsonParser();
			
			KeystoneApiClient keystoneapi = new KeystoneApiClient();
			String token = keystoneapi.getToken("http://localhost:5000", "admin", "admin", "default");
			
			System.out.println(token);
			
			NovaApiClient novaapi = new NovaApiClient();
			ArrayList<Flavor> flavors = novaapi.getFlavors("http://localhost:8774", token, parser);
			
			System.out.println(flavors.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
