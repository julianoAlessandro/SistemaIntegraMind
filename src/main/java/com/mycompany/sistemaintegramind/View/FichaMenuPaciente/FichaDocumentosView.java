/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.impl.DocumentoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.EvolucaoClinicaJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Documento;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FichaDocumentosView extends javax.swing.JPanel {

    private Paciente paciente;
    private FichaDocumentosView fichadocumentosview;

    public FichaDocumentosView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();

        pnlListarDocumentosPaciente.setLayout(
                new javax.swing.BoxLayout(
                        pnlListarDocumentosPaciente,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );
        DocumentoJPA documentojpa = new DocumentoJPA();

        List<Documento> documentos = documentojpa.listarDocumentosPorPaciente(paciente);

        for (Documento documento : documentos) {

            pnlListarDocumentosPaciente.add(carregarDocumentosDoPaciente(documento));
        }

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

        pnlListarDocumentosPaciente.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                pnlListarDocumentosPacienteAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout pnlListarDocumentosPacienteLayout = new javax.swing.GroupLayout(pnlListarDocumentosPaciente);
        pnlListarDocumentosPaciente.setLayout(pnlListarDocumentosPacienteLayout);
        pnlListarDocumentosPacienteLayout.setHorizontalGroup(
            pnlListarDocumentosPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 781, Short.MAX_VALUE)
        );
        pnlListarDocumentosPacienteLayout.setVerticalGroup(
            pnlListarDocumentosPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
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
                .addComponent(scroDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jLabel2)
                    .addContainerGap(791, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarDocumentosActionPerformed
        AdicionarDocumentoPacienteView adicionardocumentopaciente = new AdicionarDocumentoPacienteView(paciente, this);

        JDialog dialog = new JDialog();
        dialog.setTitle("Adicionar Documentos do Paciente - " + paciente.getNome());
        dialog.setModal(true);

        dialog.add(adicionardocumentopaciente);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnAdicionarDocumentosActionPerformed

    private void pnlListarDocumentosPacienteAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_pnlListarDocumentosPacienteAncestorAdded

    }//GEN-LAST:event_pnlListarDocumentosPacienteAncestorAdded

    public JPanel carregarDocumentosDoPaciente(Documento documento) {

        JPanel card = new JPanel(new BorderLayout(15, 10));

        // Dimensões do cartão
        card.setPreferredSize(new Dimension(600, 150));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Painel para a imagem do documento
        JLabel lblImagemDocumento = new JLabel();
        lblImagemDocumento.setPreferredSize(new Dimension(110, 110));
        lblImagemDocumento.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagemDocumento.setVerticalAlignment(SwingConstants.CENTER);

        // Por enquanto, utiliza um ícone conforme a extensão
        String nomeArquivo = documento.getNomeArquivo().toLowerCase();

        String caminhoImagem;

        if (nomeArquivo.endsWith(".pdf")) {
            caminhoImagem = "/imagens/pdf.png";
        } else if (nomeArquivo.endsWith(".doc") || nomeArquivo.endsWith(".docx")) {
            caminhoImagem = "/imagens/word.JPEG";
        } else if (nomeArquivo.endsWith(".jpg") || nomeArquivo.endsWith(".jpeg") || nomeArquivo.endsWith(".png")) {
            caminhoImagem = "/imagens/png.png";
        } else if (nomeArquivo.endsWith(".txt")) {
            caminhoImagem = "/imagens/txt.JPEG";
        } else {
            caminhoImagem = "/imagens/arquivo.png";
        }

        java.net.URL urlImagem = getClass().getResource(caminhoImagem);

        if (urlImagem != null) {
            ImageIcon imagem = new ImageIcon(urlImagem);

            Image imagemRedimensionada = imagem.getImage().getScaledInstance(
                    90, 90, Image.SCALE_SMOOTH
            );

            lblImagemDocumento.setIcon(new ImageIcon(imagemRedimensionada));
        } else {
            lblImagemDocumento.setText("Documento");
        }

        card.add(lblImagemDocumento, BorderLayout.WEST);

        // Painel central com as informações
        JPanel painelInformacoes = new JPanel(new BorderLayout(5, 10));

        JLabel lblNomeArquivo = new JLabel(documento.getNomeArquivo());
        lblNomeArquivo.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblTipoDocumento = new JLabel(
                documento.getTipoDocumento().toString()
        );

        JLabel lblData = new JLabel(
                "Adicionado em: " + documento.getDataAtualizacao().format(DateTimeFormatter.ofPattern("dd/MM/YYYY", new Locale("pt", "BR"))));

        JPanel painelCabecalho = new JPanel(new BorderLayout(10, 0));
        painelCabecalho.add(lblNomeArquivo, BorderLayout.WEST);
        painelCabecalho.add(lblTipoDocumento, BorderLayout.EAST);

        painelInformacoes.add(painelCabecalho, BorderLayout.NORTH);
        painelInformacoes.add(lblData, BorderLayout.CENTER);

        // Botões de operações
        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        JButton btnVisualizar = new JButton("Visualizar");
        JButton btnBaixar = new JButton("Baixar");
        JButton btnExcluir = new JButton("Excluir");

        // Visualizar documento
        btnVisualizar.addActionListener(e -> {

            try {
                File arquivo = new File(documento.getCaminhoArquivo());

                if (!arquivo.exists()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "O arquivo não foi encontrado.",
                            "Arquivo não encontrado",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                Desktop.getDesktop().open(arquivo);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível abrir o documento.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // Operações futuras
        btnBaixar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelBotoes.add(btnVisualizar);
        painelBotoes.add(btnBaixar);
        painelBotoes.add(btnExcluir);

        painelInformacoes.add(painelBotoes, BorderLayout.SOUTH);

        card.add(painelInformacoes, BorderLayout.CENTER);

        return card;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarDocumentos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel pnlListarDocumentosPaciente;
    private javax.swing.JScrollPane scroDocumentos;
    private javax.swing.JTextField txtPesquisarDocumentos;
    // End of variables declaration//GEN-END:variables
}
