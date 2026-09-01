/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.sistemaintegramind.View;

import com.mycompany.sistemaintegramind.View.Dashboard.DashboardView;
import com.mycompany.sistemaintegramind.View.Login.UsuariosView;
import com.mycompany.sistemaintegramind.View.Financeiro.FinanceiroView;
import com.mycompany.sistemaintegramind.View.Login.LoginView;
import com.mycompany.sistemaintegramind.View.Pacientes.PacienteView;
import com.mycompany.sistemaintegramind.Model.entidades.Usuarios;
import com.mycompany.sistemaintegramind.View.Agenda.AgendaView;
import com.mycompany.sistemaintegramind.View.Painel.PainelView;
import com.mycompany.sistemaintegramind.View.Recursos.RecursoView;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.util.Locale;
import javax.swing.JFrame;

/**
 *
 * @author Juliano
 */
public class MainMenu extends javax.swing.JFrame {

    private JButton botaoSelecionado;
    private FinanceiroView financeiro;
    private CardLayout cardlayout; //2025-11-08 Juliano: Criação do atributo global da classe para poder ser chamado nos métodos de eventos do java
    private Usuarios usuarioLogado;

    public MainMenu() {
        this(null);
    }

    public MainMenu(Usuarios usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        initComponents();
        //2026-01-13 Guilherme: Define o padrão brasileiro para toda a aplicação
        Locale.setDefault(new Locale("pt", "BR"));

        //2025-12-22 Guilherme: Define o tamanho padrão da janela principal do sistema
        this.setSize(1280, 720);

        //2025-12-22 Guilherme: Centraliza a janela principal na tela
        this.setLocationRelativeTo(null);

        //2025-12-29 Guilherme: Abre a tela cheia na inicialização do sistema
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        //2025-12-22 Guilherme: Define o título exibido na barra superior da aplicação
        this.setTitle("IntegraMind");

        //2025-11-08 Juliano: Define um tamahho fixo para o Menu, para o mesmo não quebrar os botões das páginas
        menu1.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        menu1.setMinimumSize(new Dimension(200, 0));
        menu1.setPreferredSize(new Dimension(210, getHeight()));

        //2025-11-08 Juliano: Cria um layout para poder adicionar várias paginas juntas como se fosse um baralho
        cardlayout = new CardLayout();
        MenuPrincipal.setLayout(cardlayout);
        MenuPrincipal.add(new PainelView(), "Painel");
        MenuPrincipal.add(new PacienteView(), "Pacientes");
        MenuPrincipal.add(new DashboardView(), "Dashboard");
        MenuPrincipal.add(new AgendaView(), "Agenda");
        financeiro = new FinanceiroView();
        MenuPrincipal.add(new FinanceiroView(), "Financeiro");
        MenuPrincipal.add(new RecursoView(), "Recursos");

        //2025-11-08 Juliano: Mostra a tela inicial de vendas antes de Clicar
        cardlayout.show(MenuPrincipal, "Painel");

        //2025-12-22 Guilherme: Faz os botões do menu lateral ficarem transparentes
        configurarHover(tblPainel);
        configurarHover(tblPacientes);
        configurarHover(tblDashboard);
        configurarHover(tblFinanceiro);
        configurarHover(tblAgenda);
        configurarHover(tblRecursos);
        btnBackup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //2025-12-22 Guilherme: Define o botão de vendas como selecionado inicialmente
        selecionarBotao(tblPainel);
        if (usuarioLogado != null) {
            setUsuarioLogado(usuarioLogado);
        } else {
            this.setTitle("IntegraMind");
        }
        //
    }

    public void setUsuarioLogado(Usuarios usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        if (usuarioLogado != null) {
            this.setTitle("IntegraMind - Usuário: " + usuarioLogado.getUsuario());
        }
    }

