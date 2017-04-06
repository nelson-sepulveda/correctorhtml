/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

/**
 *
 * @author Wilson Fernandez
 */
public class ErrorHTML 
{
    private EtiquetaHTML Etiqueta;
    private Error Error;

    public ErrorHTML() 
    {
    }

    public ErrorHTML(EtiquetaHTML e,Error err)
    {
        this.Error=err;
        this.Etiqueta=e;
    }
    
    
    public EtiquetaHTML getMyEtiqueta() 
    {
        return Etiqueta;
    }

    public void setMyEtiqueta(EtiquetaHTML myEtiqueta) 
    {
        this.Etiqueta = myEtiqueta;
    }

    public Error getMyError() {
        return Error;
    }

    public void setMyError(Error myError) {
        this.Error = myError;
    }
    
    
    
    
}
