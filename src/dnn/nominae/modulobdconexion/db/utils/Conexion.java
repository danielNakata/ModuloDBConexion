/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dnn.nominae.modulobdconexion.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author danie
 */
public class Conexion {
    /**
     * Objeto para la conexion
     */
    private Connection conn = null;
    
    /**
     * Constructor que recibe los datos del servidor de la base de datos y crea el recurso 
     * para la conexion
     * @param dbHost
     * @param dbUser
     * @param dbPass
     * @param dbPort
     * @param dbName
     * @param dbClassDriver
     * @param dbUrl
     */
    public Conexion(String dbHost
            , String dbUser
            , String dbPass
            , String dbPort
            , String dbName
            , String dbClassDriver
            , String dbUrl){
        try{
            Class.forName(dbClassDriver);
            conn = null;
            conn = DriverManager.getConnection(dbUrl+"//"+ dbHost+":"+dbPort+"/"+dbName,dbUser,dbPass);
        }catch(Exception ex){
            conn = null;
        }
    }
    
    /**
     * Constructor generico de la clase
     */
    public Conexion(){ }
    
    /**
     * Metodo para crear la conexion a la base de datos especificando todos los campos
     * @param dbHost
     * @param dbUser
     * @param dbPass
     * @param dbPort
     * @param dbName
     * @param dbClassDriver
     * @param dbUrl
     * @return objeto tipo java.sql.Connection
     */
    public void creaConexion(String dbHost
            , String dbUser
            , String dbPass
            , String dbPort
            , String dbName
            , String dbClassDriver
            , String dbUrl){
        try{
            Class.forName(dbClassDriver);
            conn = null;
            if(dbUrl.toLowerCase().contains("sybase")){
                conn = DriverManager.getConnection(dbUrl+dbHost+":"+dbPort+"/"+dbName,dbUser,dbPass);
            }
            if(dbUrl.toLowerCase().contains("mysql")){
                conn = DriverManager.getConnection(dbUrl+"//"+ dbHost+":"+dbPort+"/"+dbName,dbUser,dbPass);
            }
            
        }catch(Exception ex){
            conn = null;
        }
    }
        
    /**
     * Retorna el objeto de la conexion, nulo si no se realizo una conexion previa
     * @return objeto tipo java.sql.Connection
     */
    public Connection getConexion(){
        return conn;
    }
}
