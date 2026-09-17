/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Agenda;

import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.PacienteJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.View.Componentes.TabelaEstilo;
import com.mycompany.sistemaintegramind.View.EditarExcluirAgendamento.EditarAgendamentoView;
import com.mycompany.sistemaintegramind.util.Utilitarios.BotaoRenderizarIcones;
import com.mycompany.sistemaintegramind.util.Utilitarios.StatusAgendamento;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Micro
 */
public class AgendaView extends javax.swing.JPanel {

    private List<Agendamento> agendamentos;
    private DefaultTableModel modelo;

    public AgendaView() {
        initComponents();
        //2026-09-11 Juliano: Definindo o estil da tabela
        TabelaEstilo.aplicar(tblAgendamentos);

        //2026-09-11 Juliano: Carregando os pacientes, na combox, para aparecer na listagem
        carregarPacientes();
        //2026-09-11 Juliano: Definindo, o modo de exibição da data para o formato padrão 
        btnData.setDateFormatString("dd/MM/yyyy");

        //2026-09-10 Juliano: Trazendo todos os  tipos de atendimentos que é um enumerador  para  agendamentos listagem no combox
        jcomTipoAtendimento.setModel(new DefaultComboBoxModel<>(TipoAtendimento.values()));

        btnStatusPagamento.setModel(new DefaultComboBoxModel<>(StatusPagamento.values()));

        //2026-09-11 Juliano: definindo o placeholder do campo observação
        txtObservacao.putClientProperty("JTextField.placeholderText", "Ex: Primeira consulta, retorno, etc....");

        tblAgendamentos.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("ENTROU NO MOUSE CLICKED");

                AgendamentoJPA agendamentojpa = new AgendamentoJPA();

                int linha = tblAgendamentos.rowAtPoint(e.getPoint());
                int coluna = tblAgendamentos.columnAtPoint(e.getPoint());
                System.out.println("Linha: " + linha);
                System.out.println("Coluna: " + coluna);

                if (linha >= 0 && coluna == 6) {
                    System.out.println("Clicou no relogio");
                    Long codigoAgendamentoPaciente = (Long) tblAgendamentos.getValueAt(linha, 0);
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

                        modelo.setValueAt(
                                "<html><font color='green'><b>PAGO</b></font></html>",
                                linha,
                                5
                        );
                        modelo.setValueAt(check, linha, 6);

                        tblAgendamentos.repaint();
                    }

                }
            }
        });
    }

    //========================2026-09-11 Juliano: Métodos necessários para trabalhar ao longo do arquivo AgendaView.java(INICIO)=========================
    private void carregarPacientes() {
        PacienteJPA pacientejpa = new PacienteJPA();
        List<Paciente> carregarListaPacientes = pacientejpa.listarPacientes();
        cbListaPaciente.removeAllItems();
        for (Paciente p : carregarListaPacientes) {
            cbListaPaciente.addItem(p.getNome());
        }

    }

    private void carregarAgendamento(DefaultTableModel modelo, List<Agendamento> ListarAgendamentos) {
        for (Agendamento agendamentos : ListarAgendamentos) {
            String statuspagamento = agendamentos.getStatuspagamento().toString();
            String pagamentoformatado;
            if (statuspagamento.equals("PENDENTE")) {
                pagamentoformatado = "<html><font color='red'><b>PENDENTE</b></font></html>";
            } else {
                pagamentoformatado = "<html><font color='green'><b>PAGO</b></font></html>";
            }

            modelo.addRow(new Object[]{
                agendamentos.getId(),
                agendamentos.getPaciente().getNome(),
                agendamentos.getHorario(),
                agendamentos.getStatusagendamento(),
                agendamentos.getTipoatendimento(),
                pagamentoformatado,
                "",
                agendamentos.getValorDaConsulta(),
                "",
                ""

            });
        }
    }

    private List<Agendamento> ListarAgendamentos() {
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        return agendamentojpa.listarAgendamentos();
    }

    private void LimparDadosAgendamento() {
        txtValorConsulta.setText("");
        txtObservacao.setText("");
        jcomTipoAtendimento.setSelectedIndex(0);
        btnStatusPagamento.setSelectedIndex(0);
        btnHorario.setSelectedIndex(0);
        btnData.setDate(null);
        cbListaPaciente.setSelectedIndex(0);
    }

    //===================================================(FIM)=========================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnHorario = new javax.swing.JComboBox<>();
        jcomTipoAtendimento = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtObservacao = new javax.swing.JTextField();
        cbListaPaciente = new javax.swing.JComboBox<>();
        btnData = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnRealizarAgendamento = new javax.swing.JButton();
        btnLimparAgendamento = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAgendamentos = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnStatusPagamento = new javax.swing.JComboBox<>();
        txtValorConsulta = new javax.swing.JTextField();
        btnAtualizarTabela = new javax.swing.JButton();

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        jLabel2.setText("Agendamento ");

        jLabel3.setText("Paciente");

        jLabel4.setText("Data");

        jLabel5.setText("Horário");

        btnHorario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jcomTipoAtendimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcomTipoAtendimentoActionPerformed(evt);
            }
        });

        jLabel6.setText("Observação");

        jLabel7.setText("Tipo Atendimento");

        txtObservacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObservacaoActionPerformed(evt);
            }
        });

        cbListaPaciente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setForeground(new java.awt.Color(153, 153, 255));

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel8.setText("Horários do dia 10/09/2026");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRealizarAgendamento.setText("Agendar");
        btnRealizarAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarAgendamentoActionPerformed(evt);
            }
        });

        btnLimparAgendamento.setText("Limpar");
        btnLimparAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparAgendamentoActionPerformed(evt);
            }
        });

        tblAgendamentos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAgendamentos.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblAgendamentosAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblAgendamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAgendamentosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAgendamentos);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agendar.png"))); // NOI18N

        jLabel10.setText("Valor da Consulta:");

        jLabel11.setText("Status Pagamento");

        btnAtualizarTabela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/reflesh.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(162, 162, 162)
                                                .addComponent(jLabel4)
                                                .addGap(110, 110, 110))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cbListaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(28, 28, 28)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(btnHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel3))
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(btnStatusPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(txtValorConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jcomTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnRealizarAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnLimparAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(143, 143, 143)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtObservacao, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnAtualizarTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 837, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 836, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbListaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(btnStatusPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jcomTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnRealizarAgendamento)
                                    .addComponent(btnLimparAgendamento)))
                            .addComponent(txtObservacao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAtualizarTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 358, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 359, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObservacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObservacaoActionPerformed

    private void jcomTipoAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcomTipoAtendimentoActionPerformed

    }//GEN-LAST:event_jcomTipoAtendimentoActionPerformed

    private void tblAgendamentosAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblAgendamentosAncestorAdded
        //2025-11-22 Juliano definindo o nome das colunas da tabela
        modelo = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Paciente",
                    "Horário",
                    "Status",
                    "Tipo de Atendimento",
                    "Pagamento",
                    "Situação",
                    "Valor da Consulta",
                    "Editar",
                    "Excluir"
                },
                0
        );

        tblAgendamentos.setModel(modelo);

        tblAgendamentos.getColumnModel()
                .getColumn(6).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(6)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/PENDENTE.png"));

        tblAgendamentos.getColumnModel().getColumn(8).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(8)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/empate.png"));

        tblAgendamentos.getColumnModel().getColumn(9).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(9)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/excluir.png"));

        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        List<Agendamento> ListarAgendamentos = agendamentojpa.listarAgendamentos();
        modelo.setRowCount(0);

        carregarAgendamento(modelo, ListarAgendamentos);


    }//GEN-LAST:event_tblAgendamentosAncestorAdded

    private void btnLimparAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparAgendamentoActionPerformed
        LimparDadosAgendamento();
    }//GEN-LAST:event_btnLimparAgendamentoActionPerformed

    private void btnRealizarAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarAgendamentoActionPerformed
        Agendamento agendamento = new Agendamento();
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        PacienteJPA pacientejpa = new PacienteJPA();

        //2026-09-14 Juliano: Validação de data, para poder evitar cadastrar um agendamento sem data
        if (btnData.getDate() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma data."
            );
            return;
        }

        agendamento.setDataAgendamento(
                btnData.getDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );
        agendamento.setObservacao(txtObservacao.getText());

        //2026-09-14 Juliano: Validação de valor, se caso não digitar nenhum valor ele entende como uma STRING, então eu preciso colocar essa validação para forçar agendamentos entrada de valor, além de que não existe consulta sem valor
        if (txtValorConsulta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um valor para o agendamento da consulta para poder continuar."
            );
            return;
        }
        String valor = txtValorConsulta.getText().trim();
        BigDecimal valorConsulta = new BigDecimal(valor.replace(",", "."));
        agendamento.setValorDaConsulta(valorConsulta);
        agendamento.setHorario(LocalTime.MIN);
        agendamento.setTipoatendimento((TipoAtendimento) jcomTipoAtendimento.getSelectedItem());

        String PacienteNome = (String) cbListaPaciente.getSelectedItem();
        Paciente paciente = pacientejpa.buscarPorNome(PacienteNome);

        agendamento.setPaciente(paciente);
        agendamento.setStatuspagamento((StatusPagamento) btnStatusPagamento.getSelectedItem());
        agendamento.setStatusagendamento(StatusAgendamento.AGENDADO);
        agendamento.setStatusPacienteAgendamento(StatusPacienteAgendamento.ATIVO);

        System.out.println("============Dados cadastrado no agendamento =================");
        System.out.println("Paciente: " + agendamento.getPaciente().getNome());
        System.out.println("Valor: " + agendamento.getValorDaConsulta());
        System.out.println("Data: " + agendamento.getDataAgendamento());
        System.out.println("Tipo: " + agendamento.getTipoatendimento());
        System.out.println("Pagamento: " + agendamento.getStatuspagamento());
        System.out.println("======================================================");

        agendamentojpa.cadastrarAgendamento(agendamento);

        JOptionPane.showMessageDialog(
                this,
                "Agendamento cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon(getClass().getResource("/imagens/Alertas/sucesso.png"))
        );
        modelo.setRowCount(0);
        agendamentos = ListarAgendamentos();
        carregarAgendamento(modelo, agendamentos);
        LimparDadosAgendamento();
    }//GEN-LAST:event_btnRealizarAgendamentoActionPerformed

    private void tblAgendamentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgendamentosMouseClicked

        int linha = tblAgendamentos.rowAtPoint(evt.getPoint());
        int coluna = tblAgendamentos.columnAtPoint(evt.getPoint());

        Agendamento agendamento = new Agendamento();
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();

        if (linha >= 0 && coluna == 8) {
            System.out.println("Clicou para editar um registro já agendado");
            Long CodigoId = (Long) tblAgendamentos.getValueAt(linha, 0);
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
        }


    }//GEN-LAST:event_tblAgendamentosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizarTabela;
    private com.toedter.calendar.JDateChooser btnData;
    private javax.swing.JComboBox<String> btnHorario;
    private javax.swing.JButton btnLimparAgendamento;
    private javax.swing.JButton btnRealizarAgendamento;
    private javax.swing.JComboBox<StatusPagamento> btnStatusPagamento;
    private javax.swing.JComboBox<String> cbListaPaciente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<TipoAtendimento> jcomTipoAtendimento;
    private javax.swing.JTable tblAgendamentos;
    private javax.swing.JTextField txtObservacao;
    private javax.swing.JTextField txtValorConsulta;
    // End of variables declaration//GEN-END:variables
}
