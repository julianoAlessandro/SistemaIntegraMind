package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.AlternarEntreJanelasMenu;
import com.mycompany.sistemaintegramind.util.Utilitarios.MenuButtonUtils;
import java.awt.CardLayout;
import javax.swing.JButton;

public class FichaMenuPrincipalPacienteView extends javax.swing.JPanel {

    private Paciente paciente;
    private CardLayout cardlayout;
    private JButton botaoSelecionado;
    private Paciente pacienteSelecionado;

    public FichaMenuPrincipalPacienteView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
        paciente.getId();

        //2025-11-08 Juliano: Cria um layout para poder adicionar várias paginas juntas como se fosse um baralho
        cardlayout = new CardLayout();
        MenuPrincipal.setLayout(cardlayout);
        MenuPrincipal.add(new FichaPerfilPacienteView(paciente), "PerfilPaciente");
        MenuPrincipal.add(new FichaAnaminaseView(), "Anaminase");
        MenuPrincipal.add(new FichaProntuarioPacienteView(), "Prontuario");
        MenuPrincipal.add(new FichaAgendamentoView(), "Agendamento");
        MenuPrincipal.add(new FichaDocumentosView(), "Documentos");

        //2025-11-08 Juliano: Mostra a tela inicial de vendas antes de Clicar
        cardlayout.show(MenuPrincipal, "Painel");
        atualizarSelecao(tblPerfilPaciente);

    }

    private void atualizarSelecao(javax.swing.JButton botaoClicado) {
        javax.swing.JButton[] botoes = {tblPerfilPaciente, tblAnaminase, tblProntuario, tblAgendamento, tblDocumentos};

        String[] iconesBrancos = {
            "/imagens/MenuPacienteIcons/paciente_white.png",
            "/imagens/MenuPacienteIcons/stethoscope_white.png",
            "/imagens/MenuPacienteIcons/medical-report_white.png",
            "/imagens/MenuPacienteIcons/calendar_white.png",
            "/imagens/MenuPacienteIcons/docs_white.png",};

        String[] iconesVerdes = {
            "/imagens/MenuPacienteIcons/paciente_green.png",
            "/imagens/MenuPacienteIcons/stethoscope_green.png",
            "/imagens/MenuPacienteIcons/medical-report_green.png",
            "/imagens/MenuPacienteIcons/calendar_green.png",
            "/imagens/MenuPacienteIcons/docs_green.png",};

        MenuButtonUtils.selecionarMenu(botaoClicado, botoes, iconesBrancos, iconesVerdes);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        container1 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        jPanel1 = new javax.swing.JPanel();
        tblPerfilPaciente = new javax.swing.JButton();
        tblAnaminase = new javax.swing.JButton();
        tblProntuario = new javax.swing.JButton();
        tblAgendamento = new javax.swing.JButton();
        tblDocumentos = new javax.swing.JButton();
        MenuPrincipal = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        tblPerfilPaciente.setText("PERFIL PACIENTE");
        tblPerfilPaciente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblPerfilPaciente.setIconTextGap(15);
        tblPerfilPaciente.setMaximumSize(null);
        tblPerfilPaciente.setMinimumSize(new java.awt.Dimension(122, 35));
        tblPerfilPaciente.addActionListener(this::tblPerfilPacienteActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(tblPerfilPaciente, gridBagConstraints);

        tblAnaminase.setText("ANAMINASE");
        tblAnaminase.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblAnaminase.setIconTextGap(15);
        tblAnaminase.setMaximumSize(null);
        tblAnaminase.setMinimumSize(new java.awt.Dimension(122, 35));
        tblAnaminase.addActionListener(this::tblAnaminaseActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(tblAnaminase, gridBagConstraints);

        tblProntuario.setText("PRONTUÁRIO");
        tblProntuario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblProntuario.setIconTextGap(15);
        tblProntuario.setMaximumSize(null);
        tblProntuario.setMinimumSize(new java.awt.Dimension(122, 35));
        tblProntuario.addActionListener(this::tblProntuarioActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(tblProntuario, gridBagConstraints);

        tblAgendamento.setText("AGENDAMENTO");
        tblAgendamento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblAgendamento.setIconTextGap(15);
        tblAgendamento.setMaximumSize(null);
        tblAgendamento.setMinimumSize(new java.awt.Dimension(122, 35));
        tblAgendamento.addActionListener(this::tblAgendamentoActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(tblAgendamento, gridBagConstraints);

        tblDocumentos.setText("DOCUMENTOS");
        tblDocumentos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tblDocumentos.setIconTextGap(15);
        tblDocumentos.setMaximumSize(null);
        tblDocumentos.setMinimumSize(new java.awt.Dimension(122, 35));
        tblDocumentos.addActionListener(this::tblDocumentosActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(tblDocumentos, gridBagConstraints);

        javax.swing.GroupLayout container1Layout = new javax.swing.GroupLayout(container1);
        container1.setLayout(container1Layout);
        container1Layout.setHorizontalGroup(
            container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        container1Layout.setVerticalGroup(
            container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(745, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(container1, gridBagConstraints);

        MenuPrincipal.setForeground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout MenuPrincipalLayout = new javax.swing.GroupLayout(MenuPrincipal);
        MenuPrincipal.setLayout(MenuPrincipalLayout);
        MenuPrincipalLayout.setHorizontalGroup(
            MenuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        MenuPrincipalLayout.setVerticalGroup(
            MenuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(MenuPrincipal, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void tblAnaminaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblAnaminaseActionPerformed
        System.out.println("Clicou na aba Anaminase");
        cardlayout.show(MenuPrincipal, "Anaminase");
        atualizarSelecao(tblAnaminase);

    }//GEN-LAST:event_tblAnaminaseActionPerformed

    private void tblPerfilPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblPerfilPacienteActionPerformed
        System.out.println("Clicou na aba Perfil Paciente");
        cardlayout.show(MenuPrincipal, "PerfilPaciente");
        atualizarSelecao(tblPerfilPaciente);

    }//GEN-LAST:event_tblPerfilPacienteActionPerformed

    private void tblProntuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblProntuarioActionPerformed
        System.out.println("Clicou na aba Prontuario");
        cardlayout.show(MenuPrincipal, "Prontuario");
        atualizarSelecao(tblProntuario);

    }//GEN-LAST:event_tblProntuarioActionPerformed

    private void tblAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblAgendamentoActionPerformed
        System.out.println("Clicou na aba Agendamento");
        cardlayout.show(MenuPrincipal, "Agendamento");
        atualizarSelecao(tblAgendamento);

    }//GEN-LAST:event_tblAgendamentoActionPerformed

    private void tblDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblDocumentosActionPerformed
        System.out.println("Clicou na aba Documentos");
        cardlayout.show(MenuPrincipal, "Documentos");
        atualizarSelecao(tblDocumentos);

    }//GEN-LAST:event_tblDocumentosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuPrincipal;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton tblAgendamento;
    private javax.swing.JButton tblAnaminase;
    private javax.swing.JButton tblDocumentos;
    private javax.swing.JButton tblPerfilPaciente;
    private javax.swing.JButton tblProntuario;
    // End of variables declaration//GEN-END:variables
}
