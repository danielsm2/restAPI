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
	
	private DecodifyMessage dm = new DecodifyMessage();
	private VDC vdc;
		
	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		DataBase db = DataBase.getInstance();
		String request = e.getRequestMethod();
		String response;
		
		if(request.equals("POST")){
			
			InputStream is = e.getRequestBody();
			@SuppressWarnings("resource")
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();

			vdc = parser.fromJson(message);
			try {
				db.showDB(new VDC(), "vdc", vdc.getTenant(), "delete");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ErrorCheck ec = dm.startParse(vdc);
			try {
				resRequest(ec,e,"POST","");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			
			e.close();
		}
		else if(request.equals("DELETE")){
			String query = e.getRequestURI().getQuery();

			try {
				String tenant = checkQuery(query);
				if(!tenant.isEmpty()){
					ErrorCheck ec = db.showDB(new VDC(), "vdc", tenant, "delete");
					resRequest(ec,e,"DELETE","");
				}
				else
					resRequest(ErrorCheck.TENANTID_REQUIRED,e,"DELETE","");
				
				e.close();
			} catch (SQLException e1) {
				response = "ERROR";
				e.sendResponseHeaders(500, response.length());
				OutputStream os = e.getResponseBody();
				os.write(response.getBytes());

				os.close();
				e.close();
				e1.printStackTrace();
			}
		}
		else if(request.equals("GET")){
			vdc = new VDC();
			String query = e.getRequestURI().getQuery();
			try {
				String tenant = checkQuery(query);
				if(!tenant.isEmpty()){
					ErrorCheck ec = db.showDB(vdc, "vdc", tenant, "get");
					response = parser.toJson(vdc);
					resRequest(ec,e,"GET",response);
				}
				else
					resRequest(ErrorCheck.TENANTID_REQUIRED,e,"GET","");
				
				e.close();
			} catch (SQLException e1) {
				OutputStream os = e.getResponseBody();
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
	
	private void resRequest(ErrorCheck ec, HttpExchange e, String method, String getResponse) throws IOException, SQLException{
		String response;
		Headers header = e.getResponseHeaders();
		if(ec.equals(ErrorCheck.ALL_OK) && method.equals("GET"))
			header.add("Content-Type","application/json");
		else		
			header.add("Content-Type","text/plain");
			
		if(ec.equals(ErrorCheck.ALL_OK)){
			if(method.equals("POST")){
				response = "VDC REGISTERED";
				dm.saveVDC(vdc);
			}
			else if(method.equals("DELETE"))
				response = "VDC DELETED";
			else
				response = getResponse;
			e.sendResponseHeaders(201, response.length());
		}
		else if(ec.equals(ErrorCheck.VDC_NOT_COMPLETED)){
			response = "TENANTID IS REQUIRED";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
			dm.rollbackVDC(vdc);
			response = "ALL VLINK FIELDS REQUIRED";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
			dm.rollbackVDC(vdc);
			response = "ALL VM FIELDS REQUIRED";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED)){
			dm.rollbackVDC(vdc);
			response = "ALL VNODE FIELDS REQUIRED";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VNODE_FROM_VLINK_WRONG)){
			dm.rollbackVDC(vdc);
			response = "TO OR FROM(VNODE) PARAMETER OF VLINK IS NOT WELL DEFINED";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.TENANTID_NOT_FOUND)){
			response = "TENANTID NOT FOUND";
			e.sendResponseHeaders(404, response.length());
		}
		else if(ec.equals(ErrorCheck.TENANTID_REQUIRED)){
			response = "TENANTID REQUIRED";
			e.sendResponseHeaders(400, response.length());
		}
		else{
			response = "ERROR";
			e.sendResponseHeaders(500, response.length());
		}
		
		OutputStream os = e.getResponseBody();
		os.write(response.getBytes());
		
		os.close();
	}
}