package jsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vdc.VDC;

public class JsonParser {
	
	/**
	 * Se encarga del parseo vdc-json
	 * @param vdc
	 * @return
	 */
	public String toJson(VDC vdc){
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(vdc, VDC.class);
		System.out.println("Print de json creado: " + json);
		return json;
	}
	
	/**
	 * Se encarga del parseo json-vdc
	 * @param json
	 * @return
	 */
	public VDC fromJson(String json){
		Gson gson = new Gson();
		VDC vdc = (VDC) gson.fromJson(json, new VDC().getClass());
		vdc.printInfo();
	
		//String vdc_return = gson.toJson(vdc, VDC.class);
		//System.out.println("use of toJson to prove if it works:      \n\n");
		//System.out.println(vdc_return);
		return vdc;
		
	}
}
