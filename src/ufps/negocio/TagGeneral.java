/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

import ufps.util.Cola;

/**
 *
 * @author Wilson Fernandez
 */

public class TagGeneral {
    private String tipo;
    private Cola<EtiquetaHTML> etiquetas;

    public TagGeneral() {
    }

      public TagGeneral(String tipo, String etiquetas) {
        this.tipo = tipo;
        this.etiquetas= new Cola<EtiquetaHTML>();
        this.separarTag(etiquetas);
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Cola<EtiquetaHTML> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(Cola<EtiquetaHTML> etiquetas) {
        this.etiquetas = etiquetas;
    }
    
   
    
    
    private void separarTag(String e){
        String[] temp= e.split("@-");
        for(int i=0;i<temp.length;i++){
            String[] dato=temp[i].split(";");
            if(dato[0].equals("<h1> to <h6>")){
            crearEtiquetaEspecial(dato[1]);
            continue;

            }
         
            EtiquetaHTML d=new EtiquetaHTML(dato[0],dato[1]);
            this.etiquetas.enColar(d);
//            System.out.println(d.toString());
        }
    }

    public String buscarEtiqueta(String etiqueta){
    String  dato="";

    Cola<EtiquetaHTML> aux=new Cola();
    while(!this.etiquetas.esVacio())
    {
    EtiquetaHTML x=this.etiquetas.deColar();
    if(x.getEtiqueta().equalsIgnoreCase(etiqueta)){
    dato=x.getDescipcion();
    

    }
    aux.enColar(x);

    }
    this.etiquetas=aux;
        return dato;
    }

    private void crearEtiquetaEspecial(String def){
    for(int i=1;i<7;i++){
      EtiquetaHTML d=new EtiquetaHTML("<h"+i+">",def);
            this.etiquetas.enColar(d);
    }
    }
   
    
    public String toString(){
        Cola<EtiquetaHTML> temp=new Cola();
        String cad="";
        while(!this.etiquetas.esVacio()){
            EtiquetaHTML dato=this.etiquetas.deColar();
            cad +=dato.toString()+"@-";
            temp.enColar(dato);
        }
        this.etiquetas=temp;
        return cad;
    }
  
    
    public void enColar(EtiquetaHTML dato)
    {
        this.etiquetas.enColar(
                dato);
    }
    
    public boolean esVacio()
    {
     return this.etiquetas.esVacio(); 
    }
    
    
    public EtiquetaHTML deColar()
    {
        return this.etiquetas.deColar();
    }
    
    public int getSize()
    {
        return this.etiquetas.getSize();
    }
    
    
}
