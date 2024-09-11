package com.baquiax.idepascal.backend.stament;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;

import java.util.List;
import java.util.Objects;

public class AsignacionArray extends Sentencia {

    private String id;
    private Sentencia index;
    private Sentencia expresion;

    public AsignacionArray(String id, Sentencia index, Sentencia expresion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
        this.index = index;
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object valueIndex = this.index.interpretar(arbol, tableSimbols);
        if (valueIndex instanceof ErrorPascal) {
            return valueIndex;
        }

        if (!this.index.tipo.getDataType().equals(DataType.ENTERO) && !this.index.tipo.getDataType().equals(DataType.BOOLEAN)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "Error > el índice del un array debe ser entero",
                    this.line, this.col
            );
        }

        Object valueExpresion = this.expresion.interpretar(arbol, tableSimbols);
        if (valueExpresion instanceof ErrorPascal) {
            return valueExpresion;
        }
        Simbolo simbolo = tableSimbols.buscarVariable(this.id);
        if (simbolo == null) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "La variable '" + this.id + "' no está definido.",
                    this.line, this.col
            );
        }
        DataType tipoSimbolo = simbolo.getTipo().getDataType();
        DataType tipoBaseSimbolo = simbolo.getTipo().getTypeBase();
        DataType tipoExpresion = this.expresion.tipo.getDataType();
        switch (tipoSimbolo) {
            case ARRAY -> {
                int indice;
                if (this.index.tipo.getDataType().equals(DataType.BOOLEAN)) {
                    indice = Boolean.parseBoolean(valueIndex.toString()) ? 1 : 0;
                } else {
                    indice = (int) valueIndex;
                }
                int min = simbolo.getTipo().getIndiceMinimo();
                int max = simbolo.getTipo().getIndiceMaximo();
                if (indice < min || indice > max) {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "Error: índice fuera de rango.",
                            this.index.col, this.index.col
                    );
                } else {
                    switch (tipoBaseSimbolo) {
                        case ENTERO -> {
                            switch (tipoExpresion) {
                                case ENTERO -> {
                                    List<Object> values = (List<Object>) simbolo.getValue();
                                    values.remove(indice);
                                    values.add(indice, valueExpresion);
                                    simbolo.setValue(values);
                                }
                                case BOOLEAN -> {
                                    setValueBoolean(valueExpresion, simbolo, indice);
                                }
                                default -> {
                                    return new ErrorPascal(
                                            TipoError.SEMANTICO.name(),
                                            "Error > expresion invalida, se esperaba: ENTERO, BOOLEAN",
                                            this.index.col, this.index.col
                                    );
                                }
                            }
                        }
                        case REAL -> {
                            switch (tipoExpresion) {
                                case ENTERO, REAL -> {
                                    List<Object> values = (List<Object>) simbolo.getValue();
                                    values.remove(indice);
                                    values.add(indice, valueExpresion);
                                    simbolo.setValue(values);
                                }
                                case BOOLEAN -> {
                                    setValueBoolean(valueExpresion, simbolo, indice);
                                }
                                default -> {
                                    return new ErrorPascal(
                                            TipoError.SEMANTICO.name(),
                                            "Error > expresion invalida, se esperaba: ENTERO, BOOLEAN",
                                            this.index.col, this.index.col
                                    );
                                }
                            }
                        }
                        case CADENA -> {
                            if (tipoExpresion.equals(DataType.CADENA)) {
                                List<Object> values = (List<Object>) simbolo.getValue();
                                values.remove(indice);
                                values.add(indice, valueExpresion);
                                simbolo.setValue(values);
                            } else {
                                return new ErrorPascal(
                                        TipoError.SEMANTICO.name(),
                                        "Error > expresion invalida, se esperaba: CADENA",
                                        this.index.col, this.index.col
                                );
                            }
                        }
                        case BOOLEAN -> {
                            if (tipoExpresion.equals(DataType.BOOLEAN)) {
                                List<Object> values = (List<Object>) simbolo.getValue();
                                values.remove(indice);
                                values.add(indice, valueExpresion);
                                simbolo.setValue(values);
                            } else {
                                return new ErrorPascal(
                                        TipoError.SEMANTICO.name(),
                                        "Error > expresion invalida, se esperaba: BOOLEAN",
                                        this.expresion.col, this.expresion.col
                                );
                            }
                        }
                        case PERSONALIZADO -> {
                            Tipo tablaTipo = arbol.getTablaTipos().getTipos().get(simbolo.getTipoPersonalizado());
                            DataType typeConfigurado = tablaTipo.getDataType();
                            switch (typeConfigurado) {
                                case ENTERO -> {
                                    switch (tipoExpresion) {
                                        case ENTERO -> {
                                            List<Object> values = (List<Object>) simbolo.getValue();
                                            values.remove(indice);
                                            values.add(indice, valueExpresion);
                                            simbolo.setValue(values);
                                        }
                                        case BOOLEAN -> {
                                            setValueBoolean(valueExpresion, simbolo, indice);
                                        }
                                        default -> {
                                            return new ErrorPascal(
                                                    TipoError.SEMANTICO.name(),
                                                    "Error > expresion invalida, se esperaba: ENTERO, REAL, CADENA, CARACTER, BOOLEAN",
                                                    this.index.col, this.index.col
                                            );
                                        }
                                    }
                                }
                                case REAL -> {
                                    switch (tipoExpresion) {
                                        case ENTERO, REAL -> {
                                            List<Object> values = (List<Object>) simbolo.getValue();
                                            values.remove(indice);
                                            values.add(indice, valueExpresion);
                                            simbolo.setValue(values);
                                        }
                                        case BOOLEAN -> {
                                            setValueBoolean(valueExpresion, simbolo, indice);
                                        }
                                        default -> {
                                            return new ErrorPascal(
                                                    TipoError.SEMANTICO.name(),
                                                    "Error > expresion invalida, se esperaba: ENTERO, REAL, CADENA, CARACTER, BOOLEAN",
                                                    this.index.col, this.index.col
                                            );
                                        }
                                    }
                                }
                                case CADENA -> {
                                    if (tipoExpresion.equals(DataType.CADENA)) {
                                        List<Object> values = (List<Object>) simbolo.getValue();
                                        values.remove(indice);
                                        values.add(indice, valueExpresion);
                                        simbolo.setValue(values);
                                    } else {
                                        return new ErrorPascal(
                                                TipoError.SEMANTICO.name(),
                                                "Error > expresion invalida, se esperaba: CADENA",
                                                this.index.col, this.index.col
                                        );
                                    }
                                }
                                case BOOLEAN -> {
                                    if (tipoExpresion.equals(DataType.BOOLEAN)) {
                                        List<Object> values = (List<Object>) simbolo.getValue();
                                        values.remove(indice);
                                        values.add(indice, valueExpresion);
                                        simbolo.setValue(values);
                                    } else {
                                        return new ErrorPascal(
                                                TipoError.SEMANTICO.name(),
                                                "Error > expresion invalida, se esperaba: BOOLEAN",
                                                this.expresion.col, this.expresion.col
                                        );
                                    }
                                }
                                case SUBRANGO -> {
                                    switch (tipoExpresion) {
                                        case ENTERO -> {
                                            int valueAuxi = (int) valueExpresion;
                                            if (valueAuxi < min || valueAuxi > max) {
                                                return new ErrorPascal(
                                                        TipoError.SEMANTICO.name(),
                                                        "Error > la variable subrango '" + this.id + "' soporta valores entre: " + min + " y " + max,
                                                        this.expresion.col, this.expresion.col
                                                );
                                            } else {
                                                this.tipo.setDataType(DataType.ENTERO);
                                                List<Object> values = (List<Object>) simbolo.getValue();
                                                values.remove(indice);
                                                values.add(indice, valueAuxi);
                                                simbolo.setValue(values);
                                            }
                                        }
                                        case BOOLEAN -> {
                                            int valueAuxi = Boolean.parseBoolean(valueExpresion.toString()) ? 1 : 0;
                                            if (valueAuxi < min || valueAuxi > max) {
                                                return new ErrorPascal(
                                                        TipoError.SEMANTICO.name(),
                                                        "Error > la variable subrango '" + this.id + "' soporta valores entre: " + min + " y " + max,
                                                        this.expresion.col, this.expresion.col
                                                );
                                            } else {
                                                this.tipo.setDataType(DataType.ENTERO);
                                                List<Object> values = (List<Object>) simbolo.getValue();
                                                values.remove(indice);
                                                values.add(indice, valueAuxi);
                                                simbolo.setValue(values);
                                            }
                                        }
                                        default -> {
                                            return new ErrorPascal(
                                                    TipoError.SEMANTICO.name(),
                                                    "Error > expresion invalida, se esperaba: ENTERO, BOOLEAN",
                                                    this.expresion.col, this.expresion.col
                                            );
                                        }
                                    }
                                }
                                default -> {
                                    return new ErrorPascal(
                                            TipoError.SEMANTICO.name(),
                                            "Error > Se esperaba: ENTERO, REAL, CADENA, CARACTER, BOOLEAN, ",
                                            this.line, this.col
                                    );
                                }
                            }
                        }
                        default -> {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "Error > se esperaba: ENTERO, CARACTER, CADENA, REAL, BOOLEAN",
                                    this.line, this.col
                            );
                        }
                    }
                }
            }
            case PERSONALIZADO -> {
                Tipo tablaTipo = arbol.getTablaTipos().getTipos().get(simbolo.getTipoPersonalizado());
                DataType tipoTabla = tablaTipo.getDataType();
                DataType tipoBaseTabla = tablaTipo.getTypeBase();
                switch (tipoTabla) {
                    case ARRAY -> {
                        int indice;
                        if (tipoExpresion.equals(DataType.BOOLEAN)) {
                            indice = Boolean.parseBoolean(valueExpresion.toString()) ? 1 : 0;
                        } else {
                            indice = (int) valueExpresion;
                        }
                        int min = simbolo.getTipo().getIndiceMinimo();
                        int max = simbolo.getTipo().getIndiceMaximo();
                        if (indice < min || indice > max) {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "Error: índice fuera de rango.",
                                    this.index.col, this.index.col
                            );
                        }
                        switch (tipoBaseTabla) {
                            case ENTERO -> {
                                switch (tipoExpresion) {
                                    case ENTERO -> {
                                        this.tipo.setDataType(DataType.ENTERO);
                                        List<Object> values = (List<Object>) simbolo.getValue();
                                        values.remove(indice);
                                        values.add(indice, valueExpresion);
                                        simbolo.setValue(values);
                                    }
                                    case BOOLEAN -> {
                                        int auxBolean = Boolean.parseBoolean(valueExpresion.toString()) ? 1 : 0;
                                        List<Object> values = (List<Object>) simbolo.getValue();
                                        values.remove(indice);
                                        values.add(indice, valueExpresion);
                                        simbolo.setValue(values);
                                    }
                                    default -> {
                                        return new ErrorPascal(
                                                TipoError.SEMANTICO.name(),
                                                "Se esperaba un ENTERO.",
                                                this.expresion.line, this.expresion.col
                                        );
                                    }
                                }
                            }
                            case REAL -> {
                                switch (tipoExpresion) {
                                    case ENTERO, REAL -> {
                                        List<Object> values = (List<Object>) simbolo.getValue();
                                        values.remove(indice);
                                        values.add(indice, valueExpresion);
                                        simbolo.setValue(values);
                                    }
                                    case BOOLEAN -> {
                                        int auxBolean = Boolean.parseBoolean(valueExpresion.toString()) ? 1 : 0;
                                        List<Object> values = (List<Object>) simbolo.getValue();
                                        values.remove(indice);
                                        values.add(indice, valueExpresion);
                                        simbolo.setValue(values);
                                    }
                                    default -> {
                                        return new ErrorPascal(
                                                TipoError.SEMANTICO.name(),
                                                "Se esperaba un ENTERO, REAL, BOOLEAN",
                                                this.expresion.line, this.expresion.col
                                        );
                                    }
                                }
                            }
                            case CADENA -> {
                                if (tipoExpresion.equals(DataType.CADENA)) {
                                    List<Object> values = (List<Object>) simbolo.getValue();
                                    values.remove(indice);
                                    values.add(indice, valueExpresion);
                                    simbolo.setValue(values);
                                } else {
                                    return new ErrorPascal(
                                            TipoError.SEMANTICO.name(),
                                            "Se esperaba una CADENA",
                                            this.expresion.line, this.expresion.col
                                    );
                                }
                            }
                            default -> {
                                return new ErrorPascal(
                                        TipoError.SEMANTICO.name(),
                                        "El array '" + this.id + "' debe ser de tipo: ENTERO, REAL, CADENA, BOOLEAN.",
                                        this.line, this.col
                                );
                            }
                        }
                    }
                    default -> {
                        return new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "La variable '" + this.id + "' no está definida como un array.",
                                this.line, this.col
                        );
                    }
                }
            }
            default -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "La variable '" + this.id + "' no es un array.",
                        this.line, this.col
                );
            }
        }
        return null;
    }

    @Override
    public String generarArbolLlamadas(String anterior) {
        return null;
    }

    private void setValueBoolean(Object valueExpresion, Simbolo simbolo, int indice) {
        int aux = Boolean.parseBoolean(valueExpresion.toString()) ? 1 : 0;
        List<Object> values = (List<Object>) simbolo.getValue();
        values.remove(indice);
        values.add(indice, aux);
        simbolo.setValue(values);
    }
}
