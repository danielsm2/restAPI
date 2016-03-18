package api.horizon;

import com.sun.net.httpserver.HttpServer;

import api.horizon.VDCHandler;

import java.net.InetSocketAddress;

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
