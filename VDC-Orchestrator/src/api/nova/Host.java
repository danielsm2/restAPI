package api.nova;

public class Host {
	
	private String name;
	private int cpus;
	private double disk;
	private double memory;
	
	public Host(String name, int cpus, double disk, double memory) {
		this.name = name;
		this.cpus = cpus;
		this.disk = disk;
		this.memory = memory;
	}

	public Host(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	@Override
	public String toString() {
		return "Host [name=" + name + ", cpus=" + cpus + ", disk=" + disk + ", memory=" + memory + "]";
	}
}
