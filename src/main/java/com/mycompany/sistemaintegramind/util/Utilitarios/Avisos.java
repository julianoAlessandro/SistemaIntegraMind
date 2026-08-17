/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Micro
 */
public class Avisos {
    
    public static boolean validarItemNaVenda(JTable tabelaVenda){
     if (tabelaVenda.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    tabelaVenda,
                   "Informe um item na venda para poder finalizar a venda.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
      return true;
    }
}
