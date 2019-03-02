package com.netlify.cptwin.HashingAlgorithmBenchmark;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Argon2 {

    public static String generatePasswordHash(String password, int iterations)
    {
        char[] passwordChar = password.toCharArray();
        try {
            String salt =  getSalt();

            String hash = at.gadermaier.argon2.Argon2Factory.create()
                    .setIterations(iterations)
                    .setMemory(8) //memory usage
                    .setParallelism(1) //number of cores
                    .hash(passwordChar, salt);

            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return toHex(salt);
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
