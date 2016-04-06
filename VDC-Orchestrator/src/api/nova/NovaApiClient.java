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
import java.util.Map;
import java.util.Map.Entry;

import utils.JsonParser;
import vdc.VM;

public class NovaApiClient {
	
	public NovaApiClient() {
		
	}

	public ArrayList<Flavor> getFlavors(String novacontrollerurl, String token, JsonParser parser) {
		
		ArrayList<Flavor> flavors = new ArrayList<Flavor>(0);
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(novacontrollerurl+"/v2.1/5692d495abe741b09f6a3e9b817e1336/flavors"); //TODO TenantID
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
	    			
	    			int id = flavor.getID();
	    			url = new URL(novacontrollerurl+"/v2.1/5692d495abe741b09f6a3e9b817e1336/flavors/"+id); //TODO TenantID
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
	}
	
	/*public ArrayList<Host> getHosts(String novacontrollerurl, String token, JsonParser parser) {
		
		ArrayList<Host> hosts = new ArrayList<Host>(0);
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(novacontrollerurl+"/v2.1/os-hosts"); //TODO TenantID
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
	            	            
	            hosts = parser.readHostList(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
	            
	            Iterator<Host> iterator = hosts.iterator();
	            
	            while (iterator.hasNext()) {
	            	
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
	            }
			}
			else {
		    	System.out.println(code+" "+connection.getResponseMessage());
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(connection != null) {
		        connection.disconnect(); 
		      }
		}
		
		return hosts;
	}*/
	
public Map<String,Host> getHosts(String novacontrollerurl, String token, JsonParser parser) {
		
		Map<String,Host> hosts = new HashMap<String,Host>();
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(novacontrollerurl+"/v2.1/5692d495abe741b09f6a3e9b817e1336/os-hosts"); //TODO TenantID
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
	            
	            //Iterator<Host> iterator = hosts.iterator();
	            for(Entry<String, Host> aux : hosts.entrySet()){
	            	url = new URL(novacontrollerurl+"/v2.1/5692d495abe741b09f6a3e9b817e1336/os-hosts/"+aux.getKey()); //TODO TenantID
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
	
	public String createVM(VM vm){
		String id = null;
		
		return id;
	}
}