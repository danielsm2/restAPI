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
			
			DataBase db = DataBase.getInstance();
			db.startDB();
		
			HorizonApiServer horizonapi = new HorizonApiServer(Conf.IP_Horizon,Integer.parseInt(Conf.Port_Horizon),0);
			horizonapi.setContext("/orchestrator/algorithms/vdc/", new HorizonApiHandler(), Conf.User_Horizon, Conf.Pass_Horizon);
			horizonapi.start();

			System.out.println("Server is listening...");

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
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}