package com.example.caronas.util;

import java.util.UUID;

public class IdGenerator {
    public static String gerar() {
        return UUID.randomUUID().toString();
    }

    public static String gerar(String prefixo) {
        return prefixo + "_" + UUID.randomUUID().toString();
    }
}

