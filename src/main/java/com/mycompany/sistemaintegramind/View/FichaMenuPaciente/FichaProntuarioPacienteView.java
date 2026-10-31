/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.FichaProntuarioPacienteFiltro;
import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.EvolucaoClinicaJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.InativarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.RetificarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.RetificarNotaClinica;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class FichaProntuarioPacienteView extends javax.swing.JPanel {

    private AgendamentoJPA agendamentojpa = new AgendamentoJPA();
    private Paciente paciente;
    private Agendamento agendamentoDoDiaDoPaciente;
    private EvolucaoClinica notaOriginal;
    private java.awt.Window janelaPai = javax.swing.SwingUtilities.getWindowAncestor(this);
    private RetificarNotaClinica retificacaoPendente;
    private boolean retificacaoEmAndamento = false;
    private InativarNotaClinicaJPA inativacaoJPA = new InativarNotaClinicaJPA();

    public FichaProntuarioPacienteView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
        txtPesquisarNotaClinicaPelaEvolucao.putClientProperty("JTextField.placeholderText", "Digite a nota clinica  que deseja encontrar...");
        //2026-10-29 Juliano: Campos apenas para  visualização
        txtDataAgendamento.setEditable(false);
        txtDataAgendamento.setBackground(java.awt.Color.WHITE);
        txtDataAgendamento.setForeground(java.awt.Color.BLACK);
        txtHorario.setEditable(false);
        txtHorario.setBackground(java.awt.Color.WHITE);
        txtHorario.setForeground(java.awt.Color.BLACK);

        cmbListarNotaClinica.setModel(new DefaultComboBoxModel<>(TipoNotaClinica.values()));

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
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtDataAgendamento.setText(dataFormatada);
        LocalTime horarioAtual = LocalTime.now();

        String horarioFormatado = horarioAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtHorario.setText(horarioFormatado);

    }

    @SuppressWarnings("unchecked")


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtDataAgendamento = new javax.swing.JTextField();
        txtHorario = new javax.swing.JTextField();
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
        cmbListarNotaClinica = new javax.swing.JComboBox<>();
        txtPesquisarNotaClinicaPelaEvolucao = new javax.swing.JTextField();
        btnBuscarNotaClinica = new javax.swing.JButton();
        btnLimparPesquisa = new javax.swing.JButton();

        jLabel2.setText("Evolução da Sessão");

        txtDataAgendamento.addActionListener(this::txtDataAgendamentoActionPerformed);

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
            .addGap(0, 615, Short.MAX_VALUE)
        );
        pnlListaEvolucoesLayout.setVerticalGroup(
            pnlListaEvolucoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 751, Short.MAX_VALUE)
        );

        scrListaEvolucoes.setViewportView(pnlListaEvolucoes);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Histórico das notas Clínicas ");

        btnBuscarNotaClinica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa.png"))); // NOI18N
        btnBuscarNotaClinica.addActionListener(this::btnBuscarNotaClinicaActionPerformed);

        btnLimparPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        btnLimparPesquisa.addActionListener(this::btnLimparPesquisaActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(158, 158, 158))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(txtHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbListarNotaClinica, 0, 123, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(133, 133, 133)
                                        .addComponent(btnLimparEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSalvarEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPesquisarNotaClinicaPelaEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarNotaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrListaEvolucoes, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jLabel3)
                    .addContainerGap(802, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jLabel5)
                    .addContainerGap(874, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBuscarNotaClinica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPesquisarNotaClinicaPelaEvolucao)
                    .addComponent(btnLimparPesquisa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrListaEvolucoes, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbListarNotaClinica, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(txtHorario, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
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
                    .addComponent(btnSalvarEvolucao)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(748, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jLabel5)
                    .addContainerGap(770, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarEvolucaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarEvolucaoActionPerformed
        // Verifica se existe uma retificação pendente
        if (retificacaoEmAndamento && retificacaoPendente == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível recuperar os dados da retificação.",
                    "Erro na retificação",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Cria uma nova instância da nota clínica
        EvolucaoClinica evolucaoclinica = new EvolucaoClinica();

        EvolucaoClinicaJPA evolucaoclinicajpa = new EvolucaoClinicaJPA();

        RetificarNotaClinicaJPA retificarjpa = new RetificarNotaClinicaJPA();

        // Preenche os dados da nova nota clínica
        evolucaoclinica.setPaciente(paciente);

        evolucaoclinica.setEvolucaoSessao(txtEvolucaoSessao.getText());

        evolucaoclinica.setObservacoes(txtObservacao.getText());

        evolucaoclinica.setDataEvolucao(LocalDate.now());

        evolucaoclinica.setHorario(LocalTime.now());

        evolucaoclinica.setStatusnotaclinica(StatusNotaClinica.ATIVA);

        evolucaoclinica.setTiponotaclinica((TipoNotaClinica) cmbListarNotaClinica.getSelectedItem());

        // Primeiro, salva a nova nota clínica no banco de dados
        evolucaoclinicajpa.cadastrarEvolucaoClinica(evolucaoclinica);

        System.out.println(
                "Nova nota clínica criada. ID: "
                + evolucaoclinica.getId()
        );

        // Se houver uma retificação em andamento, salva o vínculo
        if (retificacaoEmAndamento && retificacaoPendente != null) {

            // Associa a nova nota à retificação
            retificacaoPendente.setNotaRetificadora(evolucaoclinica);

            // Persiste a retificação após salvar a nova nota
            retificarjpa.cadastrarRetificacaoNota(retificacaoPendente);
            System.out.println("Retificação vinculada à nova nota clínica.");

            //2026-10-28 Juliano: Atualiza o status da nota antiga para retificada
            notaOriginal.setStatusnotaclinica(StatusNotaClinica.RETIFICADA);
            evolucaoclinicajpa.AtualizarStatusNotaClinica(notaOriginal);
            System.out.println("Status da nota alterada para retificada");

            // Finaliza o processo de retificação
            retificacaoPendente = null;

            retificacaoEmAndamento = false;

            notaOriginal = null;
        }

        // Atualiza o histórico de notas clínicas
        Limpar();

        atualizarHistoricoEvolucaoClinica();

        JOptionPane.showMessageDialog(
                this,
                "Prontuário do paciente cadastrado com sucesso!"
        );
    }//GEN-LAST:event_btnSalvarEvolucaoActionPerformed

    private void btnLimparEvolucaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparEvolucaoActionPerformed
        Limpar();
    }//GEN-LAST:event_btnLimparEvolucaoActionPerformed

    private void pnlListaEvolucoesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_pnlListaEvolucoesAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlListaEvolucoesAncestorAdded

    private void txtDataAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataAgendamentoActionPerformed

    }//GEN-LAST:event_txtDataAgendamentoActionPerformed

    private void btnBuscarNotaClinicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarNotaClinicaActionPerformed
        FichaProntuarioPacienteFiltro filtro = new FichaProntuarioPacienteFiltro();
        EvolucaoClinicaJPA evolucaoclinicajpa = new EvolucaoClinicaJPA();
        if (!txtPesquisarNotaClinicaPelaEvolucao.getText().trim().isEmpty()) {
            filtro.setEvolucaoSessao(txtPesquisarNotaClinicaPelaEvolucao.getText());

        }
        filtro.setPacienteId(paciente.getId());

        List<EvolucaoClinica> listarNotasClinicasFiltradas = evolucaoclinicajpa.filtrarEvolucaoClinicaPacientes(filtro);
        atualizarCardsEvolucaoClinica(listarNotasClinicasFiltradas);


    }//GEN-LAST:event_btnBuscarNotaClinicaActionPerformed

    private void btnLimparPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparPesquisaActionPerformed
        txtPesquisarNotaClinicaPelaEvolucao.setText("");
        atualizarHistoricoEvolucaoClinica();
    }//GEN-LAST:event_btnLimparPesquisaActionPerformed

    private JPanel listarHistoricoEvolucaoClinicaPaciente(EvolucaoClinica evolucao) {

        // Painel externo que contém o card e a justificativa
        JPanel painelContainer = new JPanel();
        painelContainer.setLayout(new BoxLayout(painelContainer, BoxLayout.Y_AXIS));
        painelContainer.setOpaque(false);
        painelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelContainer.setPreferredSize(new Dimension(600, 192));

        // Configurações do card
        JPanel card = new JPanel(new BorderLayout(10, 8));

        card.setPreferredSize(new Dimension(600, 140));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Data e horário
        JLabel lblData = new JLabel(
                evolucao.getDataEvolucao() + " - " + evolucao.getHorario()
        );
        lblData.setFont(new Font("Arial", Font.BOLD, 13));

        // ID e tipo da nota
        JLabel lblTipo = new JLabel(
                "ID da nota: " + evolucao.getId()
                + " # | " + evolucao.getTiponotaclinica()
        );

        // Status da nota
        JLabel lblStatusNotaClinica = new JLabel(
                evolucao.getStatusnotaclinica().toString()
        );

        // Cabeçalho
        JPanel painelCabecalho = new JPanel(new BorderLayout(10, 0));
        painelCabecalho.add(lblData, BorderLayout.WEST);
        painelCabecalho.add(lblTipo, BorderLayout.CENTER);
        painelCabecalho.add(lblStatusNotaClinica, BorderLayout.EAST);
        painelCabecalho.setOpaque(false);

        // Conteúdo da evolução clínica
        JTextArea txtEvolucao = new JTextArea(
                evolucao.getEvolucaoSessao()
        );

        txtEvolucao.setLineWrap(true);
        txtEvolucao.setWrapStyleWord(true);
        txtEvolucao.setEditable(false);
        txtEvolucao.setOpaque(false);
        txtEvolucao.setFont(new Font("Arial", Font.PLAIN, 12));

        // Painel dos botões
        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 5, 0)
        );
        painelBotoes.setOpaque(false);

        JButton btnVisualizar = new JButton("Visualizar");
        JButton btnRetificar = new JButton("Retificar");
        JButton btnInativar = new JButton("Inativar");

        //2026-10-30 Juliano: Verifica se a nota está inativa
        boolean notaInativa = evolucao.getStatusnotaclinica() == StatusNotaClinica.INATIVA;
        //2026-10-30 Juliano: Verifica se a nota está  retifcada
        boolean notaRetificada = evolucao.getStatusnotaclinica() == StatusNotaClinica.RETIFICADA;

        // Desabilita os botões de retificação e inativação
        // quando a nota estiver inativa
        btnInativar.setEnabled(!notaInativa);
        btnRetificar.setEnabled(!notaInativa);

        painelBotoes.add(btnVisualizar);
        painelBotoes.add(btnRetificar);
        painelBotoes.add(btnInativar);

        InativarNotaClinica inativacao = null;

        //2026-10-28 Juliano: Configuração do Painel para exibir a justificativa e os dados da inativação
        JPanel painelJustificativa = new JPanel(new BorderLayout(10, 0));
        painelJustificativa.setOpaque(false);
        painelJustificativa.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        //2026-10-30 Juliano: Configuração do Painel para exibir a justificativa e os dados da retificaçaõ
        JPanel painelJustificativaRetificacao = new JPanel(new BorderLayout(10, 0));
        painelJustificativaRetificacao.setOpaque(false);
        painelJustificativaRetificacao.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        // Aplica o efeito de texto riscado e cria a justificativa
        if (notaInativa) {

            java.util.Map<java.awt.font.TextAttribute, Object> atributos
                    = new java.util.HashMap<>();

            atributos.put(
                    java.awt.font.TextAttribute.STRIKETHROUGH,
                    java.awt.font.TextAttribute.STRIKETHROUGH_ON
            );

            // Risca data e horário
            lblData.setFont(lblData.getFont().deriveFont(atributos));

            // Risca ID e tipo da nota
            lblTipo.setFont(lblTipo.getFont().deriveFont(atributos));

            // Risca o conteúdo da evolução
            txtEvolucao.setFont(txtEvolucao.getFont().deriveFont(atributos));

            // Destaca o status INATIVA em vermelho
            lblStatusNotaClinica.setForeground(Color.RED);

            InativarNotaClinicaJPA inativacaoJPA = new InativarNotaClinicaJPA();

            inativacao = inativacaoJPA.buscarInativacaoPorNotaClinica(evolucao.getId());

            System.out.println("ID da nota exibida no card: " + evolucao.getId());
            System.out.println("Inativação encontrada: " + inativacao);

            // Recupera a justificativa da inativação
            System.out.println("================================");
            System.out.println("ID da nota no card: " + evolucao.getId());
            System.out.println("================================");

            if (inativacao != null) {

                // Justificativa à esquerda
                JLabel lblJustificativaInativacao = new JLabel(
                        "<html><b>Motivo da inativação:</b> "
                        + inativacao.getMotivoInativarNota()
                        + "</html>"
                );

                lblJustificativaInativacao.setFont(
                        new Font("Arial", Font.PLAIN, 11)
                );

                // Data e horário à direita
                JLabel lblDataHorarioInativacao = new JLabel(
                        "<html><div style='text-align:right;'>"
                        + "<b>Inativada em:</b> "
                        + inativacao.getDataInativarNota().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        )
                        + " "
                        + inativacao.getHorario().format(
                                java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                        )
                        + "</div></html>"
                );

                lblDataHorarioInativacao.setFont(
                        new Font("Arial", Font.PLAIN, 11)
                );

                // Adiciona cada informação em seu lado
                painelJustificativa.add(
                        lblJustificativaInativacao,
                        BorderLayout.CENTER
                );

                painelJustificativa.add(
                        lblDataHorarioInativacao,
                        BorderLayout.EAST
                );

                // Texto em vermelho, como no histórico atual
                lblJustificativaInativacao.setForeground(Color.RED);
                lblDataHorarioInativacao.setForeground(Color.GRAY);

            } else {
                System.out.println("Inativação não encontrada.");
            }
        }

        //2026-10-30 Juliano:Definindo as configurações de exibição do card da nota retificada
        if (notaRetificada) {

            java.util.Map<java.awt.font.TextAttribute, Object> atributos
                    = new java.util.HashMap<>();

            atributos.put(
                    java.awt.font.TextAttribute.STRIKETHROUGH,
                    java.awt.font.TextAttribute.STRIKETHROUGH_ON
            );

            // Destaca o status RETIFICADA
            lblStatusNotaClinica.setForeground(Color.ORANGE);

            RetificarNotaClinicaJPA retificacaoJPA = new RetificarNotaClinicaJPA();

            RetificarNotaClinica retificacao
                    = retificacaoJPA.buscarRetificacaoPorNotaOriginal(evolucao.getId());

            if (retificacao != null) {

                // Motivo da retificação
                JLabel lblMotivoRetificacao = new JLabel(
                        "<html><b>Motivo da retificação:</b> "
                        + retificacao.getMotivoRetificacao()
                        + "</html>"
                );

                lblMotivoRetificacao.setFont(
                        new Font("Arial", Font.PLAIN, 11)
                );

                // Data, horário e ID da nova nota
                JLabel lblDadosRetificacao = new JLabel(
                        "<html><div style='text-align:right;'>"
                        + "<b>Retificada em:</b> "
                        + retificacao.getDataRetificacao().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        )
                        + " "
                        + retificacao.getHorarioRetificacao().format(
                                java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                        )
                        + "<br><b>Nova nota gerada com o ID:</b> "
                        + retificacao.getNotaRetificadora().getId()
                        + "</div></html>"
                );

                lblDadosRetificacao.setFont(
                        new Font("Arial", Font.PLAIN, 11)
                );

                painelJustificativaRetificacao.add(
                        lblMotivoRetificacao,
                        BorderLayout.CENTER
                );

                painelJustificativaRetificacao.add(
                        lblDadosRetificacao,
                        BorderLayout.EAST
                );

                lblMotivoRetificacao.setForeground(new Color(180, 100, 0));
                lblDadosRetificacao.setForeground(Color.GRAY);
            }
        }

        //2026-10-27 Juliano: Pegando a nota especifica que será inativa e passando o objeto da mesma para poder se comunicar com a tela de justificativa da inativação
        EvolucaoClinicaJPA evolucaoclinicajpa = new EvolucaoClinicaJPA();
        Long idEvolucao = evolucao.getId();
        EvolucaoClinica desabilitarNotaClinica = evolucaoclinicajpa.buscarNotaClinicaPorId(idEvolucao);

        btnVisualizar.addActionListener(e -> {
            VisualizarNotaClinicaView tela = new VisualizarNotaClinicaView(janelaPai, desabilitarNotaClinica);
            tela.setLocationRelativeTo(janelaPai);
            tela.setVisible(true);
        });

        btnRetificar.addActionListener(e -> {
            RetificarNotaClinicaView tela = new RetificarNotaClinicaView(janelaPai, desabilitarNotaClinica);

            tela.setLocationRelativeTo(janelaPai);
            tela.setVisible(true);

            // Verifica se o usuário confirmou a retificação
            if (tela.isRetificarNota()) {

                // Guarda o objeto da retificação
                retificacaoPendente = tela.getRetificarnota();

                // Recupera a nota clínica original
                notaOriginal = retificacaoPendente.getNotaOriginal();

                //2026-10-28 Juliano: Salvando a retificação
                // Indica que uma retificação está em andamento
                retificacaoEmAndamento = true;

                // Copia o conteúdo da nota original para a nova evolução
                txtEvolucaoSessao.setText(notaOriginal.getEvolucaoSessao());

                txtObservacao.setText(notaOriginal.getObservacoes());

                // Copia o tipo da nota original
                cmbListarNotaClinica.setSelectedItem(notaOriginal.getTiponotaclinica());

                JOptionPane.showMessageDialog(
                        this,
                        "Os dados da nota clínica original foram carregados "
                        + "para edição. Revise as informações e salve a nova nota.",
                        "Retificação de Nota Clínica",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        btnInativar.addActionListener(e -> {
            //2026-10-27 Juliano: caso tiver  INATIVO ele não pergunta mais para abrir a outra tela
            if (evolucao.getStatusnotaclinica() == StatusNotaClinica.INATIVA) {
                return;
            }

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Você realmente deseja inativar esta nota clínica?",
                    "Confirmar inativação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }

            InativarNotaClinicaView tela = new InativarNotaClinicaView(janelaPai, desabilitarNotaClinica);
            tela.setVisible(true);
            // Verifica se a inativação foi confirmada
            if (!tela.isInativacaoConfirmada()) {
                return;
            }

            desabilitarNotaClinica.setStatusnotaclinica(StatusNotaClinica.INATIVA);

            //desabilitarNotaClinica.setInativacao(desabilitarNotaClinica.getInativacao());
            evolucaoclinicajpa.AtualizarStatusNotaClinica(desabilitarNotaClinica);

            atualizarHistoricoEvolucaoClinica();

            btnInativar.setEnabled(false);

            System.out.println("Nota clinica desabilitada com sucesso!! ");
        });

        // Painel inferior com os botões
        card.add(painelCabecalho, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(
                new BoxLayout(painelConteudo, BoxLayout.Y_AXIS)
        );
        painelConteudo.setOpaque(false);

        painelConteudo.add(txtEvolucao);

        // Painel para exibir a justificativa e os dados da inativação
        if (notaInativa && inativacao != null) {
            painelConteudo.add(painelJustificativa);
        }
        // Painel para exibir a justificativa e os dados da retificação
        if (notaRetificada) {
            painelConteudo.add(painelJustificativaRetificacao);
        }

        card.add(painelConteudo, BorderLayout.CENTER);
        card.add(painelBotoes, BorderLayout.SOUTH);

        // Adiciona o card ao histórico, mesmo quando a nota estiver inativa
        painelContainer.add(card);

        return painelContainer;
    }

    private void Limpar() {
        txtEvolucaoSessao.setText("");
        txtObservacao.setText("");
    }

    private void atualizarHistoricoEvolucaoClinica() {

        EvolucaoClinicaJPA evolucaoJPA = new EvolucaoClinicaJPA();

        List<EvolucaoClinica> evolucoes = evolucaoJPA.listarEvolucoesPorPaciente(paciente.getId());

        atualizarCardsEvolucaoClinica(evolucoes);
    }

    //2026-10-31 Juliano: Atualiza os cards utilizando a lista recebida
    private void atualizarCardsEvolucaoClinica(List<EvolucaoClinica> evolucoes) {

        pnlListaEvolucoes.removeAll();

        // Percorre somente as evoluções recebidas
        for (EvolucaoClinica evolucao : evolucoes) {
            pnlListaEvolucoes.add(
                    listarHistoricoEvolucaoClinicaPaciente(evolucao)
            );
        }

        pnlListaEvolucoes.revalidate();
        pnlListaEvolucoes.repaint();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarNotaClinica;
    private javax.swing.JButton btnLimparEvolucao;
    private javax.swing.JButton btnLimparPesquisa;
    private javax.swing.JButton btnSalvarEvolucao;
    private javax.swing.JComboBox<TipoNotaClinica> cmbListarNotaClinica;
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
    private javax.swing.JTextField txtPesquisarNotaClinicaPelaEvolucao;
    // End of variables declaration//GEN-END:variables
}
