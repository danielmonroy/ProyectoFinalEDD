package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<Vertice>();
            if (raiz != null)
                cola.mete(raiz);
        }
        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (!cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            if (cola.esVacia())
                throw new NoSuchElementException("No  hay siguiente");
            Vertice aux = cola.saca();
            if (aux.izquierdo != null)
                cola.mete(aux.izquierdo);
            if (aux.derecho != null)
                cola.mete(aux.derecho);
            return aux.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento a agregar null.");
        elementos ++;
        //Vertice a agregar
        Vertice nuevo = nuevoVertice(elemento);
        if (esVacia()) {
            raiz = nuevo;
            return;
        }
        Vertice sinHijo = BFSAux(raiz);
        nuevo.padre = sinHijo;
        if (sinHijo.izquierdo == null) {
            sinHijo.izquierdo = nuevo;
            return;
        }
        sinHijo.derecho = nuevo;
    }

    private Vertice BFSAux(Vertice vertice) {
        Cola<Vertice> cola = new Cola <Vertice>();
        Vertice aux = vertice;
        cola.mete(aux);
        while (!cola.esVacia()) {
            aux = cola.saca();
            if (!aux.hayIzquierdo())
                return aux;
            cola.mete(aux.izquierdo);
            if (!aux.hayDerecho())
                return aux;
            cola.mete(aux.derecho);
        }
        return aux;
    }


    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {

        Vertice aEliminar = vertice(busca(elemento));
        if (aEliminar == null)
            return;
        elementos--;
        if (elementos == 0) {
            raiz = null;
            return;
        }
        Vertice eliminable = ultimoAgregado(raiz);
        intercambiaElementos(eliminable, aEliminar);
        if (esDerecho(eliminable)) {
           eliminable.padre.derecho = null;
           eliminable = null;
        } else {
            eliminable.padre.izquierdo = null;
            eliminable = null;
        }

    }

    private boolean esDerecho(Vertice v) {
        if ((v.padre != null)&&((v.padre.hayDerecho())&&(v.padre.derecho.equals(v))))
            return true;
        return false;
    }
    
    private Vertice ultimoAgregado(Vertice vertice) {
        Cola <Vertice> cola = new Cola<Vertice>();
        Vertice ultimo = vertice;
        cola.mete(vertice);
        while (! cola.esVacia()) {
            ultimo = cola.saca();
            if (ultimo.izquierdo != null)
                cola.mete(ultimo.izquierdo);
            if(ultimo.derecho != null)
                cola.mete(ultimo.derecho);
        }
        return ultimo;
    }
    //Método auxiliar que intercambia los vértices
    private void intercambiaElementos(Vertice v1,Vertice v2) {
        T aux = v1.elemento;
        v1.elemento = v2.elemento;
        v2.elemento = aux;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        return(esVacia())? -1 : raiz.altura();

    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (esVacia())
            return;
        Cola <Vertice> cola = new Cola<Vertice>();
        Vertice aux;
        cola.mete(raiz);
        while (!cola.esVacia()) {
            aux = cola.saca();
            accion.actua(aux);
            if (aux.izquierdo != null)
                cola.mete(aux.izquierdo);
            if(aux.derecho != null)
                cola.mete(aux.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
