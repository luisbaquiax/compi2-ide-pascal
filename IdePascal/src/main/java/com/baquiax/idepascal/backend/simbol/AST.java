package com.baquiax.idepascal.backend.simbol;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AST {
    private List<Sentencia> sentecias;
    private List<ErrorPascal> errores;
    private TableSimbols global;
    private TableTypes tablaTipos;
    private List<Sentencia> funciones;
    private List<Sentencia> records;

    private HashMap<String, Object> reporteSimbolos;

    private String log;

    public AST(List<Sentencia> sentecias){
        this.sentecias = sentecias;
        this.errores = new ArrayList<>();
        this.global = new TableSimbols();
        this.funciones = new ArrayList<>();
        this.records = new ArrayList<>();
        this.tablaTipos = new TableTypes();
        this.reporteSimbolos = new HashMap<>();
    }

    public boolean tipoAgregado(Tipo tipo){
        Tipo buscado = tablaTipos.getTipos().get(tipo.getId().toLowerCase());
        if(buscado == null){
            tipo.setAmbito(global.getNombre());
            tablaTipos.getTipos().put(tipo.getId().toLowerCase(), tipo);
            return  true;
        }
        return false;
    }

    public HashMap<String, Object> getReporteSimbolos() {
        return reporteSimbolos;
    }

    public TableTypes getTablaTipos() {
        return tablaTipos;
    }

    public List<Sentencia> getSentecias() {
        return sentecias;
    }

    public void setLog(String log){
        this.log = log;
    }

    public String getLog(){
        return log;
    }

    public void setSentecias(List<Sentencia> sentecias) {
        this.sentecias = sentecias;
    }

    public List<ErrorPascal> getErrores() {
        return errores;
    }

    public void setErrores(List<ErrorPascal> errores) {
        this.errores = errores;
    }

    public TableSimbols getGlobal() {
        return global;
    }

    public void setGlobal(TableSimbols global) {
        this.global = global;
    }

    public List<Sentencia> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<Sentencia> funciones) {
        this.funciones = funciones;
    }

    public List<Sentencia> getRecords() {
        return records;
    }

    public void setRecords(List<Sentencia> records) {
        this.records = records;
    }
}
