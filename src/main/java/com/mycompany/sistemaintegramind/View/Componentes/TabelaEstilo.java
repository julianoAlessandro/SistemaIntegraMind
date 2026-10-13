/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Componentes;

import com.mycompany.sistemaintegramind.util.Utilitarios.StatusCellRenderer;
import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.JTableHeader;

/**
 *
 * @author guilh
 */
public class TabelaEstilo {

    public static void aplicar(JTable tabela) {
        // 1. Estilo Geral
        tabela.setRowHeight(45);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.putClientProperty("FlatLaf.style", "selectionBackground: #008A3B; selectionForeground: #ffffff;");
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 2. Cabeçalho
        JTableHeader header = tabela.getTableHeader();

        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);
        header.setOpaque(true);
        header.setBackground(new Color(3, 62, 29));
        header.setForeground(Color.WHITE);
    }

    public static void aplicarEstiloDois(JTable tabela) {
        // 1. Estilo Geral da Tabela
        tabela.setRowHeight(45);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Cor de seleção da linha
        tabela.putClientProperty("FlatLaf.style", "selectionBackground: #008A3B; selectionForeground: #ffffff;");

        // Renderer de status
        try {
            tabela.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        } catch (Exception e) {
            // Ignora caso a coluna não exista no momento
        }

        // 2. Cabeçalho com o verde padrão do seu tema
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        header.putClientProperty("FlatLaf.style",
                "background: #033E1D; "
                + "foreground: #ffffff; "
                + "separatorColor: #033E1D;");

        // 3. JScrollPane com Borda Redonda e Recorte Nativo
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, tabela);
        if (scrollPane != null) {
            // Define o raio da borda arredondada e a cor da borda cinza sutil
            scrollPane.putClientProperty("FlatLaf.style", "arc: 15; borderColor: #d2d2d2;");

            // Garencia o fundo interno branco para os dados da tabela
            scrollPane.getViewport().setBackground(Color.WHITE);
        }
    }
}
