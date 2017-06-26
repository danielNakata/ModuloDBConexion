/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dnn.nominae.modulobdconexion;

import dnn.nominae.modulobdconexion.db.Consulta;
import dnn.nominae.modulobdconexion.db.utils.Conexion;
import dnn.nominae.modulobdconexion.dto.QryRespDTO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author danie
 */
public class ModuloBDConexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Conexion con = new Conexion();
        con.creaConexion("192.200.1.26", "dba", "5575349", "2638", "master64", "com.sybase.jdbc4.jdbc.SybDriver", "jdbc:sybase:Tds:");
        //con.creaConexion("localhost", "root", "123456", "3306", "modprod", "com.mysql.jdbc.Driver", "jdbc:mysql:");
        Connection conn = con.getConexion();
        QryRespDTO resp = null;
        Consulta qry = new Consulta();
        if(conn != null){
            ArrayList<Object> params = new ArrayList();
            params.add(30);
            params.add(5);
            params.add(2017);
            String sql = "{call MCT_FN_CONSULTATBLVENTAS_WEB(?,?,?);}";
            
            resp = new Consulta().ejecutaSelectSP(conn, sql, params);
            
            System.out.println("resp: " + resp.toString());
            /*
            params.add("DANIEL");
            ArrayList<Integer> paramsOut = new ArrayList();
            paramsOut.add(java.sql.Types.VARCHAR);
            resp = new Consulta().ejecutaFN(conn, "{? = call MDP_FNC_MENSAJE(?)}",params, paramsOut);
            System.out.println("resp: " + resp.toString());
            
            ArrayList<Integer> paramsOut = new ArrayList();
            paramsOut.add(java.sql.Types.INTEGER);
            paramsOut.add(java.sql.Types.VARCHAR);
            paramsOut.add(java.sql.Types.INTEGER);
            paramsOut.add(java.sql.Types.INTEGER);
            paramsOut.add(java.sql.Types.INTEGER);
            
            resp = qry.consulta(conn, "select * from fgrupos");
            if(resp.getRes()==1){
                System.out.println(resp.getDatosTabla());
            }
            
            
            ArrayList<Object> params1 = new ArrayList();
            params1.add(5);
            params1.add(5);
            resp = qry.ejecutaSelectSP(conn, "{CALL NE_FN_CONSULTAUSUARIOS(?,?)}", params1);
            System.out.println(resp.toString());
            
            ArrayList<Object> params = new ArrayList();
            params.add("ADMIN");
            params.add("123456");
            params.add("127.0.0.1");
            resp = qry.ejecutaSP(conn, "{ call NE_SP_INICIOSESION(?,?,?,?,?,?,?,?)}", params, paramsOut);
            System.out.println(resp.toString());
            */
        }
        
        qry.cerrarConexion(conn);
    }
    
}
