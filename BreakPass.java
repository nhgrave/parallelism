public class BreakPass {

  public static void main(String[] args) {
    int encryptedMessageBytes[] = new int[]{55, 65, 21, 80, 21, 0, 67, 92, 25, 19,
      17, 84, 3, 65, 7, 84, 86, 5, 12, 67, 86, 5, 6, 17, 20, 0, 17, 67, 31, 6, 2};

    String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    String pass = null;

    for (int j = 0; j < letras.length(); j++) {
      for (int k = 0; k < letras.length(); k++) {
        for (int l = 0; l < letras.length(); l++) {
          for (int m = 0; m < letras.length(); m++) {
            pass = "" + letras.charAt(j) + letras.charAt(k) + letras.charAt(l) + letras.charAt(m);

            int passTimes = (encryptedMessageBytes.length / pass.length() + 1);
            String fullPass = new String(new char[passTimes]).replace("\0", pass);
            fullPass = fullPass.substring(0, encryptedMessageBytes.length);

            String msgTemp = "";
            for (int n = 0; n < encryptedMessageBytes.length; n++) {
              msgTemp += (char) (encryptedMessageBytes[n] ^ fullPass.charAt(n));
            }

            int sum = 0;
            for (int n = 0; n < msgTemp.length(); n++) {
              sum += (int) msgTemp.charAt(n);
            }

            if (sum == 2789) {
              String speciais = "{}[]()<>&*#@$%|/\\:;~`´^";

              boolean ok = true;

              for (int n = 0; n < speciais.length() && ok; n++) {
                ok = ok && !msgTemp.contains("" + speciais.charAt(n));
              }

              if (ok) {
                System.out.println("Password: " + pass);
                System.out.println("Message: " + msgTemp);
              }
            }
          }
        }
      }
    }
  }
}
