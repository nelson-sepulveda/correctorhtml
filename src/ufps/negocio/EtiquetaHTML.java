/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

/**
 *
 * @author Wilson Fernandez
 */
public class EtiquetaHTML {
    private String etiqueta;
    private String descipcion;

    public EtiquetaHTML() {
    }

    public EtiquetaHTML(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    
     public EtiquetaHTML(String etiqueta, String descipcion) {
        this.etiqueta = etiqueta;
        this.descipcion = descipcion;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public String toString(){
        return this.etiqueta+";"+this.descipcion;
    }
    
    
}
