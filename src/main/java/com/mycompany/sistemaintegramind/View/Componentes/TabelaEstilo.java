/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author guilh
 */
public class TabelaEstilo {

    public static void aplicar(JTable tabela) {
        // 1. Estilo Geral
        tabela.setRowHeight(35);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.putClientProperty("FlatLaf.style", "selectionBackground: #6495ED; selectionForeground: #ffffff;");
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 2. Cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        header.setBackground(new Color(58, 110, 242));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

      
    }
}
