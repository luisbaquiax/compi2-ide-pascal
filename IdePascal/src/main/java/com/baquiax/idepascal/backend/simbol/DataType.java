package com.baquiax.idepascal.backend.simbol;

public enum DataType {
    ENTERO("integer"),
    REAL("real"),
    BOOLEAN("boolean"),
    CARACTER("char"),
    CADENA("string"),
    ARRAY("array"),
    RECORD("record"),
    SUBRANGO("sub-rango"),
    ANY("any");

    public String nombre;
    DataType(String nombre){
       this.nombre = nombre;
    }
}
