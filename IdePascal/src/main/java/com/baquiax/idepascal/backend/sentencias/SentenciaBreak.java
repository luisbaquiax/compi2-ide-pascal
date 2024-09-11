package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class SentenciaBreak extends Sentencia {
    public SentenciaBreak(int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        return null;
    }
}
