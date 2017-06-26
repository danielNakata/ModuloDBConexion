/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dnn.nominae.modulobdconexion.dto;

/**
 *
 * @author danie
 */
public class CampoDTO {
    public double id = 0;
    public int tipoId = 0;
    public String nombre = "";
    public String etiqueta = "";
    public Object valor = null;
    public String clase = null;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    @Override
    public String toString() {
        return "CampoDTO{" + "id=" + id + ", tipoId=" + tipoId + ", nombre=" + nombre + ", etiqueta=" + etiqueta + ", valor=" + valor + ", clase=" + clase + '}';
    }

}
