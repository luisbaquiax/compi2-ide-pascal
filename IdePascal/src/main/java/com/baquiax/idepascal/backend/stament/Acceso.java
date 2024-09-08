package com.baquiax.idepascal.backend.stament;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;

public class Acceso extends Sentencia{
    public Acceso(Tipo tipo, int line, int col) {
        super(tipo, line, col);
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        return null;
    }
}
