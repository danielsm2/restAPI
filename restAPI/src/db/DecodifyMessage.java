package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import person.Persona;

public class DecodifyMessage {

	private String name;
	private String age;
	private String location;
	
	public void startParse(Persona p){
		/*BufferedReader br = new BufferedReader(new StringReader(message));
		String info;
		
		try{
			while((info = br.readLine()) != null){
				String[] s = info.split(":");
				System.out.println("aqui  " + s[0]);
				if(s[0].equals("name"))
					name = s[1];
				else if(s[0].equals("age"))
					age = s[1];
				else if(s[0].equals("location"))
					location = s[1];
				else{
					System.err.println("Can't filter the key: "+ s[0]);
					System.exit(1);
				}
				System.out.println("aqui2");
			}
			if(!name.isEmpty() & !age.isEmpty() & !location.isEmpty()){
				Persona p = new Persona(name, Integer.parseInt(age), location);
				DataBase db = DataBase.getInstance();
				db.addRow(p);
			}
		} catch(IOException io){
			System.err.println(io);
		}*/
		DataBase db = DataBase.getInstance();
		db.addRow(p);
	}
}
