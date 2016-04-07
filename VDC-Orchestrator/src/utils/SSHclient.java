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
	
	public SSHclient() {
		jsch = new JSch();
	}

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
	
	public void disconnect(){
		session.disconnect();
	}
	
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