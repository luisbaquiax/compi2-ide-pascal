package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class RepeatUntil extends Sentencia {
    private List<Sentencia> sentencias;
    private Sentencia condicion;

    public RepeatUntil(List<Sentencia> sentencias, Sentencia condicion, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.sentencias = sentencias;
        this.condicion = condicion;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        TableSimbols entornoRepeat = new TableSimbols(tableSimbols);
        entornoRepeat.setNombre("REPEAT-UNTIL");

        Object valueCondicion = null;
        do {
            for(Sentencia s: this.sentencias){
                Object value = s.interpretar(arbol, entornoRepeat);
                if(value instanceof ErrorPascal e){
                    arbol.getErrores().add(e);
                }

            }
            valueCondicion = (boolean) this.condicion.interpretar(arbol, tableSimbols);
            System.out.println(valueCondicion);
            if (valueCondicion instanceof ErrorPascal) {
                return valueCondicion;
            }
        } while (!(boolean) valueCondicion);
        return null;
    }
}
