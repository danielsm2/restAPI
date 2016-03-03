package main;

import java.net.InetSocketAddress;

import api.Controller;

public class main {

	public static void main(String[] args) 
	{
		Controller c = new Controller(new InetSocketAddress("localhost",12119),0);
		c.start();
	}
}
