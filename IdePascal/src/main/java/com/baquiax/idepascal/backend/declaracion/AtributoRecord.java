package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class AtributoRecord extends Sentencia {
    private List<String> ids;
    public AtributoRecord(Tipo tipo, int line, int col) {
        super(tipo, line, col);
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        return null;
    }
}
