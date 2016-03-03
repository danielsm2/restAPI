package jsonParser;

import java.util.List;

import com.google.gson.Gson;

import person.Persona;

public class JsonParser {

	public String toJson(){
		Gson gson = new Gson();
		Persona person = new Persona("daniel", 23, "barcelona");
		String json = gson.toJson(person);
		System.out.println(json);
		return json;
	}
	
	public void fromJson(String json){
		Gson gson = new Gson();
		Persona info = (Persona) gson.fromJson(json, new Persona().getClass());
		info.printInfo();
	}
}
