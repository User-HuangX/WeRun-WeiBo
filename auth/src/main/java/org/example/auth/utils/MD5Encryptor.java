package org.example.auth.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encryptor {
    public static String encryptToMD5(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组并进行MD5哈希计算
            byte[] messageDigest = md.digest(password.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                // 将每个字节转换为两位十六进制数
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不存在", e);
        }
    }
}
