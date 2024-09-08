package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.List;

public class SentenciaCase extends Sentencia {

    private Sentencia expresion;
    private List<Caso> casos;

    private Caso casoPorDefecto;
    public SentenciaCase(Sentencia expresion, List<Caso> casos, Caso casoPorDefecto, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.casos = casos;
        this.casoPorDefecto = casoPorDefecto;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        return null;
    }
}
