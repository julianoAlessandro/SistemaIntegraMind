/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

public class EstilizarBotaoFecharVenda {
    
     public static JButton criarBotaoFechar() {

        JButton btnFechar = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setStroke(new BasicStroke(4));
                g2.setColor(getForeground());

                int margin = 6;

                g2.drawLine(
                        margin,
                        margin,
                        getWidth() - margin,
                        getHeight() - margin);

                g2.drawLine(
                        getWidth() - margin,
                        margin,
                        margin,
                        getHeight() - margin);

                g2.dispose();
            }
        };

        btnFechar.setPreferredSize(new Dimension(23, 23));
        btnFechar.setContentAreaFilled(false);
        btnFechar.setFocusPainted(false);
        btnFechar.setBorder(null);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.setForeground(Color.RED);

        return btnFechar;
    }
}
   
    

