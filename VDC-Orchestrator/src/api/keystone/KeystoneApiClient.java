package api.keystone;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class KeystoneApiClient {
	
	public KeystoneApiClient() {
		
	}
	
	public String getToken(String keystoneurl, String usr, String passw, String domainID) {
		String token = null;
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(keystoneurl+"/v3/auth/tokens");
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			Domain domain = new Domain(domainID);
			User user = new User(usr,domain,passw);
			Password password = new Password(user);
			Identity identity = new Identity(password);
			identity.addMethod("password");
			Authentication auth = new Authentication(identity);
			
			Gson gson = new Gson();
		    JsonElement je = gson.toJsonTree(auth);
		    JsonObject jo = new JsonObject();
		    jo.add("auth", je);
		    String json = jo.toString();
		    connection.setRequestProperty("Content-Length", Integer.toString(json.getBytes().length));
		    
		    connection.setDoOutput(true);
		    
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(json);
		    wr.close();
		    
		    int code = connection.getResponseCode();
		    
		    if(code == HttpURLConnection.HTTP_CREATED) {
		    	token = connection.getHeaderField("X-Subject-Token");
		    }
		    else {
		    	System.out.println(code+" "+connection.getResponseMessage() );
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(connection != null) {
		        connection.disconnect(); 
		      }
		}
		
		return token;
	}
}