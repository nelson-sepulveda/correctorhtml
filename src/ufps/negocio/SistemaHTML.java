package ufps.negocio;

import java.util.Iterator;
import ufps.util.ArchivoLeerTexto;
import ufps.util.ArchivoLeerURL;
import ufps.util.Cola;
import ufps.util.ExceptionUFPS;
import ufps.util.ListaCD;
import ufps.util.Pila;
import ufps.util.Secuencia;

/**
 * Borrar pastebin.com/QfG8CamA
 *
 * @author Wilson Fernandez
 */
public class SistemaHTML {

    private ListaCD<String> filasDelArchivo;
    private Pila<ErrorHTML> errores;
    private Error error1;
    private Error error2;
    private Error error3;
    private Error error4;
    private Secuencia<TagGeneral> tags;

    public SistemaHTML() throws ExceptionUFPS {
        this.tags = new Secuencia<TagGeneral>(11);
        this.errores = new Pila<ErrorHTML>();
        this.error1 = new Error("No tiene etiqueta de fin");
        this.error2 = new Error("No tiene etiqueta de inicio");
        this.error3 = new Error("Etiqueta no reconocida");
        this.error4 = new Error("No tiene etiqueta de estructura");
        this.cargarEstructuras();

    }

    public Pila<ErrorHTML> retornarPila() {
        return errores;
    }
 
    public Secuencia<TagGeneral> getTags() {
        return tags;
    }

    
    
    
    /**
     * Metodo que permite cargar desde una url los diferentes tags o etiquetas
     * @throws ExceptionUFPS 
     */
    private void cargarEstructuras() throws ExceptionUFPS 
    {
        
        
        ArchivoLeerURL archivo=new ArchivoLeerURL("http://sandbox1.ufps.edu.co/~madarme/estructuras/html_w3c.txt");
        Object[] obj=archivo.leerArchivo();
        
        String tipo = "";
        String cadena = "";
        int i=0;
        for (Object o : obj) {
            String[] v = o.toString().split(";");
            if (tipo.isEmpty()) {
                tipo = v[0];
            }
            if (tipo.equals(v[0])) {
                cadena += v[1] + ";" + v[2] + "@-";
            } else {
                this.tags.set(i, new TagGeneral(tipo, cadena));
                cadena = v[1] + ";" + v[2] + "@-";
                tipo = v[0];
                i++;
            }
        }
        
    }    
        
    
   

    
    /**
     * Metodo que permite separar cada fila del texto en html
     * colocando cada una de ellas en un posicion el vector
     * @param fuente 
     */
    
    private void pasarArchivo(String fuente) 
    {
        String v[] = fuente.split("\n");
        this.filasDelArchivo=new ListaCD<>();
        int i = 0;
        for (String x : v) 
        {
            if(i >= 0)
            {
                this.filasDelArchivo.addFin(x);
            }
            i++;
        }    
    }

    
    /**
     * Metodo que permite tomar la fuente o texto en codigo html y aplicar
     * su respectiva funcion, debera separar cada una de las etiquetas 
     * y una vez que se realice la funcion dicha las devolvera a la fila de Archivos
     * @param fuente
     * @throws ExceptionUFPS 
     */
    

    public void cargarCodigoFuente(String fuente) throws ExceptionUFPS {
        Cola<String> tag=new Cola<String>();
        String pasar="";
       
        try {
            if (fuente.isEmpty()) 
                {
                throw new Exception("Error al momento de cargar archivo");
                }
            
              this.pasarArchivo(fuente);
              tag=this.separadorDeEtiquetas();
              while(!tag.esVacio())
              {
                  pasar+=tag.deColar()+"\n";
              }
              this.pasarArchivo(pasar);
           
        } catch (Exception e)
        {
          
            throw new ExceptionUFPS(e.getMessage());
        }

      
    }
    
   /**
    * Metodo que permite trasladar todas las fila de archivos a una cadena de caracteres  
    * @return 
    */
    private String pasarArchivoEtiquetas() 
    {
        String resultado = "";
        for (String x : this.filasDelArchivo) {
            resultado += x;
        }
        return resultado;
    }

