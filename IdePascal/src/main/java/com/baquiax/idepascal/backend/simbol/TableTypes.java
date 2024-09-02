package com.baquiax.idepascal.backend.simbol;

import java.util.HashMap;

public class TableTypes {
    //id, tipo de dato
    private HashMap<String, Tipo> tipos;

    public TableTypes(){
        this.tipos = new HashMap<>();
    }

    public HashMap<String, Tipo> getTipos() {
        return tipos;
    }

    public void setTipos(HashMap<String, Tipo> tipos) {
        this.tipos = tipos;
    }
}
