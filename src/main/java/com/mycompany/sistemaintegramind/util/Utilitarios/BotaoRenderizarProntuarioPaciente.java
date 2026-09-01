/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Micro
 */
public class BotaoRenderizarProntuarioPaciente extends JButton implements TableCellRenderer {

    public BotaoRenderizarProntuarioPaciente() {

        setIcon(new ImageIcon(
                BotaoRenderizarProntuarioPaciente.class
                        .getResource("/imagens/prontuario.png")
        ));

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        return this;
    }
}
