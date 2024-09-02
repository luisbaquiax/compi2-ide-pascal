/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.baquiax.idepascal.utiles;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 *
 * @author Luis
 */
public class Utiles {

    public static final int TAM = 20;

    /**
     *
     * @param menuItem
     * @param ruta
     */
    public void setIcon(JMenuItem menuItem, String ruta) {
        ImageIcon icono1 = new ImageIcon(getClass().getResource(ruta));
        menuItem.setIcon(new ImageIcon(icono1.getImage().getScaledInstance(TAM,
                TAM, Image.SCALE_SMOOTH)));
    }

    /**
     *
     * @param menuItem
     * @param ruta
     * @return
     */
    public ImageIcon getImageIcon(JMenuItem menuItem, String ruta) {
        ImageIcon icono1 = new ImageIcon(getClass().getResource(ruta));
        menuItem.setIcon(new ImageIcon(icono1.getImage().getScaledInstance(TAM,
                TAM, Image.SCALE_SMOOTH)));

        return icono1;
    }
}
