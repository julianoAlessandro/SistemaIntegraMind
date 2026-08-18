/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Pacientes;

import com.mycompany.sistemaintegramind.util.Utilitarios.ValidarCpfCnpj;
import com.mycompany.sistemaintegramind.Model.dao.ClienteFiltro;
import com.mycompany.sistemaintegramind.Model.dao.impl.PacienteJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.FinanceiroJPA;
import com.mycompany.sistemaintegramind.Model.dto.FinanceiroDTO;
import com.mycompany.sistemaintegramind.Model.entidades.Pacientes;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.EstadosBrasileiros;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.Sexo;
import com.mycompany.sistemaintegramind.View.Componentes.BotaoEstilo;
import com.mycompany.sistemaintegramind.View.Componentes.ComponenteUtil;
import com.mycompany.sistemaintegramind.View.Componentes.TabelaEstilo;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import static javax.xml.bind.DatatypeConverter.parseInteger;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import java.text.ParseException;
import javax.swing.text.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.persistence.EntityManagerFactory;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Juliano
 */
public class PacienteView extends javax.swing.JPanel {

    private PacienteJPA pacientejpa = new PacienteJPA();
    private ClienteFiltro filtro = new ClienteFiltro();
    private List<Pacientes> listarPacientesFiltrados;

