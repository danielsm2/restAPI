package api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import db.DataBase;
import db.DecodifyMessage;
import jsonParser.JsonParser;
import vdc.VDC;

public class VDCHandler implements HttpHandler{

	JsonParser parser = new JsonParser();
	DecodifyMessage dm = new DecodifyMessage();
	DataBase db = DataBase.getInstance();
	
	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		String request = e.getRequestMethod();
		if(request.equals("POST")){
			String response = "GOOD";
			Headers header = e.getResponseHeaders();
			header.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, response.length());
			
			InputStream is = e.getRequestBody();
			@SuppressWarnings("resource")
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
			System.out.println("entro");
			System.out.println(message);

			VDC vdc = parser.fromJson(message);
			System.out.println("salgo");
			vdc.printInfo();
			dm.startParse(vdc);
			
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
			//String response = "GET";
			String response = db.showDB();
			String s = parser.toJson(response);
			System.out.println(s);
			Headers headers = e.getResponseHeaders();
			//application/json
			headers.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, s.length());
			OutputStream os = e.getResponseBody();
			os.write(s.getBytes());
			
			os.close();
			e.close();
		}
	}
	
}
