package com.baquiax.idepascal.backend.sentencias;

import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.DataType;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;

import java.util.ArrayList;
import java.util.List;

public class SentenciaCase extends Sentencia {

    private Sentencia expresion;
    private List<Caso> casos;

    private Caso casoPorDefecto;

    public SentenciaCase(Sentencia expresion, List<Caso> casos, Caso casoPorDefecto, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.expresion = expresion;
        this.casos = casos;
        this.casoPorDefecto = casoPorDefecto;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        TableSimbols entornoCase = new TableSimbols(tableSimbols);

        Object valueExpresion, valueExpresionDefault = null;
        valueExpresion = this.expresion.interpretar(arbol, entornoCase);
        if (valueExpresion instanceof ErrorPascal) {
            return valueExpresion;

        }
        if (this.expresion.tipo.getDataType().equals(DataType.BOOLEAN)
                || this.expresion.tipo.getDataType().equals(DataType.ARRAY)
                || this.expresion.tipo.getDataType().equals(DataType.RECORD)
                || this.expresion.tipo.getDataType().equals(DataType.PERSONALIZADO)) {
            return new ErrorPascal(
                    TipoError.SEMANTICO.name(),
                    "Expresion inválida en el case, solo se admite: entero, real, char, string",
                    this.expresion.line, this.expresion.col
            );
        }

        List<Object> values = new ArrayList<>();
        for (Caso c : this.casos) {
            Object valueCaseExpresion = c.getExpresion().interpretar(arbol, entornoCase);
            if (valueCaseExpresion instanceof ErrorPascal e) {
                arbol.getErrores().add(e);
            }
            values.add(valueCaseExpresion);
            if (!this.expresion.tipo.getDataType().equals(c.getExpresion().tipo.getDataType())) {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "Expresion inválida en el case, solo se admite: entero, real, char, string",
                        c.getExpresion().line, c.getExpresion().col
                );

                /*arbol.getErrores().add(
                        new ErrorPascal(
                                TipoError.SEMANTICO.name(),
                                "Expresion inválida en el case, solo se admite: entero, real, char, string",
                                c.getExpresion().line, c.getExpresion().col
                        )
                );*/
            }
        }

        for (int i = 0; i < this.casos.size(); i++) {
            Object auxiValue = values.get(i);
            if (valueExpresion.toString().equalsIgnoreCase(auxiValue.toString())) {
                Object valueCase = this.casos.get(i).interpretar(arbol, entornoCase);
                if (valueCase instanceof ErrorPascal e) {
                    arbol.getErrores().add(e);
                }
                return null;
            }
        }
        if (this.casoPorDefecto != null) {
            Object valueDefault = this.casoPorDefecto.interpretar(arbol, entornoCase);
            if (valueDefault instanceof ErrorPascal) {
                return valueDefault;
            }
        }

        return null;
    }
}
