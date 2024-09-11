package com.baquiax.idepascal.backend.expresion.accesovariable;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class AccesoArray extends Sentencia {
    private String id;
    private Sentencia indice;

    public AccesoArray(String id, Sentencia indice, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
        this.indice = indice;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object valueIndex = this.indice.interpretar(arbol, tableSimbols);
        if (valueIndex instanceof ErrorPascal) {
            return valueIndex;
        }
        if (!this.indice.tipo.getDataType().equals(DataType.ENTERO) && !this.indice.tipo.getDataType().equals(DataType.BOOLEAN)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "Error > El índice de un array debe ser entero",
                    this.line, this.col
            );
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
        DataType tipoBase = simbolo.getTipo().getTypeBase();
        int min = simbolo.getTipo().getIndiceMinimo();
        int max = simbolo.getTipo().getIndiceMaximo();
        int index;
        if (this.indice.tipo.getDataType().equals(DataType.BOOLEAN)) {
            index = Boolean.parseBoolean(valueIndex.toString()) ? 1 : 0;
        } else {
            index = (int) valueIndex;
        }
        if (index < min || index > max) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El índice está fuera de rango, rango aceptado: " + min + "-" + max,
                    this.line, this.col
            );
        } else {
            switch (tipoSimbolo) {
                case ARRAY -> {
                    switch (tipoBase) {
                        case ENTERO, REAL, CADENA, BOOLEAN, SUBRANGO -> {
                            this.tipo.setDataType(tipoBase);
                            List<Object> values = (List<Object>) simbolo.getValue();
                            return values.get(index);
                        }
                        case PERSONALIZADO -> {
                            Tipo tablaTipo = arbol.getTablaTipos().getTipos().get(simbolo.getTipoPersonalizado());
                            DataType tipoPersonalizado = tablaTipo.getDataType();
                            switch (tipoPersonalizado){
                                case ENTERO, REAL, CADENA, CARACTER, BOOLEAN, SUBRANGO -> {
                                    this.tipo.setDataType(tablaTipo.getTypeBase());
                                    List<Object> values = (List<Object>) simbolo.getValue();
                                    return values.get(index);
                                }
                                default -> {
                                    return new ErrorPascal(
                                            TipoError.SEMANTICO.name(),
                                            "La variable '" + this.id + "' no es un 'array'.",
                                            this.line, this.col
                                    );
                                }
                            }
                        }
                        default -> {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "La variable '" + this.id + "' no es un 'array'.",
                                    this.line, this.col
                            );
                        }
                    }
                }
                case PERSONALIZADO -> {
                    Tipo tablaTipo = arbol.getTablaTipos().getTipos().get(this.id);
                    DataType tipoTabla = tablaTipo.getDataType();
                    switch (tipoTabla){
                        case ENTERO, REAL, CADENA, BOOLEAN, SUBRANGO -> {
                            this.tipo.setDataType(tablaTipo.getTypeBase());
                            return simbolo.getValue();
                        }
                        default -> {
                            return new ErrorPascal(
                                    TipoError.SEMANTICO.name(),
                                    "La variable '" + this.id + "' no está definido como un 'array'.",
                                    this.line, this.col
                            );
                        }
                    }
                }
                default -> {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "La variable '" + this.id + "' no es un 'array'.",
                            this.line, this.col
                    );
                }
            }
        }
    }
}
