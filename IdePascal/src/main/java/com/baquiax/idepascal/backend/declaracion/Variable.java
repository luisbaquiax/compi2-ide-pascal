package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class Variable extends Sentencia {
    private List<String> ids;

    private List<AtributoRecord> atributosRecord;
    private String id;
    private Sentencia expresion;

    private Sentencia limite1;
    private Sentencia limite2;

    private int parametro;

    public Variable(int parametro, String id, List<AtributoRecord> atributosRecord, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.atributosRecord = atributosRecord;
        this.parametro = parametro;
        this.id = id;
    }

    public Variable(int parametro, List<String> ids, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
        this.parametro = parametro;
    }

    public Variable(int parametro, List<String> ids, Tipo tipo, Sentencia expresion, int line, int col) {
        super(tipo, line, col);
        this.expresion = expresion;
        this.ids = ids;
        this.parametro = parametro;
    }

    public Variable(int parametro, List<String> ids, String id, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.ids = ids;
        this.id = id;
        this.parametro = parametro;
    }

    public Variable(int parametro, List<String> ids, Sentencia limite1, Sentencia limite2, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.ids = ids;
        this.limite1 = limite1;
        this.limite2 = limite2;
        this.parametro = parametro;
    }

    public Variable(int parametro, List<String> ids, Sentencia limite1, Sentencia limite2, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
        this.limite1 = limite1;
        this.limite2 = limite2;
        this.parametro = parametro;
    }

    public Variable(int parametro, List<String> ids, Sentencia limite1, Sentencia limite2, String id, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.ids = ids;
        this.limite1 = limite1;
        this.limite2 = limite2;
        this.id = id;
        this.parametro = parametro;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        switch (parametro) {
            case 1 -> {
                return this.declararVariablePrimitiva(arbol, tableSimbols);
            }
            case 2 -> {
                return this.declararVariableDeOtroTipo(arbol, tableSimbols);
            }
            case 3 -> {
                return declararVariableSubrango(arbol, tableSimbols);
            }
            case 4 -> {
                return declararArray(arbol, tableSimbols);
            }
            case 5 -> {
                return declararArrayOtroTipo(arbol, tableSimbols);
            }
            case 6 -> {
                return declararRecord(arbol, tableSimbols);
            }
            default -> {
                return null;
            }
        }
    }

    private Object declararVariablePrimitiva(AST arbol, TableSimbols tableSimbols) {
        Object value;

        if (this.expresion == null) {
            value = getValueDefault(this.tipo.getDataType());
        } else {
            value = this.expresion.analizar(arbol, tableSimbols);
            if (value instanceof ErrorPascal) {
                return value;
            }
            DataType tipoExpresion = this.expresion.tipo.getDataType();

            if(this.tipo.getDataType().equals(DataType.CARACTER) && tipoExpresion.equals(DataType.CADENA)){
                String aux = value.toString();
                if(aux.length() > 1){
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "No se puede mezclar '" + this.tipo.getDataType().nombre + "' con '" + tipoExpresion.nombre + "'.",
                            this.expresion.line, this.expresion.col
                    );
                }
                tipoExpresion = DataType.CARACTER;
            }

            if (!tipoExpresion.equals(this.tipo.getDataType())) {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "No se puede mezclar '" + this.tipo.getDataType().nombre + "' con '" + tipoExpresion.nombre + "'.",
                        this.expresion.line, this.expresion.col
                );
            }

        }
        for (String id : ids) {
            this.tipo.setTypeBase(this.tipo.getDataType());
            Simbolo agregando = new Simbolo(id, this.tipo, value, true);
            if (!tableSimbols.agregarVariable(agregando, arbol)) {
                arbol.getErrores().add(new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "La variable '" + id + "' ya está en uso.",
                        this.line, this.col));
            }
        }
        return null;
    }

    private Object declararVariableDeOtroTipo(AST arbol, TableSimbols tableSimbols) {
        Tipo tipoBuscado = arbol.getTablaTipos().getTipos().get(this.id.toLowerCase());
        if (tipoBuscado == null) {
            return new ErrorPascal(TipoError.SEMANTICO.name(),
                    "El tipo de dato '" + this.id + "' no está definido en la tabla de tipos.",
                    this.line, this.col);
        }
        for (String elementId : ids) {
            Tipo tipo = new Tipo();
            tipo.setDataType(DataType.PERSONALIZADO);
            tipo.setTypeBase(tipoBuscado.getTypeBase());
            Simbolo agregando = new Simbolo(elementId, tipo, getValueDefault(tipoBuscado.getTypeBase()), true);
            agregando.setTipoPersonalizado(tipoBuscado.getId());
            if (!tableSimbols.agregarVariable(agregando, arbol)) {
                arbol.getErrores().add(new ErrorPascal(
                        TipoError.SINTACTICO.name(),
                        "La variable '" + elementId + "' ya está en uso.",
                        this.line, this.col)
                );
            }
        }
        return null;
    }

    private Object declararVariableSubrango(AST arbol, TableSimbols tableSimbols) {
        Object value1 = this.limite1.analizar(arbol, tableSimbols);
        if (value1 instanceof ErrorPascal) {
            return value1;
        }
        Object value2 = this.limite2.analizar(arbol, tableSimbols);
        if (value2 instanceof ErrorPascal) {
            return value2;
        }
        if (!this.limite1.tipo.getDataType().equals(DataType.BOOLEAN) && !this.limite1.tipo.getDataType().equals(DataType.ENTERO)) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite inferior '" + value1 + "' no es ENTERO",
                    this.limite1.line, this.limite1.col);
        }
        if (!this.limite2.tipo.getDataType().equals(DataType.BOOLEAN) && !this.limite2.tipo.getDataType().equals(DataType.ENTERO)) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite superior '" + value2 + "' no es ENTERO",
                    this.limite1.line, this.limite1.col);
        }
        int inferior = 0;
        int superior = 0;
        if (this.limite1.tipo.getDataType().equals(DataType.BOOLEAN)) {
            inferior = Boolean.parseBoolean(value1.toString()) ? 1 : 0;
        } else {
            inferior = (int) value1;
        }

        if (this.limite2.tipo.getDataType().equals(DataType.BOOLEAN)) {
            superior = Boolean.parseBoolean(value2.toString()) ? 1 : 0;
        } else {
            superior = (int) value2;
        }
        if (superior < inferior) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite superior '" + superior + "' debe ser mayor que el limite inferior '" + inferior + "'.",
                    this.limite1.line, this.limite1.col);
        }

        for (String subs : ids) {
            Tipo tipo = new Tipo(subs, DataType.SUBRANGO, DataType.ENTERO, inferior, superior);
            Simbolo subrango = new Simbolo(subs, tipo, inferior, true);
            if (!tableSimbols.agregarVariable(subrango, arbol)) {
                arbol.getErrores().add(new ErrorPascal(
                        TipoError.SINTACTICO.name(),
                        "La variable '" + subs + "' ya está en uso.",
                        this.line, this.col)
                );
            }
        }

        return null;
    }

    private Object declararArray(AST arbol, TableSimbols tableSimbols) {
        Object value1 = this.limite1.analizar(arbol, tableSimbols);
        if (value1 instanceof ErrorPascal) {
            return value1;
        }
        Object value2 = this.limite2.analizar(arbol, tableSimbols);
        if (value2 instanceof ErrorPascal) {
            return value2;
        }
        int indice1 = 0;
        int indice2 = 0;
        if (this.limite1.tipo.getDataType().equals(DataType.BOOLEAN)) {
            indice1 = Boolean.parseBoolean(value1.toString()) ? 1 : 0;
        } else {
            indice1 = (int) value1;
        }
        if (this.limite2.tipo.getDataType().equals(DataType.BOOLEAN)) {
            indice2 = Boolean.parseBoolean(value2.toString()) ? 1 : 0;
        } else {
            indice2 = (int) value2;
        }
        if (indice1 < 0) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite inferior '" + indice1 + "' debe ser mayor o igual a 0.",
                    this.limite1.line, this.limite1.col);
        }
        if (indice2 < indice1) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite superior '" + indice2 + "' debe ser mayor que el limite inferior '" + indice1 + "'.",
                    this.limite1.line, this.limite1.col);
        }

        for (String i : ids) {
            Tipo tipo = new Tipo(i, DataType.ARRAY, this.tipo.getDataType(), indice1, indice2);
            Simbolo arreglo = new Simbolo(i, tipo, null, true);
            if (!tableSimbols.agregarVariable(arreglo, arbol)) {
                arbol.getErrores().add(
                        new ErrorPascal(TipoError.SINTACTICO.name(),
                                "El la variable '" + i + "' ya está en uso.",
                                this.line, this.col)
                );
            }
        }

        return null;
    }

    private Object declararArrayOtroTipo(AST arbol, TableSimbols tableSimbols) {
        Object value1 = this.limite1.analizar(arbol, tableSimbols);
        if (value1 instanceof ErrorPascal) {
            return value1;
        }
        Object value2 = this.limite2.analizar(arbol, tableSimbols);
        if (value2 instanceof ErrorPascal) {
            return value2;
        }
        int indice1 = 0;
        int indice2 = 0;
        if (this.limite1.tipo.getDataType().equals(DataType.BOOLEAN)) {
            indice1 = Boolean.parseBoolean(value1.toString()) ? 1 : 0;
        } else {
            indice1 = (int) value1;
        }
        if (this.limite2.tipo.getDataType().equals(DataType.BOOLEAN)) {
            indice2 = Boolean.parseBoolean(value2.toString()) ? 1 : 0;
        } else {
            indice2 = (int) value2;
        }
        if (indice1 < 0) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite inferior '" + indice1 + "' debe ser mayor o igual a 0.",
                    this.limite1.line, this.limite1.col);
        }
        if (indice2 < indice1) {
            return new ErrorPascal(TipoError.SINTACTICO.name(),
                    "El límite superior '" + indice2 + "' debe ser mayor que el limite inferior '" + indice1 + "'.",
                    this.limite1.line, this.limite1.col);
        }
        Tipo tipoArray = arbol.getTablaTipos().getTipos().get(this.id.toLowerCase());
        if (tipoArray == null) {
            return new ErrorPascal(TipoError.SEMANTICO.name(),
                    "El tipo de dato '" + this.id + "' no está definido en la tabla de tipos.",
                    this.line, this.col);
        }
        for (String i : ids) {
            Tipo tipo = new Tipo();
            tipo.setDataType(DataType.ARRAY);
            tipo.setTypeBase(tipoArray.getDataType());
            tipo.setIndiceMinimo(indice1);
            tipo.setIndiceMaximo(indice2);

            Simbolo arreglo = new Simbolo(i, tipo, null, true);
            arreglo.setTipoPersonalizado(this.id);
            if (!tableSimbols.agregarVariable(arreglo, arbol)) {
                arbol.getErrores().add(
                        new ErrorPascal(TipoError.SINTACTICO.name(),
                                "El la variable '" + i + "' ya está en uso.",
                                this.line, this.col)
                );
            }
        }

        return null;
    }

    private Object declararRecord(AST arbol, TableSimbols tableSimbols) {
        for (AtributoRecord list : atributosRecord) {
            Object value = list.analizar(arbol, tableSimbols);
            if (value instanceof ErrorPascal errorPascal) {
                arbol.getErrores().add(errorPascal);
            }
        }
        return null;
    }

    private Object getValueDefault(DataType dataType) {
        switch (dataType) {
            case ENTERO -> {
                return 0;
            }
            case REAL -> {
                return 0.0;
            }
            case CARACTER, CADENA -> {
                return "";
            }
            case BOOLEAN -> {
                return true;
            }
        }
        return null;
    }
}
