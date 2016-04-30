package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHclient {
	
	JSch jsch;
	Session session;
	
	/**
	 * Creates a new instance of SSHclient
	 */
	public SSHclient() {
		jsch = new JSch();
	}

	/**
	 * Set up a ssh session in order to connect into it 
	 * @param user - user which will be logged
	 * @param hostIP - IP representing the target terminal
	 * @param port - port in which will be listening
	 * @param password - user's password
	 */
	public void connect(String user, String hostIP, int port, String password){
		try {
			session=jsch.getSession(user, hostIP, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Disconnect the connection
	 */
	public void disconnect(){
		session.disconnect();
	}
	
	/**
	 * Execute an ifconfig command in order to get the required information
	 * @return
	 */
	public String[] ExecuteIfconfig(){		
		StringBuilder outputBuffer = new StringBuilder();
		
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand("/sbin/ifconfig");
			InputStream commandOutput = channel.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(commandOutput));
	        channel.connect();
	        String line;
	        
	        while((line = reader.readLine()) != null)
	        {
	           outputBuffer.append(line+"\n");
	        }
	        
	        reader.close();
	        channel.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return outputBuffer.toString().split("\n");
	}
}