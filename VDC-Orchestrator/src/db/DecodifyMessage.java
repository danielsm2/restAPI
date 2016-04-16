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
	 * Devuelve un codigo de error o, en su defecto, un codigo de confirmacion. Esta funcion se llama a traves
	 * de una peticion POST. Una vez terminada, se habra creado o actualizado una entrada.
	 * @param vdc
	 * @return
	 */
	public ErrorCheck startParse(VDC vdc){
		this.vdc = vdc;
		ErrorCheck ec = vdc.checkVDC();
		if(ec.equals(ErrorCheck.ALL_OK)){
			db = DataBase.getInstance();
			try{
				rs = db.queryDB(vdc.entryCheckerDB_vdc());
				if(vdc.checkRow(rs))
					vdc.updateVdc();
				decodifyVnode(vdc.getNumElemVnode());
				ErrorCheck ecbis = decodifyVlink(vdc.getNumElemVlink());
				if(ecbis.equals(ErrorCheck.VNODE_FROM_VLINK_WRONG))
					return ecbis;
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
			rs = db.queryDB(vdc.entryCheckerDB_vnode(i));
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
	private ErrorCheck decodifyVlink(int vlink) throws SQLException{
		ErrorCheck ec;
		for(int i = 0; i < vlink; ++i){
			rs = db.queryDB(vdc.entryCheckerDB_vlink(i));
			ec = vdc.updateVlink(i,vdc.checkRow_vlink(rs,i));
			if(ec.equals(ErrorCheck.VNODE_FROM_VLINK_WRONG))
				return ec;
		}
		return ErrorCheck.ALL_OK;
	}
}
