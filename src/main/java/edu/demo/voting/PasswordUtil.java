package edu.demo.voting;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    public static String hash(String password) throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), salt, 120000, 256
        );

        byte[] hash = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .getEncoded();

        spec.clearPassword();

        return Base64.getEncoder().encodeToString(salt)
            + ":"
            + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verify(
        String password, String stored
    ) throws Exception {

        String[] parts = stored.split(":");

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expected = Base64.getDecoder().decode(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            120000,
            expected.length * 8
        );

        byte[] actual = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .getEncoded();

        spec.clearPassword();

        if (actual.length != expected.length) return false;

        int diff = 0;

        for (int i = 0; i < actual.length; i++) {
            diff |= actual[i] ^ expected[i];
        }

        return diff == 0;
    }
}