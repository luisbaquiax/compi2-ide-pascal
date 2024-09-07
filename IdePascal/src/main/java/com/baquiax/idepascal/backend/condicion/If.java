package com.baquiax.idepascal.backend.condicion;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class If extends Sentencia {

    private Sentencia condicion;
    private Sentencia instruccion;
    private List<Sentencia> instrucciones;

    public If(Sentencia condicion, Sentencia instruccion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.condicion = condicion;
        this.instruccion = instruccion;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        Object dataCondicion = this.condicion.analizar(arbol, tableSimbols);
        if (dataCondicion instanceof ErrorPascal) {
            return dataCondicion;
        }
        if (!this.condicion.tipo.getDataType().equals(DataType.BOOLEAN)) {
        }
        if (this.instruccion != null) {
            TableSimbols entornoIf = new TableSimbols(tableSimbols);

            Object value = this.instruccion.analizar(arbol, entornoIf);
            if (value instanceof ErrorPascal) {
                return value;
            }
        }
        return null;
    }
}
