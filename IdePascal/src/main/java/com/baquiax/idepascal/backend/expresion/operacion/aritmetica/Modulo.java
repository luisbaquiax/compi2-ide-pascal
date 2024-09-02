package com.baquiax.idepascal.backend.expresion.operacion.aritmetica;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

public class Modulo extends Sentencia {
    private Sentencia operandoIzq;
    private Sentencia operandoDer;

    public Modulo(Sentencia operandoIzq, Sentencia operandoDer, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.operandoIzq = operandoIzq;
        this.operandoDer = operandoDer;
    }

    @Override
    public Object analizar(AST arbol, TableSimbols tableSimbols) {
        Object izq = this.operandoIzq.analizar(arbol, tableSimbols);
        if (izq instanceof ErrorPascal) {
            return izq;
        }
        Object der = this.operandoDer.analizar(arbol, tableSimbols);
        if (der instanceof ErrorPascal) {
            return der;
        }
        return this.getResult(izq, der);
    }

    private Object getResult(Object izquierdo, Object derecho) {
        if (derecho.toString().equals("0") || derecho.toString().equals("0.0")) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "No es permitido la divisón entre 0.",
                    this.operandoDer.line, this.operandoDer.col);
        } else {
            DataType izq = this.operandoIzq.tipo.getDataType();
            DataType der = this.operandoDer.tipo.getDataType();
            if (izq.equals(DataType.ENTERO)) {
                if (der.equals(DataType.ENTERO)) {
                    this.tipo.setDataType(DataType.ENTERO);
                    return (int) izquierdo % (int) derecho;
                }
            }
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "arimética-mod: Expresion no válida, se esperaba un ENTERO o REAL.",
                    this.operandoDer.line, this.operandoDer.col);
        }
    }
}
