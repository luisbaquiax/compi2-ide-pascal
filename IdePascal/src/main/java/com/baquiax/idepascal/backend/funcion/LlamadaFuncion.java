package com.baquiax.idepascal.backend.funcion;

import com.baquiax.idepascal.backend.declaracion.Variable;
import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.errores.TipoError;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Sentencia;
import com.baquiax.idepascal.utiles.Contador;

import java.util.ArrayList;
import java.util.List;

public class LlamadaFuncion extends Sentencia {
    private String id;
    private List<Sentencia> parametros;

    public LlamadaFuncion(String id, List<Sentencia> parametros, int line, int col) {
        super(new Tipo(DataType.ANY), line, col);
        this.id = id;
        this.parametros = parametros;
    }

    @Override
    public Object interpretar(AST arbol, TableSimbols tableSimbols) {
        Funtion funtion = arbol.searFuncion(this.id);
        if (funtion == null) {
            return new ErrorPascal(
                    TipoError.SINTACTICO.name(),
                    "La funcion/procedimiento '" + this.id + "' no existe.",
                    this.line, this.col
            );
        }
        TableSimbols entornoLlamada = new TableSimbols(arbol.getGlobal());
        entornoLlamada.setNombre(this.id);

        funtion.setParams();
        if (this.parametros.size() != funtion.getParams().size()) {
            return new ErrorPascal(
                    TipoError.SINTACTICO.name(),
                    "La cantidad parámetros es incorrecta en la llamada de la función: " + this.id,
                    this.line, this.col
            );
        }
        for (int i = 0; i < this.parametros.size(); i++) {
            String id = funtion.getParams().get(i).get("id").toString();
            Sentencia expresionParam = this.parametros.get(i);
            Parametro parametro = (Parametro) funtion.getParams().get(i).get("tipo");
            DataType tipoParam = parametro.getDataType();
            String personalizado = "";

            List<String> ids = new ArrayList<>();
            ids.add(id);

            if (tipoParam.equals(DataType.PERSONALIZADO)) {
                /**
                 * ids_variables:ids DOS_PUNTOS IDENTIFICADOR:a PUNTO_COMA
                 *                 {: RESULT = new Variable(2, ids, a, idsleft, idsright); :}
                 */
                personalizado = parametro.getPersonalizado();

                Variable variable = new Variable(2, ids, personalizado, this.line, this.col);
                Object value = variable.interpretar(arbol, entornoLlamada);
                if (value instanceof ErrorPascal) {
                    return value;
                }
            } else {
                Variable variable1 = new Variable(1, ids, new Tipo(tipoParam), this.line, this.col);
                Object value = variable1.interpretar(arbol, entornoLlamada);
                if (value instanceof ErrorPascal) {
                    return value;
                }
            }

            Object valueParam = expresionParam.interpretar(arbol, tableSimbols);
            if (valueParam instanceof ErrorPascal) {
                return valueParam;
            }

            Simbolo simbol = entornoLlamada.buscarVariable(id);
            if (simbol == null) {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "Parametro inválido en la función: " + this.id,
                        this.line, this.col
                );
            }
            if (!simbol.getTipo().getDataType().equals(expresionParam.tipo.getDataType())) {
                return new ErrorPascal(
                        TipoError.SEMANTICO.name(),
                        "Tipo de parámetro incorrecto, se esperaba: " + simbol.getTipo().getDataType(),
                        this.line, this.col
                );
            }
            simbol.setValue(valueParam);
        }
        Object valueFuncion = funtion.interpretar(arbol, entornoLlamada);
        if (valueFuncion instanceof ErrorPascal) {
            return valueFuncion;
        }
        return null;
    }

    @Override
    public String generarArbolLlamadas(String anterior) {
        String nameAnterior = "n" + Contador.getCount();
        String nameId = "n" + Contador.getCount();

        String anteriorNode = nameAnterior + "[label=\"anterior\"];\n";
        String idNode = nameId + "[label=\""+this.id+"()\"];\n";

        String relation = nameAnterior + " -> " + nameId + ";\n";

        String result = anteriorNode + idNode + relation;

        return result;
    }
}
