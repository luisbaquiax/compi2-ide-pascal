package com.baquiax.idepascal.backend.expresion.operacion.aritmetica;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Suma extends Sentencia {

    private Sentencia operandoDer;
    private Sentencia operandoIzq;

    public Suma(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.operandoDer = operandoDer;
        this.operandoIzq = operandoIzq;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object derecho = operandoDer.interpretar(arbol, tableSimbols);
        if(derecho instanceof ErrorPascal){
            return derecho;
        }
        Object izquierdo = operandoIzq.interpretar(arbol, tableSimbols);
        if(izquierdo instanceof ErrorPascal){
            return izquierdo;
        }
        return getResult(izquierdo, derecho);
    }

    private Object getResult(Object valueIzq, Object valueDer){
        DataType izq = this.operandoIzq.tipo.getDataType();
        DataType der = this.operandoDer.tipo.getDataType();
        switch (izq){
            case ENTERO -> {
                switch (der){
                    case ENTERO -> { this.tipo.setDataType(DataType.ENTERO); return (int) valueIzq + (int) valueDer; }
                    case REAL -> { this.tipo.setDataType(DataType.REAL); return (int) valueIzq + (double) valueDer; }
                    case BOOLEAN -> {
                        int auxi = (Boolean.parseBoolean(valueDer.toString())) ? 1 : 0;
                        this.tipo.setDataType(DataType.ENTERO);
                        return (int) valueIzq + auxi;
                    }
                    case CARACTER -> {
                        int auxi =  valueDer.toString().charAt(0);
                        this.tipo.setDataType(DataType.ENTERO);
                        return (int) valueIzq + auxi;
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "aritmética-suma:Expresion no válida, se esperaba un ENTERO o REAL.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case REAL -> {
                this.tipo.setDataType(DataType.REAL);
                switch (der){
                    case ENTERO, REAL -> { return (double) valueIzq + (double) valueDer; }
                    case BOOLEAN -> {
                        int auxi = (Boolean.parseBoolean(valueDer.toString())) ? 1 : 0;
                        return (double) valueIzq + auxi;
                    }
                    case CARACTER -> {
                        int auxi =  valueDer.toString().charAt(0);
                        return (double) valueIzq + auxi;
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "aritmética-suma: Expresion no válida, se esperaba un ENTERO o REAL.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            case BOOLEAN -> {
                int auxi = (Boolean.parseBoolean(valueIzq.toString())) ? 1 : 0;
                switch (der){
                    case ENTERO -> {
                        this.tipo.setDataType(DataType.ENTERO);
                        return auxi + (int) valueDer;
                    }
                    case REAL -> {
                        this.tipo.setDataType(DataType.REAL);
                        return auxi + (double) valueDer;
                    }
                    case BOOLEAN -> {
                        this.tipo.setDataType(DataType.ENTERO);
                        int aux2 = (Boolean.parseBoolean(valueDer.toString())) ? 1 : 0;
                        return auxi + aux2;
                    }
                    case CARACTER -> {
                        int auxi2 = valueDer.toString().charAt(0);
                        this.tipo.setDataType(DataType.ENTERO);
                        return  auxi + auxi2;
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "aritmética-suma: Expresion no válida, se esperaba un ENTERO, REAL, CHAR, STRING, BOOLEAN.",
                                this.operandoDer.line, this.operandoDer.col);
                    }
                }
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "aritmética-suma: Expresion no válida, se espera un ENTERO o REAL",
                        this.operandoIzq.line, this.operandoIzq.col);
            }
        }
    }

    @Override
    public String generarArbolLlamadas(String anterior) {
        return null;
    }
}
