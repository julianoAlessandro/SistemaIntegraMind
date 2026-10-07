/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.EvolucaoClinicaJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author guilh
 */
public class FichaProntuarioPacienteView extends javax.swing.JPanel {

    private AgendamentoJPA agendamentojpa = new AgendamentoJPA();
    private Paciente paciente;
    private Agendamento agendamentoDoDiaDoPaciente;

    public FichaProntuarioPacienteView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
        txtEvolucaoSessao.putClientProperty("JTextArea.placeholderText",
                "Descreva os principais aspectos, acontecimentos e evolução observados durante a sessão.");
        txtObservacao.putClientProperty("JTextArea.placeholderText",
                "Registre informações complementares ou observações relevantes sobre a sessão.");

        EvolucaoClinicaJPA evolucaoJPA = new EvolucaoClinicaJPA();

        List<EvolucaoClinica> evolucoes = evolucaoJPA.listarEvolucoesPorPaciente(paciente.getId());

        for (EvolucaoClinica evolucao : evolucoes) {

            pnlListaEvolucoes.add(listarHistoricoEvolucaoClinicaPaciente(evolucao)
            );
        }

        //2026-10-02 Juliano: Organização dos futuros cards que serão adicionados
        pnlListaEvolucoes.setLayout(new BoxLayout(pnlListaEvolucoes, BoxLayout.Y_AXIS));

        pnlListaEvolucoes.revalidate();
        pnlListaEvolucoes.repaint();

        agendamentoDoDiaDoPaciente = agendamentojpa.buscarAgendamentoDoDia(paciente);

