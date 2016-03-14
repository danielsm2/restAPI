package api.keystone;

public class User {
	
	private String name;
	private Domain domain;
	private String password;
	
	public User(String name, Domain domain, String password) {
		this.name = name;
		this.domain = domain;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Domain getDomain() {
		return domain;
	}
	
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}