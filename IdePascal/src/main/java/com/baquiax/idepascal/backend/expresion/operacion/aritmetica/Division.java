package com.baquiax.idepascal.backend.expresion.operacion.aritmetica;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Division extends Sentencia {
    private Sentencia operandoIzq;
    private Sentencia operandoDer;

    public Division(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.operandoIzq = operandoIzq;
        this.operandoDer = operandoDer;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object izq = this.operandoIzq.interpretar(arbol, tableSimbols);
        if (izq instanceof ErrorPascal) {
            return izq;
        }
        Object der = this.operandoDer.interpretar(arbol, tableSimbols);
        if (der instanceof ErrorPascal) {
            return der;
        }
        System.out.println(this.operandoDer.tipo.getDataType());
        System.out.println(der.toString().length());
        return this.getResult(izq, der);
    }

    private Object getResult(Object valueIzq, Object valueDer) {
        if (valueDer.toString().equals("0") || valueDer.toString().equals("0.0") || valueDer.toString().equals("false")) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "aritmética-division: No es permitido la divisón entre 0.",
                    this.operandoDer.line, this.operandoDer.col);
        } else {
            this.tipo.setDataType(DataType.REAL);
            DataType izq = this.operandoIzq.tipo.getDataType();
            DataType der = this.operandoDer.tipo.getDataType();
            switch (izq) {
                case ENTERO -> {
                    switch (der) {
                        case ENTERO, REAL -> {
                            return Double.parseDouble(valueIzq.toString()) / Double.parseDouble(valueDer.toString());
                        }
                        case BOOLEAN -> {
                            return Integer.parseInt(valueIzq.toString());
                        }
                        case CADENA -> {
                            String vDer = valueDer.toString();
                            if (vDer.length() > 1) {
                                return new ErrorPascal(
                                        TipoError.SEMANTICO.name(),
                                        "aritmética-division: Expresion no válida, se esperaba un ENTERO, REAL, CARACTER, BOOLEANO",
                                        this.operandoDer.line, this.operandoDer.col);
                            }
                            int auxi = (int) vDer.charAt(0);
                            return Double.parseDouble(valueIzq.toString()) / Double.parseDouble(String.valueOf(auxi));
                        }
                        case CARACTER -> {
                            int auxi = valueDer.toString().charAt(0);
                            return Double.parseDouble(valueIzq.toString()) / Double.parseDouble(String.valueOf(auxi));
                        }
                        default -> {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "aritmética-division: Expresion no válida, se esperaba un ENTERO o REAL, y no un " + this.operandoDer.tipo.getDataType().name(),
                                    this.operandoDer.line, this.operandoDer.col);
                        }
                    }
                }
                case REAL -> {
                    switch (der) {
                        case ENTERO -> {
                            return (double) valueIzq / Double.parseDouble(valueDer.toString());
                        }
                        case REAL -> {
                            return (double) valueIzq / (double) valueDer;
                        }
                        case BOOLEAN -> {
                            int auxi = (Boolean.parseBoolean(valueDer.toString())) ? 1 : 0;
                            return (double) valueIzq / Double.parseDouble(String.valueOf(auxi));
                        }
                        case CADENA -> {
                            String vDer = valueDer.toString();
                            if (vDer.length() > 1) {
                                return new ErrorPascal(
                                        TipoError.SEMANTICO.name(),
                                        "aritmética-division: Expresion no válida, se esperaba un ENTERO, REAL, CARACTER, BOOLEANO",
                                        this.operandoDer.line, this.operandoDer.col);
                            }
                            int auxi = (int) vDer.charAt(0);
                            return Double.parseDouble(valueIzq.toString()) / Double.parseDouble(String.valueOf(auxi));
                        }
                        case CARACTER -> {
                            int auxi = valueDer.toString().charAt(0);
                            return (double) valueIzq / Double.parseDouble(String.valueOf(auxi));
                        }
                        default -> {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "aritmética-division: Expresion no válida, se esperaba un ENTERO o REAL.",
                                    this.operandoDer.line, this.operandoDer.col);
                        }
                    }
                }
                case BOOLEAN -> {
                    int auxi = Boolean.parseBoolean(valueIzq.toString()) ? 1 : 0;
                    switch (der) {
                        case ENTERO -> {
                            return auxi / (int) valueDer;
                        }
                        case REAL -> {
                            return auxi / (double) valueDer;
                        }
                        case BOOLEAN -> {
                            int aux2 = 1;
                            return auxi / aux2;
                        }
                        case CADENA -> {
                            String vDer = valueDer.toString();
                            if (vDer.length() > 1) {
                                return new ErrorPascal(
                                        TipoError.SEMANTICO.name(),
                                        "aritmética-division: Expresion no válida, se esperaba un ENTERO, REAL, CARACTER, BOOLEANO",
                                        this.operandoDer.line, this.operandoDer.col);
                            }
                            int auxi2 = (int) vDer.charAt(0);
                            return Double.parseDouble(valueIzq.toString()) / Double.parseDouble(String.valueOf(auxi2));
                        }
                        case CARACTER -> {
                            int auxi2 = valueDer.toString().charAt(0);
                            return auxi / auxi2;
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
                            "aritmética-division: Expresion no válida, se espera un ENTERO o REAL",
                            this.operandoIzq.line, this.operandoIzq.col);
                }
            }
        }
    }

}
