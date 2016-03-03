package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;

import api.VDCHandler;

public class Controller {
	HttpServer server;
	
	public Controller(InetSocketAddress address, int backlog) {
		try{
			server = HttpServer.create();
			server.bind(address, backlog);
			server.createContext("/orchestrator/algorithms/vdc/", new VDCHandler());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void start(){
		server.start();
	}
}
