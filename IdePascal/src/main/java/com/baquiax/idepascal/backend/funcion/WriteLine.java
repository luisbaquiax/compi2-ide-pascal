package com.baquiax.idepascal.backend.funcion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class WriteLine extends Sentencia {

    List<Sentencia> expresiones;

    public WriteLine(List<Sentencia> expresiones, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.expresiones = expresiones;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        StringBuilder aux = new StringBuilder();
        for (Sentencia ex : expresiones) {
            Object value = ex.interpretar(arbol, tableSimbols);
            if (value instanceof ErrorPascal) {
                return value;
            }
            aux.append(value);
        }
        arbol.setLog(aux.toString().toString());
        return null;
    }
}