        if (agendamentoDoDiaDoPaciente == null) {

            txtDataAgendamento.setText("Nenhum agendamento hoje.");
            txtHorario.setText("Nenhum agendamento hoje.");
            txtTipoAtendimento.setText("Nenhum agendamento hoje");

            txtDataAgendamento.setEnabled(false);
            txtHorario.setEnabled(false);
            txtTipoAtendimento.setEnabled(false);
            txtEvolucaoSessao.setEnabled(false);
            txtObservacao.setEnabled(false);

        } else if (!agendamentoDoDiaDoPaciente.getDataAgendamento().equals(LocalDate.now())) {

            txtDataAgendamento.setText("Nenhum agendamento hoje.");
            txtHorario.setText("Nenhum agendamento hoje.");
            txtTipoAtendimento.setText("Nenhum agendamento hoje");

            txtDataAgendamento.setEnabled(false);
            txtHorario.setEnabled(false);
            txtTipoAtendimento.setEnabled(false);
            txtEvolucaoSessao.setEnabled(false);
            txtObservacao.setEnabled(false);

        } else {

            String data = agendamentoDoDiaDoPaciente.getDataAgendamento()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String horario = agendamentoDoDiaDoPaciente.getHorario()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));

            txtDataAgendamento.setText(data);
            txtHorario.setText(horario);
            txtTipoAtendimento.setText(
                    agendamentoDoDiaDoPaciente.getTipoatendimento().toString()
            );

            txtDataAgendamento.setEnabled(true);
            txtHorario.setEnabled(true);
            txtTipoAtendimento.setEnabled(true);
            txtEvolucaoSessao.setEnabled(true);
            txtObservacao.setEnabled(true);
        }

    }

    @SuppressWarnings("unchecked")


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtDataAgendamento = new javax.swing.JTextField();
        txtHorario = new javax.swing.JTextField();
        txtTipoAtendimento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEvolucaoSessao = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacao = new javax.swing.JTextArea();
        btnLimparEvolucao = new javax.swing.JButton();
        btnSalvarEvolucao = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        scrListaEvolucoes = new javax.swing.JScrollPane();
        pnlListaEvolucoes = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jLabel2.setText("Evolução da Sessão");

        jLabel3.setText("Registre o que foi trabalhado durante a consulta.");

        txtEvolucaoSessao.setColumns(20);
        txtEvolucaoSessao.setRows(5);
        jScrollPane1.setViewportView(txtEvolucaoSessao);

        jLabel4.setText("Observações");

        txtObservacao.setColumns(20);
        txtObservacao.setRows(5);
        jScrollPane2.setViewportView(txtObservacao);

        btnLimparEvolucao.setText("Limpar");
        btnLimparEvolucao.addActionListener(this::btnLimparEvolucaoActionPerformed);

        btnSalvarEvolucao.setText("Salvar evolução");
        btnSalvarEvolucao.addActionListener(this::btnSalvarEvolucaoActionPerformed);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("Nova Evolução Clínica");

        pnlListaEvolucoes.setBackground(new java.awt.Color(204, 51, 0));
        pnlListaEvolucoes.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                pnlListaEvolucoesAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout pnlListaEvolucoesLayout = new javax.swing.GroupLayout(pnlListaEvolucoes);
        pnlListaEvolucoes.setLayout(pnlListaEvolucoesLayout);
        pnlListaEvolucoesLayout.setHorizontalGroup(
            pnlListaEvolucoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );
        pnlListaEvolucoesLayout.setVerticalGroup(
            pnlListaEvolucoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 751, Short.MAX_VALUE)
        );

        scrListaEvolucoes.setViewportView(pnlListaEvolucoes);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Histórico das notas Clínicas ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(txtHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(txtTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(btnLimparEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSalvarEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(scrListaEvolucoes, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(149, 149, 149))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jLabel3)
                    .addContainerGap(686, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jLabel5)
                    .addContainerGap(758, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHorario, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(txtTipoAtendimento)
                    .addComponent(txtDataAgendamento))
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimparEvolucao)
                    .addComponent(btnSalvarEvolucao))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrListaEvolucoes)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(741, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jLabel5)
                    .addContainerGap(763, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarEvolucaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarEvolucaoActionPerformed
        EvolucaoClinica evolucaoclinica = new EvolucaoClinica();
        EvolucaoClinicaJPA evolucaoclinicajpa = new EvolucaoClinicaJPA();

        //2026-10-01 Juliano: enviando os dados para ser cadastrado no banco
        evolucaoclinica.setAgendamento(agendamentoDoDiaDoPaciente);
        evolucaoclinica.setEvolucaoSessao(txtEvolucaoSessao.getText());
        evolucaoclinica.setObservacoes(txtObservacao.getText());

        evolucaoclinicajpa.cadastrarEvolucaoClinica(evolucaoclinica);
        Limpar();
        atualizarHistoricoEvolucaoClinica();
        JOptionPane.showMessageDialog(this, "Prontuário do paciente cadastrado com sucesso!!!");
    }//GEN-LAST:event_btnSalvarEvolucaoActionPerformed

    private void btnLimparEvolucaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparEvolucaoActionPerformed
        Limpar();
    }//GEN-LAST:event_btnLimparEvolucaoActionPerformed

    private void pnlListaEvolucoesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_pnlListaEvolucoesAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlListaEvolucoesAncestorAdded

    private JPanel listarHistoricoEvolucaoClinicaPaciente(EvolucaoClinica evolucao) {
        Agendamento agendamento = evolucao.getAgendamento();

        JPanel card = new JPanel();

        card.setLayout(new BorderLayout(10, 5));

        card.setPreferredSize(new Dimension(300, 120));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel lblData = new JLabel(agendamento.getDataAgendamento().toString() + " - " + agendamento.getHorario().toString());
        JLabel lblTipo = new JLabel(agendamento.getTipoatendimento().toString());

        JTextArea txtEvolucao = new JTextArea(
                evolucao.getEvolucaoSessao()
        );

        txtEvolucao.setLineWrap(true);
        txtEvolucao.setWrapStyleWord(true);
        txtEvolucao.setEditable(false);

        JPanel painelCabecalho = new JPanel(new BorderLayout());

        painelCabecalho.add(lblData, BorderLayout.WEST);
        painelCabecalho.add(lblTipo, BorderLayout.EAST);

        card.add(painelCabecalho, BorderLayout.NORTH);
        card.add(txtEvolucao, BorderLayout.CENTER);

        return card;
    }

    private void Limpar() {
        txtEvolucaoSessao.setText("");
        txtObservacao.setText("");
    }

    private void atualizarHistoricoEvolucaoClinica() {
        pnlListaEvolucoes.removeAll();

        EvolucaoClinicaJPA evolucaoJPA = new EvolucaoClinicaJPA();

        List<EvolucaoClinica> evolucoes = evolucaoJPA.listarEvolucoesPorPaciente(paciente.getId());

        for (EvolucaoClinica evolucao : evolucoes) {
            pnlListaEvolucoes.add(listarHistoricoEvolucaoClinicaPaciente(evolucao));
        }

        pnlListaEvolucoes.revalidate();
        pnlListaEvolucoes.repaint();

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimparEvolucao;
    private javax.swing.JButton btnSalvarEvolucao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlListaEvolucoes;
    private javax.swing.JScrollPane scrListaEvolucoes;
    private javax.swing.JTextField txtDataAgendamento;
    private javax.swing.JTextArea txtEvolucaoSessao;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextArea txtObservacao;
    private javax.swing.JTextField txtTipoAtendimento;
    // End of variables declaration//GEN-END:variables
}
