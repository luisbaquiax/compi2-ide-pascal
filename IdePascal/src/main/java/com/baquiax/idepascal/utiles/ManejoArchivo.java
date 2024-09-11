/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baquiax.idepascal.utiles;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

import static javax.swing.JFileChooser.*;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * @author Luis
 */
public class ManejoArchivo {

    private JFileChooser choser;
    public static final String FILTRO = ".pas";
    public static final String DOT_TREE = "arbol-llamadas/tree.dot";
    public static final String IMG_TREE = "arbol-llamadas/grafica.png";
    
    public static final String[] FILTERS = new String[]{"pas","pp"};
    
    public String saveFile() {
        choser = new JFileChooser();
        choser.addChoosableFileFilter(new FileNameExtensionFilter("archivos pascal", FILTERS));
        choser.setAcceptAllFileFilterUsed(false);
        int seleccionado = choser.showSaveDialog(null);
        if (seleccionado == APPROVE_OPTION) {
            return (choser.getSelectedFile().getAbsolutePath());
        }
        return "";
    }

    public String rutaEscritorio(String ruta) {
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        return desktopDir.getAbsolutePath() + File.separator + ruta;
    }

    public String openFile() {
        choser = new JFileChooser();
        choser.addChoosableFileFilter(new FileNameExtensionFilter("archivos pascal", FILTERS));
        choser.setAcceptAllFileFilterUsed(false);
        int seleccionado = choser.showOpenDialog(null);
        if (seleccionado == APPROVE_OPTION) {
            return (choser.getSelectedFile().getAbsolutePath());
        }
        return "";
    }

    public void writeFile(String ruta, String contenido) {
        try {
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter escribeArchivo = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(escribeArchivo);
            bw.write(contenido);
            bw.close();
        } catch (IOException e) {
        }
    }

    /**
     * @param ruta
     * @return
     */
    public String readFile(String ruta) {
        String content = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(ruta);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line + "\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return content;
    }

    public void openFile(String path) {
        try {
            File file = new File(path);
            if (Desktop.isDesktopSupported() && file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("Desktop API no es compatible o el archivo no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