    public PacienteView() {
        initComponents();

        jLabel3.setText("<html>Nome <font color='red'>*</font></html>");

        ComponenteUtil.formatarScrollInvisivel(jScrollPane3);
        container1.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        BotaoEstilo.aplicarEfeito(btnSalvar,
                new Color(106, 218, 160),
                new Color(87, 179, 131),
                new Color(72, 148, 109)
        );
        TabelaEstilo.aplicar(tblClientes);

        //2025-12-22 Guilherme: Define placeholders dos campos
        txtNome.putClientProperty("JTextField.placeholderText", "Ex: João da Silva");
        txtcpf.putClientProperty("JTextField.placeholderText", "Ex: 123.456.789-00");
        txtEmail.putClientProperty("JTextField.placeholderText", "Ex: joao@email.com");
        txtNomeCliente.putClientProperty("JTextField.placeholderText", "Ex: João da Silva");
        txtDataNascimento.putClientProperty("JTextField.placeholderText", "Ex: 22/12/2005");
        txtTelefoneFixo.putClientProperty("JTextField.placeholderText", "Ex: (11) 3456-7890");
        txtTelefoneCelular.putClientProperty("JTextField.placeholderText", "Ex: (11) 91234-5678");
        txtTelefoneComercial.putClientProperty("JTextField.placeholderText", "Ex: (11) 3344-5566");
        txtBuscaCep.putClientProperty("JTextField.placeholderText", "Ex: 01001-000");
        txtRua.putClientProperty("JTextField.placeholderText", "Ex: Av. Paulista");
        txtBairro.putClientProperty("JTextField.placeholderText", "Ex: Bela Vista");
        txtNumero.putClientProperty("JTextField.placeholderText", "Ex: 1000");
        txtComplemento.putClientProperty("JTextField.placeholderText", "Ex: Apto 12, Bloco B");

        //2025-12-23 Guilherme: Ajusta aparência das abas
        abasCadastrarClientes.putClientProperty("JTabbedPane.tabHeight", 45);
        UIManager.put("TabbedPane.tabInsets", new Insets(0, 20, 0, 20));
        abasCadastrarClientes.putClientProperty("JTabbedPane.underlineColor", new Color(58, 58, 191));
        abasCadastrarClientes.putClientProperty("JTabbedPane.tabUnderlineHeight", 3);

        cbSexo.setModel(new DefaultComboBoxModel<>(Sexo.values()));
        cbEstadosSigla.setModel(new DefaultComboBoxModel<>(EstadosBrasileiros.values()));

        tblClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblClientes.setRowHeight(28);

        //2026-01-10 Guilherme: Máscara para data e números de telefone
        ((AbstractDocument) txtDataNascimento.getDocument()).setDocumentFilter(new MascaraData());
        txtDataNascimento.getActionMap().put("invalid-edit", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                /* Não faz nada, logo, não faz barulho */
            }
        });

        // Lista de campos de telefone para aplicar o filtro e silenciar
        javax.swing.JTextField[] camposTelefone = {txtTelefoneCelular, txtTelefoneFixo, txtTelefoneComercial};

        for (javax.swing.JTextField campo : camposTelefone) {
            // Aplica a máscara moderna
            ((AbstractDocument) campo.getDocument()).setDocumentFilter(new MascaraTelefone());

            // Remove o barulho (beep) de erro
            campo.getActionMap().put("invalid-edit", new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                }
            });
        }

        ((AbstractDocument) txtEmail.getDocument()).setDocumentFilter(new FiltroEmail());

        // Silenciador de sons CEP, CPF e CNPJ
        javax.swing.Action silenciador = new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
            }
        };

        // Guilherme: Aplicar mascara de CPF
        ((AbstractDocument) txtcpf.getDocument()).setDocumentFilter(new MascaraCPF());
        txtcpf.getActionMap().put("invalid-edit", silenciador);

        //Guilherme: Aplica CEP (usando o que passei anteriormente)
        ((AbstractDocument) txtBuscaCep.getDocument()).setDocumentFilter(new MascaraCEP());
        txtBuscaCep.getActionMap().put("invalid-edit", silenciador);

        btnCep.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarCep();
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupPessoa = new javax.swing.ButtonGroup();
        buttonGroupCliente = new javax.swing.ButtonGroup();
        abasCadastrarClientes = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        container1 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jLabel22 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtBuscaCep = new javax.swing.JTextField();
        btnCep = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtRua = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtComplemento = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        txtCidade = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        cbEstadosSigla = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTelefoneCelular = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtTelefoneComercial = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTelefoneFixo = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNomeCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbSexo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtcpf = new javax.swing.JTextField();
        txtDataNascimento = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        container2 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jLabel18 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnBuscarFiltro = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new java.awt.GridBagLayout());

        abasCadastrarClientes.setOpaque(true);
        abasCadastrarClientes.setPreferredSize(new java.awt.Dimension(1200, 735));

        jPanel1.setBackground(new java.awt.Color(221, 223, 237));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setPreferredSize(new java.awt.Dimension(1078, 682));

        container1.setBackground(new java.awt.Color(251, 251, 253));
        container1.setMinimumSize(new java.awt.Dimension(200, 200));
        container1.setPreferredSize(new java.awt.Dimension(1078, 682));
        container1.setLayout(new java.awt.GridBagLayout());

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/paciente_black.png"))); // NOI18N
        jLabel22.setText("Cadastro de Pacientes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 410;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        container1.add(jLabel22, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1000;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        container1.add(jSeparator1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1000;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 0, 0);
        container1.add(jSeparator2, gridBagConstraints);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridBagLayout());

        btnSalvar.setBackground(new java.awt.Color(106, 218, 160));
        btnSalvar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnSalvar.setForeground(new java.awt.Color(255, 255, 255));
        btnSalvar.setText("SALVAR");
        btnSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalvar.setMaximumSize(new java.awt.Dimension(200, 50));
        btnSalvar.setMinimumSize(new java.awt.Dimension(100, 25));
        btnSalvar.setPreferredSize(new java.awt.Dimension(200, 50));
        btnSalvar.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                btnSalvarAncestorResized(evt);
            }
        });
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 25;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel7.add(btnSalvar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 293;
        gridBagConstraints.ipady = -38;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        container1.add(jPanel7, gridBagConstraints);

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(479, 324));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel14.setText("CEP");

        txtBuscaCep.setPreferredSize(new java.awt.Dimension(210, 30));
        txtBuscaCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaCepActionPerformed(evt);
            }
        });

        btnCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa-32.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel15.setText("Rua");

        txtRua.setPreferredSize(new java.awt.Dimension(210, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel17.setText("Número");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel7.setText("Bairro");

        txtBairro.setPreferredSize(new java.awt.Dimension(210, 30));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel16.setText("Complemento");

        txtComplemento.setPreferredSize(new java.awt.Dimension(210, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Endereço");

        txtNumero.setPreferredSize(new java.awt.Dimension(210, 30));

        txtCidade.setPreferredSize(new java.awt.Dimension(210, 30));
        txtCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCidadeActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel24.setText("Cidade");

        jLabel25.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel25.setText("UF");

        cbEstadosSigla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadosSiglaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel17)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtRua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtBuscaCep, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(jLabel14))
                                        .addGap(0, 0, 0)
                                        .addComponent(btnCep)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBairro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel16)
                                    .addComponent(txtComplemento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel25)
                            .addComponent(cbEstadosSigla, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscaCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnCep, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbEstadosSigla, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        container1.add(jPanel5, gridBagConstraints);

        jPanel6.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel9.setText("Telefone Celular");

        txtTelefoneCelular.setPreferredSize(new java.awt.Dimension(210, 30));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/whatsapp_icon-icons.com_62756.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel10.setText("Telefone Comercial");

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/whatsapp_icon-icons.com_62680.png"))); // NOI18N

        txtTelefoneComercial.setPreferredSize(new java.awt.Dimension(210, 30));
        txtTelefoneComercial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefoneComercialActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel11.setText("Telefone Fixo");

        txtTelefoneFixo.setPreferredSize(new java.awt.Dimension(210, 30));
        txtTelefoneFixo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefoneFixoActionPerformed(evt);
            }
        });

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/whatsapp_icon-icons.com_62756.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel12.setText("Email");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setText("Contatos");

        txtEmail.setPreferredSize(new java.awt.Dimension(210, 30));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefoneFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel30)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTelefoneComercial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel29)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTelefoneCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel21)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTelefoneCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(txtTelefoneComercial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTelefoneFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        container1.add(jPanel6, gridBagConstraints);

        jPanel9.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel3.setText("Nome");

        txtNomeCliente.setMaximumSize(new java.awt.Dimension(250, 30));
        txtNomeCliente.setMinimumSize(new java.awt.Dimension(210, 30));
        txtNomeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeClienteActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setText("Data Nascimento");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setText("Sexo");

        cbSexo.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        cbSexo.setMaximumSize(new java.awt.Dimension(250, 30));
        cbSexo.setMinimumSize(new java.awt.Dimension(210, 30));
        cbSexo.setPreferredSize(new java.awt.Dimension(250, 30));
        cbSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSexoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel5.setText("CPF");

        txtcpf.setMaximumSize(new java.awt.Dimension(250, 30));
        txtcpf.setMinimumSize(new java.awt.Dimension(210, 30));
        txtcpf.setPreferredSize(new java.awt.Dimension(250, 30));
        txtcpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcpfActionPerformed(evt);
            }
        });

        txtDataNascimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataNascimentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbSexo, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                            .addComponent(txtNomeCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtcpf, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                                .addGap(228, 228, 228))))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcpf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        container1.add(jPanel9, gridBagConstraints);

        jPanel10.setOpaque(false);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setText("Informações Básicas");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGap(7, 7, 7))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        container1.add(jPanel10, gridBagConstraints);

        jScrollPane3.setViewportView(container1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane3, gridBagConstraints);

        abasCadastrarClientes.addTab("Cadastrar Pacientes", jPanel1);

        jPanel2.setBackground(new java.awt.Color(221, 223, 237));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        tblClientes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        tblClientes.setMinimumSize(new java.awt.Dimension(500, 500));
        tblClientes.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblClientesAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane4.setViewportView(tblClientes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane4, gridBagConstraints);

        container2.setBackground(new java.awt.Color(251, 251, 253));
        container2.setMaximumSize(new java.awt.Dimension(32767, 265));
        container2.setPreferredSize(new java.awt.Dimension(1000, 700));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("ID");

        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Sexo");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Nome/Nome Fantasia");

        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(102, 255, 102));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/empate.png"))); // NOI18N
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnExcluir.setBackground(new java.awt.Color(255, 153, 153));
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluir.png"))); // NOI18N
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa.png"))); // NOI18N
        jLabel23.setText("Filtros de Pesquisa");
        jLabel23.setIconTextGap(6);

        jPanel4.setBackground(new java.awt.Color(251, 251, 253));

        btnBuscarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa-32.png"))); // NOI18N
        btnBuscarFiltro.setText("Buscar");
        btnBuscarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFiltroActionPerformed(evt);
            }
        });
        jPanel4.add(btnBuscarFiltro);

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        jPanel4.add(btnLimpar);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/printing.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        javax.swing.GroupLayout container2Layout = new javax.swing.GroupLayout(container2);
        container2.setLayout(container2Layout);
        container2Layout.setHorizontalGroup(
            container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(container2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(container2Layout.createSequentialGroup()
                        .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(container2Layout.createSequentialGroup()
                        .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(container2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(353, 353, 353)
                                .addComponent(jLabel19)))
                        .addGap(0, 775, Short.MAX_VALUE))))
        );
        container2Layout.setVerticalGroup(
            container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(container2Layout.createSequentialGroup()
                        .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 30, 5);
        jPanel2.add(container2, gridBagConstraints);

        abasCadastrarClientes.addTab("Listar Pacientes", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(abasCadastrarClientes, gridBagConstraints);
        abasCadastrarClientes.getAccessibleContext().setAccessibleName("Cadastrar Pacientes");
    }// </editor-fold>//GEN-END:initComponents

    //2025-12-09 Juliano conjunto de métodos para otimizar relacionamento com o CRUD do JPA evitando duplicidade  de código e melhorando a legibilidade do mesmo
    private void carregartabela() {
        DefaultTableModel model = (DefaultTableModel) tblClientes.getModel();
        model.setRowCount(0);
        List<Pacientes> listarClientes = pacientejpa.listarClientes();
        for (Pacientes paciente : listarClientes) {
            model.addRow(new Object[]{
                paciente.getNome(),
                paciente.getId(),
                paciente.getCpf(),
                paciente.getEmail(),
                paciente.getTelefoneCelular(),
                paciente.getTelefoneComercial(),
                paciente.getTelefoneFixo(),
                paciente.getRua(),
                paciente.getBairro(),
                paciente.getComplemento(),
                paciente.getNumero()
            });
        }
    }

    private void preencerPaciente(Pacientes paciente) {
        try {

            paciente.setNome(txtNomeCliente.getText());

            //2025-12-03 Juliano Tratamento correto da data para evitar nullpointer
            String textoData = txtDataNascimento.getText().trim();
            if (!textoData.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                paciente.setDataNascimento(LocalDate.parse(textoData, formatter));
            } else {
                paciente.setDataNascimento(null);
            }

            Sexo sexoSelecionado = (Sexo) cbSexo.getSelectedItem();
            paciente.setSexo(sexoSelecionado);

            paciente.setCpf(txtcpf.getText());
            paciente.setTelefoneCelular(txtTelefoneCelular.getText());
            paciente.setTelefoneComercial(txtTelefoneComercial.getText());
            paciente.setTelefoneFixo(txtTelefoneFixo.getText());
            paciente.setEmail(txtEmail.getText());
            paciente.setCep(txtBuscaCep.getText());
            paciente.setBairro(txtBairro.getText());
            paciente.setRua(txtRua.getText());
            paciente.setNumero(txtNumero.getText());
            paciente.setComplemento(txtComplemento.getText());
            paciente.setCidade(txtCidade.getText());
            //cliente.setTipopessoa(TipoPessoa.PESSOA_FÍSICA);

            pacientejpa.CadastrarCliente(paciente);

            ImageIcon icon = new ImageIcon(getClass().getResource("/imagens/Alertas/iconecerto.png"));

            JOptionPane.showMessageDialog(
                    null,
                    "Cliente salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE,
                    icon
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Não foi possível salvar o cliente: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void limparTelaCadastroPaciente() {
        txtId.setText("");
        txtNome.setText("");
        txtcpf.setText("");
        txtEmail.setText("");
        txtNomeCliente.setText("");
        txtDataNascimento.setText("");
        txtTelefoneFixo.setText("");
        txtTelefoneCelular.setText("");
        txtTelefoneComercial.setText("");
        txtBuscaCep.setText("");
        txtRua.setText("");
        txtBairro.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
        txtCidade.setText("");
        cbEstadosSigla.setSelectedItem(EstadosBrasileiros.SP);

    }

    private Pacientes lerCamposPaciente() {

        Pacientes paciente = new Pacientes();
        paciente.setNome(txtNomeCliente.getText());

        String dataTexto = txtDataNascimento.getText().trim();
        if (!dataTexto.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            paciente.setDataNascimento(LocalDate.parse(dataTexto, formatter));
        } else {
            paciente.setDataNascimento(null);
        }
        Sexo sexoSelecionado = (Sexo) cbSexo.getSelectedItem();
        paciente.setSexo(sexoSelecionado);
        //2026-01-20 Juliano: definindo a chamada dos estados brasileiros
        EstadosBrasileiros estadoSelecionado = (EstadosBrasileiros) cbEstadosSigla.getSelectedItem();
        paciente.setSexo(sexoSelecionado);
        paciente.setEstado(estadoSelecionado);

        //2026-01-20 Juliano: após pegar o tipo de cliente é hora de definir o valor do atributo para objeto cliente
        paciente.setCpf(txtcpf.getText());
        paciente.setEmail(txtEmail.getText());
        paciente.setTelefoneCelular(txtTelefoneCelular.getText());
        paciente.setTelefoneComercial(txtTelefoneComercial.getText());
        paciente.setTelefoneFixo(txtTelefoneFixo.getText());
        paciente.setRua(txtRua.getText());
        paciente.setBairro(txtBairro.getText());
        paciente.setComplemento(txtComplemento.getText());
        paciente.setNumero(txtNumero.getText());
        paciente.setCidade(txtCidade.getText());
        paciente.setCep(txtBuscaCep.getText());

        String idText = txtId.getText().trim();
        if (!idText.isEmpty()) {
            paciente.setId(Long.parseLong(idText));
        }

        return paciente;
    }

    private void preencherCamposDoFormularioComDadosExistentes(Pacientes paciente) {
        //2025-12-06 Juliano caso o id exista ele vai definir os campos do formulário com os dados do respectivo ID,ou seja verificou que id existe vai pegar todos os atributos da intstância desse objeto e preencher campo a campo
        txtId.setText(String.valueOf(paciente.getId()));

        txtNomeCliente.setText(paciente.getNome());

        if (paciente.getDataNascimento() != null) {
            txtDataNascimento.setText(paciente.getDataNascimento()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            txtDataNascimento.setText("");
        }

        cbSexo.setSelectedItem(paciente.getSexo());
        txtcpf.setText(paciente.getCpf());
        txtEmail.setText(paciente.getEmail());
        txtTelefoneCelular.setText(paciente.getTelefoneCelular());
        txtTelefoneComercial.setText(paciente.getTelefoneComercial());
        txtTelefoneFixo.setText(paciente.getTelefoneFixo());
        txtRua.setText(paciente.getRua());
        txtBairro.setText(paciente.getBairro());
        txtComplemento.setText(paciente.getComplemento());
        txtNumero.setText(paciente.getNumero());
        txtBuscaCep.setText(paciente.getCep());
        txtCidade.setText(paciente.getCidade());
        cbEstadosSigla.setSelectedItem(paciente.getEstado());
    }


    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        String nome = txtNomeCliente.getText().trim();
        
        if (nome.isEmpty() || nome.length() < 4) {
            txtNomeCliente.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this, "Nome obrigatório (mínimo 4 caracteres)");
            txtNomeCliente.requestFocus();
            return;
        } else {
            txtNomeCliente.putClientProperty("JComponent.outline", null);
        }
         

        String cpf = txtcpf.getText();

        // Valida CPF (Se estiver preenchido)
        if (!cpf.replace(".", "").replace("-", "").trim().isEmpty()) {
            if (!ValidarCpfCnpj.isCPF(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF Inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                txtcpf.requestFocus();
                return; 
            }
        }

        //2026-01-10 Guilherme: Validação do campo e-mail
        if (!eMailValido(txtEmail.getText())) {
            if (txtEmail.getText().trim().isEmpty()) {
                //Não validar e-mail se o campo estiver vazio
            } else {
                JOptionPane.showMessageDialog(this, "E-mail inválido! Por favor, verifique.", "Erro", JOptionPane.ERROR_MESSAGE);
                txtEmail.requestFocus();
                return;
            }
        }

        System.out.println("Clicou no botão salvar.");
        
        Pacientes pacienteEditar = lerCamposPaciente(); // 2025-12-09 juliano pega todos os valores dos atributos do objeto

        if (pacienteEditar.getId() == null) {
            pacientejpa.CadastrarCliente(pacienteEditar);
            limparTelaCadastroPaciente();
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            System.out.println("Paciente cadastrado com sucesso!");
            System.out.println("Paciente cadastrado com id ->" + pacienteEditar.getId());

        } else {
            pacientejpa.atualizarCliente(pacienteEditar);
            limparTelaCadastroPaciente();
            JOptionPane.showMessageDialog(this, "Paciente Atualizado com sucesso!!");
            System.out.println("Paciente com id -> " + pacienteEditar.getId() + "atualizado.");
            abasCadastrarClientes.setSelectedIndex(1); //2025-12-09 juliano, caso  for uma edição e o id exista ele vai retornar para a página de listagem dos clientes

        }


    }//GEN-LAST:event_btnSalvarActionPerformed

    private void tblClientesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblClientesAncestorAdded
        //2025-11-22 Juliano definindo o nome das colunas da tabela
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "Nome",
                    "id",
                    "Nome Fantasia",
                    "CPF",
                    "CNPJ",
                    "E-mail",
                    "Celular",
                    "Telefone Comercial",
                    "Telefone Fixo",
                    "Rua",
                    "Bairro",
                    "Complemento",
                    "Número"
                },
                0
        );

        tblClientes.setModel(modelo);

        List<Pacientes> listarClientes = pacientejpa.listarClientes();

        modelo.setRowCount(0);

        for (Pacientes pacientes : listarClientes) {
            modelo.addRow(new Object[]{
                pacientes.getNome(),
                pacientes.getId(),
                pacientes.getCpf(),
                pacientes.getEmail(),
                pacientes.getTelefoneCelular(),
                pacientes.getTelefoneComercial(),
                pacientes.getTelefoneFixo(),
                pacientes.getRua(),
                pacientes.getBairro(),
                pacientes.getComplemento(),
                pacientes.getNumero()
            });
        }

    }//GEN-LAST:event_tblClientesAncestorAdded

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed


    }//GEN-LAST:event_txtIdActionPerformed

    private void btnBuscarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFiltroActionPerformed

        //2026-08-10 Juliano: Limpa os filtros da pesquisa anterior, criando um novo objeto para as novas pesquisas
        filtro = new ClienteFiltro();
        if (!txtId.getText().trim().isEmpty()) {
            filtro.setId(Long.parseLong(txtId.getText()));

        }
        String sexoStr = cbSexo.getSelectedItem().toString();

        if (!sexoStr.equals("Selecione")) {
            Sexo sexo = Sexo.valueOf(sexoStr);
            filtro.setSexo(sexo);
        }

        if (!txtNome.getText().trim().isEmpty()) {
            filtro.setNome(txtNome.getText());

        }

        listarPacientesFiltrados = pacientejpa.filtrarClientes(filtro);

        //2025-11-28 Juliano Atualiza a tabela
        DefaultTableModel model = (DefaultTableModel) tblClientes.getModel();
        model.setRowCount(0); // 2025-11-28 Juliano limpa tudo deixar a tabela vazia para chegar a tabela agora filtrada

        for (Pacientes pacientes : listarPacientesFiltrados) {
            model.addRow(new Object[]{
                pacientes.getNome(),
                pacientes.getId(),
                pacientes.getCpf(),
                pacientes.getEmail(),
                pacientes.getTelefoneCelular(),
                pacientes.getTelefoneComercial(),
                pacientes.getTelefoneFixo(),
                pacientes.getRua(),
                pacientes.getBairro(),
                pacientes.getComplemento(),
                pacientes.getNumero()

            });

        }
    }//GEN-LAST:event_btnBuscarFiltroActionPerformed

    private void cbSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSexoActionPerformed
      
    }//GEN-LAST:event_cbSexoActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        //2025-11-28 Juliano limpar clientes da tabela  e recarregar a tabela novamente
        txtId.setText("");
        txtNome.setText("");
        cbSexo.setSelectedIndex(0);
        //2026-08-10 Juliano: Garantindo que o objeto filtro será NULL, possibilitando assim um relatorio geral de clientes
        filtro = new ClienteFiltro();

        DefaultTableModel model = (DefaultTableModel) tblClientes.getModel();
        List<Pacientes> listarPacientesAtivos = pacientejpa.listarClientes();
        model.setRowCount(0);
        for (Pacientes paciente : listarPacientesAtivos) {
            model.addRow(new Object[]{
                paciente.getNome(),
                paciente.getId(),
                paciente.getCpf(),
                paciente.getEmail(),
                paciente.getTelefoneCelular(),
                paciente.getTelefoneComercial(),
                paciente.getTelefoneFixo(),
                paciente.getRua(),
                paciente.getBairro(),
                paciente.getComplemento(),
                paciente.getNumero()
            });
        }


    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
      
    }//GEN-LAST:event_txtNomeActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int linhasSelecionadas = tblClientes.getSelectedRow();
        if (linhasSelecionadas == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma linha para excluir/ Editar.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;

        }
        Long idCliente = (Long) tblClientes.getValueAt(linhasSelecionadas, 1);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Você realmente deseja excluir este cliente?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao == YES_NO_OPTION) {
            PacienteJPA clientejpa = new PacienteJPA();
            ClienteFiltro filtro = new ClienteFiltro();
            filtro.setId(idCliente);
            List<Pacientes> listClientes = clientejpa.filtrarClientes(filtro);

        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int linha = tblClientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma linha para excluir/ Editar.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long idCliente = (Long) tblClientes.getValueAt(linha, 1);

        PacienteJPA clientejpa = new PacienteJPA();
        ClienteFiltro filtro = new ClienteFiltro();
        filtro.setId(idCliente);

        List<Pacientes> listarClientes = clientejpa.filtrarClientes(filtro);

        if (!listarClientes.isEmpty()) {
            Pacientes cliente = listarClientes.get(0);

            preencherCamposDoFormularioComDadosExistentes(cliente);

            abasCadastrarClientes.setSelectedIndex(0);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar cliente selecionado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnEditarActionPerformed


    private void txtCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCidadeActionPerformed
        
    }//GEN-LAST:event_txtCidadeActionPerformed

    private void txtTelefoneComercialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefoneComercialActionPerformed
        
    }//GEN-LAST:event_txtTelefoneComercialActionPerformed

    private void btnSalvarAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_btnSalvarAncestorResized
        container1.getBaselineResizeBehavior();
    }//GEN-LAST:event_btnSalvarAncestorResized

    private void txtTelefoneFixoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefoneFixoActionPerformed
       
    }//GEN-LAST:event_txtTelefoneFixoActionPerformed

    private void txtDataNascimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataNascimentoActionPerformed
       
    }//GEN-LAST:event_txtDataNascimentoActionPerformed

    private void txtcpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcpfActionPerformed
       
    }//GEN-LAST:event_txtcpfActionPerformed

    private void cbEstadosSiglaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadosSiglaActionPerformed

    }//GEN-LAST:event_cbEstadosSiglaActionPerformed

    private void txtBuscaCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaCepActionPerformed
       
    }//GEN-LAST:event_txtBuscaCepActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtNomeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeClienteActionPerformed
       
    }//GEN-LAST:event_txtNomeClienteActionPerformed

    public class MascaraData extends DocumentFilter {

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            // Se o texto for nulo (ex: deleção), executa a deleção e sai
            if (text == null) {
                super.remove(fb, offset, length);
                return;
            }

            // Obtém o texto atual e remove tudo que não for número
            Document doc = fb.getDocument();
            String textoAntigo = doc.getText(0, doc.getLength());
            String apenasNumerosAntigos = textoAntigo.replaceAll("[^0-9]", "");
            String apenasNumerosNovos = text.replaceAll("[^0-9]", "");

            // Calcula como ficará a string de números final
            StringBuilder sb = new StringBuilder(apenasNumerosAntigos);

            // Ajusta o offset para considerar apenas os números (ignorando as barras)
            int realOffset = textoAntigo.substring(0, offset).replaceAll("[^0-9]", "").length();
            int realLength = textoAntigo.substring(offset, offset + length).replaceAll("[^0-9]", "").length();

            sb.replace(realOffset, realOffset + realLength, apenasNumerosNovos);

            String resultado = sb.toString();

            // Limita a 8 dígitos (DDMMAAAA)
            if (resultado.length() <= 8) {
                fb.replace(0, doc.getLength(), formatarData(resultado), attrs);
            } else {
                // Aqui é onde o barulho acontecia. 
                // Simplesmente não fazemos nada, e o sistema fica quieto.
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            // Permite apagar e reformatar o campo automaticamente
            replace(fb, offset, length, "", null);
        }

        private String formatarData(String numeros) {
            StringBuilder formatado = new StringBuilder();
            for (int i = 0; i < numeros.length(); i++) {
                if (i == 2 || i == 4) {
                    formatado.append("/");
                }
                formatado.append(numeros.charAt(i));
            }
            return formatado.toString();
        }
    }

    public class MascaraTelefone extends DocumentFilter {

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                text = "";
            }

            // Remove tudo que não for número do que está sendo colado/digitado
            String apenasNumerosNovos = text.replaceAll("[^0-9]", "");

            Document doc = fb.getDocument();
            String textoAtual = doc.getText(0, doc.getLength());
            String numerosAtuais = textoAtual.replaceAll("[^0-9]", "");

            // Calcula o resultado final de números
            StringBuilder sb = new StringBuilder(numerosAtuais);
            int realOffset = textoAtual.substring(0, offset).replaceAll("[^0-9]", "").length();
            int realLength = textoAtual.substring(offset, offset + length).replaceAll("[^0-9]", "").length();

            sb.replace(realOffset, realOffset + realLength, apenasNumerosNovos);

            String resultado = sb.toString();

            // Limite máximo de 11 dígitos (Celular com DDD)
            if (resultado.length() <= 11) {
                String formatado = formatarTelefone(resultado);
                ((AbstractDocument) doc).setDocumentFilter(null); // Pausa filtro
                fb.replace(0, doc.getLength(), formatado, attrs);
                ((AbstractDocument) doc).setDocumentFilter(this); // Retoma filtro
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            replace(fb, offset, length, "", null);
        }

        private String formatarTelefone(String num) {
            StringBuilder f = new StringBuilder();
            int qtd = num.length();

            if (qtd > 0) {
                f.append("(");
            }
            for (int i = 0; i < qtd; i++) {
                if (i == 2) {
                    f.append(") ");
                }
                if (i == 6 && qtd <= 10) {
                    f.append("-"); // Fixo: (XX) XXXX-XXXX
                }
                if (i == 7 && qtd > 10) {
                    f.append("-");  // Celular: (XX) XXXXX-XXXX
                }
                f.append(num.charAt(i));
            }
            return f.toString();
        }
    }

    public class FiltroEmail extends DocumentFilter {

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            // Remove espaços
            String filtrado = text.replaceAll("\\s", "");
            super.replace(fb, offset, length, filtrado, attrs);
        }

    }

    public boolean eMailValido(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

    public class MascaraCPF extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            substituir(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                text = "";
            }
            substituir(fb, offset, length, text, attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder novoTexto = new StringBuilder(textoAtual);
            novoTexto.delete(offset, offset + length);

            // Se após apagar não sobrar nenhum número, limpa o campo totalmente
            if (novoTexto.toString().replaceAll("\\D", "").isEmpty()) {
                fb.replace(0, fb.getDocument().getLength(), "", null);
            } else {
                // Caso contrário, reformata o que sobrou
                substituir(fb, 0, fb.getDocument().getLength(), novoTexto.toString(), null);
            }
        }

        private void substituir(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String textoExistente = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder builder = new StringBuilder(textoExistente);
            builder.replace(offset, offset + length, text);

            // Remove tudo que não for número
            String apenasNumeros = builder.toString().replaceAll("\\D", "");

            // Limita ao tamanho do CPF (11 dígitos)
            if (apenasNumeros.length() > 11) {
                apenasNumeros = apenasNumeros.substring(0, 11);
            }

            // Aplica a máscara 000.000.000-00 dinamicamente
            StringBuilder formatado = new StringBuilder();
            for (int i = 0; i < apenasNumeros.length(); i++) {
                if (i == 3 || i == 6) {
                    formatado.append(".");
                }
                if (i == 9) {
                    formatado.append("-");
                }
                formatado.append(apenasNumeros.charAt(i));
            }

            fb.replace(0, fb.getDocument().getLength(), formatado.toString(), attrs);
        }
    }

    public class MascaraCNPJ extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            substituir(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                text = "";
            }
            substituir(fb, offset, length, text, attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            // PERMITE APAGAR: Se o usuário deletar, limpamos e deixamos o replace lidar com a nova máscara
            String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder novoTexto = new StringBuilder(textoAtual);
            novoTexto.delete(offset, offset + length);

            // Se após apagar não sobrar nada, limpa o campo totalmente
            if (novoTexto.toString().replaceAll("\\D", "").isEmpty()) {
                fb.replace(0, fb.getDocument().getLength(), "", null);
            } else {
                substituir(fb, 0, fb.getDocument().getLength(), novoTexto.toString(), null);
            }
        }

        private void substituir(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            // 1. Pega o texto que resultaria da operação, apenas números
            String textoExistente = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder builder = new StringBuilder(textoExistente);
            builder.replace(offset, offset + length, text);

            String apenasNumeros = builder.toString().replaceAll("\\D", "");

            // 2. Limita a 14 dígitos (CNPJ)
            if (apenasNumeros.length() > 14) {
                apenasNumeros = apenasNumeros.substring(0, 14);
            }

            // 3. Aplica a máscara dinamicamente
            StringBuilder formatado = new StringBuilder();
            for (int i = 0; i < apenasNumeros.length(); i++) {
                if (i == 2 || i == 5) {
                    formatado.append(".");
                }
                if (i == 8) {
                    formatado.append("/");
                }
                if (i == 12) {
                    formatado.append("-");
                }
                formatado.append(apenasNumeros.charAt(i));
            }

            // 4. Substitui o documento todo pelo texto formatado
            fb.replace(0, fb.getDocument().getLength(), formatado.toString(), attrs);
        }
    }

    public class MascaraCEP extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);
            if (isValid(sb.toString())) {
                super.insertString(fb, offset, formatar(sb.toString()), attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                text = "";
            }

            // Pega o texto atual e remove a formatação para processar
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String before = currentText.substring(0, offset);
            String after = currentText.substring(offset + length);
            String newText = (before + text + after).replaceAll("[^\\d]", "");

            if (newText.length() <= 8) {
                StringBuilder formatted = new StringBuilder(newText);
                if (formatted.length() > 5) {
                    formatted.insert(5, "-");
                }
                fb.replace(0, fb.getDocument().getLength(), formatted.toString(), attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String res = new StringBuilder(currentText).delete(offset, offset + length).toString().replaceAll("[^\\d]", "");

            StringBuilder formatted = new StringBuilder(res);
            if (formatted.length() > 5) {
                formatted.insert(5, "-");
            }
            fb.replace(0, fb.getDocument().getLength(), formatted.toString(), null);
        }

        private boolean isValid(String text) {
            return text.replaceAll("[^\\d]", "").length() <= 8;
        }

        private String formatar(String text) {
            String digits = text.replaceAll("[^\\d]", "");
            if (digits.length() > 5) {
                return digits.substring(0, 5) + "-" + digits.substring(5);
            }
            return digits;
        }
    }

    private boolean validarCampoObrigatorio(javax.swing.JTextField campo) {
        // Remove caracteres da máscara para validar se há conteúdo real
        String textoLimpo = campo.getText().replaceAll("[^0-9a-zA-Z]", "").trim();

        if (textoLimpo.isEmpty()) {
            // Define a borda de erro e a cor de fundo sem perder o arredondamento
            campo.putClientProperty("JComponent.outline", "error");
            campo.setBackground(new Color(255, 235, 235)); // Vermelho bem suave
            return false;
        } else {
            // Restaura o estado original
            campo.putClientProperty("JComponent.outline", null);
            campo.setBackground(Color.WHITE);
            return true;
        }
    }

    private void buscarCep() {
        String cep = txtBuscaCep.getText().replace("-", "").trim();

        // 1. Limpa os campos antes de qualquer coisa para não sobrar lixo ('ue', etc)
        limparCamposEndereco();

        if (cep.length() != 8) {
            JOptionPane.showMessageDialog(this, "CEP deve conter 8 dígitos.");
            return;
        }

        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(3000);

            if (conexao.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
                StringBuilder json = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    json.append(linha);
                }

                String resposta = json.toString();

                // 2. VERIFICAÇÃO DE ERRO: Se a resposta contiver "erro", para aqui!
                if (resposta.contains("\"erro\"")) {
                    JOptionPane.showMessageDialog(this, "CEP não encontrado!");
                    return;
                }

                // 3. PREENCHIMENTO SEGURO: Só entra aqui se o CEP for válido
                String rua = extrairValor(resposta, "logradouro");
                String bairro = extrairValor(resposta, "bairro");
                String cidade = extrairValor(resposta, "localidade");
                String uf = extrairValor(resposta, "uf");

                txtRua.setText(rua);
                txtBairro.setText(bairro);
                txtCidade.setText(cidade);

                txtNumero.requestFocus();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar: " + e.getMessage());
        }
    }

