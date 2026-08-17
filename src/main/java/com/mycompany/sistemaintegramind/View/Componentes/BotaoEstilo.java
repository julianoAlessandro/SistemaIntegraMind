/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class BotaoEstilo {

    /**
     * Aplica cores de Hover e Clique em um JButton.
     * @param botao O botão a ser estilizado
     * @param corPadrao Cor normal do botão
     * @param corHover Cor quando o mouse passa por cima
     * @param corClique Cor quando o botão é pressionado
     */
    public static void aplicarEfeito(JButton botao, Color corPadrao, Color corHover, Color corClique) {
        botao.setBackground(corPadrao);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(true); // Garante que a cor apareça em alguns SOs
        
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                botao.setBackground(corHover);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                botao.setBackground(corPadrao);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                botao.setBackground(corClique);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (botao.getVisibleRect().contains(evt.getPoint())) {
                    botao.setBackground(corHover);
                } else {
                    botao.setBackground(corPadrao);
                }
            }
        });
    }
}