package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class DeclaracionConstante extends Sentencia {

    private List<Constante> constantes;

    public DeclaracionConstante(List<Constante> constantes, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.constantes = constantes;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        for (Constante cons : constantes) {
            var value = cons.interpretar(arbol, tableSimbols);
            if(value instanceof ErrorPascal errorPascal){
                arbol.getErrores().add(errorPascal);
            }
        }
        return null;
    }
}
