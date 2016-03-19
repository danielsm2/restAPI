package api.keystone;

import java.util.ArrayList;

public class Identity {

	private ArrayList<String> methods;
	private Password password;
	
	public Identity(Password password) {
		this.password = password;
		this.methods = new ArrayList<String>(0);
	}

	public ArrayList<String> getMethods() {
		return methods;
	}

	public void setMethods(ArrayList<String> methods) {
		this.methods = methods;
	}

	public void addMethod(String method) {
		methods.add(method);
	}
	
	public String getMethod(int index) {
		return methods.get(index);
		
	}
	
	public void replaceMethod(int index, String method) {
		methods.set(index, method);
	}
	
	public void deleteMethod(int index) {
		methods.remove(index);
	}
	
	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}
}
