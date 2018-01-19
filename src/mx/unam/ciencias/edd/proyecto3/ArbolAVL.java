package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>> extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
	          altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
	         return getAltura(this);
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
	         return this.elemento + " " + this.altura + "/" + balancea(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return super.equals(o) && (this.altura == vertice.altura);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
      	VerticeAVL vertice = (VerticeAVL)(ultimoAgregado);
      	rebalancea(vertice);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
      	VerticeAVL vertice = (VerticeAVL)busca(elemento);
      	if (vertice == null) return;
        elementos--;
        VerticeAVL padre = (VerticeAVL)(vertice.padre);
        if ( (vertice.izquierdo != null) && (vertice.derecho != null) ) {
            vertice = (VerticeAVL)(intercambiaEliminable(vertice));
            padre = (VerticeAVL)(vertice.padre);
        }
        eliminaVertice(vertice);
        rebalancea(padre);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException();
    }

    private int balancea(VerticeAVL vertice) {
        return (vertice != null) ? getAltura((VerticeAVL)(vertice.izquierdo)) - getAltura((VerticeAVL)(vertice.derecho)) : 0;
    }

    private void calcularAltura(VerticeAVL vertice) {
        if (vertice == null){
            return;
        }
        vertice.altura =  Math.max (getAltura(vertice.derecho), getAltura(vertice.izquierdo)) + 1;
    }

    private int getAltura(VerticeArbolBinario<T> vertice) {
	     VerticeAVL ver = (VerticeAVL)vertice;
       return (vertice != null) ? ver.altura : -1;
    }

    //Método que rebalancea el árbol
    private void rebalancea(VerticeAVL vertice){
	      VerticeAVL hijoDerecho;
        VerticeAVL hijoIzquierdo;
        if (vertice == null) {
            return;
        }
	      calcularAltura(vertice);
        if (this.balancea(vertice) == -2) {
            if ( balancea((VerticeAVL)(vertice.derecho)) == 1 ) {
                hijoDerecho = (VerticeAVL)(vertice.derecho);
                super.giraDerecha(hijoDerecho);
                calcularAltura((VerticeAVL)(hijoDerecho.padre));
                calcularAltura(hijoDerecho);
            }
            super.giraIzquierda(vertice);
            calcularAltura(vertice);
        } else if (balancea(vertice) == 2) {
            if (this.balancea((VerticeAVL)(vertice.izquierdo)) == -1) {
                hijoIzquierdo = (VerticeAVL)(vertice.izquierdo);
                super.giraIzquierda(hijoIzquierdo);
                calcularAltura((VerticeAVL)(hijoIzquierdo.padre));
                calcularAltura(hijoIzquierdo);
            }
            super.giraDerecha(vertice);
            calcularAltura(vertice);
        }
        rebalancea((VerticeAVL)(vertice.padre));
    }
}