    /**
     * Metodo que permite separar la etiquetas al momento de convertir la fila de archivos
     * en una cadena de String
     * @return una Cola de String con las respectivas etiquetas singularizadas
     */
    private Cola<String> separadorDeEtiquetas() 
    {
        String fin = "";
        boolean esValido = true;
        Cola<String> retorno = new Cola<>();
        fin = this.pasarArchivoEtiquetas();
        while (esValido)
        {
            int indice = fin.indexOf("<");

            int j = fin.indexOf(">");
            if (indice == -1 || j == -1) 
            {
                esValido = false;
            } else 
            {

                if (indice > j) 
                {
                    fin=fin.substring(indice, fin.length());
                } else 
                {
                    String x = fin.substring(indice, j + 1);
                    fin = fin.substring(j + 1, fin.length());
                    retorno.enColar(x);

                }
            }
        }
        return retorno;
    }

    public void imprimirlista() {
        this.filasDelArchivo.toString();
    }

    /**
     * Metodo que permite validar las etiquetas binarias dado que pueden existir varios errores.
     * Alli sus respctivos cierres y aperturas deben estar en un orden dado ya que al momento de comparar
     * un dato de cada pila deberan estan bien escritas la etiqueta de cierre y la etiqueta de apertura.
     * Si en algun momento la pila de cierre es vacia o la pila de apertura es vacia se generaran tantos errores respectivos como
     * sea la cantidad de elementos contenga cada pila.
     * En cada caso.
     * Si las pilas de apertura y cierre contienen elementos se pasara a 
     * @param aper
     * @param cierre
     * @param noValida
     * @param noReconoHTML 
     */

    private void validarEtiquetasBinarias(Pila<String> apertura, Cola<String> cierre, Pila<String> noValida, Pila<String> noReconoHTML) 
    {

        Pila<String> aux = new Pila<String>();
        if (apertura.esVacio() && !cierre.esVacio()) 
        {

            while (!cierre.esVacio()) 
            {
                String l = cierre.deColar();
              
                this.errores.push(new ErrorHTML(new EtiquetaHTML(l), error2));
            }

        } else if (cierre.esVacio() && !apertura.esVacio()) 
        {

            while (!apertura.esVacio()) {
                String l = apertura.pop();

                this.errores.push(new ErrorHTML(new EtiquetaHTML(l), error1));

            }
        } else 
        {
            

            while (!cierre.esVacio() && !apertura.esVacio()) 
            {
                String datoCerradoConSlash = cierre.deColar();
                String datoCerradoSinSlash = this.getEtiqueta2(datoCerradoConSlash);
                String datoApertura = apertura.pop();
                if (!datoCerradoSinSlash.equalsIgnoreCase(datoApertura))
                {
                    aux.push(datoApertura);
                    boolean sw = false;
                    while (!apertura.esVacio()) 
                    {
                        String etiquetaAux = apertura.pop();
                        if (etiquetaAux.equalsIgnoreCase(datoCerradoSinSlash)) 
                        {
                            sw = true;
                            break;
                        } else 
                        {
                            aux.push(etiquetaAux);
                        }

                    }
                    if (!sw) 
                    {
                     
                        this.errores.push(new ErrorHTML(new EtiquetaHTML(datoCerradoConSlash), error2));
                    }
                    while (!aux.esVacio()) 
                    {
                        apertura.push(aux.pop());
                    }

                }
            }
            while (!apertura.esVacio()) 
            {
                String l = apertura.pop();
              
                this.errores.push(new ErrorHTML(new EtiquetaHTML(l), error1));

            }
            while (!noReconoHTML.esVacio()) 
            {
                String l = noReconoHTML.pop();
                this.errores.push(new ErrorHTML(new EtiquetaHTML(l), new Error("Etiqueta no reconocida, esta fuera de </html>")));
            }
            while(!noValida.esVacio()){
                this.errores.push(new ErrorHTML(new EtiquetaHTML(noValida.pop()), error3));
            }
        }
    }
    
    
    /**
     * Metodo que permite cargar y validar las etiquetas unarias en la pila de errores 
     * del sistema como error de etiqueta no reconocida
     * @param unaria 
     * @param noValida
     * @param noReconocida 
     */

    private void cargarEtiquetasUnariasValidas(Cola<String> unaria, Pila<String> noValida, Pila<String> noReconocida) 
    {
        Pila<String> pNoValidas = noValida;
        String dato = "";
        String dato1 = "";
        while (!unaria.esVacio()) 
        {
            dato = unaria.deColar();
            dato1 = this.getEtiqueta2(dato);
            if (this.esUnariaValida(dato)) 
            {
                if (!cuerpoValido(dato1)) 
                {
                    this.errores.push(new ErrorHTML(new EtiquetaHTML(dato), error3));
                    pNoValidas.push(dato);
                }
            }
        }
    }

