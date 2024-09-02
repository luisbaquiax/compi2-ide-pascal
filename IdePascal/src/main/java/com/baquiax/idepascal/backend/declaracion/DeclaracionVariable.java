package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class DeclaracionVariable extends Sentencia {

    private List<Variable> variables;
    public DeclaracionVariable(List<Variable> variables, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.variables = variables;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        for(Variable va: variables){
            var value = va.analizar(arbol, tableSimbols);
            if(value instanceof ErrorPascal errorPascal){
                arbol.getErrores().add(errorPascal);
            }
        }
        return null;
    }
}
