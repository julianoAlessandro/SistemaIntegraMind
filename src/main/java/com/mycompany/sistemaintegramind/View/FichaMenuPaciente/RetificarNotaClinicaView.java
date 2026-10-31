/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.impl.InativarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.RetificarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.RetificarNotaClinica;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalQueries.localDate;
import javax.swing.JOptionPane;

/**
 *
 * @author Micro
 */
public class RetificarNotaClinicaView extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RetificarNotaClinicaView.class.getName());
    private EvolucaoClinica notaclinica;
    String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    String horarioFormatado = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    private boolean retificarNota = false;
    private RetificarNotaClinica retificarnota;

    public RetificarNotaClinicaView(java.awt.Window janelaPai, EvolucaoClinica notaclinica) {
        super(janelaPai, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.notaclinica = notaclinica;
        initComponents();
        //2026-10-29 Juliano: Campos somente para visualização
        txtIdNotaRetificada.setEditable(false);
        txtIdNotaRetificada.setBackground(java.awt.Color.WHITE);
        txtIdNotaRetificada.setForeground(java.awt.Color.BLACK);
        txtDataHoraAlteracaoRetificada.setEditable(false);
        txtDataHoraAlteracaoRetificada.setBackground(java.awt.Color.WHITE);
        txtDataHoraAlteracaoRetificada.setForeground(java.awt.Color.BLACK);
        //2026-10-29 Juliano: Campos somente para visualização
        txtIdNotaRetificada.setText(notaclinica.getId() + "#");
        txtDataHoraAlteracaoRetificada.setText(dataFormatada + " - " + horarioFormatado);

        //2026-10-30 Juliano: garantindo que o usuario não ira clicar no X, por descuido perdendo o que tem feito no texto da justifiticativa
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {

                JOptionPane.showMessageDialog(
                        RetificarNotaClinicaView.this,
                        "Para fechar esta tela, confirme a retificação da nota ou clique em cancelar.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(janelaPai);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMotivoRetificação = new javax.swing.JTextArea();
        btnRetificarNota = new javax.swing.JButton();
        btnCancelarInativação = new javax.swing.JButton();
        txtIdNotaRetificada = new javax.swing.JTextField();
        txtDataHoraAlteracaoRetificada = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Retificar Nota Clínica");

        jLabel2.setText("Nota retificada : ");

        jLabel3.setText("Motivo da Retificação da nota(Obrigatório):");

        jLabel4.setText("nota retificada em:");

        txtMotivoRetificação.setColumns(20);
        txtMotivoRetificação.setRows(5);
        jScrollPane1.setViewportView(txtMotivoRetificação);

        btnRetificarNota.setText("Confirmar Retificação");
        btnRetificarNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetificarNotaActionPerformed(evt);
            }
        });

        btnCancelarInativação.setText("Cancelar");
        btnCancelarInativação.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarInativaçãoActionPerformed(evt);
            }
        });

        txtIdNotaRetificada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdNotaRetificadaActionPerformed(evt);
            }
        });

        txtDataHoraAlteracaoRetificada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataHoraAlteracaoRetificadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(btnCancelarInativação, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(btnRetificarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDataHoraAlteracaoRetificada))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtIdNotaRetificada, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdNotaRetificada, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDataHoraAlteracaoRetificada, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarInativação, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRetificarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRetificarNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetificarNotaActionPerformed

        if (txtMotivoRetificação.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "É necessário informar a justificativa da retificação da nota para continuar.",
                    "Atenção",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        System.out.println("Solicitação de retificação da nota clínica confirmada.");
        //2026-10-28 Juliano: Guardando na memória, nao no banco o objeto retificação,para poder recuperar seus valores depois e salvar no banco assim como pegar o id da nova evolucao
        retificarnota = new RetificarNotaClinica();
        retificarnota.setDataRetificacao(LocalDate.now());
        retificarnota.setHorarioRetificacao(LocalTime.now());
        retificarnota.setMotivoRetificacao(txtMotivoRetificação.getText().trim());
        retificarnota.setNotaOriginal(notaclinica);
        retificarNota = true;

        JOptionPane.showMessageDialog(
                this,
                "A solicitação de retificação da nota clínica foi confirmada.\n\n"
                + "Você será direcionado à tela de elaboração da nova nota clínica, "
                + "na qual deverá realizar as alterações necessárias.\n\n"
                + "A retificação será registrada após a conclusão e o salvamento "
                + "da nova nota clínica.",
                "Retificação de Nota Clínica",
                JOptionPane.INFORMATION_MESSAGE
        );
        retificarNota = true;
        dispose();

        //  FichaProntuarioPacienteView fichaprontuario = new FichaProntuarioPacienteView();

    }//GEN-LAST:event_btnRetificarNotaActionPerformed

    private void txtIdNotaRetificadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdNotaRetificadaActionPerformed

    }//GEN-LAST:event_txtIdNotaRetificadaActionPerformed

    private void txtDataHoraAlteracaoRetificadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataHoraAlteracaoRetificadaActionPerformed


    }//GEN-LAST:event_txtDataHoraAlteracaoRetificadaActionPerformed

    private void btnCancelarInativaçãoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarInativaçãoActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarInativaçãoActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    }

    public boolean isRetificarNota() {
        return retificarNota;
    }

    public RetificarNotaClinica getRetificarnota() {
        return retificarnota;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelarInativação;
    private javax.swing.JButton btnRetificarNota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtDataHoraAlteracaoRetificada;
    private javax.swing.JTextField txtIdNotaRetificada;
    private javax.swing.JTextArea txtMotivoRetificação;
    // End of variables declaration//GEN-END:variables
}