    /**
     * Metodo que permite cargar las etiquetas binarias validas tanto en apertura como en cierre.
     * Alli se almacenan un sus respectivas pilas de cierre y apertura para luego poder validar dado un orden
     * @param bin
     * @param noValida
     * @param noReconocida 
     */
    private void cargarEtiquetasBinariasValidas(Cola<String> bin, Pila<String> noValida, Pila<String> noReconocida) 
    {
        boolean sw = false;
        Pila<String> pApertura = new Pila<String>();
        Cola<String> cCierre = new Cola<String>();
        Pila<String> pNoValidas = noValida;
        Pila<String> pNoReconocidas = noReconocida;
        String dat = "";
        String dato = "";
        
        while (!bin.esVacio()) 
        {
            dato = bin.deColar();
            
            if (sw) 
            {
                pNoReconocidas.push(dato);

            } else if (this.esBinariaValidaApertura(dato))
            {

                dato = this.getEtiqueta(dato);
                if (cuerpoValido(dato))
                {
                    pApertura.push(dato);
                } else {

                    dato = dato.substring(0,dato.length()-1);
                    if(dato.charAt(dato.length()-1)!='>'){
                        dato=dato+'>';
                    }
                    pNoValidas.push(dato);
                }
            } else if (this.esBinariaValidacierre(dato)) {
                dat = getEtiqueta2(dato);

                if (cuerpoValido(dat)) 
                {

                    if (dat.equalsIgnoreCase("<html>")) 
                    {
                        sw = true;
                    }
                    cCierre.enColar(dato);
                } else
                {
                    pNoValidas.push(dato);
                }
            }
        }

        validarEtiquetasBinarias(pApertura, cCierre, pNoValidas, pNoReconocidas);

    }

    /**
     * 
     * @param dato
     * @return 
     */
    private String getEtiqueta(String dato)
    {
        if(!this.cuerpoValido(dato)){
        String[] v = dato.split(" ");
        return v[0] + '>';
        }
        return dato;
    }

    /**
     * 
     * @param dato
     * @return 
     */
    private String getEtiqueta2(String dato) 
    {
        String[] v;
        v = dato.split("/");
        return v[0] + v[1];
    }

    

    /**
     * Metodo que recibe una etiqueta html para alli buscarla dentro de los 
     * tags para comprobar si es valida en su escritura
     * @param d
     * @return 
     */
    private boolean cuerpoValido(String d) 
    {

        String dato = "";
        EtiquetaHTML aux;
        boolean res = false;
        int tama = 0;
        for (int i = 0; i < this.tags.length(); i++) 
        {
            tama = this.tags.get(i).getSize();
            while (tama-- > 0) 
            {
                aux = this.tags.get(i).deColar();
                if (d.equalsIgnoreCase(aux.getEtiqueta())) 
                {
                    this.tags.get(i).enColar(aux);
                    res= true;
                    break;
                } 
                else
                {
                    this.tags.get(i).enColar(aux);
                }
            }
        }
        return res;
    }

    /**
     * Metodo que permite validar si una etiqueta binaria de apertura es valida 
     * @param etiqueta
     * @return 
     */
    private boolean esBinariaValidaApertura(String etiqueta) 
    {
        return etiqueta.matches("<([^ /<>][a-zA-Z]*)(\\s*[^<>]*)[^/]>");
    }

    /**
     * Metodo que permite validar si una etiqueta binaria de cierre es valida 
     * @param etiqueta
     * @return 
     */
    
    private boolean esBinariaValidacierre(String etiqueta) 
    {
        if (etiqueta.equals("") || etiqueta.equals(" ") || etiqueta == null) 
        {
            return false;
        } 
            
        return etiqueta.matches("<(/)([a-z]*[A-Z]*)>");
    }

    /**
     * Metodo que permite validar si la etiqueta DOCTYPE que define el lenguaje de la pagina
     * se encuentra bien escrita. En caso de no serlo se enviara a la pila de errores del sistema
     * como un error de estructura
     * @param etiqueta
     * @return 
     */
    private boolean doctypeValido(String etiqueta) 
    {
        return etiqueta.matches("<(!DOCTYPE)([\\s\\S]*)>");
    }
    
    
    /**
     * Metodo que permite validar el cuerpo del html
     * para ello ya se separo las etiquetas unarias como binarias
     *  
     */
    
