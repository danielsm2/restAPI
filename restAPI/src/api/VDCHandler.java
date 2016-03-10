package api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Scanner;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import db.DataBase;
import db.DecodifyMessage;
import jsonParser.JsonParser;
import person.Persona;

public class VDCHandler implements HttpHandler{

	JsonParser parser = new JsonParser();
	DataBase db = DataBase.getInstance();
	
	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		//String s = parser.toJson();
		//parser.fromJson(s);
		String request = e.getRequestMethod();
		if(request.equals("POST")){
			String response = "POST";
			Headers header = e.getResponseHeaders();
			header.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, response.length());
			
			InputStream is = e.getRequestBody();
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
			//DecodifyMessage dm = new DecodifyMessage();
			Persona p = parser.fromJson(message);
			db.addRow(p);
			//dm.startParse(p);
			
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
			Headers headers = e.getResponseHeaders();
			//application/json
			headers.add("Content-Type","text/plain");
			e.sendResponseHeaders(200, response.length());
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			
			os.close();
			e.close();
		}
	}
	
}
