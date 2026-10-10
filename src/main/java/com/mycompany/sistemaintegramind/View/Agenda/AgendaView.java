/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Agenda;

import com.mycompany.sistemaintegramind.Model.dao.AgendaFiltro;
import com.mycompany.sistemaintegramind.Model.dao.PacienteFiltro;
import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.PacienteJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.FrequenciaAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.View.Componentes.TabelaEstilo;
import com.mycompany.sistemaintegramind.View.EditarExcluirAgendamento.EditarAgendamentoView;
import com.mycompany.sistemaintegramind.util.Utilitarios.BotaoRenderizarIcones;
import com.mycompany.sistemaintegramind.util.Utilitarios.StatusAgendamento;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Micro
 */
public class AgendaView extends javax.swing.JPanel {

    private List<Agendamento> agendamentos;
    private DefaultTableModel modeloTabelaListarTodosAgendamentos;
    private DefaultTableModel modeloTabelaListarAgendamentosDoDia;
    private LocalDate dataSelecionada = LocalDate.now();

    public AgendaView() {
        initComponents();
        container1.setCardStyle();
        container2.setCardStyle();
        container3.setCardStyle();

        //2026-09-11 Juliano: Definindo os estilos da tabelas
        TabelaEstilo.aplicar(tblAgendamentos);
        TabelaEstilo.aplicar(tblListarAgendamentosDoDia);

        //2026-09-23 Juliano: Não irá aparecer as datas anteriores a hoje
        dtDataAgendamento.setMinSelectableDate(new Date());

        //2026-09-11 Juliano: Carregando os pacientes, na combox, para aparecer na listagem
        carregarPacientes();
        carregarPacientesFiltro();

        //2026-09-23 Juliano: carregando lista de opções do combox
        carregarComboboxesAgendamento();

        //2026-09-11 Juliano: Definindo, o modo de exibição da data para o formato padrão 
        dtDataAgendamento.setDateFormatString("dd/MM/yyyy");
        dtFiltrarDataAgendamento.setDateFormatString("dd/MM/yyyy");

        //2026-09-11 Juliano: definindo o placeholder de alguns campos do sistema
        txtObservacao.putClientProperty("JTextField.placeholderText", "Ex: Primeira consulta, retorno, etc....");
        txtHorario.putClientProperty("JTextField.placeholderText", "00:00");

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

                        modeloTabelaListarTodosAgendamentos.setValueAt(
                                "<html><font color='green'><b>PAGO</b></font></html>",
                                linha,
                                5
                        );
                        modeloTabelaListarTodosAgendamentos.setValueAt(check, linha, 6);

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
        cmbListarPacientes.removeAllItems();
        for (Paciente p : carregarListaPacientes) {
            cmbListarPacientes.addItem(p);
        }

    }

    private void carregarPacientesFiltro() {
        PacienteJPA pacientejpa = new PacienteJPA();
        List<Paciente> carregarListaPacientes = pacientejpa.listarPacientes();
        cmbFiltrarPaciente.removeAllItems();
        for (Paciente p : carregarListaPacientes) {
            cmbFiltrarPaciente.addItem(p.getNome());
        }

    }

    private void carregarAgendamentosDoDIa(DefaultTableModel modelo, List<Agendamento> ListarAgendamentos) {
        for (Agendamento agendamentos : ListarAgendamentos) {
            String statuspagamento = agendamentos.getStatuspagamento().toString();
            String pagamentoformatado;
            if (statuspagamento.equals("PENDENTE")) {
                pagamentoformatado = "<html><font color='red'><b>PENDENTE</b></font></html>";
            } else {
                pagamentoformatado = "<html><font color='green'><b>PAGO</b></font></html>";
            }

            modelo.addRow(new Object[]{
                agendamentos.getPaciente().getNome(),
                agendamentos.getHorario(),
                pagamentoformatado,
                agendamentos.getValorDaConsulta()

            });
        }
    }

    private List<Agendamento> ListarAgendamentos() {
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        return agendamentojpa.listarAgendamentos();
    }

    private List<Agendamento> ListarAgendamentosDoDia() {
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        return agendamentojpa.listarAgendamentosDoDia();
    }

