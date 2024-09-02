package com.baquiax.idepascal.backend.expresion.operacion.aritmetica;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Multiplicacion extends Sentencia {
    private Sentencia operandoDer;
    private Sentencia operandoIzq;

    public Multiplicacion(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.operandoDer = operandoDer;
        this.operandoIzq = operandoIzq;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        Object derecho = operandoDer.analizar(arbol, tableSimbols);
        if(derecho instanceof ErrorPascal){
            return derecho;
        }
        Object izquierdo = operandoIzq.analizar(arbol, tableSimbols);
        if(izquierdo instanceof ErrorPascal){
            return izquierdo;
        }
        return getResult(izquierdo, derecho);
    }

    private Object getResult(Object izquierdo, Object derecho){
        DataType izq = this.operandoIzq.tipo.getDataType();
        DataType der = this.operandoDer.tipo.getDataType();
        switch (izq){
            case ENTERO -> {
                switch (der){
                    case ENTERO -> { this.tipo.setDataType(DataType.ENTERO); return (int) izquierdo * (int) derecho; }
                    case REAL -> { this.tipo.setDataType(DataType.REAL); return (int) izquierdo * (double) derecho; }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "aritmética-multi: Expresion no válida, se esperaba un ENTERO o REAL.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case REAL -> {
                switch (der){
                    case ENTERO -> { this.tipo.setDataType(DataType.ENTERO); return (double) izquierdo * (int) derecho; }
                    case REAL -> { this.tipo.setDataType(DataType.REAL); return (double) izquierdo * (double) derecho; }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "aritmética-multi: Expresion no válida, se esperaba un ENTERO o REAL.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "aritmética-multi: Expresion no válida, se espera un ENTERO o REAL",
                        this.operandoIzq.line, this.operandoIzq.col);
            }
        }
    }
}
