/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BotaoRemoverItemVenda {

    public static JButton criarBotaoRemoverItemVenda(ActionListener action) {
        ImageIcon icon = new ImageIcon(
                BotaoRemoverItemVenda.class.getResource("/imagens/excluir.png")
        );

        Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        JButton btn = new JButton(new ImageIcon(img));
        btn.setFocusPainted(false);
        btn.setBackground(Color.RED);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(40, 25));
        btn.addActionListener(action);
        return btn;
    }
}
