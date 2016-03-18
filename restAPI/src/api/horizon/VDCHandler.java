package api.horizon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Scanner;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import db.DataBase;
import db.DecodifyMessage;
import jsonParser.JsonParser;
import vdc.ErrorCheck;
import vdc.VDC;

public class VDCHandler implements HttpHandler{

	JsonParser parser = new JsonParser();
	DecodifyMessage dm = new DecodifyMessage();
	DataBase db = DataBase.getInstance();
	VDC vdc;
	
	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		String request = e.getRequestMethod();
		String response;
		if(request.equals("POST")){
			Headers header = e.getResponseHeaders();
			header.add("Content-Type","text/plain");
			
			InputStream is = e.getRequestBody();
			@SuppressWarnings("resource")
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();

			VDC vdc = parser.fromJson(message);
			ErrorCheck ec = dm.startParse(vdc);
						
			if(ec.equals(ErrorCheck.ALL_OK)){
				response = "GOOD";
				e.sendResponseHeaders(200, response.length());
			}
			else if(ec.equals(ErrorCheck.VDC_NOT_COMPLETED)){
				response = "vdc not completed";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
				response = "vlink not completed";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
				response = "vm not completed";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED)){
				response = "vnode not completed";
				e.sendResponseHeaders(400, response.length());
			}
			else{
				response = "ERROR";
				e.sendResponseHeaders(400, response.length());
			}
			
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			os.close();
			e.close();
		}
		else if(request.equals("DELETE")){
			response = "GOOD";
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			
			try {
				ErrorCheck ec = db.showDB(new VDC(), "vdc", "tenant", "delete");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.sendResponseHeaders(200, response.length());
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			os.close();
			e.close();
		}
		else if(request.equals("GET")){
			VDC result = new VDC();
			try {
				ErrorCheck ec = db.showDB(result, "vdc", "tenant", "get");
				System.out.println("Printing info result: ");
				result.printInfo();
				System.out.println("End of printing info result");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String s = parser.toJson(vdc);
			System.out.println(s);
			Headers headers = e.getResponseHeaders();
		
			headers.add("Content-Type","application/json");
			e.sendResponseHeaders(200, s.length());
			OutputStream os = e.getResponseBody();
			os.write(s.getBytes());
			
			os.close();
			e.close();
		}
	}	
}