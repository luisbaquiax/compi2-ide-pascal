/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.baquiax.idepascal;

import com.baquiax.idepascal.backend.Lexer;
import com.baquiax.idepascal.backend.Parser;
import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.simbol.AST;
import com.baquiax.idepascal.backend.simbol.Simbolo;
import com.baquiax.idepascal.backend.simbol.TableSimbols;
import com.baquiax.idepascal.backend.simbol.Tipo;
import com.baquiax.idepascal.backend.stament.Sentencia;
import com.formdev.flatlaf.FlatIntelliJLaf;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Luis
 */
public class IdePascal {

    public static void main(String[] args) {
        FlatIntelliJLaf.setup();
        //new Editor().setVisible(true);
        String content = """
                type
                    days, age = integer;
                    yes, true = boolean;
                    name, city = string;
                    fees, expenses = real;
                    a = 1...5;
                    z,t = char;
                    vector = array [1..25] of real;
                const
                    P = 18 + 1;
                    Q = 90;
                    PI = 3.141516;
                    HOLA_MUNDO = 'hola mundo';
                var
                    hola, weekdays : integer = 5;
                    taxrate, net_income: days;
                    choice, isready: boolean;
                    initials, grade: char;
                    name1, surname : string;
                    arreglo1 : vector;
                    arreglo2 : array [1..10] of days;
                    variable1 : 1...5;
                    marks: 1 ... 100;
                """;
        Lexer lexer = new Lexer(new StringReader(content));
        Parser parser = new Parser(lexer);
        try {
            List<Sentencia> result = (List<Sentencia>) parser.parse().value;
            var ast = new AST(result);
            var tabla = new TableSimbols();
            tabla.setNombre("GLOBAL");
            ast.setLog("");
            ast.setGlobal(tabla);
            for (Sentencia i : ast.getSentecias()) {
                if (i == null) {
                    continue;
                }
                var value = i.analizar(ast, tabla);
                if (value instanceof ErrorPascal) {
                    System.out.println("error pascal");
                }
            }
            if (ast.getErrores().isEmpty()) {
                System.out.println("todo bien");
            } else {
                ast.getErrores().forEach(element -> {
                    System.out.println(element.getDescription());
                });
            }
          /*  Map<String, Tipo> map = ast.getTablaTipos().getTipos();
            for (String key : map.keySet()) {
                Tipo value = map.get(key);
                System.out.println("Key = " + key + ", Value = " + value.toString());
            }*/
            System.out.println("simbolos");
            Map<String, Object> map1 = ast.getReporteSimbolos();
            for (String key : map1.keySet()) {
                Simbolo value = (Simbolo) map1.get(key);
                System.out.println(value.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
