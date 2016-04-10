package jsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vdc.VDC;

public class JsonParser {
	
	/**
	 * Devuelve un string resultado de transformar un objeto VDC a json
	 * @param vdc
	 * @return
	 */
	public String toJson(VDC vdc){
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(vdc, VDC.class);
		//System.out.println("Print de json creado: " + json);
		return json;
	}
	
	/**
	 * Devuelve un objeto de tipo VDC resultado de parsear un json
	 * @param json
	 * @return
	 */
	public VDC fromJson(String json){
		Gson gson = new Gson();
		VDC vdc = (VDC) gson.fromJson(json, new VDC().getClass());
		vdc.printInfo();
			
		return vdc;
	}
}
