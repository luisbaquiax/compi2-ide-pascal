/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.baquiax.idepascal;

import com.baquiax.idepascal.backend.Lexer;
import com.baquiax.idepascal.backend.Parser;
import com.baquiax.idepascal.backend.errores.ErrorPascal;
import com.baquiax.idepascal.backend.funcion.Funtion;
import com.baquiax.idepascal.backend.funcion.Parametro;
import com.baquiax.idepascal.backend.simbol.*;
import com.baquiax.idepascal.backend.stament.Sentencia;
import com.baquiax.idepascal.ventanas.Editor;
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
                program main;
                type
                    days, age = integer;
                    yes, true = boolean;
                    name, city = string;
                    fees, expenses = real;
                    a1 = 0...10;
                    z,t = char;
                    vector = array [1..25] of real;
                const
                    P = 18 + 1;
                    Q = 90;
                    PI = 3.141516;
                    HOLA_MUNDO = 'hola mundo';
                var
                    arreglo1 : array [1..15] of char;
                    arreglo2 : array [1..10] of a1;
                     arreglo3 : array [0..10] of integer;
                    var2: 0...2;
                    b : yes;
                    grade: string;
                    lunes, martes : days;
                    v1: vector;
                    a11: a1;
                    resultado : boolean;
                    i: integer;
                    
                    function max(num1, num2: integer; x, y: real): real;
                    var
                       result: real;
                    begin
                        result := x + y;
                        writeln('Result inside function: ', result);
                        max := result;
                    end;
                    
                    procedure findMin(var x, y, z1: integer; var m: integer);
                    begin
                       if (x < y) then
                          m := x
                       else
                          m := y;
                       if (z1 <m) then
                          begin
                            m := z1;
                            writeln('hola '); 
                          end;
                       writeln('findMin ', m);
                    end;
                                
                BEGIN
                    lunes := 12;
                    martes := 2;
                    
                     resultado := (1 <> 0) or else (5 / 2 > 1);
                     writeln('resultado ', resultado);  
                     max(2, 3, 4.5, 5.5);
                      for i := 10  to 20 do
                        begin
                            if(i = 15) then
                                break;
                            writeln('value of a: ', i);
                        end;            
                     
                    if(resultado) then
                         writeln(lunes,' , ' ,martes)
                         else
                         writeln(lunes ,' else ' ,martes);
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
            if (ast.getErrores().isEmpty()) {
                System.out.println("todo bien");
            } else {
                System.out.println("errores:");
                ast.getErrores().forEach(element -> {
                    System.out.println(element.getDescription() + " linea " + element.getRow() + " col: " + element.getCol());
                });
            }
            System.out.println("console:" + ast.getLog());
           /* System.out.println("funciones ");
            for (Sentencia sentencia : ast.getFunciones()) {
                Funtion funtion = (Funtion) sentencia;
                funtion.setParams();
                for (int i = 0; i < funtion.getParams().size(); i++) {
                    Map<String, Object> map2 = funtion.getParams().get(i);
                    Parametro param = (Parametro) map2.get("tipo");
                    System.out.println("id " + map2.get("id") + " tipo " + param.getDataType());
                }
            }*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
