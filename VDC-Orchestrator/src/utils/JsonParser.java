package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import api.nova.Flavor;
import api.nova.Host;

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
		} finally {
			reader.close();
		}
	}
	
	public Map<String,Host> readHostList(InputStream in) throws IOException {
		
		Map<String,Host> hosts = new HashMap<String,Host>(0);
		try {
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			reader.nextName();
			reader.beginArray();
			while (reader.hasNext()) {
				reader.beginObject();
				String name = null;
				boolean insert = false;
				while(reader.hasNext()) {
					String n = reader.nextName();
					if(n.equals("host_name")) {
						name = reader.nextString();
					}
					else if(n.equals("service")) {
						String service = reader.nextString();
						if(service.equals("compute"))
							insert = true;
						else
							insert = false;
					}
					else {
						reader.skipValue();
					}
				}
				reader.endObject();
				if(insert)
					//hosts.add(new Host(name));
					hosts.put(name, new Host());
			}
			reader.endArray();
			reader.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
		
		return hosts;
	}
	
	public void readHost(InputStream in, Host host) throws IOException {
		
		try {
			
			int cpu = 0, cpu_used = 0;
			double disk = 0, disk_used = 0;
			double memory = 0, memory_used = 0;
			
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			reader.nextName();
			reader.beginArray();
			while(reader.hasNext()) {
				reader.beginObject();
				reader.nextName();
				reader.beginObject();
				int c = 0;
				double d = 0;
				double m = 0;
				String project = null;
				while(reader.hasNext()) {
					String n = reader.nextName();
					if (n.equals("cpu")) {
						c = reader.nextInt();
					}else if(n.equals("disk_gb")) {
						d = reader.nextDouble();
					} else if(n.equals("memory_mb")){
						m = reader.nextDouble();
					} else if(n.equals("project")) {
						project = reader.nextString();
					} else {
						reader.skipValue();
					}
				}
				reader.endObject();
				reader.endObject();
				if(project.equals("(total)")){
					cpu = c;
					disk = d;
					memory = m;
				} else if(project.equals("(used_now)")){
					cpu_used = c;
					disk_used = d;
					memory_used = m;
				}
				
			}
			host.setCpus(cpu-cpu_used);
			host.setDisk(disk-disk_used);
			host.setMemory(memory-memory_used);
			reader.endArray();
			reader.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}
}