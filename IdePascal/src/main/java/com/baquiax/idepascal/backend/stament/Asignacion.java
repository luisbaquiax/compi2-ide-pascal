package com.baquiax.idepascal.backend.stament;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;

public class Asignacion extends Sentencia {

    private String id;
    private Sentencia expresion;

    private Object value;

    public Asignacion(String id, Sentencia expresion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object valueExpresion = this.expresion.interpretar(arbol, tableSimbols);
        if (valueExpresion instanceof ErrorPascal) {
            return valueExpresion;
        }
        return getValueAsignacion(valueExpresion, arbol, tableSimbols);
    }

    private Object getValueAsignacion(Object valueExpresion, AST arbol, TableSimbols tableSimbols) {
        Simbolo buscado = tableSimbols.buscarVariable(this.id);
        if (buscado == null) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "La variable '" + this.id + "' no está definido.",
                    this.line, this.col
            );
        }
        if (!buscado.isMutable()) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El id '" + this.id + "' no es una variable.",
                    this.line, this.col
            );
        }
        return verificarTipoDato(valueExpresion, arbol, tableSimbols, buscado);
    }

    private Object verificarTipoDato(Object valueExpresion, AST arbol, TableSimbols tableSimbols, Simbolo buscado) {
        DataType tipoSimbolo = buscado.getTipo().getDataType();

        switch (tipoSimbolo) {
            case PERSONALIZADO -> {
                return getValuePersonalizado(valueExpresion, arbol, tableSimbols, buscado);
            }
            case ENTERO, REAL, CADENA -> {
                this.value = valueExpresion;
                this.tipo.setDataType(this.expresion.tipo.getDataType());
                buscado.setValue(valueExpresion);
            }
            case CARACTER -> {
                if (this.expresion.tipo.getDataType().equals(DataType.CADENA)) {
                    if (valueExpresion.toString().length() > 1) {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "Error > expresion invalida, se esperaba: un CARACTER",
                                this.expresion.col, this.expresion.col
                        );
                    }
                    buscado.setValue(valueExpresion);
                } else {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "Error > expresion invalida, se esperaba: un CARACTER",
                            this.expresion.col, this.expresion.col
                    );
                }
            }
            case SUBRANGO -> {
                this.tipo.setDataType(DataType.ENTERO);
                this.tipo.setTypeBase(DataType.ENTERO);
                this.value = valueExpresion;
                if (this.expresion.tipo.getDataType().equals(DataType.ENTERO)) {
                    int aux = (int) valueExpresion;
                    int limiteInferior = buscado.getTipo().getIndiceMinimo();
                    int limiteSuperior = buscado.getTipo().getIndiceMaximo();
                    if (aux < limiteInferior || aux > limiteSuperior) {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "El sub-rango '" + this.id + "' solo adminte valores entre: " + limiteInferior + " y " + limiteSuperior,
                                this.line, this.col
                        );
                    } else {
                        buscado.setValue(valueExpresion);
                    }
                } else if (this.expresion.tipo.getDataType().equals(DataType.BOOLEAN)) {
                    if (Boolean.parseBoolean(valueExpresion.toString())) {
                        buscado.setValue(1);
                    } else {
                        buscado.setValue(0);
                    }
                }
            }
            case BOOLEAN -> {
                if (this.expresion.tipo.getDataType().equals(DataType.BOOLEAN)) {
                    this.value = Boolean.parseBoolean(valueExpresion.toString()) ? 1 : 0;
                    this.tipo.setDataType(DataType.ENTERO);
                    buscado.setValue(valueExpresion);
                } else {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "No se puede asignar un '" + this.expresion.tipo.getTypeBase()
                                    + "' a una expresón '" + tipoSimbolo + "'",
                            this.line, this.col
                    );
                }
            }
            case ARRAY -> {
                if (buscado.getTipo().getTypeBase().equals(DataType.CARACTER)) {
                    if (this.expresion.tipo.getDataType().equals(DataType.CADENA)) {
                        int max = buscado.getTipo().getIndiceMaximo();
                        StringBuilder aux = new StringBuilder();
                        if (valueExpresion.toString().length() > max) {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "Error > tamaño fuera de rango en el array de CARACTER. Tamaño máximo soportado: " + max,
                                    this.expresion.col, this.expresion.col
                            );
                        } else {
                            for (int i = 0; i < valueExpresion.toString().length(); i++) {
                                aux.append(valueExpresion.toString().charAt(i));
                            }
                        }

                        this.tipo.setDataType(this.expresion.tipo.getDataType());
                        buscado.setValue(valueExpresion);
                    }
                } else {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "Error > expresion invalida, se esperaba: array de CARACTER",
                            this.expresion.col, this.expresion.col
                    );
                }

            }
            case RECORD -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "No se puede asignar un expresion directamente a un tipo o RECORD",
                        this.line, this.col
                );
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "No se puede asignar un '" + this.expresion.tipo.getTypeBase()
                                + "' a una expresón '" + buscado.getTipo().getTypeBase() + "'",
                        this.line, this.col
                );
            }
        }
        return null;
    }

    private Object getValuePersonalizado(Object value, AST arbol, TableSimbols tableSimbols, Simbolo buscado) {
        Tipo tablaTipo = arbol.getTablaTipos().getTipos().get(buscado.getTipoPersonalizado());
        DataType tipoExpresion = this.expresion.tipo.getDataType();
        switch (tablaTipo.getDataType()) {
            case ENTERO -> {
                switch (tipoExpresion) {
                    case ENTERO -> {
                        this.tipo.setDataType(tipoExpresion);
                        buscado.setValue(value);
                    }
                    case BOOLEAN -> {
                        int aux = Boolean.parseBoolean(value.toString()) ? 1 : 0;
                        this.tipo.setDataType(DataType.ENTERO);
                        buscado.setValue(aux);
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "Error-asignacion > Expresion invalida, se esperaba: ENTERO, BOOLEAN",
                                this.line, this.col
                        );
                    }
                }

            }
            case REAL -> {
                switch (tipoExpresion) {
                    case ENTERO, REAL -> {
                        this.tipo.setDataType(tipoExpresion);
                        buscado.setValue(value);
                    }
                    case BOOLEAN -> {
                        int aux = Boolean.parseBoolean(value.toString()) ? 1 : 0;
                        this.tipo.setDataType(DataType.ENTERO);
                        buscado.setValue(aux);
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "Error-asignacion > Expresion invalida, se esperaba: ENTERO, BOOLEAN, REAL",
                                this.line, this.col
                        );
                    }
                }
            }
            case CADENA -> {
                if (tipoExpresion.equals(DataType.CADENA)) {
                    buscado.setValue(value);
                } else {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "Error-asignacion > Expresion invalida, se esperaba: CADENA",
                            this.line, this.col
                    );
                }
            }
            case CARACTER -> {
                if (tipoExpresion.equals(DataType.CADENA)) {
                    if (value.toString().length() > 1) {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "Error-asignacion > Expresion invalida, se esperaba: CARACTER",
                                this.line, this.col
                        );
                    }
                    buscado.setValue(value);
                }
            }
            case BOOLEAN -> {
                if (tipoExpresion.equals(DataType.BOOLEAN)) {
                    this.tipo.setDataType(tipoExpresion);
                    buscado.setValue(value);
                } else {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "No se puede mezclar un '" + tipoExpresion + "' con un BOOLEAN",
                            this.line, this.col
                    );
                }
            }
            case SUBRANGO -> {
                switch (tipoExpresion) {
                    case ENTERO -> {
                        this.tipo.setDataType(DataType.ENTERO);
                        int aux = (int) value;
                        int inferior = tablaTipo.getIndiceMinimo();
                        int superior = tablaTipo.getIndiceMaximo();
                        if (aux < inferior || aux > superior) {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "El sub-rango '" + this.id + "' solo adminte valores entre: " + inferior + " y " + superior,
                                    this.line, this.col
                            );
                        } else {
                            this.value = value;
                            buscado.setValue(value);
                        }
                    }
                    case BOOLEAN -> {
                        int aux = Boolean.parseBoolean(value.toString()) ? 1 : 0;
                        this.tipo.setDataType(DataType.ENTERO);
                        buscado.setValue(aux);
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "Expresion invalida.",
                                this.line, this.col
                        );
                    }
                }
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "No se puede asignar un '" + tipoExpresion + "' a una expresón '"
                                + buscado.getTipo().getDataType() + "'",
                        this.line, this.col
                );
            }
        }
        return null;
    }

    public Object getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
