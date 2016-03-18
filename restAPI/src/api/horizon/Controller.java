package api.horizon;

import com.sun.net.httpserver.HttpServer;

import api.horizon.VDCHandler;

import java.net.InetSocketAddress;

public class Controller {
	HttpServer server;
	
	/**
	 * Se crea el servidor HTTP.
	 * Request permitidas: POST,GET y DELETE.
	 * @param address
	 * @param backlog
	 */
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
	
	/**
	 * Se activa la escucha de peticiones del servidor
	 */
	public void start(){
		server.start();
	}
}
