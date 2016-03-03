package api;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VDCHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange e) throws IOException {
		
		String request = e.getRequestMethod();
		if(request.equals("POST")){
			String response = "POST";
			Headers header = e.getResponseHeaders();
			header.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, response.length());
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			os.close();
			e.close();
		}
		else if(request.equals("DELETE")){
			String response = "DELETE";
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, response.length());
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			os.close();
			e.close();
		}
		else if(request.equals("GET")){
			String response = "GET";
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, response.length());
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			os.close();
			e.close();
		}
	}
	
}
