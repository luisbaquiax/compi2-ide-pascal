package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class SentenciaWhile extends Sentencia {
    private Sentencia condicion;
    private List<Sentencia> sentencias;

    public SentenciaWhile(Sentencia condicion, List<Sentencia> sentencias, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.condicion = condicion;
        this.sentencias = sentencias;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        TableSimbols entornoWhile = new TableSimbols(tableSimbols);
        entornoWhile.setNombre("WHILE");

        Object valueCondicion = this.condicion.interpretar(arbol, entornoWhile);
        if (valueCondicion instanceof ErrorPascal) {
            return valueCondicion;
        }

        if (!this.condicion.tipo.getDataType().equals(DataType.BOOLEAN)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "La condición debe retonar una expresion '" + DataType.BOOLEAN + "'.",
                    this.condicion.line, this.condicion.col
            );
        }

        while ((boolean) this.condicion.interpretar(arbol, entornoWhile)) {
            TableSimbols entornoBucle = new TableSimbols(tableSimbols);
            for (Sentencia s : this.sentencias) {
                if(s instanceof SentenciaBreak){
                    return null;
                }
                if(s instanceof SentenciaContinue){
                    break;
                }

                Object valueS = s.interpretar(arbol, entornoBucle);
                if(valueS instanceof ErrorPascal e){
                    arbol.getErrores().add(e);
                }
                if(valueS instanceof SentenciaBreak){
                    return null;
                }
                if(valueS instanceof SentenciaContinue){
                    break;
                }

            }
        }

        return null;
    }
}