    private void LimparDadosAgendamento() {
        txtValorConsulta.setText("");
        txtObservacao.setText("");
        cmbTipoAtendimento.setSelectedIndex(0);
        cmbStatusPagamento.setSelectedIndex(0);
        txtHorario.setText("");
        dtDataAgendamento.setDate(null);
        cmbListarPacientes.setSelectedIndex(0);
    }

    private void atualizartabelaAgendamento() {
        agendamentos = ListarAgendamentos();
        modeloTabelaListarTodosAgendamentos.setRowCount(0);
        GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
        tblAgendamentos.revalidate();
        tblAgendamentos.repaint();
    }

    private void cadastrarAgendamento(LocalDate data, BigDecimal valorConsulta, LocalTime horario, Paciente paciente, AgendamentoJPA agendamentojpa) {
        //2026-09-22 Juliano: criação de um novo objeto para cada novo agendamento posterior
        Agendamento novoagendamento = new Agendamento();
        // ALTERE PARA CORRIGIR:

        novoagendamento.setDataAgendamento(data);
        novoagendamento.setObservacao(txtObservacao.getText());
        novoagendamento.setValorDaConsulta(valorConsulta);
        novoagendamento.setHorario(horario);
        novoagendamento.setTipoatendimento((TipoAtendimento) cmbTipoAtendimento.getSelectedItem());
        novoagendamento.setPaciente(paciente);
        novoagendamento.setStatuspagamento((StatusPagamento) cmbStatusPagamento.getSelectedItem());

        System.out.println("");

        //2026-09-30 Juliano: garantido que ao realizar um agendamento o status inicial será AGENDADO
        if (data.isBefore(LocalDate.now())) {

            novoagendamento.setStatusagendamento(StatusAgendamento.REALIZADO);
            System.out.println("Data posterior a consulta portanto atendimento já foi realizado.");

        } else {
            novoagendamento.setStatusagendamento(StatusAgendamento.AGENDADO);
            System.out.println("Data inferior a hoje ainda não foi realizada");
        }

        novoagendamento.setFrequenciaatendimento((FrequenciaAtendimento) cmbFrequenciaAtendimento.getSelectedItem());
        novoagendamento.setStatusPacienteAgendamento(StatusPacienteAgendamentoDocumento.ATIVO);

        System.out.println(" Agendamentos do Paciente --> " + novoagendamento.getPaciente().getNome() + " datas --> " + novoagendamento.getDataAgendamento());
        System.out.println("Agendamento cadastrado com sucesso!!!");
        agendamentojpa.cadastrarAgendamento(novoagendamento);

    }

    private void carregarComboboxesAgendamento() {
        //2026-09-10 Juliano: Trazendo todos os  tipos de atendimentos que é um enumerador  para  agendamentos listagem no combox
        cmbTipoAtendimento.setModel(new DefaultComboBoxModel<>(TipoAtendimento.values()));
        cmbStatusPagamento.setModel(new DefaultComboBoxModel<>(StatusPagamento.values()));
        cmbFrequenciaAtendimento.setModel(new DefaultComboBoxModel<>(FrequenciaAtendimento.values()));

        //2026-09-23 Juliano: Carregar opções do filtro do agendamento
        cmbFiltrarStatusPagamento.setModel(new DefaultComboBoxModel<>(StatusPagamento.values()));
        cmbFiltrarTipoAtendimento.setModel(new DefaultComboBoxModel<>(TipoAtendimento.values()));
        cmbFiltrarFrequenciaAtendimento.setModel(new DefaultComboBoxModel<>(FrequenciaAtendimento.values()));

    }

