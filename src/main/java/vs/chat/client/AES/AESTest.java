package vs.chat.client.AES;

public class AESTest {

    public static void main(String[] args) {
        String key = "ich bin ein Schlüssel";
        String message = "Ich werde verschlüsselt";
        String chiffre = AES.encryptAES(key, message);
        String decript = AES.decryptAES(key, chiffre);

        System.out.println(message);
        System.out.println(chiffre);
        System.out.println(decript);

    }
}
