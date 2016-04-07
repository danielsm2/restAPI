package main;

import java.net.InetSocketAddress;

import api.horizon.Controller;
import db.DataBase;

public class Main {

	public static void main(String[] args) 
	{
		DataBase db = DataBase.getInstance();
		db.startDB();
		Controller c = new Controller(new InetSocketAddress("0.0.0.0",12119),0);
		c.start();
		System.out.println("Server is listening...");
	}
}
