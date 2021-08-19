package hlife.utils.md5;

import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    private static final int  KEY_SIZE = 512;

    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(KEY_SIZE);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param content 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String content, PublicKey publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(content.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = getKeyPair();
          //  String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
          //  String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAgQ+ysYnxSA6PIGFS7LS7eORHYEqipfYOuejUxNKKWkUw2cyX++Z9dFK58/erWkYz6Z1JkH4X8apVi09JtSyTtwIDAQABAkAcH5iX2XBLfGix7KNOU1/ayxvGntzsfz7cQiFDNoHRg5z3np5tSxoFX9VCySQBs5fNZ4jU4XFvny04Ng/uVh3RAiEAwBiPqodCsJ/ZIACbQvtI98TCK657E1Q/KTpIA0jGGBkCIQCr/u2xtcqDs56SNqXM1Kl+c7S7rWBaQ4bxdXLmVVjETwIhAJ4i2go5JWZ/gN++gBJJCQ2nNW1+SqVj4kcPSn8hpqnpAiBxDIqnN9n4XuNnL0wjKdSOLPcqNHcUXTYhFxWCl65UuQIgCK01C/kUDn7h6xaQc3WnUlKd8sIhVo+imuPcEvjsmX4=";
            String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIEPsrGJ8UgOjyBhUuy0u3jkR2BKoqX2Drno1MTSilpFMNnMl/vmfXRSufP3q1pGM+mdSZB+F/GqVYtPSbUsk7cCAwEAAQ==";
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
         // data = "{\"a\":1,\"b\":2}&from=1&versionCode=2";
           String  data = "{\"a\":1,\"b\":2}&from=1&versionCode=2";
           System.out.println(data);
            // RSA签名
            String sign = sign(data, getPrivateKey(privateKey));
            System.out.println("签名："+sign);
            // RSA验签
            boolean result = verify(data, getPublicKey(publicKey), sign);
            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}
