package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null)
                throw new NoSuchElementException("No hay siguiente.");
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null)
                throw new NoSuchElementException("No hay anterior");
            siguiente = anterior;
            anterior = anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            siguiente = null;
            anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }
    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return rabo == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento a agregar null.");
        Nodo nuevo = new Nodo(elemento);
        longitud ++;
        if (esVacia()) {
            cabeza = rabo = nuevo;
        } else {
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;
        }
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }
    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {

        if (elemento == null)
            throw new IllegalArgumentException("La lista no acepta elementos null");

        Nodo nuevo = new Nodo(elemento);
        longitud ++;
        if (esVacia()) {
            cabeza = rabo = nuevo;
        } else {

            cabeza.anterior = nuevo;
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento a agregar null");
        if (i < 1) {
            agregaInicio(elemento);
            return;
        }
        if (i >= longitud) {
            agregaFinal(elemento);
            return;
        }
        longitud++;
        Nodo nuevo = new Nodo(elemento);
	Nodo aux = cabeza;
	while(i-- > 1)
	    aux = aux.siguiente;
	nuevo.anterior = aux;
	nuevo.siguiente = aux.siguiente;
	aux.siguiente.anterior = nuevo;
	aux.siguiente = nuevo;


    }
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (elemento == null) {
            System.out.println("Elemento null.");
            return;
        }
        if (!this.contiene(elemento)){
            return;
        }

        if (this.cabeza.elemento.equals(elemento) && (longitud == 1)){
            cabeza = rabo = null;
            longitud --;
            return;
        }

        if (this.cabeza.elemento.equals(elemento)) {
            this.cabeza = cabeza.siguiente;
            cabeza.anterior = null;
            longitud --;
            return;
        }
        if (this.rabo.elemento.equals(elemento)) {
            this.rabo = rabo.anterior;
            rabo.siguiente = null;
            longitud --;
            return;
        }

        Nodo aux = this.cabeza;
        while ( ! aux.elemento.equals(elemento)) {
            aux = aux.siguiente;
        }
        aux.anterior.siguiente = aux.siguiente;
        aux.siguiente.anterior = aux.anterior;
        longitud --;
    }


    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (esVacia())
            throw new NoSuchElementException("Lista vacía");
        T elemento = cabeza.elemento;
        longitud --;
        if (longitud == 0) {
            limpia();
        } else {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }
        return elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (esVacia())
            throw new NoSuchElementException("Lista vacía");
        longitud--;
        T elemento = rabo.elemento;
        if (longitud == 0) {
            limpia();
        } else {
            rabo = rabo.anterior;
            rabo.siguiente = null;
        }
        return elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento == null || esVacia())
            return false;
        Nodo aux = cabeza;
        boolean contiene = false;
        while (aux != null) {
            if (aux.elemento.equals(elemento))
                return true;
            aux = aux.siguiente;
        }
        return contiene;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> reversa = new Lista<T>();
        if (! esVacia()) {
            Nodo aux = rabo;
            while (aux != null) {
                reversa.agrega(aux.elemento);
                aux = aux.anterior;
            }
        }
        System.out.println("Lista vacía");
        return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> copia = new Lista <T>();
        if (! esVacia()) {
            Nodo aux = cabeza;
            while(aux != null) {
                copia.agrega(aux.elemento);
                aux = aux.siguiente;
            }
        }
        System.out.println("Copia vacía.");
        return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (esVacia())
            throw new NoSuchElementException("La lista está vacía");
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (esVacia())
            throw new NoSuchElementException("La lista está vacía");
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i>=longitud)
            throw new ExcepcionIndiceInvalido("Indice fuera de rango.");
        Nodo aux = cabeza;
        int c = 0;
        while(c < i) {
            aux = aux.siguiente;
            c++;
        }
        return aux.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        int indice = -1;

        if (elemento != null) {
            if (contiene(elemento)) {
                indice = 0;
                Nodo aux = cabeza;
                while (aux != null) {
                    if (! aux.elemento.equals(elemento)) {
                        aux = aux.siguiente;
                        indice++;
                    } else {
                        return indice;
                    }
                }
            }
            return indice;
        }
        System.out.println("Elemento a buscar null.");
        return indice;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (esVacia()) {
            return "[]";
        }

        String out = "[";
        Nodo aux = cabeza;

        while (  aux.siguiente != null ) {
            out += aux.elemento.toString() +", ";
            aux = aux.siguiente;
        }
        out += aux.elemento.toString() +"]";
        return out;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;

        boolean equals = false;
        if (longitud == lista.longitud) {
            equals = true;
            Nodo aux1 = cabeza;
            Nodo aux2 = lista.cabeza;
            while (aux1 != null) {
                if (! aux1.elemento.equals(aux2.elemento))
                    return false;
                aux1 = aux1.siguiente;
                aux2 = aux2.siguiente;
            }
        }
        return equals;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {

        Lista<T> i = new Lista<T>();
	Lista<T> d = new Lista<T>();
	int j = (longitud/2);
	Nodo a = cabeza;
	if(longitud == 0 || longitud == 1)
	    return copia();

	while(a != null && j != 0){
	    i.agrega(a.elemento);
	    j--;
	    a = a.siguiente;
	}
	while(a != null){
	    d.agrega(a.elemento);
	    a = a.siguiente;
	}
	i = i.mergeSort(comparador);
	d = d.mergeSort(comparador);
	return i.mezcla(d, comparador);
    }
    //Algoritmo auxiliar que mezclla las listas
    private Lista<T> mezcla(Lista<T> lista, Comparator<T> comparador){
	Lista<T> l = new Lista<T>();
	Nodo n1 = this.cabeza;
	Nodo n2 = lista.cabeza;
	while(n1 != null && n2 != null)
	    if(comparador.compare(n1.elemento, n2.elemento) <= 0){
		l.agregaFinal(n1.elemento);
		n1 = n1.siguiente;
	    }else{
		l.agregaFinal(n2.elemento);
		n2 = n2.siguiente;
	    }
	Nodo n = (n1 == null ? n2 : n1);
	while(n != null){
	    l.agregaFinal(n.elemento);
	    n = n.siguiente;
	}

	return l;
    }
    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo aux = cabeza;
        boolean contiene = false;
        while (aux != null) {
            if (comparador.compare(aux.elemento, elemento) == 0) {
                contiene = true;
                break;
            }
            aux = aux.siguiente;
        }
        return contiene;
    }
    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
