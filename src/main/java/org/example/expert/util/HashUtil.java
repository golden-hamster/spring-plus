package org.example.expert.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static Long generateNicknameHash(String nickname) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(nickname.getBytes(StandardCharsets.UTF_8));

            String hashHex = String.format("%032x", new BigInteger(1, hashBytes)).substring(0, 8);

            BigInteger hashNum = new BigInteger(hashHex, 16);
            return hashNum.mod(BigInteger.valueOf(4294967296L)).longValue();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to calculate nickname hash", e);
        }
    }
}
