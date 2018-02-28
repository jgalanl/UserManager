package com.example.jesusgalan.usermanager;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA {
    String sha(String string){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(string.getBytes());
            byte[] cadena = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte aCadena : cadena) {
                String hex = Integer.toHexString(0xFF & aCadena);
                if (hex.length() == 1) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}


