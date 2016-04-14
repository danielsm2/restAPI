package api.nova;

import topology.Node;

public class Host extends Node{
	
	private int cpus;
	private double disk;
	private double memory;
	private String mac;
	
	public Host(){}

	public Host(String mac){
		super(mac);
	}
	
	public Host(String id, Node src, Node dest){
		super(id,src,dest); 
	}
	
	public Host(int cpus, double disk, double memory) {
		//this.name = name;
		this.cpus = cpus;
		this.disk = disk;
		this.memory = memory;
		this.mac = "";
	}

	/*public Host(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}*/

	public int getCpus() {
		return cpus;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public double getDisk() {
		return disk;
	}

	public void setDisk(double disk) {
		this.disk = disk;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}
	
	public String getMac(){
		return this.mac;
	}
	
	public void setMac(String mac){
		this.mac = mac;
	}

	@Override
	public String toString() {
		//return "Host [name=" + name + ", cpus=" + cpus + ", disk=" + disk + ", memory=" + memory + "]";
		return "[cpus=" + cpus + ", disk=" + disk + ", memory=" + memory + " mac=" + mac + "]";
	}
}
