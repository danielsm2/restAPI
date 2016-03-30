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
		
	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		DecodifyMessage dm = new DecodifyMessage();
		DataBase db = DataBase.getInstance();
		VDC vdc;
		String request = e.getRequestMethod();
		String response;
		
		if(request.equals("POST")){
			Headers header = e.getResponseHeaders();
			header.add("Content-Type","text/plain");
			
			InputStream is = e.getRequestBody();
			@SuppressWarnings("resource")
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();

			vdc = parser.fromJson(message);
			ErrorCheck ec = dm.startParse(vdc);
						
			if(ec.equals(ErrorCheck.ALL_OK)){
				response = "VDC REGISTERED";
				e.sendResponseHeaders(201, response.length());
			}
			else if(ec.equals(ErrorCheck.VDC_NOT_COMPLETED)){
				response = "TENANTID IS REQUIRED";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
				response = "ALL VLINK FIELDS ARE REQUIRED";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
				response = "ALL VM FIELDS ARE REQUIRED";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED)){
				response = "ALL VNODE FIELDS ARE REQUIRED";
				e.sendResponseHeaders(400, response.length());
			}
			else if(ec.equals(ErrorCheck.VNODE_FROM_VLINK_WRONG)){
				response = "TO OR FROM(VNODE) PARAMETER OF VLINK IS NOT WELL DEFINED";
				e.sendResponseHeaders(400, response.length());
			}
			else{
				response = "ERROR";
				e.sendResponseHeaders(500, response.length());
			}
			
			OutputStream os = e.getResponseBody();
			os.write(response.getBytes());
			
			os.close();
			e.close();
		}
		else if(request.equals("DELETE")){
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			OutputStream os = e.getResponseBody();
			String query = e.getRequestURI().getQuery();

			try {
				String tenant = checkQuery(query);
				if(!tenant.isEmpty()){
					ErrorCheck ec = db.showDB(new VDC(), "vdc", tenant, "delete");
					if(ec.equals(ErrorCheck.ALL_OK)){
						response = "DELETED VDC";
						e.sendResponseHeaders(200, response.length());
					}
					else if(ec.equals(ErrorCheck.TENANTID_NOT_FOUND)){
						response = "TENANTID IS NOT FOUND";
						e.sendResponseHeaders(404, response.length());
					}
					else{
						response = "ERROR";
						e.sendResponseHeaders(500, response.length());
					}
				}
				else{
					response = "TENANTID PARAMETER IS REQUIRED";
					e.sendResponseHeaders(400, response.length());
				}
				
				os.write(response.getBytes());
				os.close();
				e.close();
			} catch (SQLException e1) {
				response = "ERROR";
				e.sendResponseHeaders(500, response.length());
				os.write(response.getBytes());

				os.close();
				e.close();
				e1.printStackTrace();
			}
		}
		else if(request.equals("GET")){
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","application/json");
			vdc = new VDC();
			OutputStream os = e.getResponseBody();
			String query = e.getRequestURI().getQuery();
			try {
				String tenant = checkQuery(query);
				if(!tenant.isEmpty()){
					ErrorCheck ec = db.showDB(vdc, "vdc", tenant, "get");
					response = parser.toJson(vdc);
					if(ec.equals(ErrorCheck.ALL_OK)){
						e.sendResponseHeaders(200, response.length());
					}
					else if(ec.equals(ErrorCheck.TENANTID_NOT_FOUND)){
						response = "TENANTID IS NOT FOUND";
						e.sendResponseHeaders(404, response.length());
					}
				}
				else{
					response = "TENANTID PARAMETER IS REQUIRED";
					e.sendResponseHeaders(400, response.length());
				}
				
				os.write(response.getBytes());
				os.close();
				e.close();
				
			} catch (SQLException e1) {
				response = "ERROR";
				e.sendResponseHeaders(500, response.length());
				os.write(response.getBytes());
				
				os.close();
				e.close();
				e1.printStackTrace();
			}
		}
		else if(request.equals("PUT") || request.equals("HEAD") || request.equals("OPTIONS")){
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			OutputStream os = e.getResponseBody();
			response = "REQUEST NOT AVAILABLE";
			e.sendResponseHeaders(400, response.length());
			os.write(response.getBytes());
			
			os.close();
			e.close();
		}
	}	
	
	/**
	 * Recoge el parametro tenantid
	 * @param query
	 * @return
	 */
	private String checkQuery(String query){
		if(query != null){
			String[] param = query.split("=");
			if(param.length > 2){
				String[] tenant = param[1].split("&");
				return tenant[0];
			}
			else{
				return param[1];
			}
		}
		else{
			return "";
		}
	}
}