package ufps.util;
import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTP;

/**
 * Clase que permite subir archivos, descargarlos, crear carpetas, borrar 
 * carpetas y archivos via FTP
 * @author Fernando Torres Y Jorge Castañeda
 * @version 2.0
 */
public class ServicioFtp {
    
    private FTPClient server;
    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private String ip = "sandbox1.ufps.edu.co";
    private String user="ftp1";
    private String pass="ufps";
   
    /**
     * Constructor vacio de la clase
     */
    public ServicioFtp() {
        
        this.server = new FTPClient();
        
        this.conectar();
        this.dirInicial();

    }
    
    /** 
     * Constructor con parametros de la clase
     * @param user es de tipo String y contiene el nombre del usuario del servicio FTP
     * @param pass es de tipo String y contiene el password para conectar el servicio FTP
     * @param url es de tipo String y contiene la direccion del url del servidor a conectar
     */    
    public ServicioFtp(String user, String pass, String url){
        
        this.server = new FTPClient();
        this.user=user;
        this.pass= pass;
        this.ip=url;
        
        this.conectar();
        this.dirInicial();

    }
    
    /**
     * Método que conecta con el servidor via FTP
     * @return un tipo boolean, true si realizo la conexion y false si no.
     */
    public boolean conectar() {
        
        try{
            
            server.connect(this.ip);
            return (server.login(this.user, this.pass));
            
        }catch (IOException ioe) {
            
            ioe.printStackTrace();
                
        }
        
        return false;      
    }
    
    /**
     * Método que desconecta el servicio FTP del servidor
     * @return un tipo boolean que retorna true si desconecto y true si no.
     */
    public boolean desconectar(){
        
        try {
            
            if(server.logout()){
                
                server.disconnect();
                return true;
                
            }
            
            return false;
            
        }catch (IOException ioe) {
            
            ioe.printStackTrace();
                
        }
        
        return false; 
    
    }
    
    /**
     * Método que recibe un String y lo escribe en un archivo en el servidor
     * @param contenido es de tipo String que contiene el texto a escribir
     * @param nombre es de tipo String que contiene el nombre del archivo
     * @return es de tipo boolean y retorna true si subio el archivo y false si no.
     */
    public boolean subirArchivoString(String contenido, String nombre) {
        
        File fichero = new File ("../"+nombre);
        
        try {
            
            if (fichero.createNewFile()){
                
                BufferedWriter escritura = new BufferedWriter(new FileWriter(fichero.getAbsoluteFile()));
                escritura.write(contenido);
                escritura.close(); 
              
                return this.subirArchivo(fichero.getAbsolutePath(), nombre);
              
            }else
                System.out.println("No ha podido ser creado el fichero");
        
        } catch (IOException ioe) {
            
            ioe.printStackTrace();
          
        }
        
        
        return false;
        
    }
    
    /**
     * Método que permite subir un archivo al servidor via FTP
     * @param localFile es de tipo String y contiene la direccion del archivo local
     * @param hostFile es de tipo String y contiene la direccion donde se alojara el archivo en el servidor
     * @return un tipo boolean que retorna true si lo pudo subir al servidor y false si no.
     */
    public boolean subirArchivo(String localFile, String hostFile){
        
        boolean rs=false;
        
        try{
            
            File file=new File(localFile);
            fileIn = new FileInputStream(file);
            
            server.setFileType(FTPClient.BINARY_FILE_TYPE);
            rs =(server.storeFile(server.printWorkingDirectory()+"/"+hostFile, fileIn));
            
            fileIn.close();
            
            //file.delete(); //Descomentar para poder utilizar en Web, ya que el archivo estara vigente en el servidor fuera del public
        }catch(IOException ioe) {
            
                ioe.printStackTrace();
                
        }
    
        return rs;
    }
    
    /**
     * Método que devuelve un String con el contenido de un archivo en el servidor
     * @param nombre es de tipo String que contiene la direccion en el servidor del archivo
     * @return un tipo String que retonar el contenido del archivo
     */
    public String descargarArchivoString(String nombre){
        
        String cadena="";
        
        try {
            
            File fichero = new File("../"+nombre);
        
            fichero.createNewFile();
        
            this.descargarArchivo(fichero.getAbsolutePath(), nombre);
        
            FileReader in=new FileReader(fichero.getAbsoluteFile()); 
            StringWriter w= new StringWriter(); 
        
            char buffer[]=new char[2048]; 
            int n=0;
            
            while((n=in.read(buffer))!=-1)
                w.write(buffer, 0, n); 
            
            w.flush();
            in.close();
        
            fichero.deleteOnExit();
            return w.toString ();
            
        }catch (IOException ioe) {
            
            ioe.printStackTrace();
            
        }
        
        return cadena;
    }
    
