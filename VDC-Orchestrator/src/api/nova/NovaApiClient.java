package api.nova;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.JsonParser;
import vdc.VDC;
import vdc.VirtualMachine;

public class NovaApiClient {
	
	private static NovaApiClient instance;
	
	public static NovaApiClient getInstance(){
		if(instance == null)
			return instance = new NovaApiClient();
		else
			return instance;
	}
	public NovaApiClient() {
		
	}

	/*public ArrayList<Flavor> getFlavors(String novacontrollerurl, String token, JsonParser parser, String id) {
		
		ArrayList<Flavor> flavors = new ArrayList<Flavor>(0);
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL("http://" + novacontrollerurl +":8774/v2.1/" + id +"/flavors"); 
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("X-Auth-Token", token);
			
			int code = connection.getResponseCode();
			
			if(code == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            StringBuffer response = new StringBuffer();
	            String inputLine = null;
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            
	            String json = response.toString();
	            
	            flavors = parser.readFlavorList(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
	            
	    		Iterator<Flavor> iterator = flavors.iterator();
	    		
	    		while(iterator.hasNext()) {
	    			Flavor flavor = iterator.next();
	    			
	    			int idFlavor = flavor.getID();
	    			url = new URL("http://" + novacontrollerurl+":8774/v2.1/" + id + "/flavors/"+idFlavor); 
	    			connection = (HttpURLConnection)url.openConnection();
	    			connection.setRequestMethod("GET");
	    			connection.setRequestProperty("X-Auth-Token", token);
	    			
	    			code = connection.getResponseCode();
	    			
	    			if(code == HttpURLConnection.HTTP_OK) {
	    				
	    				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	            response = new StringBuffer();
	    	            inputLine = null;
	    	 
	    	            while ((inputLine = in.readLine()) != null) {
	    	                response.append(inputLine);
	    	            }
	    	            in.close();
	    	            
	    	           json = response.toString();
	    	           parser.readFlavor(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),flavor);
	    			}
	    			else {
	    				System.out.println(code+" "+connection.getResponseMessage());
	    			}
	    		}
			}
			else {
		    	System.out.println(code+" "+connection.getResponseMessage()+" "+url.toURI());
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(connection != null) {
		        connection.disconnect(); 
		      }
		}
		
		return flavors;
	}*/
	
public List<String> getFlavors(String novacontrollerurl, String token, JsonParser parser, String id, VDC vdc) {
		
		List<String> flavors = new ArrayList<String>();
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL("http://" + novacontrollerurl +":8774/v2.1/" + id +"/flavors"); 
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("X-Auth-Token", token);
			
			int code = connection.getResponseCode();
			
			if(code == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            StringBuffer response = new StringBuffer();
	            String inputLine = null;
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            
	            String json = response.toString();
	            
	            flavors = parser.readFlavorList(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
	            
	    		for(int i = 0; i < flavors.size(); ++i) {
	    			VirtualMachine vm = vdc.getVMachineByName(flavors.get(i));
	    			
	    			String idFlavor = vm.getId();
	    			url = new URL("http://" + novacontrollerurl+":8774/v2.1/" + id + "/flavors/"+idFlavor); 
	    			connection = (HttpURLConnection)url.openConnection();
	    			connection.setRequestMethod("GET");
	    			connection.setRequestProperty("X-Auth-Token", token);
	    			
	    			code = connection.getResponseCode();
	    			
	    			if(code == HttpURLConnection.HTTP_OK) {
	    				
	    				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	            response = new StringBuffer();
	    	            inputLine = null;
	    	 
	    	            while ((inputLine = in.readLine()) != null) {
	    	                response.append(inputLine);
	    	            }
	    	            in.close();
	    	            
	    	           json = response.toString();
	    	           parser.readFlavor(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),vm);
	    			}
	    			else {
	    				System.out.println(code+" "+connection.getResponseMessage());
	    			}
	    		}
			}
			else {
		    	System.out.println(code+" "+connection.getResponseMessage()+" "+url.toURI());
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(connection != null) {
		        connection.disconnect(); 
		      }
		}
		
		return flavors;
	}
	
public Map<String,Host> getHosts(String novacontrollerurl, String token, JsonParser parser, String id) {
		
		Map<String,Host> hosts = new HashMap<String,Host>();
		
		HttpURLConnection connection = null;
				
		try {
			URL url = new URL("http://" + novacontrollerurl +":8774/v2.1/" + id + "/os-hosts"); 
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("X-Auth-Token", token);
			
			int code = connection.getResponseCode();
			
			if(code == HttpURLConnection.HTTP_OK) {
				
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            StringBuffer response = new StringBuffer();
	            String inputLine = null;
	 
	            while ((inputLine = in.readLine()) != null) 
	                response.append(inputLine);
	            
	            in.close();
	            
	            String json = response.toString();
	            	            
	            hosts = parser.readHostList(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
	            
	            for(Entry<String, Host> aux : hosts.entrySet()){
	            	url = new URL("http://" + novacontrollerurl +":8774/v2.1/" + id + "/os-hosts/" + aux.getKey()); 
	    			connection = (HttpURLConnection)url.openConnection();
	    			connection.setRequestMethod("GET");
	    			connection.setRequestProperty("X-Auth-Token", token);

	    			code = connection.getResponseCode();
	    			if(code == HttpURLConnection.HTTP_OK) {
	    				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	            response = new StringBuffer();
	    	            inputLine = null;

	    	            while ((inputLine = in.readLine()) != null) 
	    	                response.append(inputLine);
	    	            
	    	            in.close();
	    	            
	    	            json = response.toString();
	    	            
	    	            parser.readHost(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), aux.getValue());
	    			}
	            }
	            /*while (iterator.hasNext()) {
	            		            	
	            	Host host = iterator.next();
	            	
	            	String host_name = host.getName();
	            	
	            	url = new URL(novacontrollerurl+"/v2.1/os-hosts/"+host_name);
	    			connection = (HttpURLConnection)url.openConnection();
	    			connection.setRequestMethod("GET");
	    			connection.setRequestProperty("X-Auth-Token", token);
	    			
	    			code = connection.getResponseCode();
	    			
	    			if(code == HttpURLConnection.HTTP_OK) {
	    				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	            response = new StringBuffer();
	    	            inputLine = null;
	    	 
	    	            while ((inputLine = in.readLine()) != null) {
	    	                response.append(inputLine);
	    	            }
	    	            in.close();
	    	            
	    	           json = response.toString();
	    	           
	    	           //System.out.println(json);

	    	           parser.readHost(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), host);
	    			}
	            }*/
			}
			else {
		    	System.out.println(code+" "+connection.getResponseMessage()+" "+url.toURI());
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(connection != null) {
		        connection.disconnect(); 
		      }
		}
		
		return hosts;
	}
	
	public String createVM(VirtualMachine vm){
		String id = null;
		
		return id;
	}
}