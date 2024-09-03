package com.baquiax.idepascal.backend.expresion.operacion.relacional;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class MayorIgual extends Sentencia {

    private Sentencia operandoDer;
    private Sentencia operandoIzq;

    public MayorIgual(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
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

    private Object getResult(Object valueIzq, Object valueDer){
        DataType tipoIzq = this.operandoIzq.tipo.getDataType();
        DataType tipoDer = this.operandoDer.tipo.getDataType();
        switch (tipoIzq){
            case ENTERO, REAL -> {
                switch (tipoDer){
                    case ENTERO, REAL -> { return Double.parseDouble(valueIzq.toString()) >= Double.parseDouble(valueDer.toString()); }
                    case BOOLEAN -> {
                        int auxi = Boolean.parseBoolean(valueDer.toString()) ? 1 : 0;
                        return (int) valueIzq >= auxi;
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "relacional >=: Error semántico, expresión inválida. Se esperaba un ENTERO o REAL.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case CARACTER, CADENA -> {
                switch (tipoDer){
                    case CARACTER, CADENA -> { return valueIzq.toString().length() >= valueDer.toString().length(); }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "relacional >= : Error semántico, expresión inválida. Se esperaba un CARACTER.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case BOOLEAN -> {
                int auxi = Boolean.parseBoolean(valueIzq.toString()) ? 1 : 0;
                switch (tipoDer){
                    case ENTERO, REAL -> {
                        return  auxi >= Double.parseDouble(valueDer.toString());
                    }
                    case BOOLEAN -> {
                        int aux2 = Boolean.parseBoolean(valueDer.toString()) ? 1 : 0;
                        return auxi >= aux2;
                    }
                    case CARACTER -> {
                        int aux = valueDer.toString().charAt(0);
                        return auxi >= aux;
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "relacional >=: Error semántico, expresión inválida.",
                                this.operandoIzq.line, this.operandoIzq.col);
                    }
                }
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "relacional >=: Error semántico, expresión inválida.",
                        this.operandoIzq.line, this.operandoIzq.col);
            }
        }
    }
}
