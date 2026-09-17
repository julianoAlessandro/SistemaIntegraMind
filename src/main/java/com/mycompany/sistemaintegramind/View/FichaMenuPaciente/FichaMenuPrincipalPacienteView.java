package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.entidades.Pacientes;
import com.mycompany.sistemaintegramind.util.Utilitarios.AlternarEntreJanelasMenu;
import java.awt.CardLayout;
import javax.swing.JButton;

public class FichaMenuPrincipalPacienteView extends javax.swing.JPanel {

    private Pacientes paciente;
    private CardLayout cardlayout;
    private JButton botaoSelecionado;
    private Pacientes pacienteSelecionado;

    public FichaMenuPrincipalPacienteView(Pacientes paciente) {
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tblPerfilPaciente = new javax.swing.JButton();
        tblAnaminase = new javax.swing.JButton();
        tblProntuario = new javax.swing.JButton();
        tblAgendamento = new javax.swing.JButton();
        tblDocumentos = new javax.swing.JButton();
        MenuPrincipal = new javax.swing.JPanel();

        tblPerfilPaciente.setText("PERFIL PACIENTE");
        tblPerfilPaciente.addActionListener(this::tblPerfilPacienteActionPerformed);

        tblAnaminase.setText("ANAMINASE");
        tblAnaminase.addActionListener(this::tblAnaminaseActionPerformed);

        tblProntuario.setText("PRONTUÁRIO");
        tblProntuario.addActionListener(this::tblProntuarioActionPerformed);

        tblAgendamento.setText("AGENDAMENTO");
        tblAgendamento.addActionListener(this::tblAgendamentoActionPerformed);

        tblDocumentos.setText("DOCUMENTOS");
        tblDocumentos.addActionListener(this::tblDocumentosActionPerformed);

        MenuPrincipal.setForeground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout MenuPrincipalLayout = new javax.swing.GroupLayout(MenuPrincipal);
        MenuPrincipal.setLayout(MenuPrincipalLayout);
        MenuPrincipalLayout.setHorizontalGroup(
            MenuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );
        MenuPrincipalLayout.setVerticalGroup(
            MenuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tblPerfilPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tblAnaminase, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tblProntuario, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tblDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tblAgendamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 257, Short.MAX_VALUE)
                    .addComponent(MenuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(tblPerfilPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(tblAnaminase, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(tblProntuario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(tblAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(tblDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(MenuPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblAnaminaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblAnaminaseActionPerformed
        System.out.println("Clicou na aba Anaminase");
        cardlayout.show(MenuPrincipal, "Anaminase");
        AlternarEntreJanelasMenu.selecionarBotao(tblAnaminase, botaoSelecionado);
    }//GEN-LAST:event_tblAnaminaseActionPerformed

    private void tblPerfilPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblPerfilPacienteActionPerformed
        System.out.println("Clicou na aba Perfil Paciente");
        cardlayout.show(MenuPrincipal, "PerfilPaciente");
        AlternarEntreJanelasMenu.selecionarBotao(tblPerfilPaciente, botaoSelecionado);
    }//GEN-LAST:event_tblPerfilPacienteActionPerformed

    private void tblProntuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblProntuarioActionPerformed
        System.out.println("Clicou na aba Prontuario");
        cardlayout.show(MenuPrincipal, "Prontuario");
        AlternarEntreJanelasMenu.selecionarBotao(tblProntuario, botaoSelecionado);
    }//GEN-LAST:event_tblProntuarioActionPerformed

    private void tblAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblAgendamentoActionPerformed
        System.out.println("Clicou na aba Agendamento");
        cardlayout.show(MenuPrincipal, "Agendamento");
        AlternarEntreJanelasMenu.selecionarBotao(tblAgendamento, botaoSelecionado);
    }//GEN-LAST:event_tblAgendamentoActionPerformed

    private void tblDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblDocumentosActionPerformed
        System.out.println("Clicou na aba Documentos");
        cardlayout.show(MenuPrincipal, "Documentos");
        AlternarEntreJanelasMenu.selecionarBotao(tblDocumentos, botaoSelecionado);
    }//GEN-LAST:event_tblDocumentosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuPrincipal;
    private javax.swing.JButton tblAgendamento;
    private javax.swing.JButton tblAnaminase;
    private javax.swing.JButton tblDocumentos;
    private javax.swing.JButton tblPerfilPaciente;
    private javax.swing.JButton tblProntuario;
    // End of variables declaration//GEN-END:variables
}
