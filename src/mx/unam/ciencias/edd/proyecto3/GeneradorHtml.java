package mx.unam.ciencias.edd;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.text.Normalizer;

public class GeneradorHtml{
  public String codigo;
  private DiccionarioContador dic;
  private File archivo;
  private String directorio;

  public GeneradorHtml(String file, String dir){
    this.archivo = new File(file);
    this.directorio = dir;
    generaArchivo();
  }

  public void generaArchivo(){
    htmlHeader();
    analizarContenido();
    contarPalabras();
    generarGraficaPastel();
    htmlClosure();
    escribirArchivo();
  }

  private void htmlHeader(){
    this.codigo =
  "<!DOCTYPE html> \n<html> \n  <head> \n    <title>" + archivo.getName() + "</title> \n  </head> \n  <body> \n";
  }

  private void htmlClosure(){
    this.codigo +=
    "\n  </body> \n</html>";
  }



  private void analizarContenido(){
    try (BufferedReader br = new BufferedReader(new FileReader(this.archivo))) {
      String line;
      dic = new DiccionarioContador();
      while ((line = br.readLine()) != null) {
        line = Normalizer.normalize(line, Normalizer.Form.NFD);
        line = line.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").toLowerCase();
        String[] words = line.split("\\P{L}+");
        for (String s : words){
          if (!s.equals("")) dic.agrega(s, s); // Agregamos la palabra al arreglo
        }
      }
      System.out.println(dic.toString());

    } catch (Exception e){
      System.err.println(e);
    }
  }



  private void escribirArchivo(){
    try{
      File newFile = new File(directorio + "/" + archivo.getName() + ".html");
      if (newFile.createNewFile()){
         System.out.println("Archivo html creado!");
       }else{
         System.out.println("Se sobreescribió el archivo " + archivo.getName() + ".html");
       }
       FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
          BufferedWriter bw = new BufferedWriter(fw);

          // Write in file
          bw.write(this.codigo);

          // Close connection
          bw.close();
    } catch (Exception e) {
	      e.printStackTrace();
	    }
  }



  private void contarPalabras(){
    this.codigo += "    <div>\n";
    this.codigo += "      <h2>Contador de palabras: </h2>\n";
    this.codigo += "      <table border='1'>\n";
    int acc = 0;
    int aux = acc;
    String palabra;
    for (int i = 0; i < dic.getTamanioEntradas(); i++) {
      if ((palabra = dic.getPalabraEnIndice(i)) != null){
        if ((acc % 5) == 0) this.codigo += "        <tr>\n";
        this.codigo += "          <td width='20%'><b>" + palabra + ":</b> " + dic.getCantidadEnIndice(i) + "</td>\n";
        aux = acc + 1;
        if ((acc % 5) == 4) this.codigo += "        </tr>\n";
      }
      acc = aux;
    }
    this.codigo += "      </table>\n    </div>\n";
  }


  private void generarGraficaPastel(){
    /*

    GraficaPastel pie = new GraficaPastel()
    */
  }
}
