/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.View.Componentes.TabelaEstilo;
import com.mycompany.sistemaintegramind.View.EditarExcluirAgendamento.EditarAgendamentoView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author guilh
 */
public class FichaAgendamentoView extends javax.swing.JPanel {

    private DefaultTableModel modeloTabelaListarTodosAgendamentos;
    private AgendamentoJPA agendamentojpa = new AgendamentoJPA();
    private Paciente paciente;

    public FichaAgendamentoView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
        TabelaEstilo.aplicar(tblListarAgendamentosDeUmPaciente);
        
         tblListarAgendamentosDeUmPaciente.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("ENTROU NO MOUSE CLICKED");

                AgendamentoJPA agendamentojpa = new AgendamentoJPA();

                int linha = tblListarAgendamentosDeUmPaciente.rowAtPoint(e.getPoint());
                int coluna = tblListarAgendamentosDeUmPaciente.columnAtPoint(e.getPoint());
                System.out.println("Linha: " + linha);
                System.out.println("Coluna: " + coluna);

                if (linha >= 0 && coluna == 6) {
                    System.out.println("Clicou no relogio");
                    Long codigoAgendamentoPaciente = (Long) tblListarAgendamentosDeUmPaciente.getValueAt(linha, 0);
                    System.out.println("Código detectado ----> " + codigoAgendamentoPaciente);
                    Agendamento agendamentoatualizar = agendamentojpa.buscarPorId(codigoAgendamentoPaciente);
                    if (agendamentoatualizar.getStatuspagamento() == StatusPagamento.PENDENTE) {
                        agendamentoatualizar.setStatuspagamento(StatusPagamento.PAGO);
                        agendamentojpa.atualizarAgendamento(agendamentoatualizar);
                        System.out.println("Status alterado para PAGO com sucesso!!!");
                        //2026-09-14 Juliano: Alterando visualmente o icone e o texto
                        ImageIcon check = new ImageIcon(
                                getClass().getResource("/imagens/certoAgendamento.png")
                        );

                        modeloTabelaListarTodosAgendamentos.setValueAt(
                                "<html><font color='green'><b>PAGO</b></font></html>",
                                linha,
                                5
                        );
                        modeloTabelaListarTodosAgendamentos.setValueAt(check, linha, 6);

                        tblListarAgendamentosDeUmPaciente.repaint();
                    }

                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblListarAgendamentosDeUmPaciente = new javax.swing.JTable();

        tblListarAgendamentosDeUmPaciente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblListarAgendamentosDeUmPaciente.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblListarAgendamentosDeUmPacienteAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblListarAgendamentosDeUmPaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListarAgendamentosDeUmPacienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListarAgendamentosDeUmPaciente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void tblListarAgendamentosDeUmPacienteAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblListarAgendamentosDeUmPacienteAncestorAdded

        //2026-10-03 Juliano: definindo os campos que estarão na minha tabela
        modeloTabelaListarTodosAgendamentos = GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.formatarTabelaAgendamento(modeloTabelaListarTodosAgendamentos);

        //2026-10-03 Juliano: Utilizando a definição criada anteriormente
        tblListarAgendamentosDeUmPaciente.setModel(modeloTabelaListarTodosAgendamentos);

        //2026-10-03 Juliano: Buscando os agendamentos
        List<Agendamento> agendamentos = agendamentojpa.listarAgendamentosPorPacientes(paciente);

        // 2026-10-03 Juliano: Limpando as linhas existentes
        modeloTabelaListarTodosAgendamentos.setRowCount(0);

        //2026-10-03 Juliano: Definindo os icones presentes na minha tabela
        GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.configurarIconesDasColunasDaTabelaAgendamento(tblListarAgendamentosDeUmPaciente);

        //2026-10-03 Juliano: Carregando os agendamentos na tabela
        GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
    }//GEN-LAST:event_tblListarAgendamentosDeUmPacienteAncestorAdded

    private void tblListarAgendamentosDeUmPacienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListarAgendamentosDeUmPacienteMouseClicked
        int linha = tblListarAgendamentosDeUmPaciente.rowAtPoint(evt.getPoint());
        int coluna = tblListarAgendamentosDeUmPaciente.columnAtPoint(evt.getPoint());
        Long CodigoId = (Long) tblListarAgendamentosDeUmPaciente.getValueAt(linha, 0);

        Agendamento agendamento = new Agendamento();
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();

        //2026-09-18 Juliano: Editar um registro já agendado
        if (linha >= 0 && coluna == 9) {
            System.out.println("Clicou para editar um registro já agendado");

            Agendamento objetoAgendamentoLinhaSelecionado = agendamentojpa.buscarPorId(CodigoId);

            //2026-09-14 Juliano: Abrindo o modal com a referência do objeto selecionado
            EditarAgendamentoView telaeditar = new EditarAgendamentoView(objetoAgendamentoLinhaSelecionado);

            JDialog dialog = new JDialog();
            dialog.setTitle("Editar Agendamento do Paciente - " + objetoAgendamentoLinhaSelecionado.getPaciente().getNome());
            dialog.setModal(true);
            dialog.setContentPane(telaeditar);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.atualizartabelaAgendamento(new ArrayList<>(), modeloTabelaListarTodosAgendamentos, tblListarAgendamentosDeUmPaciente, paciente);

        } //2026-09-18 Juliano: Excluindo um registro já cadastrado
        else if (linha >= 0 && coluna == 10) {
            System.out.println("Clicou para poder excluir um agendamento!!!");
            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Você realmente deseja excluir este Agendamento?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacao == YES_NO_OPTION) {
                Agendamento agendamentoexclusao = agendamentojpa.buscarPorId(CodigoId);
                agendamentoexclusao.setStatusPacienteAgendamento(StatusPacienteAgendamentoDocumento.INATIVO);
                agendamentojpa.atualizarAgendamento(agendamentoexclusao);
                GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.atualizartabelaAgendamento(new ArrayList<>(), modeloTabelaListarTodosAgendamentos, tblListarAgendamentosDeUmPaciente, paciente);
                JOptionPane.showMessageDialog(
                        this,
                        "Agendamento excluído com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

            }

        }
    }//GEN-LAST:event_tblListarAgendamentosDeUmPacienteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListarAgendamentosDeUmPaciente;
    // End of variables declaration//GEN-END:variables
}
