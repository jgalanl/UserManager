package com.example.jesusgalan.usermanager;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;



public class KeystoreProvider {

private static final String ANDROID_KEYSTORE="AndroidKeyStore";
private static final String TAG = KeystoreProvider.class.getSimpleName();

    static KeyStore keyStore;
    static byte [] cifrado;
//Método del manejador del amacen
public static void loadKeyStore(){
    try{
         keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

    } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
        e.printStackTrace();
    }
}

//Generamos par de claves
    public static void generateNewKeyPair (String alias, Context context) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Calendar start = Calendar.getInstance();
        Calendar end =Calendar.getInstance();
        end.add(Calendar.YEAR,1);

        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context).setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias)).setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime()).setEndDate(end.getTime()).build();
        KeyPairGenerator gen =KeyPairGenerator.getInstance("RSA", ANDROID_KEYSTORE);
        gen.initialize(spec);
        KeyPair keyPair=gen.generateKeyPair();
    }

    //Obtenemos clave privada
    public static PrivateKey loadPrivateKey(String alias) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        if (!keyStore.isKeyEntry(alias)) {
            Log.e(TAG, "Could not find key alias: " + alias);
            return null;
        }
        KeyStore.Entry entry = keyStore.getEntry(alias, null);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.e(TAG, " alias: " + alias + " is not a PrivateKey");
            return null;
        }
        return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
    }

    //Cifrar
    public static String encrypt(String alias, String contrasena) {
        try {
            Log.e(TAG, "Justo antes de coger la calve publica keystore " +keyStore);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);

            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cifrado = inCipher.doFinal(contrasena.getBytes("UTF-8"));
            Log.e(TAG,"TAmaño contra cifrada encrypt "+String.format(toBase64(cifrado)).length());
            return String.format(toBase64(cifrado));
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | BadPaddingException |
                UnsupportedEncodingException | InvalidKeyException | KeyStoreException |
                NoSuchPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
    //Descifrar
    public static String decrypt(String alias, String contrasena)  {
        try {
            try{
                keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
                keyStore.load(null);

            } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
                e.printStackTrace();
            }
            PrivateKey clavePrivada = loadPrivateKey(alias);
            /*KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            PrivateKey clavePrivada =  privateKeyEntry.getPrivateKey();
            */Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            Log.e(TAG, "Justo antes de coger la calve privada " +clavePrivada);
            inCipher.init(Cipher.DECRYPT_MODE, clavePrivada);
            Log.e(TAG,"Tamaño de contrasena "+fromBase64(contrasena).length);
            byte [] contra =fromBase64(contrasena);
            byte[] cipherText = inCipher.doFinal(contra);

            String plainrStr = new String(cipherText, "UTF-8");
            return plainrStr;
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException |
                BadPaddingException | UnsupportedEncodingException | InvalidKeyException
                | KeyStoreException | NoSuchPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }
    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

}
