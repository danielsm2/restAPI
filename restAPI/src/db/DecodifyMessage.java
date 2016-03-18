package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import vdc.ErrorCheck;
import vdc.VDC;

public class DecodifyMessage {

	private VDC vdc;
	private DataBase db;
	private ResultSet rs;
	
	/**
	 * Funcion que se encarga de parsear el mensaje que se obtiene, se llama
	 * desde un POST
	 * @param vdc
	 * @return
	 */
	public ErrorCheck startParse(VDC vdc){
		this.vdc = vdc;
		ErrorCheck ec = vdc.checkVDC();
		if(ec.equals(ErrorCheck.ALL_OK)){
			db = DataBase.getInstance();
			try{
				rs = db.checkEntryDB(vdc.entryCheckerDB_vdc());
				if(vdc.checkRow(rs))
					vdc.updateVdc();
				decodifyVnode(vdc.getNumElemVnode());
				decodifyVlink(vdc.getNumElemVlink());
				System.out.println("*************** DB updated ***************");
			}catch(SQLException e){
				System.err.println(e);
				System.exit(1);
			}
		}
		return ec;
	}
	
	/**
	 * Decodificacion perteneciente a vnode.
	 * Se encarga de realizar una query contra la db.
	 * Dependiendo del resultado de la query, se hara 
	 * un update o un insert.
	 * @param vnode
	 * @throws SQLException
	 */
	private void decodifyVnode(int vnode) throws SQLException{
		for(int i = 0; i < vnode; ++i){
			rs = db.checkEntryDB(vdc.entryCheckerDB_vnode(i));
			vdc.updateVnode(i,vdc.checkRow_vnode(rs, i));
		}
	}
	
	/**
	 * Decodificacion perteneciente a vlink.
	 * Se encarga de realizar una query contra la db.
	 * Dependiendo del resultado de la query, se hara 
	 * un update o un insert.
	 * @param vlink
	 * @throws SQLException
	 */
	private void decodifyVlink(int vlink) throws SQLException{
		for(int i = 0; i < vlink; ++i){
			rs = db.checkEntryDB(vdc.entryCheckerDB_vlink(i));
			vdc.updateVlink(i,vdc.checkRow_vlink(rs,i));
		}
	}
}
