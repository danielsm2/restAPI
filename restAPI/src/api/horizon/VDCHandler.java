package api.horizon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
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
	private DataBase db = DataBase.getInstance();

	public void handle(HttpExchange e) throws IOException {
		
		JsonParser parser = new JsonParser();
		String request = e.getRequestMethod();
		String response;
		
		if(request.equals("POST")){
			
			InputStream is = e.getRequestBody();
			@SuppressWarnings("resource")
			String message = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
			List<String> content = e.getRequestHeaders().get("Content-type");
			try{
				if(content != null && content.get(0).equals("application/json")){
					vdc = parser.fromJson(message);
					db.showDB(new VDC(), "vdc", vdc.getTenant(), "delete");
					db.deleteVDC(vdc.getTenant());
					ErrorCheck ec = dm.startParse(vdc);
					resRequest(ec,e,"POST","");		
				}
				else{
					resRequest(ErrorCheck.BAD_CONTENT_TYPE,e,"POST","");
				}
			} catch (SQLException e1) {
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
					db.deleteVDC(tenant);
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
					ErrorCheck ec;
					if(db.checkVDC(tenant)){
						vdc = db.getLocalVDC(tenant);
						ec = ErrorCheck.ALL_OK; 
					}
					else{
						 ec = db.showDB(vdc, "vdc", tenant, "get");
						 db.saveVDC(vdc);
					}
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
		else {
			Headers headers = e.getResponseHeaders();
			headers.add("Content-Type","text/plain");
			OutputStream os = e.getResponseBody();
			response = "Unsupported method. Server only accepts POST,DELETE and GET operations";
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
				response = "The VDC given has been registered";
				db.saveVDC(vdc);
			}
			else if(method.equals("DELETE"))
				response = "Due to the tenant informed the VDC has been deleted";
			else
				response = getResponse;
			e.sendResponseHeaders(201, response.length());
		}
		else if(ec.equals(ErrorCheck.VDC_NOT_COMPLETED)){
			response = "Incorrect content. Tenant id has to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VLINK_NOT_COMPLETED)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. All virtual link fields have to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VM_NOT_COMPLETED)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. All virtual machina fields have to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VNODE_NOT_COMPLETED)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. All virtual node fields have to be informed";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.VNODE_FROM_VLINK_WRONG)){
			db.rollbackVDC(vdc);
			response = "Incorrect content. Some of the virtual link fields are not referencing properly to virtual node fields";
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.TENANTID_NOT_FOUND)){
			response = "Incorrect content. Tenant id given is not found";
			e.sendResponseHeaders(404, response.length());
		}
		else if(ec.equals(ErrorCheck.TENANTID_REQUIRED)){
			response = "To correctly proceed with request '" + method + "', tenant id has to be informed";	
			e.sendResponseHeaders(400, response.length());
		}
		else if(ec.equals(ErrorCheck.BAD_CONTENT_TYPE)){
			response = "Incorrect content type. The server only accepts json files from body performing POST operations";
			e.sendResponseHeaders(400, response.length());
		}
		else{
			response = "Unexpected error. Restart system.";
			e.sendResponseHeaders(500, response.length());
		}
		
		OutputStream os = e.getResponseBody();
		os.write(response.getBytes());
		
		os.close();
	}
}