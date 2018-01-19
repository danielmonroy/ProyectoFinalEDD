package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;
/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
            pila = new Pila <Vertice>();
            Vertice aux = raiz;
            while (aux != null) {
                pila.mete(aux);
                aux = aux.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (! pila.esVacia());
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice aux = pila.saca();
            if (aux != null){
                if (aux.derecho != null) {
                    Vertice aux2 = aux.derecho;
                    while (aux2 != null) {
                        pila.mete(aux2);
                        aux2 = aux2.izquierdo;
                    }
                }
            }
            return aux.elemento;
        }

    }
    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     * @throw IllegalAgumentException si el elemento a agregar es null
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento a agregar null.");
        Vertice nuevo = nuevoVertice(elemento);
        ultimoAgregado =  nuevo;
        elementos++;
        if (raiz == null) {
            raiz = nuevo;
        } else {
            agrega(raiz,nuevo);
        }
    }
    //Algoritmo auxiliar de agrega
    private void agrega(Vertice inicio, Vertice aAgregar) {
        //Caso si es menor ó igual
        if (aAgregar.elemento.compareTo(inicio.elemento) <= 0) {
            if (! inicio.hayIzquierdo()) {
                inicio.izquierdo = aAgregar;
                aAgregar.padre = inicio;
            } else {
                agrega(inicio.izquierdo,aAgregar);
            }
        } else {
            //Caso si es mayor
            if (! inicio.hayDerecho()) {
                inicio.derecho = aAgregar;
                aAgregar.padre = inicio;
            } else {
                agrega(inicio.derecho,aAgregar);
            }
        }
    }

    /**
     *Método que agrega un elemento al arbol usando una instancia de {@link Comparator}
     *@param elemento Elemento a agregar al árbol
     *@param comparador El comparador de los elementos a agregar al aŕbol
     */
    public void agrega(T elemento, Comparator<T> comparador) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento a agregar null.");
        Vertice nuevo = nuevoVertice(elemento);
        ultimoAgregado =  nuevo;
        elementos++;
        if (raiz == null) {
            raiz = nuevo;
        } else {
            agrega(raiz,nuevo,comparador);
        }
    }
    //Algoritmo auxiliar para agregar un elemento al árbol usando un comparador
    private void agrega(Vertice inicio, Vertice aAgregar, Comparator<T> comparador) {
        if (comparador.compare(inicio.elemento,aAgregar.elemento) <= 0) {
            if (! inicio.hayIzquierdo()) {
                inicio.izquierdo = aAgregar;
                aAgregar.padre = inicio;
            } else {
                agrega(inicio.izquierdo,aAgregar);
            }
        } else {
            //Caso si es mayor
            if (! inicio.hayDerecho()) {
                inicio.derecho = aAgregar;
                aAgregar.padre = inicio;
            } else {
                agrega(inicio.derecho,aAgregar);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice aEliminar = vertice(busca(elemento));
        //Si no lo contiene
        if (aEliminar == null)
            return;
        //Procedemos a eliminar decrementando el contador de elementos
        elementos--;
        //Vemos si es una hoja o la raíz sin hijos
        if ((! aEliminar.hayIzquierdo()) || (! aEliminar.hayDerecho())) {
            eliminaVertice(aEliminar);
        } else {
            eliminaVertice(intercambiaEliminable(aEliminar));
        }
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice v1 = vertice(maxSubArbol(vertice.izquierdo));
	vertice.elemento = v1.elemento;
	return v1;
    }
    //Algoritmo que nos regresa el máximo del subarbol izquierdo
    private Vertice maxSubArbol(Vertice vertice) {
        if (! vertice.hayDerecho())
            return vertice;
        return maxSubArbol(vertice.derecho);
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
        protected void eliminaVertice(Vertice vertice) {

            Vertice v1 = null;
            if(vertice.hayIzquierdo())
                v1 = vertice.izquierdo;
            if(vertice.hayDerecho())
                v1 = vertice.derecho;
            if(! vertice.hayPadre()){
                raiz = v1;
            } else {
                if(vertice.padre.derecho == vertice)
                    vertice.padre.derecho = v1;
                else
                    vertice.padre.izquierdo = v1;
            }
            if(v1 != null)
                v1.padre = vertice.padre;
        }
    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz,elemento);
    }

    //Método auxiliar que busca el vértice con el elemento
    private VerticeArbolBinario<T> busca(VerticeArbolBinario<T> vertice, T elemento) {
        //Clausula de escape
        if ((vertice == null) || (elemento == null))
            return null;
        if (vertice(vertice).elemento.compareTo(elemento) == 0)
            return vertice;
        if (elemento.compareTo(vertice(vertice).elemento) < 0)
            return busca(vertice(vertice).izquierdo,elemento);
        return busca(vertice(vertice).derecho,elemento);
    }
    //Método auxiliar que nos dice si un vértice es la raiz
    private boolean esRaiz(Vertice v) {
        return v.padre == null;
    }
    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if (vertice == null)
            return;
        Vertice v = vertice(vertice);
        Vertice i = v.izquierdo;
        //Si no tiene el hijo izquierdo
        if (i == null) {
            System.out.println("Movimiento inválido.");
            return;
        }
        if (v.padre == null) {
            raiz = i;
            i.padre = null;
        } else {
            i.padre = v.padre;
            if (v.padre.derecho == v) {
                v.padre.derecho = i;
            } else {
                v.padre.izquierdo = i;
            }
        }
        v.izquierdo = i.derecho;
        if (v.izquierdo != null)
            v.izquierdo.padre  = v;
        v.padre = i;
        i.derecho = v;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if(vertice == null || vertice(vertice).derecho == null)
	    return;
	Vertice q = vertice(vertice);
	Vertice p = q.derecho;
	if(q.padre == null){
	    raiz = p;
	    p.padre = null;
	}else{
	    p.padre = q.padre;
	    if(q.padre.derecho == q)
		q.padre.derecho = p;
	    else
		q.padre.izquierdo = p;
	}
	q.derecho = p.izquierdo;
	if(q.derecho != null)
	    q.derecho.padre = q;
	q.padre = p;
	p.izquierdo = q;
    }


    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        if (! esVacia())
            dfsPreOrder(accion,raiz);
    }
    //algoritmo auxiliar recursivo
    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if (vertice == null)
            return;
        accion.actua(vertice);
        dfsPreOrder(accion, vertice.izquierdo);
        dfsPreOrder(accion, vertice.derecho);
    }
    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        if (! esVacia())
            dfsInOrder(accion,raiz);
    }
    //Algoritmo recursivo auxiliar
    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if (vertice == null)
            return;

        dfsInOrder(accion, vertice.izquierdo);
        accion.actua(vertice);
        dfsInOrder(accion, vertice.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        if (! esVacia())
            dfsPostOrder(accion,raiz);
    }
    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if (vertice == null)
            return;
        dfsPostOrder(accion, vertice.izquierdo);
        dfsPostOrder(accion, vertice.derecho);
        accion.actua(vertice);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
