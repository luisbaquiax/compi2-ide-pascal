package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class If extends Sentencia {

    private Sentencia condicion;
    private Sentencia instruccion;
    private Sentencia instruccionElse;

    private BloqueCodigo bloqueCodigo;

    private BloqueCodigo bloqueElse;

    public If(Sentencia condicion, Sentencia instruccion, Sentencia instruccionElse, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.condicion = condicion;
        this.instruccion = instruccion;
        this.instruccionElse = instruccionElse;
    }

    public If(Sentencia condicion, BloqueCodigo bloqueCodigo, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.condicion = condicion;
        this.bloqueCodigo = bloqueCodigo;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Object dataCondicion = this.condicion.interpretar(arbol, tableSimbols);
        if (dataCondicion instanceof ErrorPascal) {
            return dataCondicion;
        }
        if (!this.condicion.tipo.getDataType().equals(DataType.BOOLEAN)
                && !dataCondicion.toString().equals("1")
                && !dataCondicion.toString().equals("0")) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "La condicion debe ser de tipo '" + DataType.BOOLEAN + "'.",
                    this.condicion.line, this.condicion.col
            );
        }
        TableSimbols entornoIf = new TableSimbols(tableSimbols);
        entornoIf.setNombre("IF");
        if (Boolean.parseBoolean(dataCondicion.toString()) || dataCondicion.toString().equals("1")) {
            if (this.instruccion != null) {
                if(this.instruccion instanceof SentenciaBreak || this.instruccion instanceof SentenciaContinue){
                    return this.instruccion;
                }
                Object value = this.instruccion.interpretar(arbol, entornoIf);
                if (value instanceof ErrorPascal) {
                    return value;
                }
            } else {
                return this.bloqueCodigo.interpretar(arbol, entornoIf);
            }
        } else {
            TableSimbols entornoElse = new TableSimbols(tableSimbols);
            entornoElse.setNombre("ELSE");
            if (this.instruccionElse != null) {
                if(this.instruccionElse instanceof SentenciaBreak || this.instruccionElse instanceof SentenciaContinue){
                    return this.instruccionElse;
                }
                Object valueElse = this.instruccionElse.interpretar(arbol, entornoElse);
                if (valueElse instanceof ErrorPascal) {
                    return valueElse;
                }
            } else {
                if(bloqueElse !=null){
                    return bloqueElse.interpretar(arbol, entornoElse);
                }
            }
        }
        return null;
    }
}
