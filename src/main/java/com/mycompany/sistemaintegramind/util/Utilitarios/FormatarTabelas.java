/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FormatarTabelas {

    public static DefaultTableModel formatarTabelaProdutosModal() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Id",
                    "Nome",
                    "Código",
                    "Quantidade",
                    "Preço"
                },
                0
        );

        return modelo;
    }

    public static DefaultTableModel formatarTabelaClientesModal() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Id",
                    "Nome",
                    "Nome_Fantasia"

                },
                0
        );

        return modelo;
    }

    public static DefaultTableModel formatarTabelaVendas() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Nome",
                    "Quantidade",
                    "Preço",
                    "SubTotal"

                },
                0
        );

        return modelo;
    }

    public static DefaultTableModel formatarTabelaControleEstoque() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Produto",
                    "Data_Criação",
                    "Data_Atualização",
                    "Quantidade",
                    "Movimentação"

                },
                0
        );

        return modelo;
    }

    public static DefaultTableModel formatarTabelaListarDocumentosSaida() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Venda",
                    "Cliente",
                    "Venda_Realizada"

                },
                0
        );

        return modelo;
    }

    public static DefaultTableModel formatarTabelaLançamentoVenda(JTable tblListarPagamentos) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Cliente",
                    "Meio_Pagamento",
                    "Valor"

                },
                0
        );
        tblListarPagamentos.setModel(modelo);


        return modelo;
    }

}
