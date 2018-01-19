package mx.unam.ciencias.edd;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;


public class Proyecto3 {

  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    if (args.length < 3) { // mínimo 3 (archivo -o directorio)
      System.err.println("*** Llamada incorrecta del programa, pock! ***");
      System.err.println("Puedes proporcionar en la línea de comandos archivos y el nombre del directorio de salida para tus resultados, precedido de la bandera '-o'");
      return;
    }
    boolean hayDirectorio = false;
    String directorio = "";
    Cola archivos = new Cola<String>();
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-o")){
        File dir = new File(args[++i]);
        if(dir.exists()){
          directorio = args[i];
          System.out.println("Directorio '" + args[i] + "' encontrado");
        } else {
          try{
            dir.mkdirs();
            directorio = args[i];
            System.out.println("Directorio '" + args[i] + "' creado");
          } catch (Exception e){
            System.err.println(e);
          }
        }
      hayDirectorio = true;
      } else {
        archivos.mete(args[i]);
        System.out.println("Encontrado archivo '" + args[i] + "'");
      }
    }
    while(!archivos.esVacia() && !directorio.equals("")){
      GeneradorHtml g = new GeneradorHtml((String) archivos.saca(), directorio);
    }



  }

}
