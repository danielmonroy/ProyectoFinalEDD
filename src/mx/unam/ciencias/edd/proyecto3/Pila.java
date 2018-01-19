package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        if (esVacia()) {
            return "";
        }

        Nodo aux = cabeza;
        String s = "";
        while (aux != null) {
            s += aux.elemento.toString() + "\n";
            aux = aux.siguiente;
        }
        return s;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento null");

        if (esVacia()) {
            this.cabeza = this.rabo = new Nodo(elemento);
            return;
        }
        Nodo nw = new Nodo(elemento);
        nw.siguiente = this.cabeza;
        this.cabeza = nw;

    }
}
