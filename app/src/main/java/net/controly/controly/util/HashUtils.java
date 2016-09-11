package net.controly.controly.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains an assortments of util methods for hashing.
 */
public class HashUtils {

    /**
     * This method is used to calculate the MD5 hash of a given string.
     *
     * @param plainText The string to hash.
     * @return The MD5 of the given string
     */
    public static String MD5(String plainText) {
        String digest = null;

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(plainText.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return digest;
    }

}
