package mx.unam.ciencias.edd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Stylesheet{

  public String codigo;
  private String directorio;

  public Stylesheet(String dir){
    this.directorio = dir;
    generaArchivo();
  }

  public void generaArchivo(){
    codigo = ".bar {\n  fill: #aaa\n  height: 21px;\n  -webkit-transition: fill .3s ease;\n  transition: fill .3s ease;\n  cursor: pointer;\n  font-family: Helvetica, sans-serif;\n}\n.bar text {\n  fill: #555;\n}\n\n.chart:hover .bar,\n.chart:focus .bar {\n  fill: #aaa;\n}\n\n.bar:hover,\n.bar:focus {\n  fill: red !important;\n}\n.bar:hover text,\n.bar:focus text {\n  fill: red;\n}\n\nfigcaption {\n  font-weight: bold;\n  color: #000;\n  margin-bottom: 20px;\n}\n\nbody {\n  font-family: 'Open Sans', sans-serif;\n}";
    codigo += ".nodo:hover { fill: white; transition: .3s ease}\n";
    codigo += ".nodo-color:hover { fill: #2184FA; transition: .3s ease }\n";

    escribirArchivo();
  }

  private void escribirArchivo(){
    try{
      File newFile = new File(directorio + "/stylesheet.css");
      if (newFile.createNewFile()){
         System.out.println("Archivo css creado!");
       }else{
         System.out.println("Se sobreescribió el archivo stylesheet.css");
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
}
