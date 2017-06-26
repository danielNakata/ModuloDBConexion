/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dnn.nominae.modulobdconexion.db;

import dnn.nominae.modulobdconexion.dto.CampoDTO;
import dnn.nominae.modulobdconexion.dto.ColumnaDTO;
import dnn.nominae.modulobdconexion.dto.QryRespDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author danie
 */
public class Consulta {
    
    
    private final static int OUT = 5;
    private final static int IN = 1;
    
    public Consulta() { }
    
    /**
     * Ejecuta un PLSQL
     * @param conn
     * @param sql
     * @return 
     */
    public QryRespDTO consultaPLSQL(Connection conn, String sql){
        QryRespDTO resp = new QryRespDTO();
        CallableStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareCall(sql);
            resp.setMsg("LA CONSULTA NO SE PUDO REALIZAR: " + sql);
            st.execute();
            resp.setRes(1);
            resp.setMsg("BLOQUE ANONIMO EJECUTADO");
        }catch(Exception ex){
            resp.setRes(0);
            resp.setMsg("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"consulta: " + sql+" | "+ex.toString());
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"consulta: " + sql+" | "+ex.toString());
        }finally{
            cerrarRecursos(st, rs);
        }        
        return resp;
    }

    /**
     * Ejecuta un query (select) que devuelve datos
     * @param conn objeto con el recurso de conexion al servidor
     * @param sql query (select) a ejecutar
     * @return 
     */
    public QryRespDTO consulta(Connection conn, String sql){
        QryRespDTO resp = new QryRespDTO();
        CallableStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareCall(sql);
            resp.setMsg("LA CONSULTA NO SE PUDO REALIZAR: " + sql);
            st.execute();
            rs = st.getResultSet();
            if(rs != null){
                resp.setResultado(rs);
                resp.setMetadatos(rs.getMetaData());
                //columnas
                resp.setColumnas(this.obtieneColumnas(resp.getMetadatos()));
                //datos
                if(resp.getColumnas().size()>0){
                    resp.setDatosTabla(this.obtieneDatos(rs, resp.getColumnas()));
                    resp.setNumReg(resp.getDatosTabla().size());
                }
                if(resp.getNumReg()>0){
                    resp.setMsg("CONSULTA REALIZADA: " + sql);
                    resp.setRes(1);
                }
            }
        }catch(Exception ex){
            resp.setRes(0);
            resp.setMsg("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"consulta: " + sql+" | "+ex.toString());
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"consulta: " + sql+" | "+ex.toString());
        }finally{
            cerrarRecursos(st, rs);
        }        
        return resp;
    }
    
    /**
     * ejecuta un SP que devuelve un resulset (select),
     * @param conn objeto con el recurso de conexion al servidor
     * @param sql qry llamada al sp en formato {call MI_STORED_PROCEDURE(?,?,?,...)
     * @param params ArrayList de objetos para pasar como parametros al SP, deben ir en orden
     *              de declaracion del SP y respetar su tipo, en caso que el SP no requiera
     *              parametros, esta lista se pude sustituir por un null
     * @return 
     */
    public QryRespDTO ejecutaSelectSP(Connection conn, String sql, ArrayList<Object> params){
        QryRespDTO resp = new QryRespDTO();
        CallableStatement st = null;
        ResultSet rs = null;
        int paramIn = 1, paramOut = 0;
        resp.setMsg("NO SE PUDO EJECUTAR EL SP: " + limpiaSQL(sql));
        try{
            st = conn.prepareCall(sql);
            if(params != null){
                for(Object param:params){
                    st.setObject(paramIn++, param);
                }
            }
            rs = st.executeQuery();
            if(rs == null){
                st.execute();
                rs = st.getResultSet();
                if(rs != null){
                    resp.setResultado(rs);
                    resp.setMetadatos(rs.getMetaData());
                    resp.setColumnas(this.obtieneColumnas(resp.getMetadatos()));
                    if(resp.getColumnas().size()>0){
                        resp.setDatosTabla(this.obtieneDatos(rs, resp.getColumnas()));
                        resp.setNumReg(resp.getDatosTabla().size());
                    }
                    if(resp.getNumReg()>0){
                        resp.setMsg("SP EJECUTADO: " + limpiaSQL(sql));
                        resp.setRes(1);
                    }
                }
            }else{
                resp.setResultado(rs);
                resp.setMetadatos(rs.getMetaData());
                resp.setColumnas(this.obtieneColumnas(resp.getMetadatos()));
                if(resp.getColumnas().size()>0){
                    resp.setDatosTabla(this.obtieneDatos(rs, resp.getColumnas()));
                    resp.setNumReg(resp.getDatosTabla().size());
                }
                if(resp.getNumReg()>0){
                    resp.setMsg("SP EJECUTADO: " + limpiaSQL(sql));
                    resp.setRes(1);
                }
            }
        }catch(Exception ex){
            resp.setRes(0);
            resp.setMsg("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"ejecutaSelectSP: " + limpiaSQL(sql)+" |"+ex.toString());
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"ejecutaSelectSP: " + limpiaSQL(sql)+" |"+ex.toString());
        }finally{
            cerrarRecursos(st, rs);
        }
        return resp;
    }
    
    /**
     * ejecuta un SP que devuelve un resulset (select),
     * @param conn objeto con el recurso de conexion al servidor
     * @param sql qry llamada al sp en formato {call MI_STORED_PROCEDURE(?,?,?,...)
     * @param params ArrayList de objetos para pasar como parametros al SP, deben ir en orden
     *              de declaracion del SP y respetar su tipo, en caso que el SP no requiera
     *              parametros, esta lista se pude sustituir por un null
     * @param paramsOut Array list de objetos para obtener los parametros de salida,
     *              deben ir en orden de declaracion del SP y respetar su tipo, en caso que el SP no
     *              lo requiera, esta lista se puede sustituir por un null
     * @return 
     */
    public QryRespDTO ejecutaSP(Connection conn, String sql, ArrayList<Object> params, ArrayList<Integer> paramsOut){
        QryRespDTO resp = new QryRespDTO();
        CallableStatement st = null;
        ResultSet rs = null;
        int paramIn = 1, paramOut = 0;
        ArrayList<CampoDTO> salida = new ArrayList();
        resp.setMsg("NO SE PUDO EJECUTAR EL SP: " + limpiaSQL(sql));
        try{
            st = conn.prepareCall(sql);
            if(params != null){
                for(Object param:params){
                    st.setObject(paramIn++, param);
                }
            }
            paramOut = paramIn;
            if(paramsOut != null){
                for(Integer param:paramsOut){
                    st.registerOutParameter(paramIn++, param);
                }
            }
            st.execute();
            resp.setMetadatosParam(st.getParameterMetaData());
            if(resp.getMetadatosParam() == null){
                int paramTotal = (params.size() + paramsOut.size());
                for(int i = paramOut; i <= paramTotal; i++){
                    CampoDTO dato = new CampoDTO();
                    dato.setValor(st.getObject(i));
                    salida.add(dato);
                }
            }else{
                for(int i = paramOut; i <= resp.getMetadatosParam().getParameterCount(); i++){
                    CampoDTO dato = new CampoDTO();
                    dato.setClase(resp.getMetadatosParam().getParameterClassName(i));
                    dato.setTipoId(resp.getMetadatosParam().getParameterType(i));
                    dato.setEtiqueta(resp.getMetadatosParam().getParameterTypeName(i));
                    dato.setValor(st.getObject(i));
                    salida.add(dato);
                }
            }
            
            if(salida.size()>0){
                resp.setParamOut(salida);
                resp.setRes(1);
                resp.setMsg("SP EJECUTADO: " + limpiaSQL(sql));
            }
            
        }catch(Exception ex){
            resp.setRes(0);
            resp.setMsg("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"ejecutaSP: " + limpiaSQL(sql)+" | "+ex.toString());
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"ejecutaSP: " + limpiaSQL(sql)+" | "+ex.toString());
        }finally{
            cerrarRecursos(st, rs);
        }
        return resp;
    }
    
    
    /**
     * Metodo para realizar llamada a una funcion, CON UN SOLO PARAMETRO DE SALIDA Y VARIOS DE ENTRADA
     * @param conn objeto con el recurso de conexion al servidor
     * @param sql qry llamada a la funcion en formato { ? = call MI_STORED_FUNCTION(?,?,?,...)
     * @param params ArrayList con los objetos para pasar como parametros de entrada 
     * @param paramsOut arraylist de enteros con un solo elemento, no puede haber mas
     * @return 
     */
    public QryRespDTO ejecutaFN(Connection conn, String sql, ArrayList<Object> params, ArrayList<Integer> paramsOut){
        QryRespDTO resp = new QryRespDTO();
        CallableStatement st = null;
        ResultSet rs = null;
        int paramIn = 1, paramOut = 0, paramReturn = 0;
        ArrayList<CampoDTO> salida = new ArrayList();
        resp.setMsg("NO SE PUDO EJECUTAR EL SP: " + limpiaSQL(sql));
        try{
            st = conn.prepareCall(sql);
            st.registerOutParameter(paramIn++, paramsOut.get(paramReturn));
            paramReturn++;
            if(params != null){
                for(Object param:params){
                    st.setObject(paramIn++, param);
                }
            }
            paramOut = paramIn;
            if(paramsOut.size() > 1){
                for(int i = 1; i < paramsOut.size(); i++){
                    st.registerOutParameter(paramIn++, paramsOut.get(i));
                }
            }
            st.execute();
            resp.setMetadatosParam(st.getParameterMetaData());
            if(resp.getMetadatosParam() == null){
                int paramTotal = (params.size() + paramsOut.size());
                CampoDTO paramSalida = new CampoDTO();
                paramSalida.setValor(st.getObject(1));
                
                for(int i = paramOut; i <= paramTotal; i++){
                    CampoDTO dato = new CampoDTO();
                    dato.setValor(st.getObject(i));
                    salida.add(dato);
                }
            }else{
                for(int i = 1; i <= resp.getMetadatosParam().getParameterCount(); i++){
                    CampoDTO dato = new CampoDTO();
                    dato.setClase(resp.getMetadatosParam().getParameterClassName(i));
                    dato.setTipoId(resp.getMetadatosParam().getParameterType(i));
                    dato.setEtiqueta(resp.getMetadatosParam().getParameterTypeName(i));
                    if(resp.getMetadatosParam().getParameterMode(i) == OUT){
                        dato.setValor(st.getObject(i));
                        salida.add(dato);
                    }
                    
                }
            }
            
            if(salida.size()>0){
                resp.setParamOut(salida);
                resp.setRes(1);
                resp.setMsg("FN EJECUTADO: " + limpiaSQL(sql));
            }
        }catch(Exception ex){
            resp.setRes(0);
            resp.setMsg("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"ejecutaSP: " + limpiaSQL(sql)+" | "+ex.toString());
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"ejecutaSP: " + limpiaSQL(sql)+" | "+ex.toString());
        }finally{
            cerrarRecursos(st, rs);
        }
        return resp;
    }
    
    /**
     * Metodo para obtener los datos resultantes de la consulta
     * @param rs
     * @param columnas
     * @return 
     */
    private ArrayList<HashMap<String, CampoDTO>> obtieneDatos(ResultSet rs, ArrayList<ColumnaDTO> columnas ){
        ArrayList<HashMap<String, CampoDTO>> datos = new ArrayList();
        try{
            while(rs.next()){
                HashMap<String, CampoDTO> fila = new HashMap();
                for(ColumnaDTO dto: columnas){
                    CampoDTO campo = new CampoDTO();
                    campo.setEtiqueta(dto.getEtiqueta());
                    campo.setNombre(dto.getNombre());
                    try{
                        campo.setValor(rs.getObject(dto.getEtiqueta())==null?"-":rs.getObject(dto.getEtiqueta()));
                    }catch(Exception ex){
                        campo.setValor("");
                    }
                    campo.setClase(dto.getClase());
                    campo.setTipoId(dto.getIdTipo());
                    fila.put(dto.getNombre(), campo);
                }
                datos.add(fila);
            }
        }catch(Exception ex){
            datos = null;
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"obtieneDatos"+"|"+ex.toString());
        }
        return datos;
    }
    
    /**
     * Metodo para obtener las columnas resultantes de la consulta
     * @param rsmd
     * @return 
     */
    private ArrayList<ColumnaDTO> obtieneColumnas(ResultSetMetaData rsmd){
        ArrayList<ColumnaDTO> lista = new ArrayList();
        try{
            for(int i= 1; i<=rsmd.getColumnCount(); i++){
                ColumnaDTO dto = new ColumnaDTO();
                try{
                    dto.setEtiqueta(rsmd.getColumnLabel(i));
                    dto.setNombre(rsmd.getColumnName(i));
                    dto.setTipo(rsmd.getColumnTypeName(i));
                    dto.setIdTipo(rsmd.getColumnType(i));
                    dto.setLongitud(rsmd.getColumnDisplaySize(i));
                    dto.setClase(rsmd.getColumnClassName(i));
                }catch(Exception ex){
                    System.out.println("Excepcion obteniendo las columnas: " + ex.toString());
                }
                lista.add(dto);
            }
        }catch(Exception ex){
            lista = null;
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"obtieneColumnas"+"|"+ex.toString());
        }        
        return lista;
    }
    
    /**
     * Metodo para cerrar las conexiones activas
     * @param st
     * @param rs 
     */
    private void cerrarRecursos(CallableStatement st, ResultSet rs){
        try{
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }catch(Exception ex){
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"cerrar"+"|"+ex.toString());
        }
    }
    
    /**
     * Metodo para cerrar la conexion de manera final
     * @param conn 
     */
    public void cerrarConexion(Connection conn){
        try{
            if(conn != null)
                conn.close();
        }catch(Exception ex){
            System.out.println("ModuloBDConexion"+"|"+this.getClass().toString()+"|"+"cerrarConexion"+"|"+ex.toString());
        }
    }
    
    
    private String limpiaSQL(String sql){
        return sql.replaceAll("\\{", "").replaceAll("call", "").replaceAll("\\}", "").replaceAll(",", "").replaceAll("\\?", "").replaceAll("\\(", "").replaceAll("\\)", "");
    }
}
