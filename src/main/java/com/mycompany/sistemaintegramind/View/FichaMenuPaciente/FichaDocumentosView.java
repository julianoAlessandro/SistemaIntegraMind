/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import javax.swing.JDialog;

/**
 *
 * @author guilh
 */
public class FichaDocumentosView extends javax.swing.JPanel {

    private Paciente paciente;
    public FichaDocumentosView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtPesquisarDocumentos = new javax.swing.JTextField();
        btnAdicionarDocumentos = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        scroDocumentos = new javax.swing.JScrollPane();
        pnlListarDocumentosPaciente = new javax.swing.JPanel();

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Documentos");

        btnAdicionarDocumentos.setText("Adicionar Documento");
        btnAdicionarDocumentos.addActionListener(this::btnAdicionarDocumentosActionPerformed);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setText("Documentos do Paciente");

        javax.swing.GroupLayout pnlListarDocumentosPacienteLayout = new javax.swing.GroupLayout(pnlListarDocumentosPaciente);
        pnlListarDocumentosPaciente.setLayout(pnlListarDocumentosPacienteLayout);
        pnlListarDocumentosPacienteLayout.setHorizontalGroup(
            pnlListarDocumentosPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 781, Short.MAX_VALUE)
        );
        pnlListarDocumentosPacienteLayout.setVerticalGroup(
            pnlListarDocumentosPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 317, Short.MAX_VALUE)
        );

        scroDocumentos.setViewportView(pnlListarDocumentosPaciente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(txtPesquisarDocumentos)
                .addGap(18, 18, 18)
                .addComponent(btnAdicionarDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(118, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(jLabel2)
                    .addContainerGap(605, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdicionarDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesquisarDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(scroDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(336, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jLabel2)
                    .addContainerGap(791, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarDocumentosActionPerformed
        AdicionarDocumentoPacienteView adicionardocumentopaciente = new AdicionarDocumentoPacienteView(paciente);

        JDialog dialog = new JDialog();
        dialog.setTitle("Adicionar Documentos do Paciente - " + paciente.getNome());
        dialog.setModal(true);

        dialog.add(adicionardocumentopaciente);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnAdicionarDocumentosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarDocumentos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel pnlListarDocumentosPaciente;
    private javax.swing.JScrollPane scroDocumentos;
    private javax.swing.JTextField txtPesquisarDocumentos;
    // End of variables declaration//GEN-END:variables
}
