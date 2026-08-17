package com.mycompany.sistemaintegramind.util.Utilitarios;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BotaoRenderizarIconeExcluirItemVenda extends JButton implements TableCellRenderer {

    public BotaoRenderizarIconeExcluirItemVenda() {
        setIcon(new ImageIcon(
           BotaoRenderizarIconeExcluirItemVenda .class.getResource("/imagens/excluir.png")
        ));

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column) {
        return this;
    }
}