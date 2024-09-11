package com.baquiax.idepascal.backend.funcion;

import com.baquiax.idepascal.backend.simbol.DataType;

import java.util.List;

public class Parametro {
    private List<String> ids;
    private DataType dataType;
    private String personalizado;

    public Parametro(List<String> ids, DataType dataType, String personalizado) {
        this.ids = ids;
        this.dataType = dataType;
        this.personalizado = personalizado;
    }

    public String getPersonalizado() {
        return personalizado;
    }

    public List<String> getIds() {
        return ids;
    }

    public DataType getDataType() {
        return dataType;
    }
}
