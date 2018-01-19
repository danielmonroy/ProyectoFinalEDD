package mx.unam.ciencias.edd;
public class GeneraArbol{
  public  DiccionarioContador diccionario;
  public int[] mayores;
  public String[] palabras;
  public int[] valoress;
  public int total;

  public GeneraArbol(DiccionarioContador dicc){
    this.diccionario = dicc;
    DiccionarioContador dic = new DiccionarioContador();
    total = diccionario.getElementos();
    dic = dicc;
    this.mayores = new int[15];
    this.palabras = new String[15];
    this.valoress = new int[15];
    int mayor = 0;
    int indice = -1;
    for (int j = 0; j < mayores.length; j++) {
      for (int i = 0; i < dic.getTamanioEntradas(); i++) {
        if (dic.getPalabraEnIndice(i) != null){
          if (dic.getCantidadEnIndice(i) > mayor){
            mayor = dic.getCantidadEnIndice(i);
            indice = i;
          }
        }
      }
      mayores[j] = indice;
      palabras[j] = dic.getPalabraEnIndice(indice);
      valoress[j] = dic.getCantidadEnIndice(indice);
      dic.elimina(palabras[j]);
      mayor = 0;
    }
  }

  private String masUsadas(){
    String usadas = "";
    usadas += "    <div>\n      <table border='1'>\n";
    int acc = 0;
    int aux = acc;
    for (int i = 0; i < valoress.length; i++) {
      if ((acc % 5) == 0) usadas += "        <tr>\n";
      usadas += "          <td width='20%'><b>" + palabras[i] + ":</b> " + valoress[i] + "</td>\n";
      aux = acc + 1;
      if ((acc % 5) == 4) usadas += "        </tr>\n";
      acc = aux;
    }
    usadas += "      </table>\n    </div>\n    <br><br><br><br>";
    return usadas;
  }

  public String generaRojinegroSVG(){
    String code = "      <h2>Arbol Rojinegro:</h2>\n";
    ArbolRojinegro ar = new ArbolRojinegro<Integer>();
    int q = 0;
    while(q < valoress.length){
      ar.agrega(valoress[q]);
      q++;
    }
    code += trazaArbolRojinegro(ar);
    code += masUsadas();
    return code;
  }

  /**
   *  Método para dibujar un 'Arbol Rojinegro'
   *
   */
  public static String trazaArbolRojinegro(ArbolBinarioOrdenado a){
    String head = "      <?xml version='1.0' encoding='UTF-8' ?>\n";
    String body = "";
    String lines = "";
    if (a.esVacia()) return "Estructura vacía. Pock!!\n";
    int height = a.altura() * 20 + 40 * (a.altura());
    int width = ((int) Math.pow((double) 2,(double) a.altura()) * 30 );
    //int x = width / 2;
    int x = width;
    int y = 0;
    body += bfsVerticesRojinegros(a.raiz(), x, 40);
    lines += bfsAristasOrdenados(a.raiz(), x, 40);
    head += "      <svg width='" + width + "' height='" + height + "'>\n        <g>\n";
    head += lines;
    head += body;
    head += "        </g>\n      </svg>\n";
    return head;
  }

  /**
   *  Método auxiliar para dibujar los vértices de un Árbol Binario
   *
   */
  private static String bfsVerticesRojinegros(VerticeArbolBinario v, int w, int h){
    String arbol = "";
    Cola<VerticeArbolBinario> c = new Cola<VerticeArbolBinario>();
    c.mete(v);
    int level = v.profundidad();
    int nodos = (int) Math.pow((double) 2, (double) level);
    int parts = 2 * nodos;
    int contador = 1;
    String color = "black";
    while(!c.esVacia()){
      ArbolRojinegro.VerticeRojinegro vab = (ArbolRojinegro.VerticeRojinegro) c.saca();
      if ((vab.color).equals(Color.ROJO)) {
        color = "red";
      } else {
        color = "black";
      }
      if(contador >= (parts / 2)){
        contador = 0;
        level = vab.profundidad();
        nodos = (int) Math.pow((double) 2, (double) level);
        parts = 2 * nodos;
        //h = 20;
      }
        arbol += "          <circle class='nodo' cx='" + ((w / parts) + contador * (w/nodos)) + "' cy='" + (level * h + 10) + "' r='10' stroke='" + color + "' fill='" + color + "' stroke-width='1' />\n";
        arbol += "          <text class='nodo-text' fill='white' font-family='sans-serif' font-size='12' x='" + ((w / parts) + contador * (w/nodos)) + "' y='" + (level * h + 15) + "' text-anchor='middle'>" + vab.get() + "</text>\n";
      if (vab.hayIzquierdo()) {
        c.mete(vab.izquierdo());
      }
      if (vab.hayDerecho()) {
        c.mete(vab.derecho());
      }
      contador++;
    }
    return arbol;
  }