    public void validarHTML() 
    {

        ListaCD<Cola<String>> l = this.getEtiquetasUnarias_Binarias();
        Cola<String> binarias = l.get(0);
        Cola<String> unarias = l.get(1);
        Pila<String> pilaNovalidas = new Pila<String>();
        Pila<String> pilaNoReconocidas = new Pila<String>();

        this.cargarEtiquetasBinariasValidas(binarias, pilaNovalidas, pilaNoReconocidas);
        this.cargarEtiquetasUnariasValidas(unarias, pilaNovalidas, pilaNoReconocidas);

    }

    /**
     * Metodo que permite averiguar si una etiqueta de unaria
     * basandonos en una expresion regular.
     * @param dato
     * @return 
     */
    private boolean esUnariaValida(String dato) 
    {
        return dato.matches("<([^ ][a-zA-Z]*)(\\s*[^<>]*)(/)>");
    }

    
    /**
     * Metodo que permite pasar la ListaCD de archivos a una lista auxiliar
     * @param l
     * @return 
     */
    
    private ListaCD<String> conversion(ListaCD<String> l) 
    {

        Iterator<String> it = l.iterator();
        ListaCD<String> laux = new ListaCD<String>();
        while (it.hasNext()) {
            String n = it.next();
            if(!"".equals(n) || !" ".equals(n) || !"\t".equals(n) || !"\n".equals(n)){
                laux.addFin(n);
            }
        }
        return laux;

    }

    /**
     * Metodo que permite en una cola de datos de tipo String
     * contener la etiquetas que conforma la estructura basica del html
     * @return 
     */
    private Cola<String> cargarEstructuraBasica() 
    {
        Cola<String> u = new Cola<String>();
        u.enColar("<!Doctype>");
        u.enColar("<html>");
        u.enColar("<head>");
        u.enColar("<title>");
        u.enColar("<body>");
        return u;
    }
    
    /**
     * Metodo que permite separar en una ListaCD<Cola<String>> de dos nodos,
     * las diferentes etiquetas unarias y las binarias.
     * Averiguando tambien si al cargar el archivo se encuentra la estructura inicial o basica del html
     * que dicha estructura la contiene una Cola de datos de tipo String,
     * en cualquier caso que encuentre un error de estructura o no reconocida la enviara directamente
     * a la pila de errores del sistema
     * Al momento de sacar un etiqueta de la fila de archivos correspondra a averiguar si es unaria o binaria
     * para esto utilizamos expresiones regulares, que gracias a estas podemos validar cualquier tipo de dominio,escritura entre
     * otros.
     * @return 
     */
    
    
    private ListaCD<Cola<String>> getEtiquetasUnarias_Binarias() 
    {
        ListaCD<Cola<String>> lista = new ListaCD<Cola<String>>();
        Cola<String> unarias = new Cola<String>();
        Cola<String> binarias = new Cola<String>();
        Cola<String> basica = cargarEstructuraBasica();
        String n;
        ListaCD<String> x = this.conversion(filasDelArchivo);
        Iterator<String> it = x.iterator();
        String dato = it.next();
        dato = this.getEtiqueta(dato);

        if (!doctypeValido(dato)) 
        {
            this.errores.push(new ErrorHTML(new EtiquetaHTML(dato), error4));
        }
        basica.deColar();

        while (it.hasNext()) 
        {
            n = it.next().toString();

            if (this.esUnariaValida(n)) {

                unarias.enColar(n);
            } 
            else if (this.esBinariaValidaApertura(n)) 
            {
                int c = basica.getSize();
                String valor = "";
                while (c-- > 0) {
                    valor = basica.deColar();

                    if (!n.equalsIgnoreCase(valor)) {
                        basica.enColar(valor);
                    }

                }

                binarias.enColar(n);
            }
            else if (this.esBinariaValidacierre(n)) 
            {
                binarias.enColar(n);
            } 
            else 
            {
                if (n.equalsIgnoreCase("")) 
                {
                    continue;
                }

                this.errores.push(new ErrorHTML(new EtiquetaHTML(n), error3));
            }

        }
        while (!basica.esVacio()) 
        {
            this.errores.push(new ErrorHTML(new EtiquetaHTML(basica.deColar()), error4));
        }

        lista.addFin(binarias);
        lista.addFin(unarias);
        return lista;

    }

    
    
}
