public class BreakPassRecursive {

    static class Solver {
      char vetor [];
      int length;
      int[] encryptedMessageBytes;
      int checksum;

      public Solver(char[] vetor, int length, int[] encryptedMessageBytes, int checksum) {
        this.vetor = vetor; // Allowed password characters
        this.length = length; // Password length
        this.encryptedMessageBytes = encryptedMessageBytes;
        this.checksum = checksum;
      }

      public void start() {
        char[] pass = new char[length];
        discover(pass, 0);
      }

      private void discover(char[] pass, int passwordIndex) {
        if (passwordIndex < length) {
          for (char c : vetor) {
            char[] newPass = pass.clone();
            newPass[passwordIndex] = c;

            discover(newPass, passwordIndex + 1);
          }
        } else if (passwordIndex == pass.length) {
          checkPass(pass);
        }
      }

      private void checkPass(char[] pass) {
        String message = "";

        int passTimes = (encryptedMessageBytes.length / pass.length + 1);
        String fullPass = new String(new char[passTimes]).replace("\0", new String(pass));
        fullPass = fullPass.substring(0, encryptedMessageBytes.length);

        int cont = 0;
        for (int i = 0; i < encryptedMessageBytes.length; i++) {
          char letra = (char) (encryptedMessageBytes[i] ^ fullPass.charAt(i));
          message = message + letra;
          cont = cont + (int) letra;
        }

        if (checksum == cont && message.matches("([a-zA-Z0-9\\s])+")) {
          System.out.println("Password___: " + (char) pass[0] + (char) pass[1] + (char) pass[2] + (char) pass[3]);
          System.out.println("Message____: " + message);
        }
      }
    }

    private static int[] encryptedMessageBytes = {55, 65, 21, 80, 21, 0, 67, 92, 25, 19,
        17, 84, 3, 65, 7, 84, 86, 5, 12, 67, 86, 5, 6, 17, 20, 0, 17, 67, 31, 6, 2};

    private static void discover(int checksum, int passwordLength) {

        // Allowed characters
        char[] vetor = allowedPassCharacters();

        // Number o processor cores
        int cores = Runtime.getRuntime().availableProcessors();
        cores = 1;

        // Allowed characters length that thread will process
        int length = (int) Math.ceil((double) vetor.length / (double) cores);

        for (int i = 0; i < cores; i++) {
          int start = i * length;
          int end = start + length;

          char[] _vetor = vetorSublist(vetor, start, end);

          Solver solver = new Solver(_vetor, passwordLength, encryptedMessageBytes, checksum);
          solver.start();
        }
    }

    private static char[] allowedPassCharacters() {
        char[] vetor = new char[62];

        // Add numbers 0-9
        for (int i = 0; i < 10; i++) {
            vetor[i] = (char) (i + 48);
        }

        // Add words A-Z
        for (int i = 0; i < 26; i++) {
            vetor[i + 10] = (char) (i + 65);
        }

        // Add words a-z
        for (int i = 0; i < 26; i++) {
            vetor[i + 36] = (char) (i + 97);
        }

        return vetor;
    }

    private static char[] vetorSublist(char[] vetor, int start, int end) {
      char[] _vetor = new char[end - start];

      for (int i = 0; i < _vetor.length; i++) {
        _vetor[i] = vetor[start + i];
      }

      return _vetor;
    }

    public static void main(String[] args) {
        // Message checksum: 2789
        // Password length: 4
        discover(2789, 4);
    }
}