    //===================================================(FIM)=========================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAgendamentos = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        btnData = new com.toedter.calendar.JDateChooser();
        container1 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        cmbListarPacientes = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        dtDataAgendamento = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        txtHorario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbStatusPagamento = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        cmbFrequenciaAtendimento = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtValorConsulta = new javax.swing.JTextField();
        cmbTipoAtendimento = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtObservacao = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnRealizarAgendamento = new javax.swing.JButton();
        btnLimparDadosAgendamento = new javax.swing.JButton();
        container2 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cmbFiltrarTipoAtendimento = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cmbFiltrarFrequenciaAtendimento = new javax.swing.JComboBox<>();
        dtFiltrarDataAgendamento = new com.toedter.calendar.JDateChooser();
        cmbFiltrarStatusPagamento = new javax.swing.JComboBox<>();
        cmbFiltrarPaciente = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnLimparFiltroAgendamento = new javax.swing.JButton();
        btnFiltroAgendamento = new javax.swing.JButton();
        container3 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListarAgendamentosDoDia = new javax.swing.JTable();
        lblDataHoje = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(244, 248, 247));
        setLayout(new java.awt.GridBagLayout());

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(13, 82, 65));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agendar.png"))); // NOI18N
        jLabel9.setText("Agenda");
        jLabel9.setIconTextGap(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jLabel9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -129;
        gridBagConstraints.ipady = -22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnData, gridBagConstraints);

        container1.setBackground(new java.awt.Color(254, 254, 254));
        container1.setLayout(new java.awt.GridBagLayout());

        cmbListarPacientes.setMaximumSize(new java.awt.Dimension(190, 32));
        cmbListarPacientes.setMinimumSize(new java.awt.Dimension(140, 32));
        cmbListarPacientes.setPreferredSize(new java.awt.Dimension(190, 32));
        cmbListarPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbListarPacientesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 7);
        container1.add(cmbListarPacientes, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Paciente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 5, 0);
        container1.add(jLabel3, gridBagConstraints);

        dtDataAgendamento.setMaximumSize(new java.awt.Dimension(190, 32));
        dtDataAgendamento.setMinimumSize(new java.awt.Dimension(140, 32));
        dtDataAgendamento.setPreferredSize(new java.awt.Dimension(190, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        container1.add(dtDataAgendamento, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 0);
        container1.add(jLabel4, gridBagConstraints);

        txtHorario.setMaximumSize(new java.awt.Dimension(190, 32));
        txtHorario.setMinimumSize(new java.awt.Dimension(140, 32));
        txtHorario.setPreferredSize(new java.awt.Dimension(190, 32));
        txtHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHorarioActionPerformed(evt);
            }
        });
        txtHorario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHorarioKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        container1.add(txtHorario, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Horário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 0);
        container1.add(jLabel5, gridBagConstraints);

        cmbStatusPagamento.setMaximumSize(new java.awt.Dimension(190, 32));
        cmbStatusPagamento.setMinimumSize(new java.awt.Dimension(140, 32));
        cmbStatusPagamento.setPreferredSize(new java.awt.Dimension(190, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        container1.add(cmbStatusPagamento, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Status Pagamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 0);
        container1.add(jLabel16, gridBagConstraints);

        cmbFrequenciaAtendimento.setMaximumSize(new java.awt.Dimension(190, 32));
        cmbFrequenciaAtendimento.setMinimumSize(new java.awt.Dimension(140, 32));
        cmbFrequenciaAtendimento.setPreferredSize(new java.awt.Dimension(190, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        container1.add(cmbFrequenciaAtendimento, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Frequência");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 0);
        container1.add(jLabel12, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Valor da Consulta:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 0);
        container1.add(jLabel10, gridBagConstraints);

        txtValorConsulta.setMaximumSize(new java.awt.Dimension(190, 32));
        txtValorConsulta.setMinimumSize(new java.awt.Dimension(140, 32));
        txtValorConsulta.setPreferredSize(new java.awt.Dimension(190, 32));
        txtValorConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorConsultaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        container1.add(txtValorConsulta, gridBagConstraints);

        cmbTipoAtendimento.setMaximumSize(new java.awt.Dimension(190, 32));
        cmbTipoAtendimento.setMinimumSize(new java.awt.Dimension(100, 32));
        cmbTipoAtendimento.setPreferredSize(new java.awt.Dimension(190, 32));
        cmbTipoAtendimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoAtendimentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 15);
        container1.add(cmbTipoAtendimento, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Tipo Atendimento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 0);
        container1.add(jLabel7, gridBagConstraints);

        txtObservacao.setMaximumSize(new java.awt.Dimension(0, 0));
        txtObservacao.setMinimumSize(new java.awt.Dimension(0, 0));
        txtObservacao.setPreferredSize(new java.awt.Dimension(0, 0));
        txtObservacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObservacaoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.ipady = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 20, 15);
        container1.add(txtObservacao, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Observação");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 15, 5, 0);
        container1.add(jLabel6, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(13, 82, 65));
        jLabel19.setText("Agendar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 5, 0);
        container1.add(jLabel19, gridBagConstraints);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        btnRealizarAgendamento.setBackground(new java.awt.Color(13, 82, 65));
        btnRealizarAgendamento.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRealizarAgendamento.setForeground(new java.awt.Color(255, 255, 255));
        btnRealizarAgendamento.setText("Agendar");
        btnRealizarAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarAgendamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 20);
        jPanel1.add(btnRealizarAgendamento, gridBagConstraints);

        btnLimparDadosAgendamento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLimparDadosAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        btnLimparDadosAgendamento.setText("Limpar");
        btnLimparDadosAgendamento.setIconTextGap(10);
        btnLimparDadosAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparDadosAgendamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(btnLimparDadosAgendamento, gridBagConstraints);

        jPanel2.add(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        container1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(container1, gridBagConstraints);

        container2.setBackground(new java.awt.Color(254, 254, 254));
        container2.setLayout(new java.awt.GridBagLayout());

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(13, 82, 65));
        jLabel14.setText("Filtrar tabela");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 2097;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 28);
        container2.add(jLabel14, gridBagConstraints);

        jPanel3.setMinimumSize(new java.awt.Dimension(0, 59));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 59));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Status Pagamento");
        jLabel11.setMaximumSize(new java.awt.Dimension(51, 20));
        jLabel11.setMinimumSize(new java.awt.Dimension(0, 20));
        jLabel11.setPreferredSize(new java.awt.Dimension(115, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jLabel11, gridBagConstraints);

        cmbFiltrarTipoAtendimento.setMinimumSize(new java.awt.Dimension(0, 32));
        cmbFiltrarTipoAtendimento.setPreferredSize(new java.awt.Dimension(0, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        jPanel3.add(cmbFiltrarTipoAtendimento, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Paciente");
        jLabel15.setMinimumSize(new java.awt.Dimension(0, 20));
        jLabel15.setPreferredSize(new java.awt.Dimension(115, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jLabel15, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Frequência");
        jLabel18.setMaximumSize(new java.awt.Dimension(51, 20));
        jLabel18.setMinimumSize(new java.awt.Dimension(0, 20));
        jLabel18.setPreferredSize(new java.awt.Dimension(115, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jLabel18, gridBagConstraints);

        cmbFiltrarFrequenciaAtendimento.setMinimumSize(new java.awt.Dimension(0, 32));
        cmbFiltrarFrequenciaAtendimento.setPreferredSize(new java.awt.Dimension(0, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        jPanel3.add(cmbFiltrarFrequenciaAtendimento, gridBagConstraints);

        dtFiltrarDataAgendamento.setMinimumSize(new java.awt.Dimension(0, 32));
        dtFiltrarDataAgendamento.setPreferredSize(new java.awt.Dimension(0, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        jPanel3.add(dtFiltrarDataAgendamento, gridBagConstraints);

        cmbFiltrarStatusPagamento.setMinimumSize(new java.awt.Dimension(0, 32));
        cmbFiltrarStatusPagamento.setPreferredSize(new java.awt.Dimension(0, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        jPanel3.add(cmbFiltrarStatusPagamento, gridBagConstraints);

        cmbFiltrarPaciente.setMinimumSize(new java.awt.Dimension(0, 32));
        cmbFiltrarPaciente.setPreferredSize(new java.awt.Dimension(0, 32));
        cmbFiltrarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFiltrarPacienteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        jPanel3.add(cmbFiltrarPaciente, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Data da Agenda:");
        jLabel13.setMaximumSize(new java.awt.Dimension(51, 20));
        jLabel13.setMinimumSize(new java.awt.Dimension(0, 20));
        jLabel13.setPreferredSize(new java.awt.Dimension(115, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jLabel13, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Tipo Atendimento");
        jLabel17.setMaximumSize(new java.awt.Dimension(51, 20));
        jLabel17.setMinimumSize(new java.awt.Dimension(0, 20));
        jLabel17.setPreferredSize(new java.awt.Dimension(115, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jLabel17, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        container2.add(jPanel3, gridBagConstraints);

        jPanel4.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        container2.add(jPanel4, gridBagConstraints);

        jPanel5.setOpaque(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        container2.add(jPanel5, gridBagConstraints);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridBagLayout());

        btnLimparFiltroAgendamento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLimparFiltroAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        btnLimparFiltroAgendamento.setText("Limpar Filtros");
        btnLimparFiltroAgendamento.setMaximumSize(new java.awt.Dimension(110, 39));
        btnLimparFiltroAgendamento.setMinimumSize(new java.awt.Dimension(110, 39));
        btnLimparFiltroAgendamento.setPreferredSize(new java.awt.Dimension(110, 39));
        btnLimparFiltroAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparFiltroAgendamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 106;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel7.add(btnLimparFiltroAgendamento, gridBagConstraints);

        btnFiltroAgendamento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnFiltroAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa-32.png"))); // NOI18N
        btnFiltroAgendamento.setText("Buscar ");
        btnFiltroAgendamento.setMaximumSize(new java.awt.Dimension(110, 39));
        btnFiltroAgendamento.setMinimumSize(new java.awt.Dimension(110, 39));
        btnFiltroAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltroAgendamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel7.add(btnFiltroAgendamento, gridBagConstraints);

        jPanel6.add(jPanel7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 0);
        container2.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(container2, gridBagConstraints);

        container3.setBackground(new java.awt.Color(236, 249, 242));

        tblListarAgendamentosDoDia.setModel(new javax.swing.table.DefaultTableModel(
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
        tblListarAgendamentosDoDia.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblListarAgendamentosDoDiaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane2.setViewportView(tblListarAgendamentosDoDia);

        lblDataHoje.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lblDataHoje.setForeground(new java.awt.Color(13, 82, 65));
        lblDataHoje.setText("30/09/2026");
        lblDataHoje.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblDataHojeAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(13, 82, 65));
        jLabel8.setText("Horários do dia");

        javax.swing.GroupLayout container3Layout = new javax.swing.GroupLayout(container3);
        container3.setLayout(container3Layout);
        container3Layout.setHorizontalGroup(
            container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, container3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(lblDataHoje)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(container3Layout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addGap(10, 10, 10)))
        );
        container3Layout.setVerticalGroup(
            container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataHoje)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(container3Layout.createSequentialGroup()
                    .addGap(81, 81, 81)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        add(container3, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObservacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObservacaoActionPerformed

    private void cmbTipoAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoAtendimentoActionPerformed

    }//GEN-LAST:event_cmbTipoAtendimentoActionPerformed

    private void tblAgendamentosAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblAgendamentosAncestorAdded
        //2026-10-03 Juliano: definindo os campos que estarão na minha tabela
        modeloTabelaListarTodosAgendamentos = GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.formatarTabelaAgendamento(modeloTabelaListarTodosAgendamentos);

        //2026-10-03 Juliano: Utilizando a definição criada anteriormente, para ser adicionada em tblAgendamentos
        tblAgendamentos.setModel(modeloTabelaListarTodosAgendamentos);

        //2026-10-03 Juliano: Definindo os icones presentes na minha tabela
        GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.configurarIconesDasColunasDaTabelaAgendamento(tblAgendamentos);

        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        agendamentos = agendamentojpa.listarAgendamentos();
        modeloTabelaListarTodosAgendamentos.setRowCount(0);

        GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);


    }//GEN-LAST:event_tblAgendamentosAncestorAdded

    private void btnLimparDadosAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparDadosAgendamentoActionPerformed
        LimparDadosAgendamento();
    }//GEN-LAST:event_btnLimparDadosAgendamentoActionPerformed

    private void btnRealizarAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarAgendamentoActionPerformed
        //2026-09-14 Juliano: instâncias necessarias para realizar o agendamento da consulta
        Agendamento agendamento = new Agendamento();
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        PacienteJPA pacientejpa = new PacienteJPA();

        //=============================================2026-09-22 Juliano: Validações necessarias no  agendamento para evitar erros e excessoes(INICIO)=================================================================
        //2026-09-14 Juliano: Validação de data, para poder evitar cadastrar um agendamento sem data
        if (dtDataAgendamento.getDate() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma data."
            );
            return;
        }

        //2026-09-14 Juliano: Validação de valor, se caso não digitar nenhum valor ele entende como uma STRING, então eu preciso colocar essa validação para forçar agendamentos entrada de valor, além de que não existe consulta sem valor
        if (txtValorConsulta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um valor para o agendamento da consulta para poder continuar."
            );
            return;
        }

        //2026-09-18 Juliano: Validação de horário, se caso não for digitado um horário, o psicologo não irá conseguir cadastrar o agendamento
        if (txtHorario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um horário para o seu agendamento."
            );
            return;
        }
        //2026-09-18 Juliano: Adicionado validação para impedir que o usuario informe um horário inexistente
        try {
            LocalTime.parse(txtHorario.getText());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um horário válido. Exemplo: 17:30."
            );
            return;
        }
        //=============================================2026-09-22 Juliano: Validações necessarias no  agendamento para evitar erros e excessoes(FIM)=================================================================

//===========================================================Tratamento das entradas no sistema 2026-09-22 Juliano: (INICIO)=======================================================================//
        String valor = txtValorConsulta.getText().trim();
        BigDecimal valorConsulta = new BigDecimal(valor.replace(",", "."));
        LocalTime horario = LocalTime.parse(txtHorario.getText());
        LocalDate dataInicial = dtDataAgendamento.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        //2026-09-22 Juliano: Pegando o nome do paciente para poder associar ao seu respectivo agendamento
        Paciente PacienteNome = (Paciente) cmbListarPacientes.getSelectedItem();
        Paciente paciente = pacientejpa.buscarPorId(PacienteNome.getId());

        //2026-09-22 Juliano: Validação dos agendamentos não é possível realizar um agendamento de um dia e um horário que já está agendado
        if (agendamentojpa.horarioOcupado(dataInicial, horario)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Este horário e está data já está agendado, escolha outro para continuar."
            );
            return;
        }

        //=========================================================================(FIM)==========================================================================================//
        if ((FrequenciaAtendimento) cmbFrequenciaAtendimento.getSelectedItem() == FrequenciaAtendimento.SEMANAL) {

            LocalDate ultimaData = dataInicial.with(TemporalAdjusters.lastDayOfMonth());
            LocalDate data = dataInicial;
            System.out.println("Cadastrando data --> " + data);
            System.out.println("Tipo de frequencia do paciente  SEMANAL");

            while (!data.isAfter(ultimaData)) {
                System.out.println("Vou cadastrar as datas ---> " + data);
                cadastrarAgendamento(data, valorConsulta, horario, paciente, agendamentojpa);
                System.out.println("Já cadastrei as datas");
                modeloTabelaListarTodosAgendamentos.setRowCount(0);
                agendamentos = ListarAgendamentos();
                GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
                LimparDadosAgendamento();

                data = data.plusWeeks(1);
                System.out.println("Data agendada em --> " + data);
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Agendamento cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.PLAIN_MESSAGE,
                    new ImageIcon(getClass().getResource("/imagens/Alertas/sucesso.png"))
            );

        }
        if ((FrequenciaAtendimento) cmbFrequenciaAtendimento.getSelectedItem() == FrequenciaAtendimento.QUINZENAL) {

            LocalDate ultimaData = dataInicial.with(TemporalAdjusters.lastDayOfMonth());
            LocalDate data = dataInicial;
            System.out.println("Cadastrando data --> " + data);
            System.out.println("Tipo de frequencia do paciente  QUINZENAL");

            while (!data.isAfter(ultimaData)) {
                cadastrarAgendamento(data, valorConsulta, horario, paciente, agendamentojpa);
                modeloTabelaListarTodosAgendamentos.setRowCount(0);
                agendamentos = ListarAgendamentos();
                GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
                LimparDadosAgendamento();

                data = data.plusDays(15);
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Agendamento cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.PLAIN_MESSAGE,
                    new ImageIcon(getClass().getResource("/imagens/Alertas/sucesso.png"))
            );
        }
        if ((FrequenciaAtendimento) cmbFrequenciaAtendimento.getSelectedItem() == FrequenciaAtendimento.MENSAL) {
            System.out.println("Cadastrando data --> " + dataInicial);
            System.out.println("Tipo de frequencia do paciente  MENSAL");
            cadastrarAgendamento(dataInicial, valorConsulta, horario, paciente, agendamentojpa);
            modeloTabelaListarTodosAgendamentos.setRowCount(0);
            agendamentos = ListarAgendamentos();
            GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
            LimparDadosAgendamento();

            JOptionPane.showMessageDialog(
                    this,
                    "Agendamento cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.PLAIN_MESSAGE,
                    new ImageIcon(getClass().getResource("/imagens/Alertas/sucesso.png"))
            );

        }


    }//GEN-LAST:event_btnRealizarAgendamentoActionPerformed

    private void tblAgendamentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgendamentosMouseClicked

        int linha = tblAgendamentos.rowAtPoint(evt.getPoint());
        int coluna = tblAgendamentos.columnAtPoint(evt.getPoint());
        Long CodigoId = (Long) tblAgendamentos.getValueAt(linha, 0);

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
            atualizartabelaAgendamento();

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
                atualizartabelaAgendamento();
                JOptionPane.showMessageDialog(
                        this,
                        "Agendamento excluído com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

            }

        }
    }//GEN-LAST:event_tblAgendamentosMouseClicked

    private void txtHorarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorarioKeyReleased
        String horario = txtHorario.getText();

        // Remove tudo que não for número
        horario = horario.replaceAll("[^0-9]", "");

        // Limita a 4 números
        if (horario.length() > 4) {
            horario = horario.substring(0, 4);
        }

        // Adiciona os dois pontos
        if (horario.length() >= 3) {
            horario = horario.substring(0, 2) + ":" + horario.substring(2);
        }

        txtHorario.setText(horario);
    }//GEN-LAST:event_txtHorarioKeyReleased

    private void txtValorConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorConsultaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorConsultaActionPerformed

    private void tblListarAgendamentosDoDiaAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblListarAgendamentosDoDiaAncestorAdded
        //2025-11-22 Juliano definindo o nome das colunas da tabela
        modeloTabelaListarAgendamentosDoDia = new DefaultTableModel(
                new Object[]{
                    "Paciente",
                    "Horário",
                    "Pagamento",
                    "Valor"
                },
                0
        );
        tblListarAgendamentosDoDia.setModel(modeloTabelaListarAgendamentosDoDia);
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        List<Agendamento> ListarAgendamentos = agendamentojpa.listarAgendamentosDoDia();
        modeloTabelaListarAgendamentosDoDia.setRowCount(0);

        carregarAgendamentosDoDIa(modeloTabelaListarAgendamentosDoDia, ListarAgendamentos);


    }//GEN-LAST:event_tblListarAgendamentosDoDiaAncestorAdded

    private void btnFiltroAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltroAgendamentoActionPerformed
        AgendaFiltro filtro = new AgendaFiltro();
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();

        if (!dtFiltrarDataAgendamento.getDate().toString().trim().isEmpty()) {
            LocalDate data = dtFiltrarDataAgendamento.getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            filtro.setDataAgendamento(data);

        }
        if (!cmbFiltrarPaciente.getSelectedItem().toString().trim().isEmpty()) {

            filtro.setPaciente((Paciente) cmbFiltrarPaciente.getSelectedItem());

        }
        if (!cmbFiltrarStatusPagamento.getSelectedItem().toString().trim().isEmpty()) {

            filtro.setStatuspagamento((StatusPagamento) cmbFiltrarStatusPagamento.getSelectedItem());

        }
        if (!cmbFiltrarTipoAtendimento.getSelectedItem().toString().trim().isEmpty()) {

            filtro.setTipoatendimento((TipoAtendimento) cmbFiltrarTipoAtendimento.getSelectedItem());

        }
        if (!cmbFiltrarFrequenciaAtendimento.getSelectedItem().toString().trim().isEmpty()) {

            filtro.setFrequenciaAtendimento((FrequenciaAtendimento) cmbFiltrarFrequenciaAtendimento.getSelectedItem());

        }

        List<Agendamento> ListarAgendamentosFiltrados = agendamentojpa.filtrarPacientes(filtro);

        //2025-11-28 Juliano Atualiza a tabela
        DefaultTableModel model = (DefaultTableModel) tblAgendamentos.getModel();
        model.setRowCount(0); // 2025-11-28 Juliano limpa tudo deixar a tabela vazia para chegar a tabela agora filtrada

        for (Agendamento agendamento : ListarAgendamentosFiltrados) {
            String statuspagamento = agendamento.getStatuspagamento().toString();
            String pagamentoformatado;
            if (statuspagamento.equals("PENDENTE")) {
                pagamentoformatado = "<html><font color='red'><b>PENDENTE</b></font></html>";
            } else {
                pagamentoformatado = "<html><font color='green'><b>PAGO</b></font></html>";
            }

            model.addRow(new Object[]{
                agendamento.getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getHorario(),
                agendamento.getStatusagendamento(),
                agendamento.getTipoatendimento(),
                pagamentoformatado,
                "",
                agendamento.getDataAgendamento().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ),
                agendamento.getValorDaConsulta(),
                "",
                ""

            });

        }
    }//GEN-LAST:event_btnFiltroAgendamentoActionPerformed

    private void btnLimparFiltroAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparFiltroAgendamentoActionPerformed

        AgendaFiltro filtro = new AgendaFiltro();
        AgendamentoJPA agendamentojpa = new AgendamentoJPA();

        List<Agendamento> ListarTodosAgendamentos = agendamentojpa.listarAgendamentos();

        //2025-11-28 Juliano Atualiza a tabela
        DefaultTableModel model = (DefaultTableModel) tblAgendamentos.getModel();
        model.setRowCount(0); // 2025-11-28 Juliano limpa tudo deixar a tabela vazia para chegar a tabela agora filtrada

        for (Agendamento agendamento : ListarTodosAgendamentos) {
            String statuspagamento = agendamento.getStatuspagamento().toString();
            String pagamentoformatado;
            if (statuspagamento.equals("PENDENTE")) {
                pagamentoformatado = "<html><font color='red'><b>PENDENTE</b></font></html>";
            } else {
                pagamentoformatado = "<html><font color='green'><b>PAGO</b></font></html>";
            }

            model.addRow(new Object[]{
                agendamento.getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getHorario(),
                agendamento.getStatusagendamento(),
                agendamento.getTipoatendimento(),
                pagamentoformatado,
                "",
                agendamento.getDataAgendamento().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ),
                agendamento.getValorDaConsulta(),
                "",
                ""

            });

        }
    }//GEN-LAST:event_btnLimparFiltroAgendamentoActionPerformed

    private void cmbListarPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbListarPacientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbListarPacientesActionPerformed

    private void txtHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorarioActionPerformed

    private void lblDataHojeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblDataHojeAncestorAdded
        lblDataHoje.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }//GEN-LAST:event_lblDataHojeAncestorAdded

    private void cmbFiltrarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFiltrarPacienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFiltrarPacienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser btnData;
    private javax.swing.JButton btnFiltroAgendamento;
    private javax.swing.JButton btnLimparDadosAgendamento;
    private javax.swing.JButton btnLimparFiltroAgendamento;
    private javax.swing.JButton btnRealizarAgendamento;
    private javax.swing.JComboBox<FrequenciaAtendimento> cmbFiltrarFrequenciaAtendimento;
    private javax.swing.JComboBox<String> cmbFiltrarPaciente;
    private javax.swing.JComboBox<StatusPagamento> cmbFiltrarStatusPagamento;
    private javax.swing.JComboBox<TipoAtendimento> cmbFiltrarTipoAtendimento;
    private javax.swing.JComboBox<FrequenciaAtendimento> cmbFrequenciaAtendimento;
    private javax.swing.JComboBox<Paciente> cmbListarPacientes;
    private javax.swing.JComboBox<StatusPagamento> cmbStatusPagamento;
    private javax.swing.JComboBox<TipoAtendimento> cmbTipoAtendimento;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container1;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container2;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container3;
    private com.toedter.calendar.JDateChooser dtDataAgendamento;
    private com.toedter.calendar.JDateChooser dtFiltrarDataAgendamento;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDataHoje;
    private javax.swing.JTable tblAgendamentos;
    private javax.swing.JTable tblListarAgendamentosDoDia;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextField txtObservacao;
    private javax.swing.JTextField txtValorConsulta;
    // End of variables declaration//GEN-END:variables
}
