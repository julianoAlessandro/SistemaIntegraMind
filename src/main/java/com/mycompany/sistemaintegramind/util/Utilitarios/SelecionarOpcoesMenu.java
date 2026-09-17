/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import javax.swing.JButton;

public class SelecionarOpcoesMenu {
     public  static void selecionarBotao(JButton botao, JButton botaoSelecionado) {
        //2025-12-22 Guilherme: Guarda referência do botão anteriormente selecionado
        JButton antigo = botaoSelecionado;

        //2025-12-22 Guilherme: Atualiza o botão atualmente selecionado
        botaoSelecionado = botao;

        //2025-12-22 Guilherme: Repaint do botão antigo para remover o destaque
        if (antigo != null) {
            antigo.repaint();
        }

        //2025-12-22 Guilherme: Repaint do novo botão para aplicar o destaque visual
        if (botaoSelecionado != null) {
            botaoSelecionado.repaint();
        }
    }
}
