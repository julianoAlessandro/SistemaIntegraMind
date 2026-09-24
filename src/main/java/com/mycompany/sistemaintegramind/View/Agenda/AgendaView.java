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
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
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
            cmbListarPacientes.addItem(p.getNome());
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
                agendamentos.getDataAgendamento().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ),
                agendamentos.getValorDaConsulta(),
                "",
                ""

            });
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
        carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
        tblAgendamentos.revalidate();
        tblAgendamentos.repaint();
    }

    private void cadastrarAgendamento(LocalDate data, BigDecimal valorConsulta, LocalTime horario, Paciente paciente, AgendamentoJPA agendamentojpa) {
        //2026-09-22 Juliano: criação de um novo objeto para cada novo agendamento posterior
        Agendamento novoagendamento = new Agendamento();

        novoagendamento.setDataAgendamento(data);
        novoagendamento.setObservacao(txtObservacao.getText());
        novoagendamento.setValorDaConsulta(valorConsulta);
        novoagendamento.setHorario(horario);
        novoagendamento.setTipoatendimento((TipoAtendimento) cmbTipoAtendimento.getSelectedItem());
        novoagendamento.setPaciente(paciente);
        novoagendamento.setStatuspagamento((StatusPagamento) cmbStatusPagamento.getSelectedItem());
        novoagendamento.setStatusagendamento(StatusAgendamento.AGENDADO);
        novoagendamento.setFrequenciaatendimento((FrequenciaAtendimento) cmbFrequenciaAtendimento.getSelectedItem());
        novoagendamento.setStatusPacienteAgendamento(StatusPacienteAgendamento.ATIVO);

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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbTipoAtendimento = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtObservacao = new javax.swing.JTextField();
        cmbListarPacientes = new javax.swing.JComboBox<>();
        dtDataAgendamento = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListarAgendamentosDoDia = new javax.swing.JTable();
        btnRealizarAgendamento = new javax.swing.JButton();
        btnLimparDadosAgendamento = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAgendamentos = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbStatusPagamento = new javax.swing.JComboBox<>();
        txtValorConsulta = new javax.swing.JTextField();
        btnAtualizarTabela = new javax.swing.JButton();
        txtHorario = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cmbFrequenciaAtendimento = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        dtFiltrarDataAgendamento = new com.toedter.calendar.JDateChooser();
        btnLimparFiltroAgendamento = new javax.swing.JButton();
        btnFiltroAgendamento = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        cmbFiltrarFrequenciaAtendimento = new javax.swing.JComboBox<>();
        cmbFiltrarPaciente = new javax.swing.JComboBox<>();
        cmbFiltrarStatusPagamento = new javax.swing.JComboBox<>();
        cmbFiltrarTipoAtendimento = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        jLabel2.setText("Agendamento ");

        jLabel3.setText("Paciente");

        jLabel4.setText("Data");

        jLabel5.setText("Horário");

        cmbTipoAtendimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoAtendimentoActionPerformed(evt);
            }
        });

        jLabel6.setText("Observação");

        jLabel7.setText("Tipo Atendimento");

        txtObservacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObservacaoActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setForeground(new java.awt.Color(153, 153, 255));

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel8.setText("Horários do dia 22/09/2026");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(108, 108, 108))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        btnRealizarAgendamento.setText("Agendar");
        btnRealizarAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarAgendamentoActionPerformed(evt);
            }
        });

        btnLimparDadosAgendamento.setText("Limpar");
        btnLimparDadosAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparDadosAgendamentoActionPerformed(evt);
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

        txtValorConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorConsultaActionPerformed(evt);
            }
        });

        btnAtualizarTabela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/reflesh.png"))); // NOI18N
        btnAtualizarTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarTabelaActionPerformed(evt);
            }
        });

        txtHorario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHorarioKeyReleased(evt);
            }
        });

        jLabel12.setText("Frequência");

        jLabel13.setText("Data da Agenda:");

        btnLimparFiltroAgendamento.setText("Limpar Filtros");
        btnLimparFiltroAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparFiltroAgendamentoActionPerformed(evt);
            }
        });

        btnFiltroAgendamento.setText("Buscar ");
        btnFiltroAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltroAgendamentoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel14.setText("Filtrar tabela");

        jLabel15.setText("Paciente");

        jLabel16.setText("Status Pagamento");

        jLabel17.setText("Tipo Atendimento");

        jLabel18.setText("Frequência");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(3, 3, 3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(6, 6, 6)
                                                        .addComponent(cmbTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(jLabel7)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(33, 33, 33)
                                                        .addComponent(btnRealizarAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnLimparDadosAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(143, 143, 143)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(151, 151, 151)
                                                        .addComponent(txtObservacao, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(48, 48, 48)
                                                        .addComponent(btnAtualizarTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(144, 144, 144)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(cmbFiltrarTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel17))
                                                        .addGap(37, 37, 37)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(cmbFiltrarFrequenciaAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(33, 33, 33)
                                                                .addComponent(btnFiltroAgendamento)
                                                                .addGap(26, 26, 26)
                                                                .addComponent(btnLimparFiltroAgendamento))
                                                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel3)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(162, 162, 162)
                                                        .addComponent(jLabel4)
                                                        .addGap(110, 110, 110)
                                                        .addComponent(jLabel5))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(cmbListarPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(37, 37, 37)
                                                        .addComponent(dtDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txtHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(26, 26, 26)
                                                .addComponent(cmbStatusPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(41, 41, 41)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(cmbFrequenciaAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(23, 23, 23)
                                                        .addComponent(jLabel10)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtValorConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(dtFiltrarDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cmbFiltrarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel15))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel11)
                                                    .addComponent(cmbFiltrarStatusPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel14)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 868, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 868, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(498, 498, 498)
                    .addComponent(jLabel16)
                    .addContainerGap(1136, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbListarPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(txtValorConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbFrequenciaAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbStatusPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dtDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnRealizarAgendamento)
                                    .addComponent(btnLimparDadosAgendamento)))
                            .addComponent(txtObservacao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAtualizarTabela, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(jLabel11)
                        .addComponent(jLabel17)
                        .addComponent(jLabel18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(dtFiltrarDataAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnFiltroAgendamento)
                        .addComponent(btnLimparFiltroAgendamento)
                        .addComponent(cmbFiltrarFrequenciaAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbFiltrarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbFiltrarStatusPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbFiltrarTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(7, 7, 7))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 374, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 374, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(jLabel16)
                    .addContainerGap(657, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObservacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObservacaoActionPerformed

    private void cmbTipoAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoAtendimentoActionPerformed

    }//GEN-LAST:event_cmbTipoAtendimentoActionPerformed

    private void tblAgendamentosAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblAgendamentosAncestorAdded
        //2025-11-22 Juliano definindo o nome das colunas da tabela
        modeloTabelaListarTodosAgendamentos = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Paciente",
                    "Horário",
                    "Status",
                    "Tipo de Atendimento",
                    "Pagamento",
                    "Situação",
                    "Data",
                    "Valor da Consulta",
                    "Editar",
                    "Excluir"
                },
                0
        );

        tblAgendamentos.setModel(modeloTabelaListarTodosAgendamentos);

        tblAgendamentos.getColumnModel()
                .getColumn(6).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(6)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/PENDENTE.png"));

        tblAgendamentos.getColumnModel().getColumn(9).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(9)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/empate.png"));

        tblAgendamentos.getColumnModel().getColumn(10).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(10)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/excluir.png"));

        AgendamentoJPA agendamentojpa = new AgendamentoJPA();
        List<Agendamento> ListarAgendamentos = agendamentojpa.listarAgendamentos();
        modeloTabelaListarTodosAgendamentos.setRowCount(0);

        carregarAgendamento(modeloTabelaListarTodosAgendamentos, ListarAgendamentos);


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
        String PacienteNome = (String) cmbListarPacientes.getSelectedItem();
        Paciente paciente = pacientejpa.buscarPorNome(PacienteNome);
        
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
                carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
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
                carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
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
            carregarAgendamento(modeloTabelaListarTodosAgendamentos, agendamentos);
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
                agendamentoexclusao.setStatusPacienteAgendamento(StatusPacienteAgendamento.INATIVO);
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

    private void btnAtualizarTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarTabelaActionPerformed
        //2026-09-17 Juliano: Atualizando a listagem de pacientes agendados no sistema!!
        System.out.println("Recarregando a tabela de agendameto!!!!");
        atualizartabelaAgendamento();
    }//GEN-LAST:event_btnAtualizarTabelaActionPerformed

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

            filtro.setPaciente((Paciente)cmbFiltrarPaciente.getSelectedItem());

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizarTabela;
    private javax.swing.JButton btnFiltroAgendamento;
    private javax.swing.JButton btnLimparDadosAgendamento;
    private javax.swing.JButton btnLimparFiltroAgendamento;
    private javax.swing.JButton btnRealizarAgendamento;
    private javax.swing.JComboBox<FrequenciaAtendimento> cmbFiltrarFrequenciaAtendimento;
    private javax.swing.JComboBox<String> cmbFiltrarPaciente;
    private javax.swing.JComboBox<StatusPagamento> cmbFiltrarStatusPagamento;
    private javax.swing.JComboBox<TipoAtendimento> cmbFiltrarTipoAtendimento;
    private javax.swing.JComboBox<FrequenciaAtendimento> cmbFrequenciaAtendimento;
    private javax.swing.JComboBox<String> cmbListarPacientes;
    private javax.swing.JComboBox<StatusPagamento> cmbStatusPagamento;
    private javax.swing.JComboBox<TipoAtendimento> cmbTipoAtendimento;
    private com.toedter.calendar.JDateChooser dtDataAgendamento;
    private com.toedter.calendar.JDateChooser dtFiltrarDataAgendamento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblAgendamentos;
    private javax.swing.JTable tblListarAgendamentosDoDia;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextField txtObservacao;
    private javax.swing.JTextField txtValorConsulta;
    // End of variables declaration//GEN-END:variables
}
