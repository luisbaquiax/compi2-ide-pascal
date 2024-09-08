package com.baquiax.idepascal.backend.funcion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class ProgramaPrincipal extends Sentencia {
    private List<Sentencia> sentencias;
    public ProgramaPrincipal(List<Sentencia> sentencias, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.sentencias = sentencias;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        for (Sentencia sentencia: sentencias) {
            var value = sentencia.interpretar(arbol, tableSimbols);
            if(value instanceof ErrorPascal e){
                arbol.getErrores().add(e);
            }
        }
        return null;
    }
}
