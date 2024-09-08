package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class Caso extends Sentencia {

    private Sentencia expresion;
    private Sentencia sentencia;
    private List<Sentencia> sentencias;

    public Caso(Sentencia expresion, Sentencia sentencia, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.sentencia = sentencia;
        this.expresion = expresion;
    }

    public Caso(Sentencia expresion, List<Sentencia> sentencias, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.sentencias = sentencias;
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        TableSimbols tableCase = new TableSimbols(tableSimbols);
        tableCase.setNombre("CASO");
        if (this.sentencia != null) {
            Object value = this.sentencia.interpretar(arbol, tableCase);
            if (value instanceof ErrorPascal e) {
                arbol.getErrores().add(e);
            }
        } else {
            for (Sentencia s : this.sentencias) {
                Object valueS = s.interpretar(arbol, tableCase);
                if (valueS instanceof ErrorPascal e) {
                    arbol.getErrores().add(e);
                }
            }
        }
        return null;
    }

    public Sentencia getExpresion() {
        return expresion;
    }
}
