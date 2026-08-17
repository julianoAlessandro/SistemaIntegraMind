/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import com.mycompany.sistemaintegramind.util.Utilitarios.EstilizarBotaoFecharVenda;
import com.mycompany.sistemaintegramind.Model.Service.interfaces.PossuiAbas;
import com.mycompany.sistemaintegramind.View.MainMenu;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class GerenciadorAbasVenda {
    /*

    public static void abrirNovaVenda(JTabbedPane Venda, MainMenu menu) {

        //2026-07-13 Juliano: Criando uma nova venda
        //Vendas novaVenda = new Vendas();
        //PainelSimplesSemSubGuiasView novoPainel = new PainelSimplesSemSubGuiasView(novaVenda, Venda, menu);

        //2026-02-16 Juliano: Permite scrollar, para ver as vendas no mesmo nível sem criar uma venda sobre a outra
        Venda.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

       // Venda.addTab("NOVA VENDA", novoPainel);
       // int index = Venda.indexOfComponent(novoPainel);

        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tabPanel.setOpaque(false);

        //2026-02-16 Juliano: Uitlizando o o estilo vindo da guia Venda
        JLabel lblTitulo = new JLabel(Venda.getTitleAt(index));
        lblTitulo.setFont(UIManager.getFont("TabbedPane.font")); //2026-02-16 Juliano: mesma fonte do sistema
        JButton btnFechar = EstilizarBotaoFecharVenda.criarBotaoFechar();

        //2026-02-16 Juliano: efeito para escurecer o X, ao passar o mouse
        btnFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFechar.setForeground(new Color(180, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFechar.setForeground(Color.RED);
            }
        });

        //2026-02-16 Juliano: Fechando a guia Janela ao clicar no x
        btnFechar.addActionListener(e -> {
            Venda.remove(novoPainel);
        });

        tabPanel.add(lblTitulo);
        tabPanel.add(Box.createHorizontalStrut(5));
        tabPanel.add(btnFechar);

        Venda.setTabComponentAt(index, tabPanel);
        Venda.setSelectedComponent(novoPainel);
    }

    public static void fecharVendaAtualEAbrirNova(PossuiAbas tela) {

        JTabbedPane abas = tela.getVenda();
        MainMenu menu = tela.getMainMenu();

        int abaAtual = abas.getSelectedIndex();

        abrirNovaVenda(abas,menu);

        abas.remove(abaAtual);

    }

*/
}
