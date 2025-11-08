package br.com.alura.AluraFake.core.util;

import java.util.Random;

public class PasswordGeneration{

    public static String generatePassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }
}
