package com.em.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

public class AESUtil {
    private final static String AES = "AES";
    private final static String ENCODE = "UTF-8";
    private final static String defaultKey = "EMBigDataSystem";

    // 密钥偏移量
    private static final String OFFSET = "1234567890123456";

    // 算法模式-CBC加密块链
    private static final String ALGORITHM_MODE = "CBC";

    // 补码方式
    private static final String COMPLEMENT_MODE = "PKCS5Padding";

    // 16位默认密钥
    private final static String KEY_16BYTE = "EMBigDataSystem1";

    /**
     * 加密函数
     *
     * @param content 加密的内容
     * @return 返回二进制字符数组
     * @throws Exception
     */
    public static String enCrypt(String content) throws Exception {
        KeyGenerator keygen;
        SecretKey desKey;
        Cipher c;
        byte[] cByte;
        String str = content;

        keygen = KeyGenerator.getInstance(AES);

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(defaultKey.getBytes());

        keygen.init(128, secureRandom);

        desKey = keygen.generateKey();
        c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, desKey);
        cByte = c.doFinal(str.getBytes(ENCODE));

        return parseByte2HexStr(cByte);
    }

    /**
     * 解密函数
     *
     * @param src 加密过的二进制字符数组
     * @return
     * @throws Exception
     */
    public static String deCrypt(String src) throws Exception {
        byte[] v = parseHexStr2Byte(src);
        KeyGenerator keygen;
        SecretKey desKey;
        Cipher c;
        byte[] cByte;

        keygen = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(defaultKey.getBytes());

        keygen.init(128, secureRandom);
        desKey = keygen.generateKey();
        c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, desKey);
        cByte = c.doFinal(v);
        return new String(cByte, ENCODE);
    }

    /**
     * 2进制转化成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    /**
     * AES加密
     *
     * @param content 明文
     * @param key·    密钥 null为默认
     * @param mode    展示方式 0-base64 1-16进制串大写
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key, int mode) throws Exception {

        key = key == null ? KEY_16BYTE : key;
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);

        byte[] bytes = cipher.doFinal(content.getBytes());

        switch (mode) {
            case 1:
                return parseByte2HexStr(bytes);

            case 0:
            default:
                return new Base64().encodeAsString(bytes);
        }
    }

    /**
     * AES解密
     *
     * @param content 密文
     * @param key     密钥 null为默认
     * @param mode    展示方式 0-base64 1-16进制串大写
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String key, int mode) throws Exception {

        key = key == null ? KEY_16BYTE : key;
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);

        byte[] bytes;

        switch (mode) {
            case 1:
                bytes = cipher.doFinal(parseHexStr2Byte(content));
                return new String(bytes);

            case 0:
            default:
                bytes = cipher.doFinal(new Base64().decode(content));
                return new String(bytes);
        }
    }

    private static Cipher getCipher(String key, int opmode) throws Exception {
        Key keySpec = new SecretKeySpec(key.getBytes(), AES);
        IvParameterSpec ivSpec = new IvParameterSpec(OFFSET.getBytes());
        Cipher cipher = Cipher.getInstance(AES + "/" + ALGORITHM_MODE + "/" + COMPLEMENT_MODE);
        cipher.init(opmode, keySpec, ivSpec);
        return cipher;
    }

}
