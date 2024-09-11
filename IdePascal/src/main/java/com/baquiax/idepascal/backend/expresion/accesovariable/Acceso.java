package com.baquiax.idepascal.backend.expresion.accesovariable;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Acceso extends Sentencia {

    private String id;

    public Acceso(String id, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Simbolo simbolo = tableSimbols.buscarVariable(this.id);
        if (simbolo == null) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "La varible '" + this.id + "' al que intentas accesar no está definido.",
                    this.line, this.col
            );
        }
        DataType tipoDato = simbolo.getTipo().getDataType();
        switch (tipoDato) {
            case ENTERO, REAL, CARACTER, CADENA -> {
                this.tipo.setDataType(simbolo.getTipo().getDataType());
                this.tipo.setTypeBase(simbolo.getTipo().getTypeBase());
                return simbolo.getValue();
            }
            case SUBRANGO -> {
                this.tipo.setDataType(DataType.ENTERO);
                this.tipo.setTypeBase(DataType.ENTERO);
                return simbolo.getValue();
            }
            case BOOLEAN -> {
                this.tipo.setDataType(simbolo.getTipo().getDataType());
                this.tipo.setTypeBase(simbolo.getTipo().getTypeBase());
                int aux = Boolean.parseBoolean(simbolo.getValue().toString()) ? 1 : 0;
                return aux;
            }
            case ARRAY -> {
                if(simbolo.getTipo().getTypeBase().equals(DataType.CARACTER)){
                    return simbolo.getValue();
                }else{
                    return new ErrorPascal(
                            TipoError.SEMANTICO.name(),
                            "La variable al que intentas accesar no es ENTERO, REAL, CHAR, STRING, SUB-RANGO, array de CHAR",
                            this.line, this.col
                    );
                }
            }
            case RECORD -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "La variable al que intentas accesar no es ENTERO, REAL, CHAR, STRING, SUB-RANGO",
                        this.line, this.col
                );
            }
            case PERSONALIZADO -> {
                return getValuePersonalizado(arbol, tableSimbols, simbolo);
            }
        }
        return null;
    }

    private Object getValuePersonalizado(AST arbol, TableSimbols tableSimbols, Simbolo simbolo) {
        Tipo tablaTipo = arbol.getTablaTipos().getTipos().get(simbolo.getTipoPersonalizado());
        DataType tipoDato = tablaTipo.getDataType();
        switch (tipoDato) {
            case ENTERO, REAL, CADENA, CARACTER -> {
                this.tipo.setDataType(tipoDato);
                this.tipo.setTypeBase(tipoDato);
                return simbolo.getValue();
            }
            case SUBRANGO -> {
                this.tipo.setDataType(DataType.ENTERO);
                this.tipo.setTypeBase(DataType.ENTERO);
                return simbolo.getValue();
            }
            case BOOLEAN -> {
                this.tipo.setDataType(simbolo.getTipo().getDataType());
                this.tipo.setTypeBase(simbolo.getTipo().getTypeBase());
                int aux = Boolean.parseBoolean(simbolo.getValue().toString()) ? 1 : 0;
                return aux;
            }
            case RECORD, ARRAY -> {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "La variable al que intentas accesar es de tipo RECORD, ARRAY",
                        this.line, this.col
                );
            }
        }
        return null;
    }

}
