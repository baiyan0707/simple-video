package com.simple.util;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * @ClassName: RSA.java
 * @description: 非对称加密RSA算法 工具类
 * @author: fanyanghua
 * @create: 2019-10-25 17:15
 **/
public class RSAUtil {
    /**
     * RSA最大加密明文大小
     * 1024位的证书，加密时最大支持117个字节，解密时为128
     * 2048位的证书，加密时最大支持245个字节，解密时为256
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     * 1024位的证书，加密时最大支持117个字节，解密时为128
     * 2048位的证书，加密时最大支持245个字节，解密时为256
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final String UTF8 = "utf-8";

    /**
     * 获取公钥
     */
    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 获取私钥
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 使用私钥签名
     * @param data 明文数据
     * @param privateKey 私钥
     * @param signatureAlgorithm 签名算法
     */
    private static String signByPrivate(byte[] data, String privateKey, SignatureAlgorithm signatureAlgorithm) throws Exception {
        PrivateKey key = getPrivateKey(privateKey);
        return signByPrivate(data, key, signatureAlgorithm);
    }

    private static String signByPrivate(byte[] data, PrivateKey privateKey, SignatureAlgorithm signatureAlgorithm) throws Exception {
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(signatureAlgorithm.getName());
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64Util.encode(sign);
    }

    /**
     * 使用公钥验证签名
     * @param data 密文数据
     * @param publicKey 公钥
     * @param signatureAlgorithm 签名算法
     * @param sign 待验证签名字符串
     */
    private static boolean verifyByPublicKey(byte[] data, String publicKey, SignatureAlgorithm signatureAlgorithm, String sign) throws Exception {
        PublicKey key = getPublicKey(publicKey);
        return verifyByPublicKey(data, key, signatureAlgorithm, sign);
    }

    private static boolean verifyByPublicKey(byte[] data, PublicKey publicKey, SignatureAlgorithm signatureAlgorithm, String sign) throws Exception {
        Signature signature = Signature.getInstance(signatureAlgorithm.getName());
        signature.initVerify(publicKey);
        signature.update(data);

        // 验证签名是否正常
        byte[] decode = Base64Util.decode(sign);
        return signature.verify(decode);
    }

    /**
     * 使用私钥加密数据
     * @param data 明文数据
     * @param privateKey 私钥
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        // 获取私钥对象
        PrivateKey key = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 使用公钥加密数据
     * @param data 明文数据
     * @param publicKey 公钥
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        // 获取公钥对象
        PublicKey key = getPublicKey(publicKey);

        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 使用私钥解密数据
     * @param encryptedData 密文数据
     * @param privateKey 私钥
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {

        // 获取私钥对象
        PrivateKey key = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 使用公钥解密
     * @param encryptedData 密文数据
     * @param publicKey 公钥
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        // 获取公钥对象
        PublicKey key = getPublicKey(publicKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 生成密钥对
     * @param keysize 密钥长度：1024、2048
     */
    private static void generateKeyPair(Integer keysize) throws Exception {
        // 生成RSA Key
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keysize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        System.out.println("公钥: " + Base64Util.encode(publicKey.getEncoded()));
        System.out.println("私钥: " + Base64Util.encode(privateKey.getEncoded()));
    }

    /**
     * RSA的签名算法
     */
    public static enum SignatureAlgorithm {
        MD5withRSA("MD5withRSA"),
        SHA1withRSA("SHA1withRSA"),
        ;

        private String name;

