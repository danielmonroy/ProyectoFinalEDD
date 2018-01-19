package mx.unam.ciencias.edd;
public class GeneraGrafica{
  public  DiccionarioContador diccionario;
  public int[] mayores;
  public String[] palabras;
  public int[] valoress;
  public int total;

  public GeneraGrafica(DiccionarioContador dicc){
    this.diccionario = dicc;
    DiccionarioContador dic = new DiccionarioContador();
    total = diccionario.getElementos();
    dic = dicc;
    this.mayores = new int[5];
    this.palabras = new String[5];
    this.valoress = new int[5];
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


  public String generaPieSVG(){
    return null;
  }

  public String generaBarSVG(){
    //max width 300:
    int sumaMayores = valoress[0] + valoress[1] + valoress[2] + valoress[3] + valoress[4];

    String code = "    <div>\n";
    code += "      <figure>\n        <figcaption>Palabras más usadas</figcaption>\n";
    code += "          <svg version='1.1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' class='chart' width='500' height='150' aria-labelledby='title' role='img'>\n";
    code += "          <title id='title'>Palabras más usadas</title>";
    code += "          <g class='bar'>\n";
    code += "            <rect width='" + (300 * valoress[0]) / total + "' height='19' y='80'></rect>\n";
    code += "            <text x='" + ((300 * valoress[0] / total) + 15)  + "' y='89.5' dy='.35em'>" + palabras[0] + " (" + (valoress[0]) * 100 / total + "%)" + "</text>\n";
    code += "          </g>\n";
    code += "          <g class='bar'>";
    code += "            <rect width='" + (300 * valoress[1] / total) + "' height='19'></rect>\n";
    code += "            <text x='" + ((300 * valoress[1] / total) + 15) + "' y='9.5' dy='.35em'>" + palabras[1] + " (" + (valoress[1] * 100) / total + "%)" + "</text>\n";
    code += "          </g>\n";
    code += "          <g class='bar'>\n";
    code += "            <rect width='" + (300 * valoress[2]) / total + "' height='19' y='20'></rect>\n";
    code += "            <text x='" + ((300 * valoress[2] / total) + 15)  + "' y='29.5' dy='.35em'>" + palabras[2] + " (" + (valoress[2] * 100 / total) + "%)" + "</text>\n";
    code += "          </g>\n";
    code += "          <g class='bar'>\n";
    code += "            <rect width='" + (300 * valoress[3]) / total + "' height='19' y='40'></rect>\n";
    code += "            <text x='" + ((300 * valoress[3] / total) + 15)  + "' y='49.5' dy='.35em'>" + palabras[3] + " (" + (valoress[3]) * 100 / total + "%)" + "</text>\n";
    code += "          </g>\n";
    code += "          <g class='bar'>\n";
    code += "            <rect width='" + (300 * valoress[4]) / total + "' height='19' y='60'></rect>\n";
    code += "            <text x='" + ((300 * valoress[4] / total) + 15)  + "' y='69.5' dy='.35em'>" + palabras[4] + " (" + (valoress[4]) * 100 / total + "%)" + "</text>\n";
    code += "          </g>\n";
    code += "          <g class='bar'>\n";
    code += "            <rect width='" + (300 * (total - sumaMayores) / total) + "' height='19' y='100'></rect>\n";
    code += "            <text x='" + ((300 * (total - sumaMayores) / total) + 15)  + "' y='109.5' dy='.35em'>" + "Palabras restantes" + " (" + ((total - sumaMayores) * 100 / total) + "%)" + "</text>\n";
    code += "          </g>\n";
    code += "           </svg>\n";
    code += "         </figure>\n    </div>\n";
    return code;
  }
}
