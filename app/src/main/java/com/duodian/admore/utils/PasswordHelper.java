package com.duodian.admore.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 */
public class PasswordHelper {
    private PasswordHelper() {
    }

    private static final String algorithm = "SHA";

    /*
     */
    public static String encodePassword(String password) {
        byte[] unEncode;
        try {
            unEncode = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.reset();
        md.update(unEncode);

        byte[] encodedPassword = md.digest();
        StringBuilder buf = new StringBuilder();
        for (byte b : encodedPassword) {
            if ((b & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(b & 0xff, 16));
        }
        return buf.toString();
    }

//    public static void main(String[] args) {
//        System.out.println(PasswordHelper.encodePassword("123456789"));
//    }
}
