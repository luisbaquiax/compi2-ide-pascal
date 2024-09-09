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
                    z,t = char;
                    vector = array [1..25] of real;
                const
                    P = 18 + 1;
                    Q = 90;
                    PI = 3.141516;
                    HOLA_MUNDO = 'hola mundo';
                var
                    arreglo1 : array [1..15] of char;
                    arreglo2 : array [1..10] of days;
                    var1 : vector;
                    var2: 1...2;
                    b : yes;
                    aa: integer;
                    a: integer;
                BEGIN
                    var1:= 2;
                    aa := 10;
                    a := 10;
                         (* repeat until loop execution *)
                         repeat
                            writeln('value of a: ', a);
                            a := a + 1;
                         until a = 20;
                         writeln('valor final de a ', a);
                END .   
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
                var det = i.interpretar(ast, tabla);
                if (det instanceof ErrorPascal e) {
                    ast.getErrores().add(e);
                }
            }
            if (ast.getErrores().isEmpty()) {
                System.out.println("todo bien");
            } else {
                ast.getErrores().forEach(element -> {
                    System.out.println(element.getDescription() + " linea " + element.getRow() + " col: " + element.getCol());
                });
            }
            System.out.println("tipos: ");
            Map<String, Tipo> map = ast.getTablaTipos().getTipos();
            for (String key : map.keySet()) {
                Tipo value = map.get(key);
                System.out.println("id: " + key + ", tipo: " + value.toString());
            }
            System.out.println("simbolos");
            Map<String, Object> map1 = ast.getReporteSimbolos();
            for (String key : map1.keySet()) {
                Simbolo value = (Simbolo) map1.get(key);
                System.out.println(value.toString());
            }
            System.out.println("console:" + ast.getLog());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
