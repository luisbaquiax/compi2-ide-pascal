package com.baquiax.idepascal.backend.stament;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;

public class Asignacion extends Sentencia {

    private String id;
    private Sentencia expresion;

    public Asignacion(String id, Sentencia expresion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
        this.expresion = expresion;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        Object value = this.expresion.analizar(arbol, tableSimbols);
        if (value instanceof ErrorPascal) {
            return value;
        }
        return getValueAsignacion(value, arbol, tableSimbols);
    }

    private Object getValueAsignacion(Object value, AST arbol, TableSimbols tableSimbols) {
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
        return verificarTipoDato(value, arbol, tableSimbols, buscado);
    }

    private Object verificarTipoDato(Object value, AST arbol, TableSimbols tableSimbols, Simbolo buscado) {
        DataType tipoSimbolo = buscado.getTipo().getDataType();
        if (tipoSimbolo.equals(DataType.PERSONALIZADO)) {
            return getValuePersonalizado(value, arbol, tableSimbols, buscado);
        }
        if (tipoSimbolo.equals(DataType.ARRAY)
                || tipoSimbolo.equals(DataType.RECORD)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "No se puede asignar un expresion directamente a un tipo ARRAY o RECORD",
                    this.line, this.col
            );
        }
        if (tipoSimbolo.equals(DataType.SUBRANGO)) {
            if (this.expresion.tipo.getDataType().equals(DataType.ENTERO)) {
                int aux = (int) value;
                int limiteInferior = buscado.getTipo().getIndiceMinimo();
                int limiteSuperior = buscado.getTipo().getIndiceMaximo();
                if (aux < limiteInferior || aux > limiteSuperior) {
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "El sub-rango '" + this.id + "' solo adminte valores entre: " + limiteInferior + " y " + limiteSuperior,
                            this.line, this.col
                    );
                }
            }
        }
        if (!this.expresion.tipo.getDataType().equals(buscado.getTipo().getTypeBase())) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "No se puede asignar un '" + this.expresion.tipo.getDataType()
                            + "' a una expresón '" + tipoSimbolo + "'",
                    this.line, this.col
            );
        }
        buscado.setValue(value);
        return null;
    }

    private Object getValuePersonalizado(Object value, AST arbol, TableSimbols tableSimbols, Simbolo buscado) {
        Tipo tipoPersonalizado = arbol.getTablaTipos().getTipos().get(buscado.getTipoPersonalizado());
        DataType tipoExpresion = this.expresion.tipo.getDataType();
        switch (tipoExpresion) {
            case ENTERO -> {
            }
            case REAL -> {
            }
            case CARACTER -> {
            }
            case CADENA -> {
            }
            case BOOLEAN -> {
            }
            case SUBRANGO -> {
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
}
