package conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import utils.JsonParser;

public class Conf {

	public static String IP_Horizon;
	public static String Port_Horizon;
	public static String User_Horizon;
	public static String Pass_Horizon;
	public static String IP_Keystone;
	public static String User_Keystone;
	public static String Pass_Keystone;
	public static String IP_Nova;
	public static String IP_ODL;
	public static String User_ODL;
	public static String Pass_ODL;
	public static String IP_Heat;
	public static String User_BD_Horizon;
	public static String Pass_BD_Horizon;
	public static String User_BD_Nova;
	public static String Pass_BD_Nova;
	public static String User_Compute;
	public static String Pass_Compute;

	public static void initiateConf() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("server.conf", "UTF-8");
		
		writer.println("IP_Horizon=0.0.0.0");
		writer.println("Port_Horizon=12119");
		writer.println("User_Horizon=admin");
		writer.println("Pass_Horizon=admin");
		writer.println("IP_Keystone=172.26.37.249");
		writer.println("User_Keystone=admin");
		writer.println("Pass_Keystone=cosign");
		writer.println("IP_Nova=172.26.37.249");
		writer.println("IP_ODL=172.26.37.89");
		writer.println("User_ODL=admin");
		writer.println("Pass_ODL=admin");
		writer.println("IP_Heat=172.26.37.249");
		writer.println("User_BD_Horizon=root");
		writer.println("Pass_BD_Horizon=password");
		writer.println("User_BD_Nova=root");
		writer.println("Pass_BD_Nova=cosign");
		writer.println("User_Compute=stack");
		writer.println("Pass_Compute=stack");
		/*writer.println("IP_Horizon=");
		writer.println("Port_Horizon=");
		writer.println("User_Horizon=");
		writer.println("Pass_Horizon=");
		writer.println("IP_Keystone=");
		writer.println("User_Keystone=");
		writer.println("Pass_Keystone=");
		writer.println("IP_Nova=");
		writer.println("IP_ODL=");
		writer.println("User_ODL=");
		writer.println("Pass_ODL=");
		writer.println("User_BD_Horizon=");
		writer.println("Pass_BD_Horizon=");
		writer.println("User_BD_Nova=");
		writer.println("Pass_BD_Nova=");
		writer.println("User_Compute=");
		writer.println("Pass_Compute=");*/
		writer.close();
	}
	
	public static void readConf() throws IOException{
		File check = new File("server.conf");
		if(!check.exists()){
			initiateConf();
			System.err.println("The configuration file 'server.conf' must exist. Refill the configuration file created at " + check.getAbsolutePath());
			System.exit(1);
		}
		BufferedReader br = new BufferedReader(new FileReader("server.conf"));
		
		String line = null;
		while((line = br.readLine()) != null){
			if(line.contains("IP_Horizon"))
				IP_Horizon = line.split("=")[1];
			else if(line.contains("Port_Horizon"))
				Port_Horizon = line.split("=")[1];
			else if(line.contains("User_Horizon"))
				User_Horizon = line.split("=")[1];
			else if(line.contains("Pass_Horizon"))
				Pass_Horizon = line.split("=")[1];
			else if(line.contains("IP_Keystone"))
				IP_Keystone = line.split("=")[1];
			else if(line.contains("User_Keystone"))
				User_Keystone = line.split("=")[1];
			else if(line.contains("Pass_Keystone"))
				Pass_Keystone = line.split("=")[1];
			else if(line.contains("IP_Nova"))
				IP_Nova = line.split("=")[1];
			else if(line.contains("IP_ODL"))
				IP_ODL = line.split("=")[1];
			else if(line.contains("User_ODL"))
				User_ODL = line.split("=")[1];
			else if(line.contains("Pass_ODL"))
				Pass_ODL = line.split("=")[1];
			else if(line.contains("IP_Heat"))
				IP_Heat = line.split("=")[1];
			else if(line.contains("User_BD_Horizon"))
				User_BD_Horizon = line.split("=")[1];
			else if(line.contains("Pass_BD_Horizon"))
				Pass_BD_Horizon = line.split("=")[1];
			else if(line.contains("User_BD_Nova"))
				User_BD_Nova = line.split("=")[1];
			else if(line.contains("Pass_BD_Nova"))
				Pass_BD_Nova = line.split("=")[1];
			else if(line.contains("User_Compute"))
				User_Compute = line.split("=")[1];
			else if(line.contains("Pass_Compute"))
				Pass_Compute = line.split("=")[1];
		}
		br.close();
	}

	public List<String> readDBFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("codeDB.txt"));
		List<String> sqlStatements = new ArrayList<String>();
		String sql = "";
		String aux = "";
		while((aux = br.readLine()) != null){
			sql += aux;
			if(sql.contains(";")){
				sqlStatements.add(sql);
				sql = "";
			}		
		}
		
		return sqlStatements; 
	}	
}
