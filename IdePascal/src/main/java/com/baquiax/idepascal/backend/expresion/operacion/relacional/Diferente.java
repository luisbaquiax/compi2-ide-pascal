package com.baquiax.idepascal.backend.expresion.operacion.relacional;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Diferente extends Sentencia {

    private Sentencia operandoDer;
    private Sentencia operandoIzq;

    public Diferente(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
        super(new Tipo(DataType.BOOLEAN), line, col);
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

    private Object getResult(Object izquierdo, Object derecho) {
        DataType izq = this.operandoIzq.tipo.getDataType();
        DataType der = this.operandoDer.tipo.getDataType();
        switch (izq) {
            case ENTERO, REAL -> {
                switch (der) {
                    case ENTERO, REAL -> {
                        return Double.parseDouble(izquierdo.toString()) != Double.parseDouble(derecho.toString());
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "relacional-igual: Error semántico, expresión inválida. Se esperaba un ENTERO o REAL.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case CARACTER, CADENA -> {
                switch (der) {
                    case CARACTER, CADENA -> {
                        return !izquierdo.equals(derecho);
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "relacional-diferente: Error semántico, expresión inválida. Se esperaba un CARACTER.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case BOOLEAN -> {
                if(der.equals(DataType.BOOLEAN)){

                }
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "relacional-diferente: Error semántico, expresión inválida. Se esperaba un BOOLEANO.",
                        this.operandoDer.line, this.operandoDer.col);
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "relacional-diferente: Error semántico, expresión inválida.",
                        this.operandoIzq.line, this.operandoIzq.col);
            }
        }
    }
}
