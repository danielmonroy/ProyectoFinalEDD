package mx.unam.ciencias.edd;

import java.util.Comparator;
/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;
        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            return (color == Color.ROJO) ? "R{"+elemento.toString()+"}" :
                "N{"+elemento.toString()+"}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)o;
            return color == vertice.color && super.equals(o);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() {
        super();
    }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro nuevo = null;
        nuevo = verticeRojinegro(vertice);
        return nuevo.color;
    }
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeArbolBinario<T> v = getUltimoVerticeAgregado();
        VerticeRojinegro vertice = verticeRojinegro(v);
        vertice.color = Color.ROJO;
        rebalanceo(vertice);
    }
    /**
     *Método que agrega un nuevo elemento al árbol. El método invoca al método {@link
     *ArbolBinarioOrdenado#agrega} y después balancea el árbol recoloreando vertices
     *y girando vértices como sea necesario
     *Este método recibe una instancia de Comparator
     *@param elemento El elemento genérico que se agregará
     *@param comparador Instancia de comparador para poder agregar
     */
    @Override public void agrega(T elemento, Comparator<T> comparador) {
        super.agrega(elemento,comparador);
        VerticeArbolBinario<T> v = getUltimoVerticeAgregado();
        VerticeRojinegro vertice = verticeRojinegro(v);
        vertice.color = Color.ROJO;
        rebalanceo(vertice);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeArbolBinario<T> v =super.busca(elemento);
        if (v == null){
            return;
        }
        elementos--;
        VerticeRojinegro vertice = verticeRojinegro(v);
        if (vertice.derecho != null & vertice.izquierdo != null){
            vertice = verticeRojinegro(intercambiaEliminable(vertice));
        }
        VerticeRojinegro fantasma = null;
        if (vertice.derecho == null & vertice.izquierdo == null){
            fantasma = verticeRojinegro(nuevoVertice(null));
            fantasma.color = Color.NEGRO;
            fantasma.padre = vertice;
            vertice.izquierdo = fantasma;
        }
        VerticeRojinegro hijo = obtenerHijo(vertice);
        eliminaVertice(vertice);
        if (hijo.color == Color.ROJO){
            hijo.color = Color.NEGRO;
            return;
        }
        if (hijo.color == Color.NEGRO && vertice.color == Color.NEGRO){
            rebalanceoElimina(hijo);
        }
        if (fantasma != null){
            eliminaVertice(fantasma);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    /* Métodos auxiliares */
    /**
     * Método para hacer audición a un Vértice
     * @param Vertice para hacer la audición
     * @return Vertice Rojinegro
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice){
        return (VerticeRojinegro)vertice;
    }

    /**
     * Método auxiliar para rebalancear el árbol
     * @param Vertice a verificar
     */
    private void rebalanceo(VerticeRojinegro vertice){

         if (vertice.color != Color.ROJO){
             return;
         }
         if (vertice.padre == null){//CASO 1 : Padre Vacío.
             vertice.color = Color.NEGRO;
             return;
         }
         VerticeRojinegro p = verticeRojinegro(vertice.padre);
         if (p.color == Color.NEGRO){//CASO 2 : Padre Negro
             return;
         }
         VerticeRojinegro t = obtenerTio(vertice);
         VerticeRojinegro a = verticeRojinegro(p.padre);
         if (esRojo(t)){//CASO 3 : Tío Rojo
             p.color = Color.NEGRO;
             t.color = Color.NEGRO;
             a.color = Color.ROJO;
             rebalanceo(a);
             return;
         }
         VerticeRojinegro aux = p;
         if (cruzadosIzq(vertice, p) || cruzadosDer(vertice, p)){
             //CASO 4 : Están cruzados
             if (cruzadosIzq(vertice, p)){
                 super.giraDerecha(p);
                 p = vertice;
                 vertice = aux;
             }else{
                 super.giraIzquierda(p);
                 p = vertice;
                 vertice = aux;
             }
         }
         //CASO 5 : Lo que ocurre si lo demás no ocurre
         p.color = Color.NEGRO;
         a.color = Color.ROJO;
         if (esIzquierdo(vertice)){
             super.giraDerecha(a);
         }else{
             super.giraIzquierda(a);
         }
         return;
    }

    /**
     * Método para verificar si un vértice es diferente de null y de color rojo
     * @param Vértice a verificar
     * @return true si diferente de null y rojo, false en otro caso
     */
    private boolean esRojo(VerticeRojinegro vertice){
        return vertice != null && vertice.color == Color.ROJO;
    }

    /**
     * Método para verificar si un vértice es diferente de null y negro
     * @param Vértice a verificar
     * @return true si diferente de null y negro, false en otro caso
     */
    private boolean esNegro(VerticeRojinegro vertice){
        if(vertice == null){
            return true;
        }
        return vertice.color == Color.NEGRO;
    }

    /**
     * Método para obtener el tio de un vértice
     * @param vertice
     * @return vértice tío
     */
    private VerticeRojinegro obtenerTio(VerticeRojinegro vertice){
        VerticeRojinegro p = verticeRojinegro(vertice.padre);
        VerticeRojinegro a = verticeRojinegro(p.padre);
        if (esIzquierdo(p)){
            return verticeRojinegro(a.derecho);
        }else{
            return verticeRojinegro(a.izquierdo);
        }
    }

    /**
     * Método para verificar si un vértice es hijo izquiero
     * @param Vértice a comprobar
     * @return true si es izquierdo, false si es derecho
     */
    private boolean esIzquierdo(VerticeRojinegro vertice){
        return (vertice.padre.izquierdo == vertice) ? true : false;
    }

    /**
     * Método para determinar si están cruzados, hijo izq. padre der.
     * @param vértice hijo
     * @param vértice padre
     * @return true si están cruzados
     */
    private boolean cruzadosIzq(VerticeRojinegro vertice, VerticeRojinegro p){
        return esIzquierdo(vertice) && !(esIzquierdo(p));
    }

    /**
     * Método para determinar si están cruzados, hijo der. padre izq.
     * @param vértice hijo
     * @param vértice padre
     * @return true si están cruzados
     */
    private boolean cruzadosDer(VerticeRojinegro vertice, VerticeRojinegro p){
        return !(esIzquierdo(vertice)) && esIzquierdo(p);
    }

    /**
     * Método de rebalanceo para elimina
     * @param vértice sobre el que se rebalancea
     */
    private void rebalanceoElimina(VerticeRojinegro vertice){
        if (vertice.padre == null){//CASO 1 : Padre null
            return;
        }
        VerticeRojinegro h = obtenerHermano(vertice);
        VerticeRojinegro p = verticeRojinegro(vertice.padre);
        if (h.color == Color.ROJO){//CASO 2 : Hermano rojo
            p.color = Color.ROJO;
            h.color = Color.NEGRO;
            if (esIzquierdo(vertice)){
                super.giraIzquierda(p);
                h = verticeRojinegro(vertice.padre.derecho);
            }else{
                super.giraDerecha(p);
                h = verticeRojinegro(vertice.padre.izquierdo);
            }
        }
        VerticeRojinegro hi = verticeRojinegro(h.izquierdo);
        VerticeRojinegro hd = verticeRojinegro(h.derecho);
        //CASO 3 : Todos negros
        if (h.color == Color.NEGRO && p.color == Color.NEGRO &&
            esNegro(hi) && esNegro(hd)){
            h.color = Color.ROJO;
            rebalanceoElimina(p);
            return;
        }

        //CASO 4 : Padre Rojo
        if (esNegro(h) && esRojo(p) && esNegro(hi) && esNegro(hd)){
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }
        //CASO 5 : Hijos de colores
        if ((esIzquierdo(vertice) && esRojo(hi) && esNegro(hd))
            || (!esIzquierdo(vertice) && esNegro(hi) && esRojo(hd))){
            h.color = Color.ROJO;
            if (esRojo(hi)){
                hi.color = Color.NEGRO;
            }else{
                hd.color = Color.NEGRO;
            }
            if(esIzquierdo(vertice)){
                super.giraDerecha(h);
                h = verticeRojinegro(vertice.padre.derecho);
            }else{
                super.giraIzquierda(h);
                h = verticeRojinegro(vertice.padre.izquierdo);
            }
        }
        //CASO 6 : Lo que ocurre si lo demás no ocurre
        hi = verticeRojinegro(h.izquierdo);
        hd = verticeRojinegro(h.derecho);
        h.color = p.color;
        p.color = Color.NEGRO;
        if (esIzquierdo(vertice)){
            hd.color = Color.NEGRO;
            super.giraIzquierda(p);
        }else{
            hi.color = Color.NEGRO;
            super.giraDerecha(p);
        }
    }

    /**
     * Método para obtener el hijo de un vértice
     * @param vértice del que queremos su hijo
     * @return vertice hijo(único)
     */
    private VerticeRojinegro obtenerHijo(VerticeRojinegro vertice){
        return vertice.izquierdo == null ? verticeRojinegro(vertice.derecho) :
            verticeRojinegro(vertice.izquierdo);
    }

    /**
     * Método para obtener el hermano de un vértice
     * @param vértice del que queremos su hermano
     * @return vértice hermano
     */
    private VerticeRojinegro obtenerHermano(VerticeRojinegro vertice){
        return esIzquierdo(vertice) ? verticeRojinegro(vertice.padre.derecho) :
            verticeRojinegro(vertice.padre.izquierdo);
    }
}
