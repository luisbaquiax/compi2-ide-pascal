package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Asignacion;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class SentenciaFor extends Sentencia {

    private Sentencia asignacion;
    private Sentencia valorFinal;
    private List<Sentencia> sentencias;

    public SentenciaFor(Sentencia asignacion, Sentencia valorFinal, List<Sentencia> sentencias, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.asignacion = asignacion;
        this.valorFinal = valorFinal;
        this.sentencias = sentencias;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        TableSimbols entornoForDo = new TableSimbols(tableSimbols);
        Object valueAsig, valueFinal;

        valueAsig = this.asignacion.interpretar(arbol, entornoForDo);
        if (valueAsig instanceof ErrorPascal) {
            return valueAsig;
        }
        if (!this.asignacion.tipo.getDataType().equals(DataType.ENTERO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "ciclo-for-do: el valor inicial debe ser ENTERO.",
                    this.asignacion.col, this.asignacion.col
            );
        }

        valueFinal = this.valorFinal.interpretar(arbol, entornoForDo);
        if (valueFinal instanceof ErrorPascal) {
            return valueFinal;
        }
        if (!this.valorFinal.tipo.getDataType().equals(DataType.ENTERO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "ciclo-for-do: el valor final debe ser ENTERO.",
                    this.asignacion.col, this.asignacion.col
            );
        }
        Asignacion asig = (Asignacion) this.asignacion;
        int inicial = (int) asig.getValue();
        int finalizacion = (int) valueFinal;
        for (int i = inicial; i <= finalizacion; i++) {
            TableSimbols entornoInterior = new TableSimbols(entornoForDo);
            for (Sentencia s : this.sentencias) {
                if(s instanceof SentenciaBreak){
                    return null;
                }
                if(s instanceof SentenciaContinue){
                    break;
                }

                var value = s.interpretar(arbol, entornoInterior);
                if (value instanceof ErrorPascal e) {
                    arbol.getErrores().add(e);
                }

                if(value instanceof SentenciaBreak){
                    return null;
                }
                if(value instanceof SentenciaContinue){
                    break;
                }
            }
            actualizarVariable(entornoForDo, asig.getId());
        }
        desactualizarVariable(entornoForDo, asig.getId());
        return null;
    }

    private void actualizarVariable(TableSimbols tableSimbols, String id) {
        Simbolo simbolo = tableSimbols.buscarVariable(id);
        int value = (int) simbolo.getValue();
        ++value;
        simbolo.setValue(value);
    }
    private void desactualizarVariable(TableSimbols tableSimbols, String id) {
        Simbolo simbolo = tableSimbols.buscarVariable(id);
        int value = (int) simbolo.getValue();
        --value;
        simbolo.setValue(value);
    }
}
