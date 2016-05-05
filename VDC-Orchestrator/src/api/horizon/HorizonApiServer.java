package api.horizon;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpServer;

/** The HorizonApiServer class implements a REST API for the communication between the VDC dashboard in Horizon and the algorithms module. For this, it
 * implements an HTTP server that accepts CRUD requests (POST, READ and DELETE operations) form the dashborad to allow the creation of new VDC instances, consulting
 * the details of already established VDCs, modify/update previously deployed VDCs and eliminate an established VDC. The client of the API at the Horizon side has to
 * send an HTTP request to the specified URI, providing the method and the parameters of the operation requested.
 * 
 * @author Albert Pagès, Dani Sanchez, Universitat Politècnica de Catalunya (UPC), Barcelona, Spain
 * @version v4.0 March 01, 2016
 * 
 */
public class HorizonApiServer {
	
	/** The HTTP server that accepts the requests coming from the dashboard. */
	private HttpServer server;
	
	/** The IP socket address (IP + port number) to which the HTTP server is bound. */
	private InetSocketAddress addr;
	
	/** Creates a new HorizonApiServer object, initializing the address of the HTTP server with the desired IP and port and binds the server to this address.
	 *
	 * @param hostname - A String representing the IP address or hostname to which the HTTP server has to be bound.
	 * @param port - An integer representing the port number to which the HTTP server has to bound.
	 * @param backlog - An integer representing the socket backlog, that is, the maximum number of queued incoming connections to allow on the listening socket.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public HorizonApiServer(String hostname, int port, int backlog) throws IOException {
		addr = new InetSocketAddress(hostname, port);
		server = HttpServer.create();
		server.bind(addr, backlog);
	}
	
	/** Sets the context for accepting incoming requests, passing as parameters the desired URI, the handler for treating the requests and the credentials to authenticate
	 * with the server.
	 *
	 * @param uri - A String representing the URI in which the server accepts requests.
	 * @param handler - A HorizonApiHandler object utilized to treat the HTTP exchange of the requests.
	 * @param username - A String stating the username to access the server.
	 * @param password - A String stating the password to access the server.
	 */
	public void setContext(String uri, HorizonApiHandler handler, final String username, final String password) {
		server.createContext(uri, handler).setAuthenticator(new BasicAuthenticator(uri) {
	        @Override
	        public boolean checkCredentials(String user, String pwd) {
	            return user.equals(username) && pwd.equals(password);
	        }
	    });
	}
	
	/** Starts the HTTP server, allowing it to listen to incoming requests.
	 * 
	 */
	public void start() {
		
		server.start();
	}
}