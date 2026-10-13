/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

/**
 *
 * @author guilh
 */
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        String status = (value != null) ? value.toString() : "";
        
        // Estilização baseada no status (FlatLaf suporta bordas arredondadas e cores)
        if (status.equalsIgnoreCase("PAGO") || status.equalsIgnoreCase("REALIZADO") || status.equalsIgnoreCase("AGENDADO")) {
            label.setForeground(new Color(15, 118, 110)); // Verde escuro texto
            // Se quiser usar fundo tipo pílula, você pode retornar um JPanel com JLabel dentro
        } else if (status.equalsIgnoreCase("PENDENTE")) {
            label.setForeground(new Color(185, 28, 28)); // Vermelho escuro texto
        }
        
        return label;
    }
}