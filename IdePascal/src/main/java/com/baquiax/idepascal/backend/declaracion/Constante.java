package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Constante extends Sentencia {
    private String id;
    private Sentencia expresion;
    private boolean mutable;

    public Constante(String id, Sentencia expresion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
        this.expresion = expresion;
        this.mutable = false;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        Object value = this.expresion.analizar(arbol, tableSimbols);
        if (value instanceof ErrorPascal) {
            return value;
        }
        Simbolo simbolo = new Simbolo(this.id, new Tipo(this.expresion.tipo.getDataType()), value, this.mutable);
        if (tableSimbols.agregarVariable(simbolo, arbol)) {
            return null;
        }
        return new ErrorPascal(
                TipoError.SINTACTICO.name(),
                "La constante '" + this.id + "' ya está en uso.",
                this.line, this.col);
    }
}
