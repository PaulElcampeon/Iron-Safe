package com.pauloladele.ironsafe.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class StringEncrypterDecrypter {

    private Cipher cipher;
    private Key aesKey;
    private String key = "secret_key431232";


    public StringEncrypterDecrypter() {

        try {
            this.aesKey = new SecretKeySpec(this.key.getBytes(), "AES");
            this.cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }


    public String encrypt(String strToEncrypt) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    public String decrypt(String strToDecrypt) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

}
