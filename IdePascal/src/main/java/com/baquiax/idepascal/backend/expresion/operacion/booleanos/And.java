package com.baquiax.idepascal.backend.expresion.operacion.booleanos;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class And extends Sentencia {

    private Sentencia operandoDer;
    private Sentencia operandoIzq;

    public And(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
        super(new Tipo(DataType.BOOLEAN), line, col);
        this.operandoDer = operandoDer;
        this.operandoIzq = operandoIzq;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object derecho = operandoDer.interpretar(arbol, tableSimbols);
        if (derecho instanceof ErrorPascal) {
            return derecho;
        }
        Object izquierdo = operandoIzq.interpretar(arbol, tableSimbols);
        if (izquierdo instanceof ErrorPascal) {
            return izquierdo;
        }
        return getResult(izquierdo, derecho);
    }

    private Object getResult(Object valueIzq, Object valueDer) {
        DataType tipoIzq = this.operandoIzq.tipo.getDataType();
        DataType tipoDer = this.operandoDer.tipo.getDataType();
        if (tipoIzq.equals(DataType.BOOLEAN)) {
            if (tipoDer.equals(DataType.BOOLEAN) ) {
                return (Boolean.parseBoolean(valueIzq.toString()) && Boolean.parseBoolean(valueDer.toString()));
            }
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "relacional-and: Error semántico, expresión inválida. Se esperaba un BOOLEAN.",
                    this.operandoDer.line, this.operandoDer.col);
        }
        return new ErrorPascal(
                TipoError.SEMANTICO.name(),
                "relacional-and: Error semántico, expresión inválida. Se esperaba un BOOLEAN",
                this.operandoIzq.line, this.operandoIzq.col);
    }

    @Override
    public String generarArbolLlamadas(String anterior) {
        return null;
    }
}
