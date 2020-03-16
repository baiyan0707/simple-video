package com.simple.util;

import cn.hutool.extra.qrcode.QrCodeUtil;

import java.awt.image.BufferedImage;
import java.util.Base64;

/**
 * Base64转码工具类
 * 对字节数据做base64转码
 */
public class Base64Util {

    /**
     * 将字节数组编码为字符串
     */
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 将字符串解码为字节数组
     */
    public static byte[] decode(String str) {
        // 处理换行符
        str = str.replace("\n", "");
        return Base64.getDecoder().decode(str);
    }

}
