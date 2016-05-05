package orchestrator;

import api.horizon.HorizonApiHandler;
import api.horizon.HorizonApiServer;
import conf.Conf;
import db.DataBase;

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

			//check salida topologia ODL
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