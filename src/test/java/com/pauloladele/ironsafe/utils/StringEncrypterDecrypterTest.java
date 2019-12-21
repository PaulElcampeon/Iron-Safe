package com.pauloladele.ironsafe.utils;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
public class StringEncrypterDecrypterTest {

    private StringEncrypterDecrypter stringEncrypterDecrypter = new StringEncrypterDecrypter();

    @Test
    public void encrypt() {
        String stringToBeEncrypted = "dad";

        String encryptedString = stringEncrypterDecrypter.encrypt(stringToBeEncrypted);

        assertNotEquals(stringToBeEncrypted, encryptedString);
    }

    @Test
    public void decrypt() {
        String stringToBeEncrypted = "dad";

        String encryptedString = stringEncrypterDecrypter.encrypt(stringToBeEncrypted);

        String decryptedString = stringEncrypterDecrypter.decrypt(encryptedString);

        assertEquals(stringToBeEncrypted, decryptedString);
    }
}