// Melhore o extrairValor para retornar vazio se não encontrar a chave
    private String extrairValor(String json, String campo) {
        String chave = "\"" + campo + "\": \"";
        if (!json.contains(chave)) {
            return ""; // Se não tem a chave, retorna vazio e evita o "ue"
        }
        int inicio = json.indexOf(chave) + chave.length();
        int fim = json.indexOf("\"", inicio);
        return json.substring(inicio, fim);
    }

    private void limparCamposEndereco() {
        txtRua.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        // Se o seu combo tiver o primeiro item como "Selecione" ou vazio:

    }

    public String buscarEnderecoPorCep(String cep) throws Exception {
        // 1. Define a URL do ViaCEP
        String urlParaChamada = "https://viacep.com.br/ws/" + cep + "/json/";

        try {
            URL url = new URL(urlParaChamada);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            // 2. Lê a resposta
            BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
            StringBuilder jsonRetorno = new StringBuilder();
            String linha;

            while ((linha = resposta.readLine()) != null) {
                jsonRetorno.append(linha);
            }

            // --- ISSO FARÁ APARECER NO TERMINAL ---
            System.out.println("\n[DEBUG API] Resposta recebida:");
            System.out.println(jsonRetorno.toString());
            System.out.println("--------------------------------\n");

            return jsonRetorno.toString();

        } catch (Exception e) {
            throw new Exception("Erro ao conectar com a API: " + e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane abasCadastrarClientes;
    private javax.swing.JButton btnBuscarFiltro;
    private javax.swing.JButton btnCep;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroupCliente;
    private javax.swing.ButtonGroup buttonGroupPessoa;
    private javax.swing.JComboBox<EstadosBrasileiros> cbEstadosSigla;
    private javax.swing.JComboBox<Sexo> cbSexo;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container1;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container2;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtBuscaCep;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JFormattedTextField txtDataNascimento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtRua;
    private javax.swing.JTextField txtTelefoneCelular;
    private javax.swing.JTextField txtTelefoneComercial;
    private javax.swing.JTextField txtTelefoneFixo;
    private javax.swing.JTextField txtcpf;
    // End of variables declaration//GEN-END:variables
}
