package com.baquiax.idepascal.backend.expresion;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Terminal extends Sentencia {

    public Object value;

    public Terminal(Object value, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.value = value;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        return this.value;
    }
}
