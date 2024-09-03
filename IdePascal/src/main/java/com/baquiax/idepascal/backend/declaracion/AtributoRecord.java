package com.baquiax.idepascal.backend.declaracion;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class AtributoRecord extends Sentencia {
    private List<String> ids;
    private Sentencia limite1;
    private Sentencia limite2;

    public AtributoRecord(List<String> ids, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
    }

    public AtributoRecord(List<String> ids, Sentencia limite1, Sentencia limite2, Tipo tipo, int line, int col) {
        super(tipo, line, col);
        this.ids = ids;
        this.limite1 = limite1;
        this.limite2 = limite2;
    }


    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        if (this.limite1 == null) {

        }else{

        }
        return null;
    }

    private Object getAtributoNativo(){
        return null;
    }
}
