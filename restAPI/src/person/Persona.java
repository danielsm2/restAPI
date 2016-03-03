package person;

public class Persona {

	private String name;
	private int age;
	private String location;
	
	public Persona(){};
	public Persona(String name, int age, String location){
		this.name = name;
		this.age = age;
		this.location = location;
	}
	
	public void printInfo(){
		System.out.print("Name: "+name+" Age: "+age+" Location: "+location);
	}
}
