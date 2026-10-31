/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.impl.InativarNotaClinicaJPA;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalQueries.localDate;
import javax.swing.JOptionPane;

/**
 *
 * @author Micro
 */
public class InativarNotaClinicaView extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InativarNotaClinicaView.class.getName());
    private EvolucaoClinica notaclinica;
    private boolean inativacaoConfirmada = false; //2026-10-30 Juliano: controlando o fechamento da tela impedindo de fechar a tela sem ter uma justificativa da inativacao

    public InativarNotaClinicaView(java.awt.Window janelaPai, EvolucaoClinica notaclinica) {
        super(janelaPai, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.notaclinica = notaclinica;
        initComponents();
        //2026-10-29 Juliano: Campos somente para visualização
        txtIdNotaClinica.setEditable(false);
        txtIdNotaClinica.setBackground(java.awt.Color.WHITE);
        txtIdNotaClinica.setForeground(java.awt.Color.BLACK);
        txtDataHoraAlteracao.setEditable(false);
        txtDataHoraAlteracao.setBackground(java.awt.Color.WHITE);
        txtDataHoraAlteracao.setForeground(java.awt.Color.BLACK);

        //2026-10-29 Juliano: Campos somente para visualização
        txtIdNotaClinica.setText("Identificação da nota " + notaclinica.getId() + "#");
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horarioFormatado = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtDataHoraAlteracao.setText(dataFormatada + " - " + horarioFormatado);

        //2026-10-30 Juliano: garantindo que o usuario não ira clicar no X, por descuido perdendo o que tem feito no texto da justifiticativa
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {

                JOptionPane.showMessageDialog(
                        InativarNotaClinicaView.this,
                        "Para fechar esta tela, confirme a inativação da nota clínica ou utilize o botão Cancelar.",
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
        txtMotivoInativacao = new javax.swing.JTextArea();
        btnInativarNota = new javax.swing.JButton();
        btnCancelarInativação = new javax.swing.JButton();
        txtIdNotaClinica = new javax.swing.JTextField();
        txtDataHoraAlteracao = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("                              Inativar Nota Clínica");

        jLabel2.setText("Nota Selecionada : ");

        jLabel3.setText("Motivo da inativação(Obrigatório):");

        jLabel4.setText("Evolução Clinica alterada em: ");

        txtMotivoInativacao.setColumns(20);
        txtMotivoInativacao.setRows(5);
        jScrollPane1.setViewportView(txtMotivoInativacao);

        btnInativarNota.setText("Confirmar inativação");
        btnInativarNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInativarNotaActionPerformed(evt);
            }
        });

        btnCancelarInativação.setText("Cancelar");
        btnCancelarInativação.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarInativaçãoActionPerformed(evt);
            }
        });

        txtIdNotaClinica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdNotaClinicaActionPerformed(evt);
            }
        });

        txtDataHoraAlteracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataHoraAlteracaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(btnCancelarInativação, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(btnInativarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDataHoraAlteracao, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtIdNotaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdNotaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDataHoraAlteracao, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarInativação, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInativarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInativarNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInativarNotaActionPerformed
        // 2026-10-27: Cadastrando a justificativa da inativação da nota clínica

// Valida a justificativa
        String motivo = txtMotivoInativacao.getText().trim();

        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "É necessário informar a justificativa da inativação da nota para continuar.",
                    "Atenção",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        try {

            // Cria o registro da inativação
            InativarNotaClinica notaclinicadesabilitada
                    = new InativarNotaClinica();

            InativarNotaClinicaJPA inativarnotaclinicajpa
                    = new InativarNotaClinicaJPA();

            // Define a data, o horário e o motivo
            notaclinicadesabilitada.setDataInativarNota(LocalDate.now());
            notaclinicadesabilitada.setHorario(LocalTime.now());
            notaclinicadesabilitada.setMotivoInativarNota(motivo);

            // Associa o registro à nota clínica selecionada
            notaclinicadesabilitada.setNotaclinica(notaclinica);

            // Salva a justificativa no banco de dados
            inativarnotaclinicajpa.cadastrarJustificativaInativarNota(
                    notaclinicadesabilitada
            );

            System.out.println(
                    "Justificativa da inativação da nota cadastrada!"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Justificativa da inativação da nota cadastrada com sucesso!",
                    "Sistema IntegraMIND",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // Indica que a inativação foi confirmada com sucesso
            inativacaoConfirmada = true;

            // Fecha a tela somente após salvar com sucesso
            dispose();

        } catch (RuntimeException ex) {

            ex.printStackTrace();

            Throwable causa = ex.getCause();

            String mensagem = causa != null && causa.getMessage() != null
                    ? causa.getMessage()
                    : ex.getMessage();

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível salvar a justificativa da inativação.\n"
                    + mensagem,
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }//GEN-LAST:event_btnInativarNotaActionPerformed

    private void txtIdNotaClinicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdNotaClinicaActionPerformed

    }//GEN-LAST:event_txtIdNotaClinicaActionPerformed

    private void txtDataHoraAlteracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataHoraAlteracaoActionPerformed


    }//GEN-LAST:event_txtDataHoraAlteracaoActionPerformed

    private void btnCancelarInativaçãoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarInativaçãoActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarInativaçãoActionPerformed
    public boolean isInativacaoConfirmada() {
        return inativacaoConfirmada;
    }

    /**
     * @param args the command line arguments
     */
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelarInativação;
    private javax.swing.JButton btnInativarNota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtDataHoraAlteracao;
    private javax.swing.JTextField txtIdNotaClinica;
    private javax.swing.JTextArea txtMotivoInativacao;
    // End of variables declaration//GEN-END:variables
}
