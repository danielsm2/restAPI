package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import api.nova.Flavor;
import api.nova.Host;
import tenant.Tenant;
import tenant.TenantList;
import topology.Switch;
import topology.Topology;
import vdc.VDC;

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
	public void readTopology(InputStream in, Topology topology, Map<String, Host> hosts){
		try{
			
			hosts.put("hola", new Host("08:00:27:65:42:a0"));
			hosts.put("adios", new Host("08:00:27:4b:4f:c9"));

			Map<String, Integer> collectNode = new HashMap<String, Integer>();
			List<String> excludes = new ArrayList<String>();
			Integer countH = 0;
			Integer countS = 0;
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			reader.nextName();
			reader.beginObject();
			reader.nextName();
			reader.beginArray();
			reader.beginObject();
			reader.nextName();
			reader.skipValue();

			while(reader.hasNext()){
				String aux = reader.nextName();
				if(aux.equals("node")){
					reader.beginArray();
					while(reader.hasNext()){
						reader.beginObject();
						while(reader.hasNext()){
							aux = reader.nextName();
							if(aux.equals("node-id")){						
								String node_id = reader.nextString();
								if(node_id.contains("host")){
									String[] mac = node_id.split(":");
									String res = mac[1];
									for(int i = 2; i < mac.length; ++i)
										res = res + ":" + mac[i];
									
									for(Entry<String,Host> host : hosts.entrySet()){
										if(host.getValue().getMac().equals(res)){
											topology.addHost(new Host(res));
											collectNode.put(res, countH++);
											break;
										}
									}
									if(!collectNode.containsKey(res))
										excludes.add(res);
								}
								else{
									topology.addSwitch(new Switch(node_id.split(":")[1]));
									collectNode.put(node_id.split(":")[1], countS++);
								}
							}
							else{
								reader.skipValue();
							}
						}
						reader.endObject();
					}
					reader.endArray();
				}
				else if(aux.equals("link")){
					String src = "";
					String dest = "";
					reader.beginArray();
					while(reader.hasNext()){
						reader.beginObject();
						while(reader.hasNext()){
							aux = reader.nextName();
							if(aux.equals("destination")){
								reader.beginObject();
								reader.nextName();
								String pDest = reader.nextString();
								String[] id = pDest.split(":");
								dest = id[1];
								if(id[0].equals("host"))
									for(int i = 2; i < id.length; ++i)
										dest = dest + ":" + id[i];
								reader.skipValue();
								reader.skipValue();
								reader.endObject();
							}
							else if(aux.equals("source")){
								reader.beginObject();
								reader.nextName();
								String pSrc = reader.nextString();
								String[] id = pSrc.split(":");
								src = id[1];
								if(id[0].equals("host"))
									for(int i = 2; i < id.length; ++i)
										src = src + ":" + id[i];
								if(!excludes.contains(src) && !excludes.contains(dest) && !topology.checkLinks(src, dest))
									topology.addLink(src, collectNode.get(src), dest, collectNode.get(dest));
								
								reader.skipValue();
								reader.skipValue();
								reader.endObject();
							}
							else{
								reader.skipValue();
							}
						}
						reader.endObject();
					}		
				}
				else {
					reader.skipValue();
				}
			}
			reader.endArray();
			reader.endObject();
			reader.endArray();
			reader.endObject();
			reader.endObject();
			
			topology.clearTopology();
			topology.printSwitch();
			topology.printHost();
			topology.printLink();

		} catch(Exception e){
			e.printStackTrace();
		}
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
	
	public List<Tenant> getTenants(String json){
		Gson gson = new Gson();
		TenantList tenants = (TenantList) gson.fromJson(json, TenantList.class);
			
		return tenants.getTenants();
	}
	
	/**
	 * Devuelve un string resultado de transformar un objeto VDC a json
	 * @param vdc
	 * @return
	 */
	public String toJson(VDC vdc){
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.create();
		String json = gson.toJson(vdc, VDC.class);
		return json;
	}
	
	/**
	 * Devuelve un objeto de tipo VDC resultado de parsear un json
	 * @param json
	 * @return
	 */
	public VDC fromJson(String json){
		Gson gson = new Gson();
		VDC vdc = (VDC) gson.fromJson(json, VDC.class);
		vdc.printInfo();
			
		return vdc;
	}
}