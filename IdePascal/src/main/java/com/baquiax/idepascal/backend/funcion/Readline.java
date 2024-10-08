package com.baquiax.idepascal.backend.funcion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class Readline extends Sentencia {

    private List<Sentencia> expresiones;
    public Readline(List<Sentencia> expresiones, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.expresiones = expresiones;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        for(Sentencia ex: expresiones){
            var value = ex.interpretar(arbol, tableSimbols);
            if(value instanceof ErrorPascal){
                return value;
            }
        }
        return null;
    }

    @Override
    public String generarArbolLlamadas(String anterior) {
        return null;
    }
}
