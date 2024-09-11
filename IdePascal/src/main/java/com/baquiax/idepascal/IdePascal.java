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
        new Editor().setVisible(true);
    }
}
