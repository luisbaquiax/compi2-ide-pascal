package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class DeclaracionTipos extends Sentencia {

    private List<SentenciaTipo> tipos;
    public DeclaracionTipos(List<SentenciaTipo> tipos, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.tipos = tipos;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        for(SentenciaTipo s: tipos){
            Object sintetizado = s.interpretar(arbol, tableSimbols);
            if(sintetizado instanceof ErrorPascal errorPascal){
                arbol.getErrores().add(errorPascal);
            }
        }
        return null;
    }
}
