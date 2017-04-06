/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.util;

import java.util.Iterator;

/**
 *
 * @author MADARME
 */
public class IteratorSecuencia<T> implements Iterator<T> {

    private int posActual=0;
    private T vector[];

    public IteratorSecuencia(T[] vector) {
        this.vector = vector;
    }
    
    
    
    @Override
    public boolean hasNext() {
        
       return (this.posActual!=this.vector.length);
        
        
    }

    @Override
    public T next() {
        if(! this.hasNext())
        {
            throw new RuntimeException("No hay mas elementos");
        }
        
        T elemento=this.vector[this.posActual];
        this.posActual++;
        return (elemento);
        
        
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