    public void AtualizarMovimentacaoFinanceira() {
        System.out.println("ENTROU NO ATUALIZAR FINANCEIRO");

        if (financeiro != null) {
            System.out.println("Atualizar Movimentação!!! ");
        } else {
            System.out.println("FINANCEIRO ESTÁ NULL");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        txtNomeProduto = new javax.swing.JTextField();
        menu1 = new com.mycompany.sistemaintegramind.View.Componentes.Menu();
        jLabel4 = new javax.swing.JLabel();
        tblPainel = new javax.swing.JButton();
        tblFinanceiro = new javax.swing.JButton();
        tblAgenda = new javax.swing.JButton();
        tblRecursos = new javax.swing.JButton();
        tblDashboard = new javax.swing.JButton();
        tblPacientes = new javax.swing.JButton();
        btnBackup = new javax.swing.JButton();
        MenuPrincipal = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        txtNomeProduto.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtNomeProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeProdutoActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        menu1.setMinimumSize(new java.awt.Dimension(250, 0));
        menu1.setName(""); // NOI18N
        menu1.setPreferredSize(new java.awt.Dimension(250, 917));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Logo60x60.png"))); // NOI18N
        jLabel4.setText("IntegraMind");

        tblPainel.setBackground(new java.awt.Color(58, 58, 191));
        tblPainel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tblPainel.setForeground(new java.awt.Color(255, 255, 255));
        tblPainel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuIcones/icons8-layout-do-painel-48.png"))); // NOI18N
        tblPainel.setText("PAINEL");
        tblPainel.setToolTipText("");
        tblPainel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblPainel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tblPainel.setIconTextGap(10);
        tblPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblPainelActionPerformed(evt);
            }
        });

        tblFinanceiro.setBackground(new java.awt.Color(58, 58, 191));
        tblFinanceiro.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tblFinanceiro.setForeground(new java.awt.Color(255, 255, 255));
        tblFinanceiro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuIcones/finance_icon_48x48.png"))); // NOI18N
        tblFinanceiro.setText("FINANCEIRO");
        tblFinanceiro.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblFinanceiro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tblFinanceiro.setIconTextGap(10);
        tblFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblFinanceiroActionPerformed(evt);
            }
        });

        tblAgenda.setBackground(new java.awt.Color(58, 58, 191));
        tblAgenda.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tblAgenda.setForeground(new java.awt.Color(255, 255, 255));
        tblAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuIcones/agendar48x48.png"))); // NOI18N
        tblAgenda.setText("AGENDA");
        tblAgenda.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblAgenda.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tblAgenda.setIconTextGap(10);
        tblAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblAgendaActionPerformed(evt);
            }
        });

        tblRecursos.setBackground(new java.awt.Color(58, 58, 191));
        tblRecursos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tblRecursos.setForeground(new java.awt.Color(255, 255, 255));
        tblRecursos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuIcones/folder48x48.png"))); // NOI18N
        tblRecursos.setText("RECURSOS");
        tblRecursos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblRecursos.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tblRecursos.setIconTextGap(10);
        tblRecursos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblRecursosActionPerformed(evt);
            }
        });

        tblDashboard.setBackground(new java.awt.Color(58, 58, 191));
        tblDashboard.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tblDashboard.setForeground(new java.awt.Color(255, 255, 255));
        tblDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuIcones/Dashboard_Icon_48x48.png"))); // NOI18N
        tblDashboard.setText("DASHBOARD");
        tblDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblDashboard.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tblDashboard.setIconTextGap(10);
        tblDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblDashboardActionPerformed(evt);
            }
        });

        tblPacientes.setBackground(new java.awt.Color(58, 58, 191));
        tblPacientes.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tblPacientes.setForeground(new java.awt.Color(255, 255, 255));
        tblPacientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuIcones/paciente48x48.png"))); // NOI18N
        tblPacientes.setText("PACIENTES");
        tblPacientes.setToolTipText("");
        tblPacientes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblPacientes.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tblPacientes.setIconTextGap(10);
        tblPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblPacientesActionPerformed(evt);
            }
        });

        btnBackup.setBackground(new java.awt.Color(242, 242, 242));
        btnBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Backup_1.png"))); // NOI18N
        btnBackup.setContentAreaFilled(false);
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menu1Layout = new javax.swing.GroupLayout(menu1);
        menu1.setLayout(menu1Layout);
        menu1Layout.setHorizontalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tblDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tblPainel, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(tblAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tblRecursos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tblFinanceiro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tblPacientes, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(menu1Layout.createSequentialGroup()
                .addComponent(btnBackup)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        menu1Layout.setVerticalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(tblPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tblPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(tblDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tblAgenda, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tblFinanceiro, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tblRecursos, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 374, Short.MAX_VALUE)
                .addComponent(btnBackup))
            .addGroup(menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4)
                    .addContainerGap(849, Short.MAX_VALUE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(menu1, gridBagConstraints);

        MenuPrincipal.setBackground(new java.awt.Color(221, 223, 237));
        MenuPrincipal.setPreferredSize(new java.awt.Dimension(300, 0));

        javax.swing.GroupLayout MenuPrincipalLayout = new javax.swing.GroupLayout(MenuPrincipal);
        MenuPrincipal.setLayout(MenuPrincipalLayout);
        MenuPrincipalLayout.setHorizontalGroup(
            MenuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );
        MenuPrincipalLayout.setVerticalGroup(
            MenuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 917, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(MenuPrincipal, gridBagConstraints);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //2025-12-22 Guilherme: Configura efeitos visuais de hover, clique e seleção para botões do menu
    private void configurarHover(JButton botao) {
        //2025-12-22 Guilherme: Ajusta o tamanho do ícone do botão
        ajustarIcone(botao, 30, 30);

        //2025-12-22 Guilherme: Define a cor de fundo quando o mouse passa sobre o botão
        Color corFundoHover = new Color(255, 255, 255, 45);

        //2025-12-22 Guilherme: Define a cor de fundo quando o botão é clicado
        Color corFundoClick = new Color(255, 255, 255, 80);

        //2025-12-22 Guilherme: Guarda a fonte normal do botão
        java.awt.Font fonteNormal = botao.getFont().deriveFont(java.awt.Font.PLAIN);

        //2025-12-22 Guilherme: Cria a versão em negrito da fonte do botão
        java.awt.Font fonteBold = botao.getFont().deriveFont(java.awt.Font.BOLD);

        //2025-12-22 Guilherme: Remove o preenchimento padrão do botão
        botao.setContentAreaFilled(false);

        //2025-12-22 Guilherme: Remove o destaque de foco do botão
        botao.setFocusPainted(false);

        //2025-12-22 Guilherme: Remove a borda padrão do botão
        botao.setBorderPainted(false);

        //2025-12-22 Guilherme: Define o botão como não opaco para permitir transparência
        botao.setOpaque(false);

        //2025-12-22 Guilherme: Altera o cursor para mão ao passar sobre o botão
        botao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //2025-12-22 Guilherme: Define margens internas do botão
        botao.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));

        //2025-12-22 Guilherme: Aplica uma UI personalizada para controlar o desenho do botão
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void update(java.awt.Graphics g, javax.swing.JComponent c) {
                //2025-12-22 Guilherme: Cria um contexto gráfico 2D para renderização avançada
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

                //2025-12-22 Guilherme: Ativa suavização de bordas (antialiasing)
                g2.setRenderingHint(
                        java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON
                );

                //2025-12-22 Guilherme: Verifica se o botão atual está selecionado
                boolean selecionado = (botao == botaoSelecionado);

                //2025-12-22 Guilherme: Aplica o fundo se estiver selecionado, em hover ou pressionado
                if (selecionado || botao.getModel().isRollover() || botao.getModel().isPressed()) {
                    if (botao.getModel().isPressed()) {
                        g2.setColor(corFundoClick);
                    } else {
                        g2.setColor(corFundoHover);
                    }

                    //2025-12-22 Guilherme: Desenha um retângulo arredondado como fundo do botão
                    g2.fillRoundRect(2, 2, c.getWidth() - 4, c.getHeight() - 4, 15, 15);
                }

                //2025-12-22 Guilherme: Altera a fonte para negrito se estiver selecionado
                botao.setFont(selecionado ? fonteBold : fonteNormal);

                //2025-12-22 Guilherme: Libera os recursos gráficos criados
                g2.dispose();

                //2025-12-22 Guilherme: Chama a pintura padrão do componente
                super.paint(g, c);
            }
        });
    }

    //2025-12-22 Guilherme: Define o botão selecionado no menu lateral
    private void selecionarBotao(JButton botao) {
        //2025-12-22 Guilherme: Guarda referência do botão anteriormente selecionado
        JButton antigo = botaoSelecionado;

        //2025-12-22 Guilherme: Atualiza o botão atualmente selecionado
        botaoSelecionado = botao;

        //2025-12-22 Guilherme: Repaint do botão antigo para remover o destaque
        if (antigo != null) {
            antigo.repaint();
        }

        //2025-12-22 Guilherme: Repaint do novo botão para aplicar o destaque visual
        if (botaoSelecionado != null) {
            botaoSelecionado.repaint();
        }
    }

    //2025-12-22 Guilherme: Redimensiona o ícone do botão mantendo a qualidade da imagem
    private void ajustarIcone(javax.swing.JButton botao, int largura, int altura) {
        //2025-12-22 Guilherme: Verifica se o botão possui um ícone definido
        if (botao.getIcon() != null) {
            //2025-12-22 Guilherme: Extrai a imagem do ImageIcon atual
            java.awt.Image img = ((javax.swing.ImageIcon) botao.getIcon()).getImage();

            //2025-12-22 Guilherme: Redimensiona a imagem com suavização
            java.awt.Image novaImg = img.getScaledInstance(
                    largura, altura, java.awt.Image.SCALE_SMOOTH
            );

            //2025-12-22 Guilherme: Define o novo ícone redimensionado no botão
            botao.setIcon(new javax.swing.ImageIcon(novaImg));
        }
    }


    private void txtNomeProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeProdutoActionPerformed

    private void tblRecursosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblRecursosActionPerformed
        // TODO add your handling code here:
        cardlayout.show(MenuPrincipal, "Recursos");

        selecionarBotao(tblRecursos); //2025-12-22 Guilherme: Define o botão clientes como selecionado, o mudando visualmente
    }//GEN-LAST:event_tblRecursosActionPerformed

    private void tblAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblAgendaActionPerformed
        // TODO add your handling code here:
        cardlayout.show(MenuPrincipal, "Agenda");

        selecionarBotao(tblAgenda); //2025-12-22 Guilherme: Define o botão clientes como selecionado, o mudando visualmente
    }//GEN-LAST:event_tblAgendaActionPerformed

    private void tblDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblDashboardActionPerformed
        cardlayout.show(MenuPrincipal, "Dashboard");
        selecionarBotao(tblDashboard); //2025-12-22 Guilherme: Define o botão clientes como selecionado, o mudando visualmente
    }//GEN-LAST:event_tblDashboardActionPerformed

    private void tblFinanceiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblFinanceiroActionPerformed
        cardlayout.show(MenuPrincipal, "Financeiro");
        selecionarBotao(tblFinanceiro); //2025-12-22 Guilherme: Define o botão financeiro como selecionado, o mudando visualmente
    }//GEN-LAST:event_tblFinanceiroActionPerformed

    private void tblPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblPainelActionPerformed
        cardlayout.show(MenuPrincipal, "Painel");
        selecionarBotao(tblPainel); //2025-12-22 Guilherme: Define o botão clientes como selecionado, o mudando visualmente
    }//GEN-LAST:event_tblPainelActionPerformed

    private void tblPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblPacientesActionPerformed
        cardlayout.show(MenuPrincipal, "Pacientes");
        selecionarBotao(tblPacientes); //2025-12-22 Guilherme: Define o botão clientes como selecionado, o mudando visualmente
    }//GEN-LAST:event_tblPacientesActionPerformed

    private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupActionPerformed
       
    }//GEN-LAST:event_btnBackupActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {

            UIManager.put("Component.minimumHeight", 35);
            // --- CUSTOMIZAÇÃO GLOBAL ---

            // Configurações Globais para TODAS as JTables do sistema
            UIManager.put("Table.rowHeight", 35);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.selectionBackground", new Color(100, 149, 237));

            // Configurações do Cabeçalho
            UIManager.put("TableHeader.background", new Color(58, 110, 242));
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("TableHeader.font", new Font("Segoe UI Semibold", Font.BOLD, 14));
            UIManager.put("TableHeader.height", 40);

            // Define a fonte padrão para campos de texto, campos de senha, etc. 
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 14));

            //2025-12-22 Guilherme: Define a cor do underline globalmente
            UIManager.put("TabbedPane.underlineColor", new Color(58, 58, 191));

            //UIManager.put("TabbedPane.inactiveUnderlineColor", new Color(200, 200, 200, 0)); // Linha transparente se não estiver selecionada
            UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 18));

            // 2025-12-22 Guilherme: texto da aba selecionada tenha uma fonte diferente (ex: maior)
            UIManager.put("TabbedPane.selectedFont", new Font("Segoe UI", Font.BOLD, 18));
            UIManager.put("TabbedPane.selectedForeground", new Color(58, 58, 191));

            //2025-12-22 Guilherme: Arredondamento Geral (Botões, Inputs, Tabelas)
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);

            //2025-12-22 Guilherme: JTABLE (Tabelas)
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false); // Visual moderno sem linhas verticais
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("Table.rowHeight", 35); // Linhas mais altas (melhor leitura)
            UIManager.put("Table.selectionBackground", new Color(58, 58, 191, 40)); // Azul suave
            UIManager.put("Table.selectionForeground", Color.BLACK);
            UIManager.put("TableHeader.background", new Color(245, 245, 250));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
            UIManager.put("TableHeader.separatorColor", new Color(220, 220, 220));

            //2025-12-22 Guilherme: JTEXTFIELD (Campos de Texto)
            UIManager.put("TextComponent.placeholderForeground", new Color(180, 180, 180));
            UIManager.put("TextField.padding", new Insets(5, 10, 5, 10)); // Espaçamento interno (Padding)
            UIManager.put("TextField.focusedBackground", Color.WHITE);

            //2025-12-22 Guilherme: SCROLLBAR (Barras de Rolagem modernas)
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.width", 12);

            //2025-12-22 Guilherme: Ativa o tema claro (ou FlatDarkLaf para tema escuro)
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o Look and Feel");
        }

        java.awt.EventQueue.invokeLater(() -> {
            //new MainMenu().setVisible(true);
        });
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                //new MainMenu().setVisible(true);
            }
        });

        javax.swing.SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("IntegraERP");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new LoginView());

            frame.setSize(850, 600);
            frame.setLocationRelativeTo(null);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);

        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuPrincipal;
    private javax.swing.JButton btnBackup;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollBar jScrollBar1;
    private com.mycompany.sistemaintegramind.View.Componentes.Menu menu1;
    private javax.swing.JButton tblAgenda;
    private javax.swing.JButton tblDashboard;
    private javax.swing.JButton tblFinanceiro;
    private javax.swing.JButton tblPacientes;
    private javax.swing.JButton tblPainel;
    private javax.swing.JButton tblRecursos;
    private javax.swing.JTextField txtNomeProduto;
    // End of variables declaration//GEN-END:variables
}
