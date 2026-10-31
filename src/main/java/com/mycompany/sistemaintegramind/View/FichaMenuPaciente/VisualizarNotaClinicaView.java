package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisualizarNotaClinicaView extends javax.swing.JDialog {

    private EvolucaoClinica notaclinica;

    public VisualizarNotaClinicaView(java.awt.Window janelaPai, EvolucaoClinica notaclinica) {
        super(janelaPai, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.notaclinica = notaclinica;
        initComponents();
        //2026-10-29 Juliano: Campos somente para visualização
        txtIDNota.setEditable(false);
        txtIDNota.setBackground(java.awt.Color.WHITE);
        txtIDNota.setForeground(java.awt.Color.BLACK);
        txtData.setEditable(false);
        txtData.setBackground(java.awt.Color.WHITE);
        txtData.setForeground(java.awt.Color.BLACK);
        txtHorarioCriacao.setEditable(false);
        txtHorarioCriacao.setBackground(java.awt.Color.WHITE);
        txtHorarioCriacao.setForeground(java.awt.Color.BLACK);
        txtTipoNotaClinica.setEditable(false);
        txtTipoNotaClinica.setBackground(java.awt.Color.WHITE);
        txtTipoNotaClinica.setForeground(java.awt.Color.BLACK);
        txtEvolucaoClinica.setEditable(false);
        txtEvolucaoClinica.setBackground(java.awt.Color.WHITE);
        txtEvolucaoClinica.setForeground(java.awt.Color.BLACK);
        txtObservação.setEditable(false);
        txtObservação.setBackground(java.awt.Color.WHITE);
        txtObservação.setForeground(java.awt.Color.BLACK);
        //2026-10-29 Juliano: Campos somente para visualização

        txtIDNota.setText(notaclinica.getId() + "#");
        String dataFormatada = notaclinica.getDataEvolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtData.setText(dataFormatada);
        txtHorarioCriacao.setText(notaclinica.getHorario().toString());
        txtTipoNotaClinica.setText(notaclinica.getTiponotaclinica().toString());
        txtEvolucaoClinica.setText(notaclinica.getEvolucaoSessao());
        txtObservação.setText(notaclinica.getObservacoes());
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtIDNota = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        txtHorarioCriacao = new javax.swing.JTextField();
        txtTipoNotaClinica = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservação = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtEvolucaoClinica = new javax.swing.JTextArea();

        jLabel1.setText("Data da criação da nota:");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setText("Visualizar Nota");

        jLabel3.setText("Tipo da nota:");

        jLabel4.setText("ID NOTA:");

        jLabel5.setText("Horário de criação da nota:");

        jLabel6.setText("Relatório evolução clinica:");

        jLabel7.setText("Observação:");

        txtObservação.setColumns(20);
        txtObservação.setRows(5);
        jScrollPane1.setViewportView(txtObservação);

        txtEvolucaoClinica.setColumns(20);
        txtEvolucaoClinica.setRows(5);
        jScrollPane2.setViewportView(txtEvolucaoClinica);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtHorarioCriacao, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIDNota, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6))
                        .addContainerGap(11, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTipoNotaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel2)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtIDNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtHorarioCriacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTipoNotaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextArea txtEvolucaoClinica;
    private javax.swing.JTextField txtHorarioCriacao;
    private javax.swing.JTextField txtIDNota;
    private javax.swing.JTextArea txtObservação;
    private javax.swing.JTextField txtTipoNotaClinica;
    // End of variables declaration//GEN-END:variables

}
