package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;

import api.nova.Flavor;

public class JsonParser {

	private JsonReader reader;
	
	public JsonParser() {
		
	}
	
	public ArrayList<Flavor> readFlavorList(InputStream in) throws IOException {
		
		ArrayList<Flavor> flavors = new ArrayList<Flavor>(0);
		
		try {
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			reader.nextName();
			reader.beginArray();
			while (reader.hasNext()) {
				reader.beginObject();
				int id = 0;
				String name = null;
				while (reader.hasNext()) {
					String n = reader.nextName();
					   
					   if(n.equals("id"))
					   {
						   id = reader.nextInt();
					   }
					   else if(n.equals("name"))
					   {
						   name = reader.nextString();
					   }
					   else
					   {
						   reader.skipValue();
					   }
				   }
				flavors.add(new Flavor(id,name));
				reader.endObject();
			   }
			reader.endArray();
			reader.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
		
		return flavors;
	}
	
	public void readFlavor(InputStream in, Flavor flavor) throws IOException {
		
		try {
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			reader.nextName();
			reader.beginObject();;
			while (reader.hasNext()) {
				String n = reader.nextName();
				
				if(n.equals("disk")) {
					
					flavor.setDisk(reader.nextDouble());
					
				} else if (n.equals("ram")) {
					
					flavor.setMemory(reader.nextDouble());
					
				} else if (n.equals("vcpus")) {
					
					flavor.setVcpus(reader.nextInt());
					
				} else {
					
					reader.skipValue();
				}
				
			}
			reader.endObject();
			reader.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
