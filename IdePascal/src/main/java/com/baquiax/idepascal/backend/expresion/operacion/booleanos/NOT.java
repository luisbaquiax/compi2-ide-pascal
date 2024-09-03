package com.baquiax.idepascal.backend.expresion.operacion.booleanos;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class NOT extends Sentencia {

    private Sentencia expresion;

    public NOT(Sentencia expresion, int line, int col) {
        super(new Tipo(DataType.BOOLEAN), line, col);
        this.expresion = expresion;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        Object value = expresion.analizar(arbol, tableSimbols);
        if (value instanceof ErrorPascal) {
            return value;
        }
        return getResult(value);
    }

    private Object getResult(Object value) {
        DataType tip = this.expresion.tipo.getDataType();
        if (tip.equals(DataType.BOOLEAN)) {
            return !Boolean.parseBoolean(value.toString());
        }
        return new ErrorPascal(
                TipoError.SEMANTICO.name(),
                "relacional-not: Error semántico, expresión inválida. Se esperaba un BOOLEAN. valor: " + value,
                this.expresion.line, this.expresion.line);
    }
}
