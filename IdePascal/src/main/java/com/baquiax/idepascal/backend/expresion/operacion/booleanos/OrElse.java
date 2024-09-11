package com.baquiax.idepascal.backend.expresion.operacion.booleanos;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class OrElse extends Sentencia {

    private Sentencia condicion1;
    private Sentencia condicion2;

    public OrElse(Sentencia condicion1, Sentencia condicion2, int line, int col) {
        super(new Tipo(DataType.BOOLEAN), line, col);
        this.condicion1 = condicion1;
        this.condicion2 = condicion2;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object value1 = this.condicion1.interpretar(arbol, tableSimbols);
        if (value1 instanceof ErrorPascal) {
            return value1;
        }
        Object value2 = this.condicion2.interpretar(arbol, tableSimbols);
        if(value2 instanceof ErrorPascal){
            return value2;
        }
        if(!this.condicion1.tipo.getDataType().equals(DataType.BOOLEAN)){
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "Error and-then > expresion invalida, se esperaba: BOOLEAN",
                    this.condicion1.col, this.condicion1.col
            );
        }
        if(!this.condicion2.tipo.getDataType().equals(DataType.BOOLEAN)){
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "Error and-then > expresion invalida, se esperaba: BOOLEAN",
                    this.condicion2.col, this.condicion2.col
            );
        }
        if(Boolean.parseBoolean(value1.toString()) || Boolean.parseBoolean(value2.toString())){
            return true;
        }
        return false;
    }

    @Override
    public String generarArbolLlamadas(String anterior) {
        return null;
    }
}
