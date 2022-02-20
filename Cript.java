public class Cript {

  public static void main(String[] args) {
    String mens = "Meu sapo pulou";
    String chave = "tatu";

    int times = mens.length() / chave.length() + 1;
    String chaveFull = new String(new char[times]).replace("\0", chave);
    chaveFull = chaveFull.substring(0, mens.length());

    String aux = "";
    for (int i = 0; i < mens.length(); i++) {
      aux += (char) (mens.charAt(i) ^ chaveFull.charAt(i));
    }
    System.out.println(aux);

    String temp = "";
    for (int i = 0; i < aux.length(); i++) {
      temp += (char) (aux.charAt(i) ^ chaveFull.charAt(i));
    }
    System.out.println(temp);
  }
}
