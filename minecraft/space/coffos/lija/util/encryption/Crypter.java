package space.coffos.lija.util.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Zhn17
 * <-> 2018-09-03 <->
 * space.lunaclient.luna.util.encryption
 **/
public class Crypter {

    private static final String Algorithm = "AES";
    private static final byte[] keyValue = new byte[]{'A', 'Q', '2', '2', 'O', 'P', 'L', '_', 'E', 'W', 'X', '9', '2'};

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(Algorithm);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        return new BASE64Encoder().encode(encVal);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(Algorithm);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    public static String hCrypt(String text, String enc) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(final byte[] data) {
        final StringBuilder buf = new StringBuilder();
        for (byte aData : data) {
            int halfbyte = aData >>> 4 & 0xF;
            int two_halfBytes = 0;
            do {
                buf.append(halfbyte <= 9 ? (char) (48 + halfbyte) : (char) (97 + (halfbyte - 10)));
                halfbyte = (aData & 0xF);
            }
            while (two_halfBytes++ < 1);
        }
        return buf.toString();
    }

    private static Key generateKey() {
        return new SecretKeySpec(keyValue, Algorithm);
    }
}