package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase para las entradas del diccionario. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
          this.llave = llave;
          this.valor = valor;
        }
    }

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
          iterador =null;
          if (entradas == null || elementos == 0){
            iterador = null;
            indice =0;
            return;
          }
          while(indice < entradas.length){
            if( entradas[indice] != null && !entradas[indice].esVacia()){
              iterador = entradas[indice].iterator();
              break;
            }
            indice ++;
          }
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
          if( iterador == null)
            throw new NoSuchElementException();
          Entrada e = iterador.next();
          if (!iterador.hasNext() ){
            indice ++;
            while( entradas.length > indice ){
              if( entradas[indice] != null && !entradas[indice].esVacia()){
                iterador = entradas[indice].iterator();
                break;
              }
              indice ++;
            }
            if( indice == entradas.length){
                iterador = null;
            }
          }
          return e;
        }

    }

    /* Clase privada para iteradores de llaves de diccionarios. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Construye un nuevo iterador de llaves del diccionario. */
        public IteradorLlaves() {
            super();
        }

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return siguiente().llave;
        }
    }

    /* Clase privada para iteradores de valores de diccionarios. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Construye un nuevo iterador de llaves del diccionario. */
        public IteradorValores() {
            super();
        }

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores*/
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
      int cap = longitud(capacidad);
      entradas = nuevoArreglo(cap);
      this.dispersor = dispersor;
      elementos = 0;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
      if( llave == null || valor == null)
          throw new IllegalArgumentException();
      int i = mascara(llave);
      Entrada entrada = new Entrada(llave, valor);
      if (entradas[i] == null){
          entradas[i] = new Lista<Entrada>();
      }
      for (Entrada e : entradas[i]){
          if( e.llave.equals(llave)){
            e.valor = valor;
            return;
          }
      }
      entradas[i].agrega(entrada);
      elementos ++;
      if( carga() >= MAXIMA_CARGA){
          aumentaTamaño();
      }
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
      if( llave == null)
          throw new IllegalArgumentException();
      int i = mascara(llave);
      if( entradas[i] == null)
          throw new NoSuchElementException();
      for (Entrada e : entradas[i]){
          if( e.llave.equals(llave)){
            return e.valor;
          }
      }
      throw new NoSuchElementException();
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
      if( llave == null)
          return false;
      int i = mascara(llave);
      if( entradas[i] == null)
          return false;
      for (Entrada e : entradas[i]){
          if( e.llave.equals(llave)){
            return true;
          }
      }
      return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
      if( llave == null)
          throw new IllegalArgumentException();
            int i = mascara(llave);
      if( entradas[i] == null)
          throw new NoSuchElementException();
      for (Entrada e : entradas[i]){
          if( e.llave.equals(llave)){
            entradas[i].elimina(e);
            elementos --;
            return;
          }
      }
      throw new NoSuchElementException();
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
      int colisiones = 0;
      for (Lista<Entrada> l : entradas){
          if( l != null){
              colisiones += l.getLongitud()-1;
          }
      }
      return colisiones;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
      int colisionM = 0;
      for (Lista<Entrada> l : entradas){
          if( l != null){
              if( l.getLongitud()-1 > colisionM){
                  colisionM = l.getLongitud()-1;
              }
          }
      }
      return colisionM;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return ((double)elementos/(double)entradas.length);
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
      Lista<Entrada>[] entradasaux = entradas;
      entradas =nuevoArreglo(entradasaux.length);
      elementos = 0;
    }

    private int mascara(K llave){
    	int i = dispersor.dispersa(llave);
    	int j = entradas.length -1;
    	return i & j;
    }
    private void aumentaTamaño(){
    	int tamaño= longitud(entradas.length);
    	Lista<Entrada>[] a = entradas;
    	entradas =nuevoArreglo(tamaño);
    	this.elementos = 0;
    	for (int i=0; i< a.length; i++){
    	    if( a[i] != null){
    		for (Entrada e : a[i]){
    		    agrega(e.llave, e.valor);
    		}
    	    }
    	}
    }
    private int longitud(int n){
    	int r = 1;
    	while (r< n)
    	    r<<= 1;
    	return (r<< 1 < 64) ? 64: r<<1;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
      String s = "";
      if( this.esVacia()){
          return s+= "{}";
      }
      s += "{ ";
      for ( Lista<Entrada> l: entradas){
          if( l!= null){
            for( Entrada e: l){
                s+= "'"+e.llave+"': '"+e.valor+"', ";
            }
          }
      }
      s+= "}";
      return s;
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
            if(d.elementos != elementos)
          	    return false;
          	for (int i=0; i< entradas.length; i++){
          	    if( entradas[i] != null)
          		    for (Entrada e : entradas[i]){
          		      if(!d.contiene(e.llave))
          			      return false;
          		      if(!e.valor.equals(d.get(e.llave)))
          		       return false;
          		    }
          	}
          	return true ;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
