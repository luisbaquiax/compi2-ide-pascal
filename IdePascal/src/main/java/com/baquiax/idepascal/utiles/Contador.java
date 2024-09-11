package com.baquiax.idepascal.utiles;

public class Contador {

    private static int CONTADOR;

    private Contador() {
    }

    public static int getCount() {
        return ++CONTADOR;
    }
}
