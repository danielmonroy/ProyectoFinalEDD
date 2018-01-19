import java.text.Normalizer;
import java.io.File;
public class Prueba{
  public static void main(String[] args) {
    String s = "Hola a tódos amígos míos! Esta es una cñna!! Yeiii! Jeje. lol,you";
    s = Normalizer.normalize(s, Normalizer.Form.NFD);
    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    String[] words = s.split("\\P{L}+");
    for (int i = 0; i < words.length; i++) {
      System.out.println(words[i].toLowerCase());
    }
    String ante = "aloha";
    String onte = "aloha";
    if (ante == onte) {
      System.out.println("Iguales con ==");
    }
    if (ante.equals(onte)){
      System.out.println("Iguales con equals");
    }

    File dir = new File(args[0]);

    boolean exists = dir.exists();
    System.out.println("Directory " + dir.getPath() + " exists: " + exists);
    if(!exists)
      try{
        dir.mkdirs();
        System.out.println("Directorio creado");
      } catch(Exception e){
        System.err.println("Error al crear directorio");
      }
    }

  }
