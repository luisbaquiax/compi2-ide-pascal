package com.baquiax.idepascal.backend.funcion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.sentencias.SentenciaBreak;
import com.baquiax.idepascal.backend.sentencias.SentenciaContinue;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Funtion extends Sentencia {

    private String id;
    private List<Parametro> parametros;

    private Sentencia declaracionVariables;
    private List<Sentencia> sentencias;
    private List<HashMap> params;

    public Funtion(String id, List<Parametro> parametros, Tipo tipo, Sentencia declaracionVariables, List<Sentencia> sentencias, int line, int col) {
        super(tipo, line, col);
        this.parametros = parametros;
        this.sentencias = sentencias;
        this.params = new ArrayList<>();
        this.declaracionVariables = declaracionVariables;
        this.id = id;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        if (this.declaracionVariables != null) {
            Object d = this.declaracionVariables.interpretar(arbol, tableSimbols);
            if (d instanceof ErrorPascal) {
                return d;
            }
        }
        for (Sentencia s : this.sentencias) {
            Object value = s.interpretar(arbol, tableSimbols);
            if (value instanceof ErrorPascal e) {
                arbol.getErrores().add(e);
            }
            if (value instanceof SentenciaContinue || value instanceof SentenciaBreak) {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "La instruccion BREA/CONTINUE solo debe instanciarse en un ciclo.",
                        s.line, s.col);
            }
            //validamos si es un asignacion para el return
        }
        return null;
    }

    public void setParams() {
        for (Parametro parametro : this.parametros) {
            for (String id : parametro.getIds()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("tipo", parametro);
                params.add(map);
            }
        }
    }

    public String getId() {
        return id;
    }

    public List<Parametro> getParametros() {
        return parametros;
    }

    public List<HashMap> getParams() {
        return params;
    }
}
