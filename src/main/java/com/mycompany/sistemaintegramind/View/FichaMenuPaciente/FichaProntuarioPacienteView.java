/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.FichaProntuarioPacienteFiltro;
import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.AudioEvolucaoClinicaJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.EvolucaoClinicaJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.InativarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.RetificarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.AudioEvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.RetificarNotaClinica;
import com.mycompany.sistemaintegramind.util.Utilitarios.GravadorAudio;
import com.mycompany.sistemaintegramind.util.Utilitarios.TelasUtil.FichaProntuarioDesignUtil;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FichaProntuarioPacienteView extends javax.swing.JPanel {

    private AgendamentoJPA agendamentojpa = new AgendamentoJPA();
    private Paciente paciente;
    private Agendamento agendamentoDoDiaDoPaciente;
    private EvolucaoClinica notaOriginal;
    private java.awt.Window janelaPai = javax.swing.SwingUtilities.getWindowAncestor(this);
    private RetificarNotaClinica retificacaoPendente;
    private boolean retificacaoEmAndamento = false;
    private InativarNotaClinicaJPA inativacaoJPA = new InativarNotaClinicaJPA();
    private GravadorAudio gravadorAudio = new GravadorAudio();

    public FichaProntuarioPacienteView() {
        initComponents();
        aplicarDesignTela();
    }

    public FichaProntuarioPacienteView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
        aplicarDesignTela();
        txtPesquisarNotaClinicaPelaEvolucao.putClientProperty("JTextField.placeholderText", "Pesquisar...");
        //2026-10-29 Juliano: Campos apenas para  visualização
        txtDataAgendamento.setEditable(false);
        txtDataAgendamento.setOpaque(false);
        txtDataAgendamento.setForeground(new java.awt.Color(30, 41, 59));
        txtHorario.setEditable(false);
        txtHorario.setOpaque(false);
        txtHorario.setForeground(new java.awt.Color(30, 41, 59));

        cmbListarNotaClinica.setModel(new DefaultComboBoxModel<>(TipoNotaClinica.values()));

        //2026-10-02 Juliano: Organização dos futuros cards que serão adicionados
        pnlListaEvolucoes.setLayout(new BoxLayout(pnlListaEvolucoes, BoxLayout.Y_AXIS));

        if (paciente != null && paciente.getId() != null) {
            EvolucaoClinicaJPA evolucaoJPA = new EvolucaoClinicaJPA();

            List<EvolucaoClinica> evolucoes = evolucaoJPA.listarEvolucoesPorPaciente(paciente.getId());

            for (EvolucaoClinica evolucao : evolucoes) {

                pnlListaEvolucoes.add(listarHistoricoEvolucaoClinicaPaciente(evolucao)
                );
            }
        }

        pnlListaEvolucoes.revalidate();
        pnlListaEvolucoes.repaint();
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtDataAgendamento.setText(dataFormatada);
        LocalTime horarioAtual = LocalTime.now();

        String horarioFormatado = horarioAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtHorario.setText(horarioFormatado);

    }

    private void aplicarDesignTela() {
        FichaProntuarioDesignUtil.aplicarDesignTela(
                containerNovaEvolucao,
                containerHistoricoNotas,
                containerDataAgendamento,
                txtDataAgendamento,
                iconeCalendarioData,
                containerHorario,
                txtHorario,
                iconeRelogioHorario,
                containerBarraPesquisa,
                txtPesquisarNotaClinicaPelaEvolucao,
                iconeHeaderHistorico,
                scrListaEvolucoes,
                pnlListaEvolucoes,
                jScrollPane1,
                txtEvolucaoSessao,
                jScrollPane2,
                txtObservacao,
                cmbListarNotaClinica,
                btnBuscarNotaClinica,
                btnLimparPesquisa,
                btnGravarAudioEvolucaoClinicaPaciente,
                btnLimparEvolucao,
                btnSalvarEvolucao
        );
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerNovaEvolucao = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        containerDataAgendamento = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        txtDataAgendamento = new javax.swing.JTextField();
        iconeCalendarioData = new com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel();
        containerHorario = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        txtHorario = new javax.swing.JTextField();
        iconeRelogioHorario = new com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel();
        cmbListarNotaClinica = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnGravarAudioEvolucaoClinicaPaciente = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEvolucaoSessao = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacao = new javax.swing.JTextArea();
        btnLimparEvolucao = new javax.swing.JButton();
        btnSalvarEvolucao = new javax.swing.JButton();
        containerHistoricoNotas = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        iconeHeaderHistorico = new com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel();
        jLabel1 = new javax.swing.JLabel();
        containerBarraPesquisa = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        txtPesquisarNotaClinicaPelaEvolucao = new javax.swing.JTextField();
        pnlBotoesPesquisa = new javax.swing.JPanel();
        btnBuscarNotaClinica = new javax.swing.JButton();
        btnLimparPesquisa = new javax.swing.JButton();
        scrListaEvolucoes = new javax.swing.JScrollPane();
        pnlListaEvolucoes = new javax.swing.JPanel();

        setBackground(new java.awt.Color(242, 246, 244));

        containerNovaEvolucao.setBackground(new java.awt.Color(255, 255, 255));
        containerNovaEvolucao.setBorderRadius(18);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(30, 41, 59));
        jLabel5.setText("Nova Evolução Clínica");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(100, 116, 139));
        jLabel3.setText("Registre o que foi trabalhado durante a consulta.");

        containerDataAgendamento.setBackground(new java.awt.Color(255, 255, 255));
        containerDataAgendamento.setBorderRadius(10);
        containerDataAgendamento.setMinimumSize(new java.awt.Dimension(126, 36));
        containerDataAgendamento.setPreferredSize(new java.awt.Dimension(126, 36));
        containerDataAgendamento.setLayout(new java.awt.BorderLayout());

        txtDataAgendamento.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtDataAgendamento.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 2));
        txtDataAgendamento.addActionListener(this::txtDataAgendamentoActionPerformed);
        containerDataAgendamento.add(txtDataAgendamento, java.awt.BorderLayout.CENTER);

        iconeCalendarioData.setBackground(new java.awt.Color(255, 255, 255));
        iconeCalendarioData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuPacienteIcons/calendar_green.png"))); // NOI18N
        iconeCalendarioData.setIconColor(new java.awt.Color(71, 85, 105));
        iconeCalendarioData.setIconHeight(15);
        iconeCalendarioData.setIconWidth(15);
        iconeCalendarioData.setPreferredSize(new java.awt.Dimension(26, 26));
        iconeCalendarioData.setShadowBlur(0);
        containerDataAgendamento.add(iconeCalendarioData, java.awt.BorderLayout.EAST);

        containerHorario.setBackground(new java.awt.Color(255, 255, 255));
        containerHorario.setBorderRadius(10);
        containerHorario.setMinimumSize(new java.awt.Dimension(108, 36));
        containerHorario.setPreferredSize(new java.awt.Dimension(108, 36));
        containerHorario.setLayout(new java.awt.BorderLayout());

        txtHorario.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtHorario.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 2));
        containerHorario.add(txtHorario, java.awt.BorderLayout.CENTER);

        iconeRelogioHorario.setBackground(new java.awt.Color(255, 255, 255));
        iconeRelogioHorario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alarm (1).png"))); // NOI18N
        iconeRelogioHorario.setIconColor(new java.awt.Color(71, 85, 105));
        iconeRelogioHorario.setIconHeight(15);
        iconeRelogioHorario.setIconWidth(15);
        iconeRelogioHorario.setPreferredSize(new java.awt.Dimension(26, 26));
        iconeRelogioHorario.setShadowBlur(0);
        containerHorario.add(iconeRelogioHorario, java.awt.BorderLayout.EAST);

        cmbListarNotaClinica.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 65, 85));
        jLabel2.setText("Evolução da Sessão");

        btnGravarAudioEvolucaoClinicaPaciente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGravarAudioEvolucaoClinicaPaciente.setForeground(new java.awt.Color(13, 82, 65));
        btnGravarAudioEvolucaoClinicaPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ferramenta-de-audio-com-alto-falante.png"))); // NOI18N
        btnGravarAudioEvolucaoClinicaPaciente.setText("Gravar áudio");
        btnGravarAudioEvolucaoClinicaPaciente.addActionListener(this::btnGravarAudioEvolucaoClinicaPacienteActionPerformed);

        txtEvolucaoSessao.setColumns(20);
        txtEvolucaoSessao.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtEvolucaoSessao.setLineWrap(true);
        txtEvolucaoSessao.setRows(5);
        txtEvolucaoSessao.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtEvolucaoSessao);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 65, 85));
        jLabel4.setText("Observações");

        txtObservacao.setColumns(20);
        txtObservacao.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtObservacao.setLineWrap(true);
        txtObservacao.setRows(5);
        txtObservacao.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtObservacao);

        btnLimparEvolucao.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLimparEvolucao.setForeground(new java.awt.Color(71, 85, 105));
        btnLimparEvolucao.setText("Limpar");
        btnLimparEvolucao.addActionListener(this::btnLimparEvolucaoActionPerformed);

        btnSalvarEvolucao.setBackground(new java.awt.Color(21, 128, 61));
        btnSalvarEvolucao.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSalvarEvolucao.setForeground(new java.awt.Color(255, 255, 255));
        btnSalvarEvolucao.setText("Salvar evolução");
        btnSalvarEvolucao.addActionListener(this::btnSalvarEvolucaoActionPerformed);

        javax.swing.GroupLayout containerNovaEvolucaoLayout = new javax.swing.GroupLayout(containerNovaEvolucao);
        containerNovaEvolucao.setLayout(containerNovaEvolucaoLayout);
        containerNovaEvolucaoLayout.setHorizontalGroup(
            containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerNovaEvolucaoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addGroup(containerNovaEvolucaoLayout.createSequentialGroup()
                        .addComponent(containerDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(containerHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(cmbListarNotaClinica, 0, 234, Short.MAX_VALUE))
                    .addGroup(containerNovaEvolucaoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGravarAudioEvolucaoClinicaPaciente))
                    .addGroup(containerNovaEvolucaoLayout.createSequentialGroup()
                        .addGroup(containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addGroup(containerNovaEvolucaoLayout.createSequentialGroup()
                                .addComponent(btnLimparEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(btnSalvarEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        containerNovaEvolucaoLayout.setVerticalGroup(
            containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerNovaEvolucaoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(2, 2, 2)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(containerDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(containerHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbListarNotaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnGravarAudioEvolucaoClinicaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(containerNovaEvolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimparEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvarEvolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        containerHistoricoNotas.setBackground(new java.awt.Color(255, 255, 255));
        containerHistoricoNotas.setBorderRadius(18);

        iconeHeaderHistorico.setBackground(new java.awt.Color(255, 255, 255));
        iconeHeaderHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuPacienteIcons/docs_green.png"))); // NOI18N
        iconeHeaderHistorico.setIconColor(new java.awt.Color(51, 65, 85));
        iconeHeaderHistorico.setIconHeight(18);
        iconeHeaderHistorico.setIconWidth(18);
        iconeHeaderHistorico.setPreferredSize(new java.awt.Dimension(24, 24));
        iconeHeaderHistorico.setShadowBlur(0);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(30, 41, 59));
        jLabel1.setText("Histórico das notas Clínicas");

        containerBarraPesquisa.setBackground(new java.awt.Color(255, 255, 255));
        containerBarraPesquisa.setBorderRadius(10);
        containerBarraPesquisa.setMinimumSize(new java.awt.Dimension(265, 34));
        containerBarraPesquisa.setPreferredSize(new java.awt.Dimension(265, 34));
        containerBarraPesquisa.setLayout(new java.awt.BorderLayout());

        txtPesquisarNotaClinicaPelaEvolucao.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtPesquisarNotaClinicaPelaEvolucao.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 4));
        containerBarraPesquisa.add(txtPesquisarNotaClinicaPelaEvolucao, java.awt.BorderLayout.CENTER);

        pnlBotoesPesquisa.setOpaque(false);
        pnlBotoesPesquisa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));

        btnBuscarNotaClinica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa.png"))); // NOI18N
        btnBuscarNotaClinica.setPreferredSize(new java.awt.Dimension(26, 26));
        btnBuscarNotaClinica.addActionListener(this::btnBuscarNotaClinicaActionPerformed);
        pnlBotoesPesquisa.add(btnBuscarNotaClinica);

        btnLimparPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        btnLimparPesquisa.setPreferredSize(new java.awt.Dimension(26, 26));
        btnLimparPesquisa.addActionListener(this::btnLimparPesquisaActionPerformed);
        pnlBotoesPesquisa.add(btnLimparPesquisa);

        containerBarraPesquisa.add(pnlBotoesPesquisa, java.awt.BorderLayout.EAST);

        scrListaEvolucoes.setBorder(null);

        pnlListaEvolucoes.setBackground(new java.awt.Color(255, 255, 255));
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
            .addGap(0, 545, Short.MAX_VALUE)
        );
        pnlListaEvolucoesLayout.setVerticalGroup(
            pnlListaEvolucoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );

        scrListaEvolucoes.setViewportView(pnlListaEvolucoes);

        javax.swing.GroupLayout containerHistoricoNotasLayout = new javax.swing.GroupLayout(containerHistoricoNotas);
        containerHistoricoNotas.setLayout(containerHistoricoNotasLayout);
        containerHistoricoNotasLayout.setHorizontalGroup(
            containerHistoricoNotasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerHistoricoNotasLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(containerHistoricoNotasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrListaEvolucoes)
                    .addGroup(containerHistoricoNotasLayout.createSequentialGroup()
                        .addComponent(iconeHeaderHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(containerBarraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12))
        );
        containerHistoricoNotasLayout.setVerticalGroup(
            containerHistoricoNotasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerHistoricoNotasLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(containerHistoricoNotasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(containerBarraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconeHeaderHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(scrListaEvolucoes, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(containerNovaEvolucao, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(containerHistoricoNotas, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(containerNovaEvolucao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(containerHistoricoNotas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
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

        //2026-11-07 Juliano: Cria uma nova instância da nota clínica
        EvolucaoClinica evolucaoclinica = new EvolucaoClinica();

        EvolucaoClinicaJPA evolucaoclinicajpa = new EvolucaoClinicaJPA();

        RetificarNotaClinicaJPA retificarjpa = new RetificarNotaClinicaJPA();

        //2026-11-07 Juliano: Preenche os dados da nova nota clínica
        evolucaoclinica.setPaciente(paciente);

        evolucaoclinica.setEvolucaoSessao(txtEvolucaoSessao.getText());

        evolucaoclinica.setObservacoes(txtObservacao.getText());

        evolucaoclinica.setDataEvolucao(LocalDate.now());

        evolucaoclinica.setHorario(LocalTime.now());

        evolucaoclinica.setStatusnotaclinica(StatusNotaClinica.ATIVA);

        evolucaoclinica.setTiponotaclinica((TipoNotaClinica) cmbListarNotaClinica.getSelectedItem());

        //2026-11-07 Juliano: Primeiro, salva a nova nota clínica no banco de dados
        evolucaoclinicajpa.cadastrarEvolucaoClinica(evolucaoclinica);

        System.out.println(
                "Nova nota clínica criada. ID: "
                + evolucaoclinica.getId()
        );

        //2026-11-07 Juliano: Salvando o áudio do prontuario
        File arquivoTemporario = gravadorAudio.getArquivoAudioTemporario();

        if (arquivoTemporario != null && arquivoTemporario.exists()) {

            if (gravadorAudio.isGravando()) {
                throw new IllegalStateException(
                        "Finalize a gravação antes de salvar a evolução."
                );
            }

            Long idPaciente = evolucaoclinica.getPaciente().getId();
            Long idEvolucao = evolucaoclinica.getId();

            //2026-11-07 Juliano: Diretório definitivo do áudio
            File diretorioAudio = new File("C:\\IntegraMIND\\Audios\\Pacientes\\" + idPaciente);

            if (!diretorioAudio.exists() && !diretorioAudio.mkdirs()) {
                try {
                    throw new IOException("Não foi possível criar a pasta dos áudios.");
                } catch (IOException ex) {
                    System.getLogger(FichaProntuarioPacienteView.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }

            //2026-11-07 Juliano: Nome do arquivo
            String nomeArquivo = "evolucao_" + idEvolucao + ".wav";

            //2026-11-07 Juliano: Caminho definitivo
            File arquivoDefinitivo = new File(diretorioAudio, nomeArquivo);

            try {
                //2026-11-07 Juliano: Transfere o arquivo temporário para a pasta definitiva
                Files.move(arquivoTemporario.toPath(), arquivoDefinitivo.toPath());
            } catch (IOException ex) {
                System.getLogger(FichaProntuarioPacienteView.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

            //2026-11-07 Juliano:Instância para gravar um áudio do prontuário
            AudioEvolucaoClinica audioevolucao = new AudioEvolucaoClinica();
            AudioEvolucaoClinicaJPA audioevolucaojpa = new AudioEvolucaoClinicaJPA();

            audioevolucao.setNomeArquivo(nomeArquivo);
            audioevolucao.setCaminhoArquivo(arquivoDefinitivo.getAbsolutePath());
            audioevolucao.setFormato("WAV");
            audioevolucao.setDataGravacao(LocalDateTime.now());
            audioevolucao.setEvolucaoclinica(evolucaoclinica);
            audioevolucaojpa.cadastrarAudioEvolucaoClinica(audioevolucao);

            // Libera a referência do arquivo temporário
            gravadorAudio.liberarAudioSalvo();

        }

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

    private void btnGravarAudioEvolucaoClinicaPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarAudioEvolucaoClinicaPacienteActionPerformed
        try {
            gravadorAudio.listarDispositivosAudio();

            // Verifica se já existe uma gravação em andamento
            if (gravadorAudio.isGravando()) {

                // Interrompe e finaliza a gravação
                File arquivoAudio = gravadorAudio.pararGravacao();

                btnGravarAudioEvolucaoClinicaPaciente.setText("Gravar áudio");

                JOptionPane.showMessageDialog(
                        this,
                        "Gravação finalizada com sucesso!\n"
                        + "O áudio está aguardando o salvamento da evolução clínica.",
                        "Gravação de áudio",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                // Verifica se existe uma gravação anterior aguardando salvamento
                File audioAnterior = gravadorAudio.getArquivoAudioTemporario();

                if (audioAnterior != null && audioAnterior.exists()) {

                    int resposta = JOptionPane.showConfirmDialog(
                            this,
                            "Já existe uma gravação aguardando salvamento.\n"
                            + "Deseja descartá-la e iniciar uma nova gravação?",
                            "Substituir gravação",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (resposta != JOptionPane.YES_OPTION) {
                        return;
                    }

                    // Descarta o áudio anterior
                    gravadorAudio.cancelarAudio();
                }

                // Inicia uma nova gravação
                gravadorAudio.iniciarGravacao();

                btnGravarAudioEvolucaoClinicaPaciente.setText("Parar gravação");

            }

        } catch (Exception e) {

            btnGravarAudioEvolucaoClinicaPaciente.setText("Gravar áudio");

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao realizar a gravação:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }//GEN-LAST:event_btnGravarAudioEvolucaoClinicaPacienteActionPerformed

    private JPanel listarHistoricoEvolucaoClinicaPaciente(EvolucaoClinica evolucao) {

        //2026-10-30 Juliano: Verifica se a nota está inativa
        boolean notaInativa = evolucao.getStatusnotaclinica() == StatusNotaClinica.INATIVA;
        //2026-10-30 Juliano: Verifica se a nota está retificada
        boolean notaRetificada = evolucao.getStatusnotaclinica() == StatusNotaClinica.RETIFICADA;

        JButton btnReproduzirAudio = FichaProntuarioDesignUtil.criarBotaoReproduzirAudio();
        JButton btnVisualizar = FichaProntuarioDesignUtil.criarBotaoVisualizar();
        JButton btnRetificar = FichaProntuarioDesignUtil.criarBotaoRetificar(notaInativa);
        JButton btnInativar = FichaProntuarioDesignUtil.criarBotaoInativar(notaInativa);

        //2026-11-07 Juliano: Verifica se a evolução possui áudio
        AudioEvolucaoClinicaJPA audioJPA = new AudioEvolucaoClinicaJPA();

        AudioEvolucaoClinica audioEvolucao
                = audioJPA.buscarAudioPorEvolucao(evolucao.getId());

        if (audioEvolucao != null) {

            String caminhoAudio = audioEvolucao.getCaminhoArquivo();

            File arquivoAudio = caminhoAudio != null
                    ? new File(caminhoAudio) : null;

            if (arquivoAudio != null && arquivoAudio.isFile()) {

                btnReproduzirAudio.addActionListener(e -> {

                    try {

                        if (Desktop.isDesktopSupported()
                                && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {

                            Desktop.getDesktop().open(arquivoAudio);

                        } else {

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Não foi possível abrir o reprodutor de áudio.",
                                    "Reprodução de áudio",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }

                    } catch (IOException ex) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Erro ao abrir o áudio: " + ex.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );

                        ex.printStackTrace();
                    }
                });

            } else {

                btnReproduzirAudio.setEnabled(false);
                btnReproduzirAudio.setToolTipText("Arquivo de áudio não encontrado.");
            }
        }

        // Desabilita os botões de retificação e inativação quando a nota estiver inativa
        btnInativar.setEnabled(!notaInativa);
        btnRetificar.setEnabled(!notaInativa);

        InativarNotaClinica inativacao = null;
        if (notaInativa) {
            InativarNotaClinicaJPA inativacaoJPA = new InativarNotaClinicaJPA();
            inativacao = inativacaoJPA.buscarInativacaoPorNotaClinica(evolucao.getId());
        }

        RetificarNotaClinica retificacao = null;
        if (notaRetificada) {
            RetificarNotaClinicaJPA retificacaoJPA = new RetificarNotaClinicaJPA();
            retificacao = retificacaoJPA.buscarRetificacaoPorNotaOriginal(evolucao.getId());
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

        return FichaProntuarioDesignUtil.montarCardNotaClinica(
                evolucao,
                scrListaEvolucoes,
                notaInativa,
                notaRetificada,
                inativacao,
                retificacao,
                audioEvolucao != null,
                btnReproduzirAudio,
                btnVisualizar,
                btnRetificar,
                btnInativar
        );
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
    private javax.swing.JButton btnGravarAudioEvolucaoClinicaPaciente;
    private javax.swing.JButton btnLimparEvolucao;
    private javax.swing.JButton btnLimparPesquisa;
    private javax.swing.JButton btnSalvarEvolucao;
    private javax.swing.JComboBox<TipoNotaClinica> cmbListarNotaClinica;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerBarraPesquisa;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerDataAgendamento;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerHistoricoNotas;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerHorario;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerNovaEvolucao;
    private com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel iconeCalendarioData;
    private com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel iconeHeaderHistorico;
    private com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel iconeRelogioHorario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlBotoesPesquisa;
    private javax.swing.JPanel pnlListaEvolucoes;
    private javax.swing.JScrollPane scrListaEvolucoes;
    private javax.swing.JTextField txtDataAgendamento;
    private javax.swing.JTextArea txtEvolucaoSessao;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextArea txtObservacao;
    private javax.swing.JTextField txtPesquisarNotaClinicaPelaEvolucao;
    // End of variables declaration//GEN-END:variables
}