    /**
     * Método que descarga un archivo a la carpeta indicada, cuando se ejecuta 
     * via Web lo mueve a otra carpeta en el servidor
     * @param localFile es de tipo String y contiene la direccion local donde se desea poner el archivo
     * @param hostFile es de tipo String y contiene la direccion en el servidor donde se encuentra alojado el archivo
     * @return un tipo boolean y retorna true si lo descarga y false si no.
     */
    public boolean descargarArchivo(String localFile, String hostFile) {
        
        boolean rs=false;
        
        try{
            
            fileOut = new FileOutputStream(localFile);
            String host=server.printWorkingDirectory()+"/"+hostFile;
            
            rs =(server.retrieveFile(host, fileOut));
            fileOut.flush();    
            fileOut.close();
            
        }catch (IOException ioe) {
            
            ioe.printStackTrace();
            
        }
        
        return rs;
        
    }
    
    /**
     * Método que retorna la lista de archivos que se encuentran en un directorio del servidor
     * @param dir es de tipo String y contiene el directorio en el servidor que se desea listar
     * @return un array de String que contiene todos los elementos existentes en el directorio
     */    
    public String[] listarSimple(String dir) {
        
        try {
            
            return server.listNames(dir);
            
        } catch (Exception e) {
            
            return null;
            
        }
        
    }
    
    /**
     * Método que retorna un ArrayList de archivos y si es o no un directorio o archivo
     * listando del directorio donde se encuentre actualmente en el servidor
     * @return un 
     */
    public ArrayList<String> listarConTipo(){
               
        ArrayList<String> archivos=new ArrayList<String>();
        
        try {
            
            FTPFile[] archivo;
            
            if(server.isConnected()) { 
                
                archivo=server.listFiles();
                
                for(FTPFile file: archivo){
                    
                    if(file.isDirectory())
                        archivos.add(file.getName()+";"+file.getRawListing()+";directorio");
                    else archivos.add(file.getName()+";"+file.getRawListing()+";archivo");
                    
                }
                
            }else return null;
            
            return archivos;
            
        } catch (Exception ex) {
            
            return null;
            
        }
        
    }
    
    /**
     * Método que posicion el servidor en el directorio "/public_html"
     * @return un tipo boolean que retorna true se mueve al directorio y false si no.
     */
    public boolean dirInicial(){
        
        try{
            
            return server.changeWorkingDirectory("/public_html");
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            
        }
        
        return false;
        
    }
    
    /**
     * Método que crea una carpeta en el servidor
     * @param nombre
     * @return 
     */
    public boolean crearCarpeta(String nombre) {
        
        try{
            
            String lugar=server.printWorkingDirectory();
            boolean valor=server.makeDirectory(nombre);
            
            return valor;
            
        }catch (Exception e) {
            
            
            return false;
            
        }
    }
    
    /**
     * Método que se mueve al directorio que contiene el actual
     * @return un tipo boolean, true si se movio y false si no.
     */
    public boolean atras(){
        
        try{
            
            return server.changeToParentDirectory();
            
        }catch (Exception e) {
            
            return false;
            
        }
    }
    
    /**
     * Método que retorna el nombre del directorio actual donde se encuentra el servicio FTP
     * @return un tipo Stirng con el nombre del directorio actual en el servidor
     */
    public String dirActual(){
        
         try {
             
            return server.printWorkingDirectory();
            
        } catch (Exception e) {
            
            return null;
            
        }
    }
    
    /**
     * Método que permite cambiar de directorio
     * @param dir es un tipo String qe contiene la direccion del nuevo directorio
     * @return es de tipo boolean y retorna true si cambia el directorio y false si no.
     */
    public boolean goToDir(String dir){
        
        try {
            
            return server.changeWorkingDirectory(dir);
            
        } catch (Exception e) {
            
            return false;
            
        }
    }
    
    /**
     * Método que permite eliminar un archivo o directorio del servidor
     * @param path es de tipo String y contiene la direccion del directorio o del servidor
     * @return un tipo boolean, true si lo elimino y false si no.
     */
    public boolean eliminar(String path){
        
        try{
            
            FTPFile file=this.buscarArchivo(path);
            
            if(file.isDirectory()) {
                
                this.goToDir(file.getName());
                
                FTPFile [] f = this.server.listFiles();
                
                for(FTPFile x: f)
                    this.eliminar(x.getName());
                
                this.atras();
                
                return this.server.removeDirectory(path);
            } else 
                return this.server.deleteFile(path);
            
        } catch (Exception e) {
            
            return false;
            
        }
    }
    
    /**
     * Método que busca un archivo adentro de un directorio
     * @param path la direccion del archivo en el servidor
     * @return un tipo FTPFile que contiene la informacion del archivo en el directorio
     */
    private FTPFile buscarArchivo(String path){
        try {
            
            FTPFile[] archivo=null;
            
            if(server.isConnected())
            {  archivo=server.listFiles();

            
            for(FTPFile file: archivo){
                
                if(file.getName().equals(path))
                    return file;

            }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}//Fin de la Clase