        SignatureAlgorithm(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

//    public static void main(String[] args) throws Exception {
//        // 生成密钥对
//        RSA.generateKeyPair(1024);
//
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC+AIkvhBSnMADSm78i5qxuaygWoVo08fUJ0QyfRGDBxmpffMY2DvTm2eK2TsEQKk+QRpZQlEVBPohNXAo0dLXwmAuzoEWz2BmMhjFXq1J9k5wGS7DIuK14m6bIJoM0AJeGXXL2w2iymZ6/f7pMbL/GmAZiBshdVvd6MOgFbNcWQIDAQAB";
//        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIL4AiS+EFKcwANKbvyLmrG5rKBahWjTx9QnRDJ9EYMHGal98xjYO9ObZ4rZOwRAqT5BGllCURUE+iE1cCjR0tfCYC7OgRbPYGYyGMVerUn2TnAZLsMi4rXibpsgmgzQAl4ZdcvbDaLKZnr9/ukxsv8aYBmIGyF1W93ow6AVs1xZAgMBAAECgYBRZgsPAwHqFnpQkUaBt9KheyELWLdp+5ZLCUydhJYJtbQBX8JjQqGMVx33ciP39Kg7hQL3TvxN8BX15dybwi0kAIjsbxOeH6VhYe8li9KIh5XJI8oiwAL07tB8l3SBnBUHajA/M+XOV2EkO4Aj8I3COljWdgfAOJMQgRAmUCwVsQJBALqNyDTqJhwu8TEWUAUfdUHC+az+/9+iPpdnMG1XBiHAFs9RSTpTt/9ob8AQTUoeaLCmocB13x6IWS4lg4C0BA8CQQCzuRQqhWKQOgX3E3TNn+d/IgvYMTrj4qNG3zjFzuXk/0Iw7ew29MfkKSLeku1aiOFHcIG3nhSBy333LPXwnhEXAkEAt4fjDkPJaHK6MLutz8xqFF9YONzKKF46y8Ll/rvduXh8OEvVCf/xBeHeBwtCroeKIThd67VK44PqkJ6xZ/4x+wJAdpEPgM3mrIfbD5nCYTWxH+pyLfqsg1MtrL/vUHCismTEYktUnxz2EswguP3toODU3rCWo3Cizs5VGBCNYGSnmQJBAJx66Fd+j37f2/PqOx3RBONHIocYaGJ+jgvb6cUCE7nce2NmHE3Tr4zNUPcCZa3mulQUI+fB0sn2uov5b9Exla4=";
//        byte[] data = "6320b12af41c66be".getBytes(UTF8);
//
////        byte[] encryptByPrivateKey = RSA.encryptByPrivateKey(data, privateKey);
////        System.out.println("encryptByPrivateKey: " + Base64Util.encode(encryptByPrivateKey));
//        byte[] encryptByPublicKey = RSA.encryptByPublicKey(data, publicKey);
//        System.out.println("encryptByPublicKey: " + encryptByPublicKey.toString());
//        System.out.println("encryptByPublicKey: " + Base64Util.encode(encryptByPublicKey));
//
////        byte[] decryptByPublicKey = RSA.decryptByPublicKey(encryptByPrivateKey, publicKey);
////        System.out.println("decryptByPublicKey: " + new String(decryptByPublicKey, UTF8));
////        byte[] decryptByPrivateKey = RSA.decryptByPrivateKey(encryptByPublicKey, privateKey);
//        String rsaContent = "YzdERV5YfxeM2GYsa00uUMRCuhJi1pEH7D2r/ZfOnUQG9LB/9FArzxU1XQ9qRvjzbtLG6WpIghKglNahaV+vQ2oI371q9ne76XQrqhQCEQAuUOinfiUNyn+iCi4cIuWDIptKV+E3yorK13YIqW7c2GCCqjLOFqXrT20UDA7nocg=";
//        byte[] keyBytes = Base64Util.decode(rsaContent);
//        byte[] aesKey = RSA.decryptByPrivateKey(keyBytes, privateKey);
////        byte[] decryptByPrivateKey = RSA.decryptByPrivateKey(Base64Util.decode("dZZBH8AHM2lK4KibWEARl/K/JpkeRoq9Sc1/6/9FbQqQUfD9x9Fk+qhlnrQal7T7g+OEfnWK73kD71b+55gcbV58cMWteT86VcbpOVcunH2QFT1Kd8QZYBEKFCFKu86lNRAKolvatPsBYHzhW8ESD++mdqDNnEDFlbBP7xxTFXI="), privateKey);
//        System.out.println("decryptByPrivateKey: " + new String(aesKey, UTF8));
//
//    }

}
