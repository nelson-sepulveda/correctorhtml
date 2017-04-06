package ufps.util;
import java.net.*;
import java.io.*;
import org.apache.commons.net.ftp.FTPClient;


/**
 * Clase que permite subir y borrar archivos y carpetas, utilizando el protocolo FTP
 * 
 * @author Fernando Torres
 */
public class ArchivoFtp {

    //Almacena la dirección URL del archivo
    private String direccion;
    private String usuario;
    private String clave;
    private String path;
    private FTPClient url=null;
    
    public ArchivoFtp() {
    }

    public ArchivoFtp(String direccion, String usuario, String clave, String path) {
        this.direccion = direccion;
        this.usuario = usuario;
        this.clave = clave;
        this.path = path;
    }

    public String getClave() {
        return clave;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPath() {
        return path;
    }

    public String getUsuario() {
        return usuario;
    }

    public FTPClient getUrl() {
        return url;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setUrl(FTPClient url) {
        this.url = url;
    }
    
    private void Conectar(){
        
        try{
        
            this.setUrl(new FTPClient());

            this.getUrl().connect(this.getDireccion());
            if(this.getUrl().login(this.getUsuario(), this.getClave()))
            {
                this.getUrl().changeWorkingDirectory(path);
               System.out.println("login correcto ");
            }else System.out.println("login incorrecto");
        
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public boolean copiar(String cadena, String nombreArchivo){
        this.Conectar();
        java.util.ArrayList<String> l=new java.util.ArrayList<String>();
        
               
        try {
                           
                PrintWriter esc= new PrintWriter(new BufferedWriter(new FileWriter(nombreArchivo, false)));
                
                esc.close();
                
                File archivo=new File(nombreArchivo);
                archivo.createNewFile();
                
                BufferedWriter write=new BufferedWriter(new FileWriter(archivo));
                write.write(cadena);
                write.close();      
                
                this.enviarArchivoFTP(nombreArchivo);
                
                archivo.deleteOnExit();
            }catch (MalformedURLException ex1) {
		      System.out.println("URL erronea: "+ex1.getMessage());
		    } catch (IOException ioe) {
                            System.out.println("IOException: " + ioe);
                return false;
                }
        
        return true;
    }
    
    private void enviarArchivoFTP(String localFile) throws FileNotFoundException, IOException{
    
    FileInputStream fis = new FileInputStream(localFile);

    if(this.getUrl().storeFile(this.getPath(), fis)){
        
        System.out.println("Envio correcto");
    }else
        System.out.println("Error Envio ");
    
    fis.close();    
  }
}
