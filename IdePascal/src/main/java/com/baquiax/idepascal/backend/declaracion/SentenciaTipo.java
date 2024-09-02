package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class SentenciaTipo extends Sentencia {

    private List<String> ids;

    private Sentencia expresion1;
    private Sentencia expresion2;

    private Tipo typeBase;

    public SentenciaTipo(List<String> ids, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
    }

    /**
     * Para crear tipos basados en subrangos
     *
     * @param ids
     * @param expresion1
     * @param expresion2
     * @param tipo
     * @param line
     * @param col
     */
    public SentenciaTipo(List<String> ids, Sentencia expresion1, Sentencia expresion2, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
        this.expresion1 = expresion1;
        this.expresion2 = expresion2;
    }

    public SentenciaTipo(List<String> ids, Sentencia expresion1, Sentencia expresion2, Tipo tipo, Tipo tipoDato, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
        this.expresion1 = expresion1;
        this.expresion2 = expresion2;
        this.typeBase = tipoDato;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        switch (this.tipo.getDataType()) {
            case ENTERO, REAL, CADENA, CARACTER, BOOLEAN -> {
                for (String id : ids) {
                    Tipo tipo1 = new Tipo(id, this.tipo.getDataType());
                    if (!arbol.tipoAgregado(tipo1)) {
                        arbol.getErrores().add(new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "El id :'" + id + "' ya esta en uso.",
                                this.line, this.col));
                    }
                }
            }
            case SUBRANGO -> {
                return declararTipoSubrango(arbol, tableSimbols);
            }
            case ARRAY -> {
                return declararTipoArray(arbol, tableSimbols);
            }
            case RECORD -> {
                return null;
            }
        }
        return null;
    }

    private Object declararTipoSubrango(AST arbol, TableSimbols tableSimbols) {
        Object value1 = expresion1.analizar(arbol, tableSimbols);
        if (value1 instanceof ErrorPascal) {
            return value1;
        }
        Object value2 = expresion2.analizar(arbol, tableSimbols);
        if (value2 instanceof ErrorPascal) {
            return value2;
        }
        DataType type1 = this.expresion1.tipo.getDataType();
        DataType type2 = this.expresion2.tipo.getDataType();
        if (!type1.equals(DataType.BOOLEAN) && !type1.equals(DataType.ENTERO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El indice '" + value1.toString() + "' mínimo no es entero. ",
                    this.expresion1.line, this.expresion1.col);
        }
        if (!type2.equals(DataType.BOOLEAN) && !type2.equals(DataType.ENTERO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El indice '" + value2.toString() + "' máximo no es entero. ",
                    this.expresion2.line, this.expresion2.col);
        }
        int indice1 = 0;
        int indice2 = 0;
        if (type1.equals(DataType.BOOLEAN)) {
            indice1 = Boolean.parseBoolean(value1.toString()) ? 1 : 0;
        }
        if (type1.equals(DataType.ENTERO)) {
            indice1 = (int) value1;
        }
        if (type2.equals(DataType.BOOLEAN)) {
            indice2 = Boolean.parseBoolean(value2.toString()) ? 1 : 0;
        }
        if (type2.equals(DataType.ENTERO)) {
            indice2 = (int) value2;
        }
        for (String id : ids) {
            Tipo tipoSubrango = new Tipo(id, DataType.SUBRANGO, indice1, indice2);
            if (!arbol.tipoAgregado(tipoSubrango)) {
                arbol.getErrores().add(new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "El id : '" + id + "' ya esta en uso.",
                        this.line, this.col));
            }
        }
        return null;
    }

    private Object declararTipoArray(AST arbol, TableSimbols tableSimbols) {
        Object value1 = expresion1.analizar(arbol, tableSimbols);
        if (value1 instanceof ErrorPascal) {
            return value1;
        }
        Object value2 = expresion2.analizar(arbol, tableSimbols);
        if (value2 instanceof ErrorPascal) {
            return value2;
        }
        DataType type1 = this.expresion1.tipo.getDataType();
        DataType type2 = this.expresion2.tipo.getDataType();
        if (!type1.equals(DataType.BOOLEAN) && !type1.equals(DataType.ENTERO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El indice '" + value1.toString() + "' mínimo no es entero. ",
                    this.line, this.col);
        }
        if (!type2.equals(DataType.BOOLEAN) && !type2.equals(DataType.ENTERO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El indice'" + value2.toString() + "' máximo no es entero. ",
                    this.line, this.col);
        }
        int minimo = 0;
        int maximo = 0;
        if (type1.equals(DataType.BOOLEAN)) {
            minimo = Boolean.parseBoolean(value1.toString()) ? 1 : 0;
        }
        if (type1.equals(DataType.ENTERO)) {
            minimo = (int) value1;
        }
        if (type2.equals(DataType.BOOLEAN)) {
            maximo = Boolean.parseBoolean(value2.toString()) ? 1 : 0;
        }
        if (type2.equals(DataType.ENTERO)) {
            maximo = (int) value2;
        }
        if (minimo < 0) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "El indice mínimo: '" + minimo + "' debe ser mayor a 0.",
                    this.expresion1.line, this.expresion1.col);
        }
        for (String id : ids) {
            Tipo tipoArray = new Tipo(id, DataType.ARRAY, this.typeBase.getDataType(), minimo, maximo);
            tipoArray.setDimension(maximo);
            if (!arbol.tipoAgregado(tipoArray)) {
                arbol.getErrores().add(new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "El id :'" + id + "' ya esta en uso.",
                        this.line, this.col));
            }
        }
        return null;
    }

    private Object validarExpresiones(Object value1, Object value2) {
        return null;
    }
}
