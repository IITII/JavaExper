import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

/**
 * @author IITII
 */
public class Rsa {

    public static void main(String[] args) throws Exception {
        System.out.println("请输入明文：");
        String string = new Scanner(System.in).nextLine();
        String[] keyPair = genKeyPair();
        System.out.format("明文：%s\n加密后：%s\n解密后：%s\n",
                string,
                encrypt(string, keyPair[0]),
                decrypt(encrypt(string, keyPair[0]), keyPair[1]));
        System.out.format("公钥：%s\n私钥：%s\n",
                keyPair[0],
                keyPair[1]
        );
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String[] genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey keyPairPrivate = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey keyPairPublic = (RSAPublicKey) keyPair.getPublic();
        return new String[]{
                Base64.encodeBase64String(keyPairPublic.getEncoded()),
                Base64.encodeBase64String((keyPairPrivate.getEncoded()))
        };
    }

    /**
     * RSA公钥加密
     *
     * @param str       明文
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        密文
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

}