  public String generaAVLSVG(){
    String code = "      <h2>Arbol AVL:</h2>\n";
    ArbolAVL avl = new ArbolAVL<Integer>();
    int q = 0;
    while(q < valoress.length){
      avl.agrega(valoress[q]);
      q++;
    }
    code += trazaArbolBinarioOrdenado(avl);
    code += masUsadas();
    return code;
  }

  /**
   *  Método para dibujar un 'Arbol Binario Ordenado'
   *
   */
  public static String trazaArbolBinarioOrdenado(ArbolBinarioOrdenado a){
    String head = "      <?xml version='1.0' encoding='UTF-8' ?>\n";
    String body = "";
    String lines = "";
    if (a.esVacia()) return "Estructura vacía. Pock!!\n";
    int height = a.altura() * 20 + 40 * (a.altura());
    int width = ((int) Math.pow((double) 2,(double) a.altura()) * 30 );
    //int x = width / 2;
    int x = width;
    int y = 0;
    body += bfsVerticesOrdenados(a.raiz(), x, 40);
    lines += bfsAristasOrdenados(a.raiz(), x, 40);
    //body += "<circle cx='" + x + "' cy='" + (y + 10) + "' r='10' stroke='black' fill='white' stroke-width='1' />\n";
    //body += "<text fill='black' font-family='sans-serif' font-size='12' x='" + x + "' y='" + (y + 15) + "' text-anchor='middle'>" + a.raiz().get() + "</text>\n";
    //while(a.raiz)
    //height = 30 * longitud + 20 * --longitud;
    head += "      <svg width='" + width + "' height='" + height + "'>\n        <g>\n";
    head += lines;
    head += body;
    head += "        </g>\n      </svg>\n";
    return head;
  }

  /**
   *  Método auxiliar para dibujar los vértices de un Árbol Binario
   *
   */
  private static String bfsVerticesOrdenados(VerticeArbolBinario v, int w, int h){
    String arbol = "";
    Cola<VerticeArbolBinario> c = new Cola<VerticeArbolBinario>();
    c.mete(v);
    int level = v.profundidad();
    int nodos = (int) Math.pow((double) 2, (double) level);
    int parts = 2 * nodos;
    int contador = 1;
    while(!c.esVacia()){
      VerticeArbolBinario vab = c.saca();
      if(contador >= (parts / 2)){
        contador = 0;
        level = vab.profundidad();
        nodos = (int) Math.pow((double) 2, (double) level);
        parts = 2 * nodos;
        //h = 20;
      }
        arbol += "          <circle class='nodo-color' cx='" + ((w / parts) + contador * (w/nodos)) + "' cy='" + (level * h + 10) + "' r='10' stroke='black' fill='white' stroke-width='1' />\n";
        arbol += "          <text fill='black' font-family='sans-serif' font-size='12' x='" + ((w / parts) + contador * (w/nodos)) + "' y='" + (level * h + 15) + "' text-anchor='middle'>" + vab.get() + "</text>\n";
      if (vab.hayIzquierdo()) {
        c.mete(vab.izquierdo());
      }
      if (vab.hayDerecho()) {
        c.mete(vab.derecho());
      }
      contador++;
    }
    return arbol;
  }

  /**
   *  Método auxiliar para dibujar las aristas de un Árbol Binario
   *
   */
  private static String bfsAristasOrdenados(VerticeArbolBinario v, int w, int h){
    String arbol = "";
    Cola<VerticeArbolBinario> c = new Cola<VerticeArbolBinario>();
    c.mete(v);
    int level = v.profundidad();
    int nodos = (int) Math.pow((double) 2, (double) level);
    int parts = 2 * nodos;
    int contador = 1;
    while(!c.esVacia()){
      VerticeArbolBinario vab = c.saca();
      if(contador >= (parts / 2)){
        contador = 0;
        level = vab.profundidad();
        nodos = (int) Math.pow((double) 2, (double) level);
        parts = 2 * nodos;
        //h = 20;
      }

      if (vab.hayIzquierdo()) {
        c.mete(vab.izquierdo());
        arbol += "          <line x1='" + ((w / parts) + contador * (w/nodos)) + "' y1='"
        + (level * h + 10) + "' x2='" + (((w / parts) + contador * (w/nodos)) - ((w/parts)/2)) + "' y2='" + ((level + 1) * h + 10) + "' stroke='black' stroke-width='1' />\n";
      }
      if (vab.hayDerecho()) {
        c.mete(vab.derecho());
        arbol += "          <line x1='" + ((w / parts) + contador * (w/nodos)) + "' y1='"
        + (level * h + 10) + "' x2='" + (((w / parts) + contador * (w/nodos)) + ((w/parts)/2)) + "' y2='" + ((level + 1) * h + 10) + "' stroke='black' stroke-width='1' />\n";
      }
      contador++;
    }
    return arbol;
  }
}
