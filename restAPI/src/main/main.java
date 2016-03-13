package main;

import java.net.InetSocketAddress;

import api.Controller;
import db.DataBase;

public class Main {

	public static void main(String[] args) 
	{
		DataBase db = DataBase.getInstance();
		db.initializeDB();
		System.out.println("Prepared to get request...");
		Controller c = new Controller(new InetSocketAddress("localhost",12119),0);
		c.start();
	}
}