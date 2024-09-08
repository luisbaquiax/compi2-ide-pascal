package com.baquiax.idepascal.backend.stament;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;

public abstract class Sentencia {
    public Tipo tipo;
    public int line;
    public int col;

    public Sentencia(Tipo tipo, int line, int col){
        this.tipo = tipo;
        this.line = line;
        this.col = col;
    }

    public abstract Object interpretar(AST arbol, TableSimbols tableSimbols);
}