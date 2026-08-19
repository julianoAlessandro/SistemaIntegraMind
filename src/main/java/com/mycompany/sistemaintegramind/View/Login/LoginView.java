/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Login;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import com.mycompany.sistemaintegramind.Model.dao.UsuarioDAO;
import com.mycompany.sistemaintegramind.Model.dao.impl.UsuarioJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Usuarios;
import com.mycompany.sistemaintegramind.View.MainMenu;

/**
 * @author Juliano
 */
public class LoginView extends javax.swing.JPanel {

    private MainMenu mainMenuPreCarregado;
    private boolean senhaVisivel = false;

    public LoginView() {
        initComponents();

        // Inicia o Pré-carregamento assíncrono da MainMenu em segundo plano
        iniciarPreCarregamentoMainMenu();

        // Propriedades visuais do FlatLaf para placeholders
        txtUsuario.putClientProperty("JTextField.placeholderText", "Usuário ou Email");
        txtSenha.putClientProperty("JTextField.placeholderText", "Senha");

        btnEntrarSistema.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Carregamento de ícones
        try {
            java.net.URL urlUser = getClass().getResource("/imagens/user.png");
            if (urlUser != null) {
                Image imgUser = new ImageIcon(urlUser).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                txtUsuario.putClientProperty("JTextField.leadingIcon", new ImageIcon(imgUser));
            }

            java.net.URL urlLock = getClass().getResource("/imagens/lock.png");
            if (urlLock != null) {
                Image imgLock = new ImageIcon(urlLock).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                txtSenha.putClientProperty("JTextField.leadingIcon", new ImageIcon(imgLock));
            }
        } catch (Exception e) {
            System.err.println("Aviso: Falha ao carregar ícones decorativos iniciais: " + e.getMessage());
        }

        atualizarIconeSenha();

        txtSenha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getX() >= txtSenha.getWidth() - 30) {
                    toggleSenha();
                }
            }
        });

        txtSenha.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (e.getX() >= txtSenha.getWidth() - 35) {
                    txtSenha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    txtSenha.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }

    /**
     * Executa a instanciação da MainMenu em uma Thread separada sem travar a
     * interface de Login.
     */
    private void iniciarPreCarregamentoMainMenu() {
        Thread threadPreCarregamento = new Thread(() -> {
            try {
                // Instancia todas as sub-views da MainMenu em background
                MainMenu tempMenu = new MainMenu();

                // Garantimos que a referência final seja atribuída com segurança
                SwingUtilities.invokeLater(() -> {
                    this.mainMenuPreCarregado = tempMenu;
                });
            } catch (Exception e) {
                System.err.println("Erro durante o pré-carregamento da MainMenu: " + e.getMessage());
            }
        });
        threadPreCarregamento.setPriority(Thread.MIN_PRIORITY); // Baixa prioridade para priorizar a UI de Login
        threadPreCarregamento.start();
    }

    private void toggleSenha() {
        senhaVisivel = !senhaVisivel;
        if (senhaVisivel) {
            txtSenha.setEchoChar((char) 0);
        } else {
            txtSenha.setEchoChar('\u2022');
        }
        atualizarIconeSenha();
    }

    private void atualizarIconeSenha() {
        try {
            String caminhoIcone = senhaVisivel ? "/imagens/eye.png" : "/imagens/hide.png";
            java.net.URL urlEye = getClass().getResource(caminhoIcone);
            if (urlEye != null) {
                Image imgEye = new ImageIcon(urlEye).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                txtSenha.putClientProperty("JTextField.trailingIcon", new ImageIcon(imgEye));
            }
            txtSenha.repaint();
        } catch (Exception e) {
            System.err.println("Erro ao atualizar alternância visual de ícone: " + e.getMessage());
        }
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha usuário e senha",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioJPA();
            Usuarios usuarioLogado = usuarioDAO.login(usuario, senha);

            if (usuarioLogado != null) {
                MainMenu principal;

                // Verifica se o pré-carregamento foi concluído a tempo
                if (mainMenuPreCarregado != null) {
                    principal = mainMenuPreCarregado;
                    principal.setUsuarioLogado(usuarioLogado);
                } else {
                    // Fallback: se o usuário logar muito rápido antes da thread terminar
                    principal = new MainMenu(usuarioLogado);
                }

                // Exibe a MainMenu no Event Dispatch Thread (EDT)
                MainMenu menuExibir = principal;
                SwingUtilities.invokeLater(() -> {
                    menuExibir.setVisible(true);

                    // Fecha a janela de Login
                    Window janela = SwingUtilities.getWindowAncestor(this);
                    if (janela != null) {
                        janela.dispose();
                    }
                });

            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuário ou senha inválidos",
                        "Erro de login",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar login no banco de dados.",
                    "Erro Interno",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        container1 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        btnEntrarSistema = new javax.swing.JButton();
        txtSenha = new javax.swing.JPasswordField();

        setBackground(new java.awt.Color(236, 236, 241));
        setPreferredSize(new java.awt.Dimension(850, 600));
        setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 204, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoLoginV2_60x60.png"))); // NOI18N
        jLabel4.setText("IntegraMind");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        add(jLabel4, gridBagConstraints);

        jPanel2.setPreferredSize(new java.awt.Dimension(850, 600));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(236, 236, 241));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        container1.setBackground(new java.awt.Color(255, 255, 255));
        container1.setPreferredSize(new java.awt.Dimension(450, 530));
        container1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setText("Usuário ou Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 3, 0);
        container1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Senha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 30, 3, 0);
        container1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        jLabel3.setText("Acessar Sistema");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 30, 6);
        container1.add(jLabel3, gridBagConstraints);

        txtUsuario.setPreferredSize(new java.awt.Dimension(64, 30));
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 30);
        container1.add(txtUsuario, gridBagConstraints);

        btnEntrarSistema.setBackground(new java.awt.Color(63, 112, 235));
        btnEntrarSistema.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnEntrarSistema.setForeground(new java.awt.Color(255, 255, 255));
        btnEntrarSistema.setText("Entrar");
        btnEntrarSistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarSistemaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(40, 30, 40, 30);
        container1.add(btnEntrarSistema, gridBagConstraints);

        txtSenha.setPreferredSize(new java.awt.Dimension(64, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 30);
        container1.add(txtSenha, gridBagConstraints);

        jPanel3.add(container1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void btnEntrarSistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarSistemaActionPerformed
        realizarLogin();
    }//GEN-LAST:event_btnEntrarSistemaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrarSistema;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
