package com.simple.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ParamAESCodec.java
 * @description: AES 加密工具
 * @author:
 * @create: 2019-10-25 16:15
 **/
public class ParamAESCodec {

    public static final Charset CHARSET = Charset.forName("utf-8");

    public static final String ALGORITHM = "AES";

    public static final String AES_CBC_NOPADDING = "AES/CBC/NoPadding";


    /**
     * 为了平台的通用，选择 AES/CBC/NoPadding 的模式，然后手动 padding
     * 对应PHP 平台为 mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $data, MCRYPT_MODE_CBC, $iv)
     * <p>
     * AES/CBC/NoPadding encrypt
     * 16 bytes secretKeyStr
     * 16 bytes intVector
     *
     * @param secretKeyBytes
     * @param intVectorBytes
     * @param input
     * @return
     */
    public static byte[] encryptCBCNoPadding(byte[] secretKeyBytes, byte[] intVectorBytes, byte[] input) {
        try {
            IvParameterSpec iv = new IvParameterSpec(intVectorBytes);
            SecretKey secretKey = new SecretKeySpec(secretKeyBytes, ALGORITHM);
            int inputLength = input.length;
            int srcLength;

            Cipher cipher = Cipher.getInstance(AES_CBC_NOPADDING);
            int blockSize = cipher.getBlockSize();
            byte[] srcBytes;
            if (0 != inputLength % blockSize) {
                srcLength = inputLength + (blockSize - inputLength % blockSize);
                srcBytes = new byte[srcLength];
                System.arraycopy(input, 0, srcBytes, 0, inputLength);
            } else {
                srcBytes = input;
            }

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptBytes = cipher.doFinal(srcBytes);
            return encryptBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES/CBC/NoPadding decrypt
     * 16 bytes secretKeyStr
     * 16 bytes intVector
     *
     * @param input
     * @return
     */
    public static byte[] decryptCBCNoPadding(byte[] secretKeyBytes, byte[] intVectorBytes, byte[] input) {
        try {
            IvParameterSpec iv = new IvParameterSpec(intVectorBytes);
            SecretKey secretKey = new SecretKeySpec(secretKeyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_CBC_NOPADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] encryptBytes = cipher.doFinal(input);
            return encryptBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用 AES 算法加密 inputStr。
     * 使用 secretStr 作为 key，secretStr 的前 16 个字节作为 iv。
     *
     * @param secretStr
     * @param inputStr
     * @return
     */
    public static byte[] encode(String secretStr, String inputStr) {
        byte[] secretKeyBytes = secretStr.getBytes(CHARSET);
        byte[] ivBytes = Arrays.copyOfRange(secretKeyBytes, 0, 16);
        byte[] inputBytes = inputStr.getBytes(CHARSET);

        byte[] outputBytes = encryptCBCNoPadding(secretKeyBytes, ivBytes, inputBytes);
        return outputBytes;
    }

    /**
     * 用 AES 算法加密 inputStr。
     * 使用 secretStr 作为 key，secretStr 的前 16 个字节作为 iv。
     * 并对加密后的字节数组调用 sun.misc.BASE64Encoder.encode 方法，
     * 转换成 base64 字符串返回。
     *
     * @param secretStr
     * @param inputStr
     * @return
     */
    public static String strEncodBase64(String secretStr, String inputStr) {
        String base64Str = Base64.encodeBase64String(encode(secretStr, inputStr));
        return base64Str;
    }

    /**
     * 用 AES 算法加密 inputStr。
     * 使用 secretStr 作为 key，secretStr 的前 16 个字节作为 iv。
     *
     * @param secretStr
     * @return
     */
    public static byte[] decode(String secretStr, byte[] inputBytes) {
        byte[] secretKeyBytes = secretStr.getBytes(CHARSET);
        byte[] ivBytes = Arrays.copyOfRange(secretKeyBytes, 0, 16);

        byte[] outputBytes = decryptCBCNoPadding(secretKeyBytes, secretKeyBytes, inputBytes);
        return outputBytes;
    }

    /**
     * 用 AES 算法加密 inputStr。
     * 使用 secretStr 作为 key，secretStr 的前 16 个字节作为 iv。
     * 并对加密后的字节数组调用 sun.misc.BASE64Encoder.encode 方法，
     * 转换成 base64 字符串返回。
     * <p>
     * （仅作为测试用途，具体加密流程以接口文档为准）
     *
     * @param secretStr
     * @param inputStr
     * @return
     */
    public static String base64StrDecode(String secretStr, String inputStr) {
        byte[] inputBytes;
        inputBytes = Base64.decodeBase64(inputStr);
        String outputStr = new String(decode(secretStr, inputBytes), CHARSET).trim();
        return outputStr;
    }

    public static void main(String[] args) throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC+AIkvhBSnMADSm78i5qxuaygWoVo08fUJ0QyfRGDBxmpffMY2DvTm2eK2TsEQKk+QRpZQlEVBPohNXAo0dLXwmAuzoEWz2BmMhjFXq1J9k5wGS7DIuK14m6bIJoM0AJeGXXL2w2iymZ6/f7pMbL/GmAZiBshdVvd6MOgFbNcWQIDAQAB";
        String key = "d941e076a28dd97b";
        String phoneNo = "15021603712";
        int fromChannel = 5;
        String token = "ec226273334b9a11c967a329f537abb60c59bc93bcafe0da8e85db5cc29c0896";
        long timestamp = System.currentTimeMillis();
        // 业务参数
//        String data = "{\"id\":51,\"title\":\"page标题\",\"introduction\":\"page简介\",\"imageUrl\":\"15533334444\",\"linkAddress\":\"123\",\"sort\":12,\"type\":45,\"pageId\":78}";
		String data = "{\"id\":21}";
//		String data = "{}";

        Map<String, Object> map = new HashMap<>();
        map.put("paramContent", strEncodBase64(key, data));
        map.put("secret", Base64Util.encode(RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"), publicKey)));
        map.put("fromChannel", fromChannel);
        map.put("timestamp", timestamp);
        map.put("sign", com.simple.util.Charset.getMacStr(key + "&phoneNo=" + phoneNo + "&timestamp=" + timestamp + "&fromChannel=" + fromChannel + key));
        map.put("phoneNo", phoneNo);
        map.put("api_token", token);
        System.out.println(JSONObject.toJSONString(map));
    }
}