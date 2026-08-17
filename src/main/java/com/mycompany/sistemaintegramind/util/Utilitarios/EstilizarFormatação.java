/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.Color;
import javax.swing.JTextField;

public class EstilizarFormatação {

    public static void formatarestilototalvendas(double total, JTextField txtTotal) {
        txtTotal.setText("R$ " + String.format("%.2f", total));
        txtTotal.setForeground(Color.RED);
    }
}

