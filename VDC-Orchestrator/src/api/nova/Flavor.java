package api.nova;

public class Flavor {
	
	private int ID;
	private String name;
	private double memory;
	private double disk;
	private int vcpus;
	
	public Flavor(int iD, String name, double memory, double disk, int vcpus) {
		ID = iD;
		this.name = name;
		this.memory = memory;
		this.disk = disk;
		this.vcpus = vcpus;
	}

	public Flavor(int iD, String name) {
		ID = iD;
		this.name = name;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getDisk() {
		return disk;
	}

	public void setDisk(double disk) {
		this.disk = disk;
	}

	public int getVcpus() {
		return vcpus;
	}

	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}

	@Override
	public String toString() {
		return "Flavor [ID=" + ID + ", name=" + name + ", memory=" + memory + ", disk=" + disk + ", vcpus=" + vcpus
				+ "]";
	}
}