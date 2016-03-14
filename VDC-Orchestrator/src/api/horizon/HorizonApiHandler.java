package api.horizon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import vdc.VDC;

public class HorizonApiHandler implements HttpHandler {
	
	public HorizonApiHandler() {
		
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		String method = t.getRequestMethod();
		
		if(method.equals("POST")) {
			List<String> contentType = t.getRequestHeaders().get("Content-Type");
			if(contentType != null && contentType.contains("application/json") && contentType.size() == 1) {
				InputStream is = t.getRequestBody();
				
				@SuppressWarnings("resource")
				String json = new Scanner(is,"UTF-8").useDelimiter("\\z").next();
				
				Gson gson = new Gson();
				VDC vdc = gson.fromJson(json, VDC.class);
				
				
				//TODO implement json parsing
				
				//TODO check if id already exists
				
				//TODO implement optimization mechanism
				
				
				
				
				String response = "GOOD";
				Headers headers = t.getResponseHeaders();
				headers.add("Content-Type","text/plain");
				t.sendResponseHeaders(201, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				t.close();
			}
			else {
				String response = "Incorrect content type. The server only accepts json files for the body content when performing a POST operation.";
				Headers headers = t.getResponseHeaders();
				headers.add("Content-Type","text/plain");
				t.sendResponseHeaders(400, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				t.close();
			}
		} else if(method.equals("GET")) {
			
		} else if (method.equals("DELETE")) {
			
		}
		else {
			String response = "Unsupported method. The server only supports GET, POST and DELETE operations.";
			Headers headers = t.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			t.sendResponseHeaders(405, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			t.close();
		}
	}
}