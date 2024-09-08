package com.baquiax.idepascal.backend.expresion.operacion.aritmetica;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Unario extends Sentencia{
    private Sentencia expresion;

    public Unario(Sentencia expresion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object value = this.expresion.interpretar(arbol, tableSimbols);
        if(value instanceof ErrorPascal){
            return value;
        }
        return getValue(value);
    }

    private Object getValue(Object value){
        DataType tipo = this.expresion.tipo.getDataType();
        switch (tipo){
            case ENTERO -> { this.tipo.setDataType(DataType.ENTERO); return (int) value * -1; }
            case REAL -> { this.tipo.setDataType(DataType.REAL); return (double) value * -1; }
            case BOOLEAN -> {
                int auxi = (Boolean.parseBoolean(value.toString()))? 1 : 0;
                return auxi * -1;
            }
            default -> { return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "Expresion no válida, se espera un ENTERO/REAL/BOOLEANO",
                    this.expresion.line, this.expresion.col);}
        }
    }
}
