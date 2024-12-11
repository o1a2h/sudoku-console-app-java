import java.io.IOException;
import java.util.Scanner;

public class Console {

  static Scanner scanner = new Scanner(System.in);

  // https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
  public static void clear() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }
    } catch (IOException | InterruptedException e) {
      System.out.println(e);
    }
  }

  // https://stackoverflow.com/questions/1001335/move-console-cursor-to-specified-position
  public static void moveConsoleCursor(int row, int column) {
    char escCode = 0x1B;
    System.out.print(String.format("%c[%d;%df", escCode, row, column));
  }

  

  public static String ask(String q) {
    print(q);
    return getLine();
  }
  
  public static String ask(String[] q) {
    for (int i = 0; i < q.length; i++) {
      print(((q[i].startsWith("(")) ? ", " : "") + q[i]);
    }
    return getLine();
  }

  public static String ask(String q, String[] v) {
    print(q);
    for (int i = 0; i < v.length; i++) {
      print("(" + (i + 1) + ")" + ((v[i].startsWith("(")) ? ", " : "") + v[i]);
    }
    return getLine();
  }

  public static String ask(String[] q, String[] v) {
    for (int i = 0; i < q.length; i++) {
      print(((q[i].startsWith("(")) ? ", " : "") + q[i]);
    }
    for (int i = 0; i < v.length; i++) {
      print("(" + (i + 1) + ")" + ((v[i].startsWith("(")) ? ", " : "") + v[i]);
    }
    return getLine();
  }


  public static String getLine() {
    System.out.print(" >> ");
    return scanner.nextLine();
  }

  public static void print(String s) {
    System.out.printf(" << %s\n", s);
  }



}
