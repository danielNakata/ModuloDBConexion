/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dnn.nominae.modulobdconexion.dto;

import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

/**
 *
 * @author danie
 */
public class QryRespDTO {
    public int res = 0;
    public String msg = "";
    public int numReg = 0;
    public ArrayList<ColumnaDTO> columnas = null;
    public ArrayList<HashMap<String,CampoDTO>> datosTabla = null;
    public ArrayList<CampoDTO> datosLista = null;
    private ResultSet resultado = null;
    private ResultSetMetaData metadatos = null;
    private ParameterMetaData metadatosParam = null;
    private ArrayList<CampoDTO> paramOut = null;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getNumReg() {
        return numReg;
    }

    public void setNumReg(int numReg) {
        this.numReg = numReg;
    }

    public ArrayList<ColumnaDTO> getColumnas() {
        return columnas;
    }

    public void setColumnas(ArrayList<ColumnaDTO> columnas) {
        this.columnas = columnas;
    }

    public ArrayList<CampoDTO> getDatosLista() {
        return datosLista;
    }

    public void setDatosLista(ArrayList<CampoDTO> datosLista) {
        this.datosLista = datosLista;
    }

    public ResultSet getResultado() {
        return resultado;
    }

    public void setResultado(ResultSet resultado) {
        this.resultado = resultado;
    }

    public ResultSetMetaData getMetadatos() {
        return metadatos;
    }

    public void setMetadatos(ResultSetMetaData metadatos) {
        this.metadatos = metadatos;
    }

    public ArrayList<HashMap<String, CampoDTO>> getDatosTabla() {
        return datosTabla;
    }

    public void setDatosTabla(ArrayList<HashMap<String, CampoDTO>> datosTabla) {
        this.datosTabla = datosTabla;
    }

    public ParameterMetaData getMetadatosParam() {
        return metadatosParam;
    }

    public void setMetadatosParam(ParameterMetaData metadatosParam) {
        this.metadatosParam = metadatosParam;
    }

    public ArrayList<CampoDTO> getParamOut() {
        return paramOut;
    }

    public void setParamOut(ArrayList<CampoDTO> paramOut) {
        this.paramOut = paramOut;
    }

    @Override
    public String toString() {
        return "QryRespDTO{" + "res=" + res + ", msg=" + msg + ", numReg=" + numReg + ", columnas=" + columnas + ", datosTabla=" + datosTabla + ", datosLista=" + datosLista + ", resultado=" + resultado + ", metadatos=" + metadatos + ", metadatosParam=" + metadatosParam + ", paramOut=" + paramOut + '}';
    }

        
}
