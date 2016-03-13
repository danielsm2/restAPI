package db;

import vdc.VDC;

public class DecodifyMessage {

	private VDC vdc;
	private DataBase db;
	
	public void startParse(VDC vdc){
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
		this.vdc = vdc;
		db = DataBase.getInstance();
		String statement = vdc.getInfoVdc();
		db.addRow(statement);
		decodifyVnode(vdc.getNumElemVnode());
		decodifyVlink(vdc.getNumElemVlink());
	}
	
	private void decodifyVnode(int vnode){
		String statement;
		for(int i = 0; i < vnode; ++i){
			statement = vdc.getInfoVnode(i);
			db.addRow(statement);
			vdc.addInfoVm(i);
		}
	}
	
	private void decodifyVlink(int vlink){
		String statement;
		for(int i = 0; i < vlink; ++i){
			statement = vdc.getInfoVlink(i);
			db.addRow(statement);
		}
	}
}
