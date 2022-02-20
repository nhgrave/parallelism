public class BreakPassRecursive2 {

  static class Discover {

    private int[] encryptedMessageBytes;
    private int checksum;
    private int powerChecksum;
    private int passwordLength;
    private char[] allowedPassCharacters;

    public Discover(int[] encryptedMessageBytes, int checksum, int powerChecksum, int passwordLength, char[] allowedPassCharacters) {
      this.encryptedMessageBytes = encryptedMessageBytes;
      this.checksum = checksum;
      this.powerChecksum = powerChecksum;
      this.passwordLength = passwordLength;
      this.allowedPassCharacters = allowedPassCharacters;
    }

    public void start() {
      for (char c : allowedPassCharacters) {
        char[] password = new char[passwordLength];

        for (int i = 0; i < passwordLength; i++) {
            password[i] = c;
        }

        Solver solver = new Solver(password, encryptedMessageBytes, checksum, powerChecksum, allowedPassCharacters);
        solver.setPriority(Thread.MAX_PRIORITY);
        solver.start();
      }
    }
  }

  static class Solver extends Thread {

    private char[] password;
    private int[] encryptedMessageBytes;
    private int checksum;
    private int powerChecksum;
    private char[] allowedPassCharacters;

    public Solver(char[] password, int[] encryptedMessageBytes, int checksum, int powerChecksum, char[] allowedPassCharacters) {
      this.password = password;
      this.encryptedMessageBytes = encryptedMessageBytes;
      this.checksum = checksum;
      this.powerChecksum = powerChecksum;
      this.allowedPassCharacters = allowedPassCharacters;
    }

    @Override
    public void run() {
      checkPass(password);
      discover(password, 1);
    }

    private void discover(char[] pass, int passwordIndex) {
      if (passwordIndex < pass.length) {
        for (char c : allowedPassCharacters) {
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

      int count = 0;
      int powerCount = 0;
      for (int i = 0; i < encryptedMessageBytes.length; i++) {
        char letra = (char) (encryptedMessageBytes[i] ^ fullPass.charAt(i));
        message = message + letra;
        count = count + (int) letra;
        powerCount += (int) (Math.pow(letra, i + 1));
      }

      if (checksum == count && powerCount == powerChecksum && message.matches("([a-zA-Z0-9\\s])+")) {
        System.out.println("Password___: " + new String(pass));
        System.out.println("Message____: " + message);
      }
    }
  }

  private static char[] allowedPassCharacters() {
    char[] vetor = new char[26];

    // Add numbers 0-9
    // for (int i = 0; i < 10; i++) {
    //   vetor[i] = (char) (i + 48);
    // }
    // Add words A-Z
    // for (int i = 0; i < 26; i++) {;
    //   vetor[i + 10] = (char) (i + 65);
    // }
    // Add words a-z
    for (int i = 0; i < 26; i++) {
      vetor[i] = (char) (i + 97);
    }

    return vetor;
  }

  public static void main(String[] args) {
    // Encrypted message
    int[] encryptedMessageBytes = {21, 1, 65, 0, 10, 7, 8, 26, 19, 18, 77, 8,
        6, 18, 0, 19, 12, 77, 11, 12, 65, 4, 0, 4, 30, 10, 7, 21, 17, 1, 65, 14, 14, 27, 14, 7};

    // Original message checksum
    int checksum = 3492;

    // Message power checksum
    int powerChecksum = 141204219;

    // Password length
    int passwordLength = 7;

    // Allowed characters
    char[] allowedPassCharacters = allowedPassCharacters();

    Discover discover = new Discover(encryptedMessageBytes, checksum, powerChecksum, passwordLength, allowedPassCharacters);
    discover.start();
  }
}
