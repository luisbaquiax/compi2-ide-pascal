package com.baquiax.idepascal.backend.simbol;

import java.util.HashMap;

public class TableSimbols {

    private TableSimbols tablaAnterior;
    private HashMap<String, Object> tablaActual;

    /**
     * El 'nombre' sera el nombre del entorno donde se encuentre la variable.
     */
    private String nombre;
    public TableSimbols() {
        this.tablaActual = new HashMap<>();
        this.nombre = "";
    }

    public TableSimbols(TableSimbols tablaAnterior) {
        this.tablaAnterior = tablaAnterior;
        this.tablaActual = new HashMap<>();
        this.nombre = "";
    }

    /**
     * Retorna true si la variable existe y false si la variable no existe, si es falsa se agrega la
     * variable en la tabla de simbolos
     *
     * @param simbolo
     * @return
     */
    public boolean agregarVariable(Simbolo simbolo, AST arbol) {
        Simbolo buscado = (Simbolo) getTablaActual().get(simbolo.getId().toLowerCase());
        if (buscado == null) {
            Tipo idTipo = arbol.getTablaTipos().getTipos().get(simbolo.getId().toLowerCase());
            if(idTipo == null){
                getTablaActual().put(simbolo.getId().toLowerCase(), simbolo);
                arbol.getReporteSimbolos().put(simbolo.getId().toLowerCase(), simbolo);
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna nulo si la variable no existe
     * @param id
     * @return
     */
    public Simbolo buscarVariable(String id) {
        for (TableSimbols i = this; i != null; i = i.getTablaAnterior()) {
            Simbolo buscado = (Simbolo) i.getTablaActual().get(id.toLowerCase());
            if (buscado != null) {
                return buscado;
            }
        }
        return null;
    }

    public TableSimbols getTablaAnterior() {
        return tablaAnterior;
    }

    public void setTablaAnterior(TableSimbols tablaAnterior) {
        this.tablaAnterior = tablaAnterior;
    }

    public HashMap<String, Object> getTablaActual() {
        return tablaActual;
    }

    public void setTablaActual(HashMap<String, Object> tablaActual) {
        this.tablaActual = tablaActual;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
