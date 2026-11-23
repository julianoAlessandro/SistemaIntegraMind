package com.mycompany.sistemaintegramind.util.Utilitarios.TelasUtil;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.RetificarNotaClinica;
import com.mycompany.sistemaintegramind.View.Componentes.BotaoEstilo;
import com.mycompany.sistemaintegramind.View.Componentes.ComponenteUtil;
import com.mycompany.sistemaintegramind.View.Componentes.Container;
import com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel;
import com.mycompany.sistemaintegramind.util.Utilitarios.ImagemUtil;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Utilitário responsável por isolar toda a estilização visual e construção de layout
 * dos cards e componentes da tela {@code FichaProntuarioPacienteView}.
 */
public final class FichaProntuarioDesignUtil {

    private FichaProntuarioDesignUtil() {
    }

    /**
     * Aplica o design visual aos componentes da tela FichaProntuarioPacienteView.
     */
    public static void aplicarDesignTela(
            Container containerNovaEvolucao,
            Container containerHistoricoNotas,
            Container containerDataAgendamento,
            JTextField txtDataAgendamento,
            CircularIconLabel iconeCalendarioData,
            Container containerHorario,
            JTextField txtHorario,
            CircularIconLabel iconeRelogioHorario,
            Container containerBarraPesquisa,
            JTextField txtPesquisarNotaClinicaPelaEvolucao,
            CircularIconLabel iconeHeaderHistorico,
            final JScrollPane scrListaEvolucoes,
            final JPanel pnlListaEvolucoes,
            JScrollPane scrollEvolucaoSessao,
            JTextArea txtEvolucaoSessao,
            JScrollPane scrollObservacao,
            JTextArea txtObservacao,
            JComboBox<TipoNotaClinica> cmbListarNotaClinica,
            final JButton btnBuscarNotaClinica,
            JButton btnLimparPesquisa,
            JButton btnGravarAudioEvolucaoClinicaPaciente,
            JButton btnLimparEvolucao,
            JButton btnSalvarEvolucao
    ) {
        containerNovaEvolucao.setCardStyle();
        containerNovaEvolucao.setBorderRadius(18);

        containerHistoricoNotas.setCardStyle();
        containerHistoricoNotas.setBorderRadius(18);

        containerDataAgendamento.removeShadow();
        containerDataAgendamento.setBackground(Color.WHITE);
        containerDataAgendamento.setBorderRadius(10);
        containerDataAgendamento.setBorder(new Color(203, 213, 225), 1);
        containerDataAgendamento.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        txtDataAgendamento.setOpaque(false);
        iconeCalendarioData.setBackground(new Color(0, 0, 0, 0));

        containerHorario.removeShadow();
        containerHorario.setBackground(Color.WHITE);
        containerHorario.setBorderRadius(10);
        containerHorario.setBorder(new Color(203, 213, 225), 1);
        containerHorario.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        txtHorario.setOpaque(false);
        iconeRelogioHorario.setBackground(new Color(0, 0, 0, 0));
        iconeRelogioHorario.setIconColor(null);
        iconeRelogioHorario.setIcon(criarIconeRelogioTransparente(new Color(71, 85, 105), 15, 15));

        containerBarraPesquisa.removeShadow();
        containerBarraPesquisa.setBackground(Color.WHITE);
        containerBarraPesquisa.setBorderRadius(10);
        containerBarraPesquisa.setBorder(new Color(203, 213, 225), 1);
        containerBarraPesquisa.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        txtPesquisarNotaClinicaPelaEvolucao.setOpaque(false);
        iconeHeaderHistorico.setBackground(new Color(0, 0, 0, 0));

        ComponenteUtil.formatarScrollInvisivel(scrListaEvolucoes);
        scrListaEvolucoes.getVerticalScrollBar().setUnitIncrement(16);
        scrListaEvolucoes.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                pnlListaEvolucoes.revalidate();
                pnlListaEvolucoes.repaint();
            }
        });

        scrollEvolucaoSessao.setBorder(BorderFactory.createLineBorder(new Color(214, 222, 230), 1, true));
        scrollObservacao.setBorder(BorderFactory.createLineBorder(new Color(214, 222, 230), 1, true));
        instalarPlaceholderTextArea(txtEvolucaoSessao, "Digite aqui a evolução da sessão...");
        instalarPlaceholderTextArea(txtObservacao, "Digite aqui observações adicionais...");

        cmbListarNotaClinica.setBackground(Color.WHITE);
        cmbListarNotaClinica.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoNotaClinica) {
                    lbl.setText(formatarTipoNotaMaiusculo((TipoNotaClinica) value));
                }
                lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
                return lbl;
            }
        });

        Icon icoLupa = carregarIconeRedimensionado("/imagens/lupa.png", new Color(100, 116, 139), 14, 14);
        if (icoLupa != null) {
            btnBuscarNotaClinica.setIcon(icoLupa);
        }
        btnBuscarNotaClinica.setBorderPainted(false);
        btnBuscarNotaClinica.setContentAreaFilled(false);
        btnBuscarNotaClinica.setFocusPainted(false);
        btnBuscarNotaClinica.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscarNotaClinica.setToolTipText("Pesquisar notas clínicas");

        Icon icoLimparPesq = carregarIconeRedimensionado("/imagens/limpar.png", new Color(148, 163, 184), 13, 13);
        if (icoLimparPesq != null) {
            btnLimparPesquisa.setIcon(icoLimparPesq);
        }
        btnLimparPesquisa.setBorderPainted(false);
        btnLimparPesquisa.setContentAreaFilled(false);
        btnLimparPesquisa.setFocusPainted(false);
        btnLimparPesquisa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimparPesquisa.setToolTipText("Limpar pesquisa");

        txtPesquisarNotaClinicaPelaEvolucao.addActionListener(e -> btnBuscarNotaClinica.doClick());

        Icon icoAudio = carregarIconeRedimensionado("/imagens/ferramenta-de-audio-com-alto-falante.png", new Color(13, 82, 65), 15, 15);
        if (icoAudio != null) {
            btnGravarAudioEvolucaoClinicaPaciente.setIcon(icoAudio);
        }
        btnGravarAudioEvolucaoClinicaPaciente.setIconTextGap(6);
        btnGravarAudioEvolucaoClinicaPaciente.setBackground(new Color(240, 250, 244));
        btnGravarAudioEvolucaoClinicaPaciente.setForeground(new Color(13, 82, 65));
        btnGravarAudioEvolucaoClinicaPaciente.setFocusPainted(false);
        btnGravarAudioEvolucaoClinicaPaciente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGravarAudioEvolucaoClinicaPaciente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(167, 215, 186), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));

        btnLimparEvolucao.setIcon(criarIconeLixeira(new Color(71, 85, 105), 14, 14));
        btnLimparEvolucao.setIconTextGap(6);
        btnLimparEvolucao.setBackground(Color.WHITE);
        btnLimparEvolucao.setForeground(new Color(71, 85, 105));
        btnLimparEvolucao.setFocusPainted(false);
        btnLimparEvolucao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimparEvolucao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 14, 6, 14)
        ));

        btnSalvarEvolucao.setIcon(criarIconeDisqueteSalvar(Color.WHITE, 14, 14));
        btnSalvarEvolucao.setIconTextGap(8);
        BotaoEstilo.aplicarEfeito(
                btnSalvarEvolucao,
                new Color(21, 128, 61),
                new Color(17, 105, 50),
                new Color(13, 82, 39)
        );
    }

    public static JButton criarBotaoReproduzirAudio() {
        Icon icoAudioBtn = carregarIconeRedimensionado("/imagens/ferramenta-de-audio-com-alto-falante.png", new Color(13, 82, 65), 12, 12);
        return criarBotaoAcaoCard(
                "Reproduzir áudio",
                icoAudioBtn,
                Color.WHITE,
                new Color(13, 82, 65),
                new Color(167, 215, 186),
                true
        );
    }

    public static JButton criarBotaoVisualizar() {
        Icon icoEye = carregarIconeRedimensionado("/imagens/ProntuarioPacienteIcons/eye.png", new Color(51, 65, 85), 13, 13);
        return criarBotaoAcaoCard(
                "Visualizar",
                icoEye,
                Color.WHITE,
                new Color(51, 65, 85),
                new Color(203, 213, 225),
                true
        );
    }

    public static JButton criarBotaoRetificar(boolean notaInativa) {
        Icon icoCopy = carregarIconeRedimensionado("/imagens/ProntuarioPacienteIcons/copy.png", Color.WHITE, 12, 12);
        JButton btn = criarBotaoAcaoCard(
                "Retificar",
                icoCopy,
                new Color(37, 99, 235),
                Color.WHITE,
                null,
                !notaInativa
        );
        btn.setDisabledIcon(icoCopy);
        return btn;
    }

    public static JButton criarBotaoInativar(boolean notaInativa) {
        Icon icoMinusWhite = carregarIconeRedimensionado("/imagens/ProntuarioPacienteIcons/minus_white.png", null, 12, 12);
        Icon icoMinusGray = carregarIconeRedimensionado("/imagens/ProntuarioPacienteIcons/minus_gray.png", null, 12, 12);
        JButton btn = criarBotaoAcaoCard(
                "Inativar",
                notaInativa ? icoMinusGray : icoMinusWhite,
                notaInativa ? new Color(241, 245, 249) : new Color(220, 38, 38),
                notaInativa ? new Color(148, 163, 184) : Color.WHITE,
                notaInativa ? new Color(203, 213, 225) : null,
                !notaInativa
        );
        btn.setDisabledIcon(icoMinusGray);
        return btn;
    }

    /**
     * Monta o card visual compacto de uma nota clínica no histórico.
     */
    public static JPanel montarCardNotaClinica(
            EvolucaoClinica evolucao,
            final JScrollPane scrListaEvolucoes,
            boolean notaInativa,
            boolean notaRetificada,
            InativarNotaClinica inativacao,
            RetificarNotaClinica retificacao,
            boolean possuiAudio,
            JButton btnReproduzirAudio,
            JButton btnVisualizar,
            JButton btnRetificar,
            JButton btnInativar
    ) {
        final Color corFundoCard;
        final Color corBordaCard;
        final Color corFaixaLateral;
        final Color corBadgeStatus;
        final String textoBadgeStatus;

        if (notaInativa) {
            corFundoCard = new Color(248, 249, 250);
            corBordaCard = new Color(226, 232, 240);
            corFaixaLateral = new Color(148, 163, 184);
            corBadgeStatus = new Color(220, 38, 38);
            textoBadgeStatus = "INATIVADA";
        } else if (notaRetificada) {
            corFundoCard = new Color(242, 247, 255);
            corBordaCard = new Color(191, 219, 254);
            corFaixaLateral = new Color(37, 99, 235);
            corBadgeStatus = new Color(37, 99, 235);
            textoBadgeStatus = "RETIFICADA";
        } else {
            corFundoCard = new Color(243, 250, 246);
            corBordaCard = new Color(198, 233, 212);
            corFaixaLateral = new Color(31, 157, 85);
            corBadgeStatus = new Color(34, 153, 84);
            textoBadgeStatus = "ATIVA";
        }

        JPanel painelContainer = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int vw = (scrListaEvolucoes != null && scrListaEvolucoes.getViewport() != null)
                        ? scrListaEvolucoes.getViewport().getWidth()
                        : 0;
                if (vw > 120) {
                    return new Dimension(vw, d.height);
                }
                return d;
            }

            @Override
            public Dimension getMaximumSize() {
                Dimension d = getPreferredSize();
                int vw = (scrListaEvolucoes != null && scrListaEvolucoes.getViewport() != null)
                        ? scrListaEvolucoes.getViewport().getWidth()
                        : 0;
                return new Dimension(vw > 120 ? vw : Integer.MAX_VALUE, d.height);
            }
        };
        painelContainer.setOpaque(false);
        painelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelContainer.setBorder(new EmptyBorder(0, 0, 12, 4));

        Container card = new Container() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(corFaixaLateral);
                g2.fillRect(0, 0, 5, getHeight());
                g2.dispose();
            }
        };
        card.removeShadow();
        card.setBackground(corFundoCard);
        card.setBorderRadius(12);
        card.setBorder(corBordaCard, 1);
        card.setLayout(new BorderLayout(8, 12));
        card.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 12));

        String dataFmt = evolucao.getDataEvolucao() != null
                ? evolucao.getDataEvolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        String horaFmt = evolucao.getHorario() != null
                ? evolucao.getHorario().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "";
        JLabel lblData = new JLabel((dataFmt + " " + horaFmt).trim());
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblData.setForeground(new Color(30, 41, 59));

        String textoIdNota = evolucao.getId() != null ? ("ID: #" + evolucao.getId() + "  |  ") : "";
        JLabel lblTipo = new JLabel(textoIdNota + formatarTipoNotaSubtitulo(evolucao.getTiponotaclinica()));
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTipo.setForeground(new Color(100, 116, 139));

        JPanel painelTopoEsquerda = new JPanel();
        painelTopoEsquerda.setLayout(new BoxLayout(painelTopoEsquerda, BoxLayout.X_AXIS));
        painelTopoEsquerda.setOpaque(false);
        painelTopoEsquerda.add(lblData);
        painelTopoEsquerda.add(Box.createHorizontalStrut(8));
        painelTopoEsquerda.add(lblTipo);

        JLabel lblStatusNotaClinica = criarBadgeStatusNota(textoBadgeStatus, corBadgeStatus);

        // Linha 1 (Cabeçalho): Data + ID + Tipo à esquerda, e somente a tag de status (ATIVA/INATIVADA/RETIFICADA) à direita
        JPanel painelCabecalho = new JPanel(new BorderLayout(8, 0));
        painelCabecalho.setOpaque(false);
        painelCabecalho.setBorder(new EmptyBorder(0, 0, 6, 0));
        painelCabecalho.add(painelTopoEsquerda, BorderLayout.WEST);
        painelCabecalho.add(lblStatusNotaClinica, BorderLayout.EAST);

        // Linha 2: Texto da evolução à esquerda e botões de ação alinhados à direita na mesma linha
        JTextArea txtEvolucao = new JTextArea(evolucao.getEvolucaoSessao());
        txtEvolucao.setRows(1);
        txtEvolucao.setLineWrap(true);
        txtEvolucao.setWrapStyleWord(true);
        txtEvolucao.setEditable(false);
        txtEvolucao.setOpaque(false);
        txtEvolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEvolucao.setForeground(notaInativa ? new Color(100, 116, 139) : new Color(51, 65, 85));
        txtEvolucao.setBorder(new EmptyBorder(4, 0, 2, 0));

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.X_AXIS));
        painelBotoes.setOpaque(false);
        if (possuiAudio && btnReproduzirAudio != null) {
            painelBotoes.add(btnReproduzirAudio);
            painelBotoes.add(Box.createHorizontalStrut(4));
        }
        painelBotoes.add(btnVisualizar);
        painelBotoes.add(Box.createHorizontalStrut(4));
        painelBotoes.add(btnRetificar);
        painelBotoes.add(Box.createHorizontalStrut(4));
        painelBotoes.add(btnInativar);

        JPanel painelBotoesWrapper = new JPanel(new BorderLayout());
        painelBotoesWrapper.setOpaque(false);
        painelBotoesWrapper.add(painelBotoes, BorderLayout.SOUTH);

        JPanel painelCorpoEBotoes = new JPanel(new BorderLayout(8, 0));
        painelCorpoEBotoes.setOpaque(false);
        painelCorpoEBotoes.add(txtEvolucao, BorderLayout.CENTER);
        painelCorpoEBotoes.add(painelBotoesWrapper, BorderLayout.EAST);

        JPanel painelJustificativa = new JPanel(new BorderLayout(6, 0));
        painelJustificativa.setOpaque(false);
        painelJustificativa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(6, 0, 2, 0)
        ));

        JPanel painelJustificativaRetificacao = new JPanel(new BorderLayout(6, 0));
        painelJustificativaRetificacao.setOpaque(false);
        painelJustificativaRetificacao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(6, 0, 2, 0)
        ));

        if (notaInativa) {
            Map<TextAttribute, Object> atributos = new HashMap<>();
            atributos.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            lblData.setFont(lblData.getFont().deriveFont(atributos));
            lblTipo.setFont(lblTipo.getFont().deriveFont(atributos));
            txtEvolucao.setFont(txtEvolucao.getFont().deriveFont(atributos));

            if (inativacao != null) {
                CircularIconLabel iconeAlertaInativacao = new CircularIconLabel();
                iconeAlertaInativacao.setShadowBlur(0);
                iconeAlertaInativacao.setBackground(new Color(0, 0, 0, 0));
                iconeAlertaInativacao.setIcon(new ImageIcon(FichaProntuarioDesignUtil.class.getResource("/imagens/ProntuarioPacienteIcons/warning-sign.png")));
                iconeAlertaInativacao.setIconWidth(13);
                iconeAlertaInativacao.setIconHeight(13);
                iconeAlertaInativacao.setPreferredSize(new Dimension(15, 15));

                final String motivoCompletoInat = inativacao.getMotivoInativarNota() != null
                        ? inativacao.getMotivoInativarNota()
                        : "";
                final String dtInat = inativacao.getDataInativarNota() != null
                        ? inativacao.getDataInativarNota().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "";
                final String hrInat = inativacao.getHorario() != null
                        ? inativacao.getHorario().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "";

                JPanel painelPrefixoInat = new JPanel();
                painelPrefixoInat.setLayout(new BoxLayout(painelPrefixoInat, BoxLayout.X_AXIS));
                painelPrefixoInat.setOpaque(false);
                painelPrefixoInat.add(iconeAlertaInativacao);
                painelPrefixoInat.add(Box.createHorizontalStrut(4));
                JLabel lblPrefixoInat = new JLabel("Motivo da inativação: ");
                lblPrefixoInat.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lblPrefixoInat.setForeground(new Color(71, 85, 105));
                painelPrefixoInat.add(lblPrefixoInat);

                JLabel lblJustificativaInativacao = new JLabel(motivoCompletoInat.replaceAll("\\s+", " ").trim());
                lblJustificativaInativacao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblJustificativaInativacao.setForeground(new Color(71, 85, 105));
                lblJustificativaInativacao.setMinimumSize(new Dimension(20, 16));

                JPanel painelEsquerdaInat = new JPanel(new BorderLayout(0, 0));
                painelEsquerdaInat.setOpaque(false);
                painelEsquerdaInat.add(painelPrefixoInat, BorderLayout.WEST);
                painelEsquerdaInat.add(lblJustificativaInativacao, BorderLayout.CENTER);

                JLabel lblDataHorarioInativacao = new JLabel(
                        "<html><div style='text-align:right;'>"
                        + "<b>Inativada em:</b> " + dtInat + " " + hrInat
                        + "</div></html>"
                );
                lblDataHorarioInativacao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblDataHorarioInativacao.setForeground(new Color(100, 116, 139));

                painelJustificativa.add(painelEsquerdaInat, BorderLayout.CENTER);
                painelJustificativa.add(lblDataHorarioInativacao, BorderLayout.EAST);

                final String subtituloInat = "Nota #" + evolucao.getId()
                        + (dtInat.isEmpty() ? "" : ("  |  Inativada em: " + dtInat + " " + hrInat));
                instalarCliqueTextoCompleto(
                        new Component[]{painelJustificativa, painelEsquerdaInat, painelPrefixoInat, iconeAlertaInativacao, lblPrefixoInat, lblJustificativaInativacao, lblDataHorarioInativacao},
                        "Motivo da Inativação",
                        subtituloInat,
                        motivoCompletoInat
                );
            }
        }

        if (notaRetificada && retificacao != null) {
            CircularIconLabel iconeLinkRetificacao = new CircularIconLabel();
            iconeLinkRetificacao.setShadowBlur(0);
            iconeLinkRetificacao.setBackground(new Color(0, 0, 0, 0));
            iconeLinkRetificacao.setIcon(new ImageIcon(FichaProntuarioDesignUtil.class.getResource("/imagens/ProntuarioPacienteIcons/link.png")));
            iconeLinkRetificacao.setIconWidth(13);
            iconeLinkRetificacao.setIconHeight(13);
            iconeLinkRetificacao.setPreferredSize(new Dimension(15, 15));

            final String dtRet = retificacao.getDataRetificacao() != null
                    ? retificacao.getDataRetificacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : dataFmt;
            final String hrRet = retificacao.getHorarioRetificacao() != null
                    ? retificacao.getHorarioRetificacao().format(DateTimeFormatter.ofPattern("HH:mm"))
                    : horaFmt;
            final String motivoCompletoRet = retificacao.getMotivoRetificacao() != null
                    ? retificacao.getMotivoRetificacao()
                    : "";

            JPanel painelPrefixoRet = new JPanel();
            painelPrefixoRet.setLayout(new BoxLayout(painelPrefixoRet, BoxLayout.X_AXIS));
            painelPrefixoRet.setOpaque(false);
            painelPrefixoRet.add(iconeLinkRetificacao);
            painelPrefixoRet.add(Box.createHorizontalStrut(4));
            JLabel lblPrefixoRet = new JLabel("Retifica a nota #" + evolucao.getId() + " (" + dtRet + " " + hrRet + ")  |  Justificativa: ");
            lblPrefixoRet.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblPrefixoRet.setForeground(new Color(29, 78, 216));
            painelPrefixoRet.add(lblPrefixoRet);

            JLabel lblMotivoRetificacao = new JLabel(motivoCompletoRet.replaceAll("\\s+", " ").trim());
            lblMotivoRetificacao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblMotivoRetificacao.setForeground(new Color(29, 78, 216));
            lblMotivoRetificacao.setMinimumSize(new Dimension(20, 16));

            JPanel painelEsquerdaRet = new JPanel(new BorderLayout(0, 0));
            painelEsquerdaRet.setOpaque(false);
            painelEsquerdaRet.add(painelPrefixoRet, BorderLayout.WEST);
            painelEsquerdaRet.add(lblMotivoRetificacao, BorderLayout.CENTER);

            final String idNovaNota = (retificacao.getNotaRetificadora() != null && retificacao.getNotaRetificadora().getId() != null)
                    ? String.valueOf(retificacao.getNotaRetificadora().getId())
                    : "";
            JLabel lblDadosRetificacao = new JLabel(
                    idNovaNota.isEmpty() ? "" : ("<html><b>Nova nota #" + idNovaNota + "</b></html>")
            );
            lblDadosRetificacao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblDadosRetificacao.setForeground(new Color(59, 130, 246));

            painelJustificativaRetificacao.add(painelEsquerdaRet, BorderLayout.CENTER);
            painelJustificativaRetificacao.add(lblDadosRetificacao, BorderLayout.EAST);

            final String subtituloRet = "Retifica a nota #" + evolucao.getId()
                    + " (" + dtRet + " " + hrRet + ")"
                    + (idNovaNota.isEmpty() ? "" : ("  |  Nova nota #" + idNovaNota));
            instalarCliqueTextoCompleto(
                    new Component[]{painelJustificativaRetificacao, painelEsquerdaRet, painelPrefixoRet, iconeLinkRetificacao, lblPrefixoRet, lblMotivoRetificacao, lblDadosRetificacao},
                    "Justificativa da Retificação",
                    subtituloRet,
                    motivoCompletoRet
            );
        }

        card.add(painelCabecalho, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setOpaque(false);
        painelCorpoEBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelConteudo.add(painelCorpoEBotoes);

        if (notaInativa && inativacao != null) {
            painelJustificativa.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelConteudo.add(Box.createVerticalStrut(6));
            painelConteudo.add(painelJustificativa);
        }
        if (notaRetificada && retificacao != null) {
            painelJustificativaRetificacao.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelConteudo.add(Box.createVerticalStrut(6));
            painelConteudo.add(painelJustificativaRetificacao);
        }

        card.add(painelConteudo, BorderLayout.CENTER);
        painelContainer.add(card, BorderLayout.CENTER);

        return painelContainer;
    }

    private static void instalarPlaceholderTextArea(final JTextArea area, final String placeholder) {
        area.putClientProperty("JTextArea.placeholderText", placeholder);
        area.setToolTipText(placeholder);
        area.setBorder(new EmptyBorder(8, 10, 8, 10) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                super.paintBorder(c, g, x, y, width, height);
                if (area.getText().isEmpty() && !area.hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(new Color(148, 163, 184));
                    g2.setFont(area.getFont());
                    g2.drawString(placeholder, x + 10, y + 22);
                    g2.dispose();
                }
            }
        });
        area.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                area.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                area.repaint();
            }
        });
    }

    public static Icon carregarIconeRedimensionado(String caminhoRecurso, Color corTint, int largura, int altura) {
        try {
            java.net.URL url = FichaProntuarioDesignUtil.class.getResource(caminhoRecurso);
            if (url == null) {
                return null;
            }
            ImageIcon orig = new ImageIcon(url);
            if (corTint != null) {
                return ImagemUtil.recolorirERedimensionar(orig, corTint, largura, altura);
            }
            return ImagemUtil.redimensionar(orig, largura, altura);
        } catch (Exception e) {
            return null;
        }
    }

    private static Icon criarIconeRelogioTransparente(Color cor, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(1, 1, w - 3, h - 3);
        int cx = w / 2;
        int cy = h / 2;
        g2.drawLine(cx, cy, cx, 4);
        g2.drawLine(cx, cy, cx + 3, cy + 1);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeLixeira(Color cor, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.drawLine(2, 3, w - 3, 3);
        g2.drawLine(5, 1, w - 6, 1);
        g2.drawRoundRect(3, 3, w - 7, h - 5, 2, 2);
        g2.drawLine(6, 6, 6, h - 4);
        g2.drawLine(w - 7, 6, w - 7, h - 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeDisqueteSalvar(Color cor, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.drawRoundRect(1, 1, w - 3, h - 3, 3, 3);
        g2.fillRect(4, 1, w - 9, 5);
        g2.drawRect(3, h - 7, w - 7, 5);
        g2.dispose();
        return new ImageIcon(img);
    }

    public static String formatarTipoNotaMaiusculo(TipoNotaClinica tipo) {
        if (tipo == null) {
            return "";
        }
        switch (tipo) {
            case EVOLUCAO_CLINICA:
                return "EVOLUÇÃO CLÍNICA";
            case NOTA_DE_SESSAO:
                return "NOTA DE SESSÃO";
            case NOTA_DE_ACOMPANHAMENTO:
                return "NOTA DE ACOMPANHAMENTO";
            case NOTA_DE_AVALIACAO_PSICOLOGICA:
                return "NOTA DE AVALIAÇÃO PSICOLÓGICA";
            case NOTA_DE_OBSERVACAO_CLINICA:
                return "NOTA DE OBSERVAÇÃO CLÍNICA";
            case NOTA_DE_INTERVENCAO_TERAPEUTICA:
                return "NOTA DE INTERVENÇÃO TERAPÊUTICA";
            case NOTA_DE_RESPOSTA_AO_TRATAMENTO:
                return "NOTA DE RESPOSTA AO TRATAMENTO";
            case NOTA_DE_INTERCORRENCIA:
                return "NOTA DE INTERCORRÊNCIA";
            case NOTA_DE_ORIENTACAO_AO_PACIENTE:
                return "NOTA DE ORIENTAÇÃO AO PACIENTE";
            case NOTA_DE_PLANEJAMENTO_TERAPEUTICO:
                return "NOTA DE PLANEJAMENTO TERAPÊUTICO";
            case NOTA_DE_ACOMPANHAMENTO_DE_METAS:
                return "NOTA DE ACOMPANHAMENTO DE METAS";
            case NOTA_DE_ENCERRAMENTO_DO_ATENDIMENTO:
                return "NOTA DE ENCERRAMENTO DO ATENDIMENTO";
            case NOTA_DE_ENCAMINHAMENTO:
                return "NOTA DE ENCAMINHAMENTO";
            case NOTA_DE_CONTATO_COM_O_PACIENTE:
                return "NOTA DE CONTATO COM O PACIENTE";
            case NOTA_ADMINISTRATIVA_RELACIONADA_AO_ATENDIMENTO:
                return "NOTA ADMINISTRATIVA DO ATENDIMENTO";
            default:
                return tipo.name().replace('_', ' ');
        }
    }

    public static String formatarTipoNotaSubtitulo(TipoNotaClinica tipo) {
        if (tipo == null) {
            return "Evolução Clínica";
        }
        if (tipo == TipoNotaClinica.EVOLUCAO_CLINICA) {
            return "Evolução Clínica";
        }
        String m = formatarTipoNotaMaiusculo(tipo).toLowerCase();
        if (m.isEmpty()) {
            return "Evolução Clínica";
        }
        return Character.toUpperCase(m.charAt(0)) + m.substring(1);
    }

    private static JButton criarBotaoAcaoCard(String texto, Icon icone, Color corFundo, Color corTexto, Color corBorda, boolean habilitado) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                if (corBorda != null) {
                    g2.setColor(corBorda);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(corFundo);
        btn.setForeground(corTexto);
        if (icone != null) {
            btn.setIcon(icone);
            btn.setIconTextGap(3);
        }
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(3, 6, 3, 6));
        btn.setEnabled(habilitado);
        if (habilitado) {
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        return btn;
    }

    private static JLabel criarBadgeStatusNota(String texto, Color corFundo) {
        JLabel badge = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));
        return badge;
    }

    // =========================================================================
    // ESTILIZAÇÃO DAS 3 SUBTELAS (INATIVAR, RETIFICAR E VISUALIZAR NOTA CLÍNICA)
    // =========================================================================

    /**
     * Aplica o design visual da subtela InativarNotaClinicaView (conforme mockup).
     */
    public static void aplicarDesignInativarNota(
            javax.swing.JDialog dialog,
            JLabel lblTitulo,
            JLabel lblNotaSelecionada,
            JLabel lblMotivo,
            JLabel lblDataAlteracao,
            JTextField txtIdNotaClinica,
            JTextField txtDataHoraAlteracao,
            JScrollPane scrollMotivo,
            JTextArea txtMotivoInativacao,
            JButton btnCancelar,
            JButton btnConfirmar
    ) {
        estilizarDialogNotaComMotivo(
                dialog,
                "Inativar Nota Clínica",
                criarIconeHeaderDocMenos(42, 42),
                lblNotaSelecionada,
                "Nota Selecionada:",
                txtIdNotaClinica,
                lblDataAlteracao,
                "Evolução Clínica alterada em:",
                txtDataHoraAlteracao,
                criarIconeProibidoVerde(18, 18),
                lblMotivo,
                "Motivo da inativação (Obrigatório):",
                scrollMotivo,
                txtMotivoInativacao,
                "Informe o motivo da inativação...",
                btnCancelar,
                "Cancelar",
                btnConfirmar,
                "Confirmar inativação"
        );
    }

    /**
     * Aplica o design visual da subtela RetificarNotaClinicaView (conforme mockup).
     */
    public static void aplicarDesignRetificarNota(
            javax.swing.JDialog dialog,
            JLabel lblTitulo,
            JLabel lblNotaRetificada,
            JLabel lblMotivo,
            JLabel lblDataRetificada,
            JTextField txtIdNotaRetificada,
            JTextField txtDataHoraAlteracaoRetificada,
            JScrollPane scrollMotivo,
            JTextArea txtMotivoRetificacao,
            JButton btnCancelar,
            JButton btnConfirmar
    ) {
        estilizarDialogNotaComMotivo(
                dialog,
                "Retificar Nota Clínica",
                criarIconeHeaderDocLapis(42, 42),
                lblNotaRetificada,
                "Nota retificada:",
                txtIdNotaRetificada,
                lblDataRetificada,
                "Nota retificada em:",
                txtDataHoraAlteracaoRetificada,
                criarIconeLapisVerde(18, 18),
                lblMotivo,
                "Motivo da Retificação da nota (Obrigatório):",
                scrollMotivo,
                txtMotivoRetificacao,
                "Informe o motivo da retificação...",
                btnCancelar,
                "Cancelar",
                btnConfirmar,
                "Confirmar Retificação"
        );
    }

    private static void estilizarDialogNotaComMotivo(
            javax.swing.JDialog dialog,
            String tituloHeader,
            Icon iconeHeader,
            JLabel lblCampo1,
            String textoCampo1,
            JTextField txtCampo1,
            JLabel lblCampo2,
            String textoCampo2,
            JTextField txtCampo2,
            Icon iconeCampo3,
            JLabel lblCampo3,
            String textoCampo3,
            JScrollPane scrollMotivo,
            JTextArea txtMotivo,
            String placeholderMotivo,
            JButton btnCancelar,
            String textoCancelar,
            JButton btnConfirmar,
            String textoConfirmar
    ) {
        dialog.setTitle(tituloHeader);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // 1. Cabeçalho verde com faixa diagonal e ícone branco à esquerda
        JPanel bannerHeader = criarBannerHeaderVerde(tituloHeader, iconeHeader);
        root.add(bannerHeader, BorderLayout.NORTH);

        // 2. Corpo branco com ícones circulares verdes e campos arredondados
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 26, 20, 26));

        // Linha 1: # + Rótulo + Campo ID
        JPanel row1 = new JPanel(new BorderLayout(12, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel left1 = new JPanel();
        left1.setLayout(new BoxLayout(left1, BoxLayout.X_AXIS));
        left1.setOpaque(false);
        left1.add(criarCirculoIconeVerde(criarIconeHashtagVerde(18, 18), 38));
        left1.add(Box.createHorizontalStrut(12));
        lblCampo1.setText(textoCampo1);
        lblCampo1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCampo1.setForeground(new Color(15, 23, 42));
        left1.add(lblCampo1);
        left1.add(Box.createHorizontalStrut(10));

        estilizarTextFieldSomenteLeitura(txtCampo1);
        Container boxCampo1 = criarBoxCampoLeitura(txtCampo1, null);
        row1.add(left1, BorderLayout.WEST);
        row1.add(boxCampo1, BorderLayout.CENTER);
        body.add(row1);
        body.add(Box.createVerticalStrut(14));

        // Linha 2: Calendário + Rótulo + Campo Data/Hora com ícone de calendário à direita
        JPanel row2 = new JPanel(new BorderLayout(12, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel left2 = new JPanel();
        left2.setLayout(new BoxLayout(left2, BoxLayout.X_AXIS));
        left2.setOpaque(false);
        left2.add(criarCirculoIconeVerde(criarIconeCalendarioVerde(18, 18), 38));
        left2.add(Box.createHorizontalStrut(12));
        lblCampo2.setText(textoCampo2);
        lblCampo2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCampo2.setForeground(new Color(15, 23, 42));
        left2.add(lblCampo2);
        left2.add(Box.createHorizontalStrut(10));

        estilizarTextFieldSomenteLeitura(txtCampo2);
        JLabel icoDirCal = new JLabel(criarIconeCalendarioVerde(16, 16));
        icoDirCal.setBorder(new EmptyBorder(0, 4, 0, 6));
        Container boxCampo2 = criarBoxCampoLeitura(txtCampo2, icoDirCal);
        row2.add(left2, BorderLayout.WEST);
        row2.add(boxCampo2, BorderLayout.CENTER);
        body.add(row2);
        body.add(Box.createVerticalStrut(14));

        // Linha 3: Ícone (proibido/lápis) + Rótulo do motivo
        JPanel row3 = new JPanel(new BorderLayout(12, 0));
        row3.setOpaque(false);
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel left3 = new JPanel();
        left3.setLayout(new BoxLayout(left3, BoxLayout.X_AXIS));
        left3.setOpaque(false);
        left3.add(criarCirculoIconeVerde(iconeCampo3, 38));
        left3.add(Box.createHorizontalStrut(12));
        lblCampo3.setText(textoCampo3);
        lblCampo3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCampo3.setForeground(new Color(15, 23, 42));
        left3.add(lblCampo3);
        row3.add(left3, BorderLayout.WEST);
        body.add(row3);
        body.add(Box.createVerticalStrut(8));

        // Caixa de texto do motivo arredondada com placeholder
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMotivo.setForeground(new Color(30, 41, 59));
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.setOpaque(false);
        instalarPlaceholderTextArea(txtMotivo, placeholderMotivo);

        scrollMotivo.setOpaque(false);
        scrollMotivo.getViewport().setOpaque(false);
        scrollMotivo.setBorder(BorderFactory.createEmptyBorder());
        ComponenteUtil.formatarScrollInvisivel(scrollMotivo);

        Container boxMotivo = new Container();
        boxMotivo.removeShadow();
        boxMotivo.setBackground(Color.WHITE);
        boxMotivo.setBorderRadius(14);
        boxMotivo.setBorder(new Color(186, 198, 214), 1);
        boxMotivo.setLayout(new BorderLayout());
        boxMotivo.setBorder(new EmptyBorder(6, 6, 6, 6));
        boxMotivo.add(scrollMotivo, BorderLayout.CENTER);
        boxMotivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        boxMotivo.setPreferredSize(new Dimension(520, 175));

        JPanel motivoIndentWrapper = new JPanel(new BorderLayout());
        motivoIndentWrapper.setOpaque(false);
        motivoIndentWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        motivoIndentWrapper.setBorder(new EmptyBorder(0, 48, 0, 0));
        motivoIndentWrapper.add(boxMotivo, BorderLayout.CENTER);
        body.add(motivoIndentWrapper);

        root.add(body, BorderLayout.CENTER);

        // 3. Barra de rodapé verde-clara com botões Cancelar e Confirmar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(231, 246, 236));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(209, 235, 218)),
                new EmptyBorder(14, 52, 14, 34)
        ));

        estilizarBotaoCancelarSubtela(btnCancelar, textoCancelar);
        estilizarBotaoConfirmarSubtela(btnConfirmar, textoConfirmar);

        footer.add(btnCancelar, BorderLayout.WEST);
        footer.add(btnConfirmar, BorderLayout.EAST);

        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setMinimumSize(new Dimension(610, 495));
        dialog.setPreferredSize(new Dimension(610, 495));
        dialog.pack();
    }

    /**
     * Aplica o design visual da subtela VisualizarNotaClinicaView (conforme mockup).
     */
    public static void aplicarDesignVisualizarNota(
            javax.swing.JDialog dialog,
            JLabel lblTitulo,
            JLabel lblIdNota,
            JLabel lblData,
            JLabel lblHorario,
            JLabel lblTipo,
            JLabel lblRelatorio,
            JLabel lblObservacao,
            JTextField txtIDNota,
            JTextField txtData,
            JTextField txtHorarioCriacao,
            JTextField txtTipoNotaClinica,
            JScrollPane scrollEvolucao,
            JTextArea txtEvolucaoClinica,
            JScrollPane scrollObservacao,
            JTextArea txtObservacao
    ) {
        dialog.setTitle("Visualizar Nota");
        dialog.setResizable(false);

        if (txtHorarioCriacao.getText() != null && txtHorarioCriacao.getText().contains(".")) {
            txtHorarioCriacao.setText(txtHorarioCriacao.getText().substring(0, txtHorarioCriacao.getText().indexOf('.')));
        }

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // 1. Cabeçalho verde com ícone de documento + lupa
        JPanel bannerHeader = criarBannerHeaderVerde("Visualizar Nota", criarIconeHeaderDocLupa(42, 42));
        root.add(bannerHeader, BorderLayout.NORTH);

        // 2. Corpo branco
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(18, 26, 16, 26));

        // 4 linhas de metadados no topo
        body.add(criarLinhaMetadadoVisualizar(
                criarIconeHashtagVerde(17, 17),
                lblIdNota,
                "ID NOTA:",
                txtIDNota,
                160
        ));
        body.add(Box.createVerticalStrut(10));

        body.add(criarLinhaMetadadoVisualizar(
                criarIconeCalendarioVerde(17, 17),
                lblData,
                "Data da criação da nota:",
                txtData,
                250
        ));
        body.add(Box.createVerticalStrut(10));

        body.add(criarLinhaMetadadoVisualizar(
                criarIconeRelogioTransparente(new Color(13, 122, 59), 17, 17),
                lblHorario,
                "Horário de criação da nota:",
                txtHorarioCriacao,
                235
        ));
        body.add(Box.createVerticalStrut(10));

        body.add(criarLinhaMetadadoVisualizar(
                criarIconeTagVerde(17, 17),
                lblTipo,
                "Tipo da nota:",
                txtTipoNotaClinica,
                380
        ));
        body.add(Box.createVerticalStrut(18));

        // Seção 1: Relatório evolução clínica
        body.add(criarTituloSecaoVisualizar(criarIconeGraficoBarrasVerde(18, 18), lblRelatorio, "Relatório evolução clínica:"));
        body.add(Box.createVerticalStrut(8));
        body.add(criarBoxTextoComIconeDocumento(scrollEvolucao, txtEvolucaoClinica, 92));
        body.add(Box.createVerticalStrut(16));

        // Seção 2: Observação
        body.add(criarTituloSecaoVisualizar(criarIconeOlhoVerde(20, 16), lblObservacao, "Observação:"));
        body.add(Box.createVerticalStrut(8));
        body.add(criarBoxTextoComIconeDocumento(scrollObservacao, txtObservacao, 84));

        root.add(body, BorderLayout.CENTER);

        // 3. Barra de rodapé verde-clara com "/ (i) Informações da nota" à direita
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(231, 246, 236));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(209, 235, 218)),
                new EmptyBorder(10, 24, 10, 24)
        ));

        JPanel infoRight = new JPanel();
        infoRight.setLayout(new BoxLayout(infoRight, BoxLayout.X_AXIS));
        infoRight.setOpaque(false);

        JLabel lblSlash = new JLabel("/");
        lblSlash.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSlash.setForeground(new Color(46, 150, 88));

        JLabel lblIcoInfo = new JLabel(criarIconeInfoCirculoVerde(18, 18));
        JLabel lblTextoInfo = new JLabel("Informações da nota");
        lblTextoInfo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblTextoInfo.setForeground(new Color(13, 82, 44));

        infoRight.add(lblSlash);
        infoRight.add(Box.createHorizontalStrut(8));
        infoRight.add(lblIcoInfo);
        infoRight.add(Box.createHorizontalStrut(6));
        infoRight.add(lblTextoInfo);

        footer.add(infoRight, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setMinimumSize(new Dimension(595, 590));
        dialog.setPreferredSize(new Dimension(595, 590));
        dialog.pack();
    }

    private static JPanel criarLinhaMetadadoVisualizar(
            Icon icone,
            JLabel lbl,
            String textoLabel,
            JTextField txt,
            int larguraBox
    ) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setOpaque(false);
        left.add(criarCirculoIconeVerde(icone, 36));
        left.add(Box.createHorizontalStrut(12));

        lbl.setText(textoLabel);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(new Color(15, 23, 42));
        left.add(lbl);
        left.add(Box.createHorizontalStrut(12));

        estilizarTextFieldSomenteLeitura(txt);
        Container box = criarBoxCampoLeitura(txt, null);
        box.setPreferredSize(new Dimension(larguraBox, 36));

        JPanel rightWrap = new JPanel(new BorderLayout());
        rightWrap.setOpaque(false);
        rightWrap.add(box, BorderLayout.WEST);

        row.add(left, BorderLayout.WEST);
        row.add(rightWrap, BorderLayout.CENTER);
        return row;
    }

    private static JPanel criarTituloSecaoVisualizar(Icon icone, JLabel lbl, String texto) {
        JPanel headerSecao = new JPanel();
        headerSecao.setLayout(new BoxLayout(headerSecao, BoxLayout.X_AXIS));
        headerSecao.setOpaque(false);
        headerSecao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel barraVertical = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(13, 122, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 3, 3);
                g2.dispose();
            }
        };
        barraVertical.setOpaque(false);
        barraVertical.setPreferredSize(new Dimension(4, 22));
        barraVertical.setMaximumSize(new Dimension(4, 22));

        JLabel icoLbl = new JLabel(icone);
        lbl.setText(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(13, 104, 50));

        headerSecao.add(barraVertical);
        headerSecao.add(Box.createHorizontalStrut(8));
        headerSecao.add(icoLbl);
        headerSecao.add(Box.createHorizontalStrut(8));
        headerSecao.add(lbl);
        return headerSecao;
    }

    private static Container criarBoxTextoComIconeDocumento(JScrollPane scroll, JTextArea area, int alturaPref) {
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setForeground(new Color(15, 23, 42));
        area.setBorder(new EmptyBorder(4, 6, 4, 6));

        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        ComponenteUtil.formatarScrollInvisivel(scroll);

        JLabel icoDoc = new JLabel(criarIconeDocumentoVerdePreenchido(24, 28));
        icoDoc.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        icoDoc.setBorder(new EmptyBorder(4, 6, 0, 6));

        Container box = new Container();
        box.removeShadow();
        box.setBackground(new Color(241, 245, 249));
        box.setBorderRadius(12);
        box.setBorder(new Color(203, 213, 225), 1);
        box.setLayout(new BorderLayout(6, 0));
        box.setBorder(new EmptyBorder(8, 10, 8, 10));
        box.add(icoDoc, BorderLayout.WEST);
        box.add(scroll, BorderLayout.CENTER);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setPreferredSize(new Dimension(540, alturaPref));
        return box;
    }

    private static void estilizarTextFieldSomenteLeitura(JTextField txt) {
        txt.setEditable(false);
        txt.setOpaque(false);
        txt.setBorder(new EmptyBorder(4, 10, 4, 8));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(new Color(15, 23, 42));
    }

    private static Container criarBoxCampoLeitura(JTextField txt, JComponent trailingComp) {
        Container box = new Container();
        box.removeShadow();
        box.setBackground(new Color(241, 245, 249));
        box.setBorderRadius(12);
        box.setBorder(new Color(195, 207, 222), 1);
        box.setLayout(new BorderLayout(4, 0));
        box.setBorder(new EmptyBorder(2, 4, 2, 6));
        box.add(txt, BorderLayout.CENTER);
        if (trailingComp != null) {
            box.add(trailingComp, BorderLayout.EAST);
        }
        box.setPreferredSize(new Dimension(240, 36));
        return box;
    }

    private static CircularIconLabel criarCirculoIconeVerde(Icon icone, int diametro) {
        CircularIconLabel circle = new CircularIconLabel();
        circle.setShadowBlur(0);
        circle.setBackground(new Color(220, 252, 231));
        circle.setIconColor(null);
        circle.setIcon(icone);
        circle.setIconWidth(icone.getIconWidth());
        circle.setIconHeight(icone.getIconHeight());
        circle.setPreferredSize(new Dimension(diametro, diametro));
        circle.setMinimumSize(new Dimension(diametro, diametro));
        circle.setMaximumSize(new Dimension(diametro, diametro));
        return circle;
    }

    private static JPanel criarBannerHeaderVerde(final String titulo, final Icon icone) {
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                        0, 0, new Color(20, 148, 78),
                        w, h, new Color(11, 108, 53)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // Faixas diagonais translúcidas à direita como nos mockups
                g2.setColor(new Color(255, 255, 255, 28));
                java.awt.Polygon p1 = new java.awt.Polygon(
                        new int[]{w - 170, w - 95, w - 35, w - 110},
                        new int[]{h, 0, 0, h},
                        4
                );
                g2.fillPolygon(p1);
                java.awt.Polygon p2 = new java.awt.Polygon(
                        new int[]{w - 85, w - 15, w, w - 30},
                        new int[]{h, 0, 0, h},
                        4
                );
                g2.fillPolygon(p2);
                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setPreferredSize(new Dimension(595, 70));
        banner.setBorder(new EmptyBorder(12, 26, 12, 26));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setOpaque(false);

        JLabel lblIco = new JLabel(icone);
        JPanel sep = new JPanel();
        sep.setBackground(new Color(255, 255, 255, 110));
        sep.setPreferredSize(new Dimension(1, 38));
        sep.setMaximumSize(new Dimension(1, 38));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTit.setForeground(Color.WHITE);

        left.add(lblIco);
        left.add(Box.createHorizontalStrut(16));
        left.add(sep);
        left.add(Box.createHorizontalStrut(18));
        left.add(lblTit);

        banner.add(left, BorderLayout.WEST);
        return banner;
    }

    private static void estilizarBotaoCancelarSubtela(JButton btn, String texto) {
        btn.setText(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(new Color(13, 82, 44));
        btn.setBackground(new Color(242, 251, 245));
        btn.setIcon(criarIconeCirculoXVerde(18, 18));
        btn.setIconTextGap(8);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(18, 107, 56), 1, true),
                new EmptyBorder(7, 28, 7, 28)
        ));
    }

    private static void estilizarBotaoConfirmarSubtela(JButton btn, String texto) {
        btn.setText(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setIcon(criarIconeCheckBranco(18, 18));
        btn.setIconTextGap(8);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        BotaoEstilo.aplicarEfeito(
                btn,
                new Color(14, 122, 60),
                new Color(11, 102, 49),
                new Color(9, 82, 39)
        );
        btn.setBorder(new EmptyBorder(8, 30, 8, 30));
    }

    // Ícones vetoriais nítidos para as subtelas
    private static Icon criarIconeHeaderDocMenos(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawRoundRect(4, 3, 24, 32, 5, 5);
        g2.drawLine(9, 11, 18, 11);
        g2.drawLine(9, 17, 22, 17);
        g2.drawLine(9, 23, 18, 23);
        g2.setColor(new Color(16, 130, 68));
        g2.fillOval(21, 21, 16, 16);
        g2.setColor(Color.WHITE);
        g2.drawOval(21, 21, 16, 16);
        g2.drawLine(25, 29, 33, 29);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeHeaderDocLapis(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawRoundRect(4, 3, 24, 32, 5, 5);
        g2.drawLine(9, 11, 20, 11);
        g2.drawLine(9, 17, 22, 17);
        g2.drawLine(9, 23, 17, 23);
        g2.drawLine(22, 29, 34, 17);
        g2.drawLine(25, 32, 37, 20);
        g2.drawLine(21, 33, 25, 32);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeHeaderDocLupa(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawRoundRect(4, 3, 24, 32, 5, 5);
        g2.drawLine(9, 11, 20, 11);
        g2.drawLine(9, 17, 22, 17);
        g2.drawLine(9, 23, 17, 23);
        g2.setColor(new Color(16, 130, 68));
        g2.fillOval(20, 20, 13, 13);
        g2.setColor(Color.WHITE);
        g2.drawOval(20, 20, 13, 13);
        g2.drawLine(31, 31, 37, 37);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeHashtagVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(6, 2, 4, h - 3);
        g2.drawLine(w - 5, 2, w - 7, h - 3);
        g2.drawLine(2, 6, w - 3, 6);
        g2.drawLine(2, h - 6, w - 3, h - 6);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeCalendarioVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawRoundRect(2, 3, w - 4, h - 5, 3, 3);
        g2.drawLine(2, 7, w - 2, 7);
        g2.drawLine(5, 1, 5, 4);
        g2.drawLine(w - 6, 1, w - 6, 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeProibidoVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(2, 2, w - 4, h - 4);
        g2.drawLine(4, 4, w - 4, h - 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeLapisVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(3, h - 3, 7, h - 4);
        g2.drawLine(3, h - 3, 4, h - 7);
        g2.drawLine(4, h - 7, w - 5, 2);
        g2.drawLine(7, h - 4, w - 2, 5);
        g2.drawLine(w - 5, 2, w - 2, 5);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeTagVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[] xs = {8, w - 2, w - 2, 9, 2};
        int[] ys = {2, 2, 9, h - 2, 9};
        g2.drawPolygon(xs, ys, 5);
        g2.fillOval(w - 6, 4, 3, 3);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeGraficoBarrasVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.fillRoundRect(1, h - 7, 4, 6, 2, 2);
        g2.fillRoundRect(7, h - 11, 4, 10, 2, 2);
        g2.fillRoundRect(13, 2, 4, h - 3, 2, 2);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeOlhoVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(2, 2, w - 4, h - 4);
        g2.fillOval(w / 2 - 3, h / 2 - 3, 6, 6);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeDocumentoVerdePreenchido(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(19, 132, 68));
        g2.fillRoundRect(2, 2, w - 4, h - 4, 5, 5);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(6, 9, w - 7, 9);
        g2.drawLine(6, 14, w - 6, 14);
        g2.drawLine(6, 19, w - 9, 19);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeCirculoXVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.fillOval(1, 1, w - 2, h - 2);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(6, 6, w - 6, h - 6);
        g2.drawLine(w - 6, 6, 6, h - 6);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeCheckBranco(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(3, h / 2 + 1, w / 2 - 1, h - 4);
        g2.drawLine(w / 2 - 1, h - 4, w - 3, 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeInfoCirculoVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.fillOval(1, 1, w - 2, h - 2);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.drawString("i", w / 2 - 2, h - 5);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static void instalarCliqueTextoCompleto(
            Component[] alvos,
            final String tituloModal,
            final String subtituloMeta,
            final String textoCompleto
    ) {
        java.awt.event.MouseAdapter cliqueListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirModalTextoCompleto(e.getComponent(), tituloModal, subtituloMeta, textoCompleto);
            }
        };
        for (Component c : alvos) {
            if (c != null) {
                c.setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (c instanceof JComponent) {
                    ((JComponent) c).setToolTipText("Clique para visualizar o texto completo");
                }
                c.addMouseListener(cliqueListener);
            }
        }
    }

    private static void abrirModalTextoCompleto(
            Component origem,
            String tituloModal,
            String subtituloMeta,
            String textoCompleto
    ) {
        java.awt.Window owner = origem != null ? javax.swing.SwingUtilities.getWindowAncestor(origem) : null;
        final javax.swing.JDialog dialog = new javax.swing.JDialog(
                owner,
                tituloModal,
                java.awt.Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JPanel bannerHeader = criarBannerHeaderVerde(tituloModal, criarIconeHeaderDocLupa(42, 42));
        root.add(bannerHeader, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(18, 24, 18, 24));

        if (subtituloMeta != null && !subtituloMeta.isBlank()) {
            JPanel metaRow = new JPanel();
            metaRow.setLayout(new BoxLayout(metaRow, BoxLayout.X_AXIS));
            metaRow.setOpaque(false);
            metaRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            metaRow.add(criarCirculoIconeVerde(criarIconeInfoCirculoVerde(16, 16), 32));
            metaRow.add(Box.createHorizontalStrut(10));
            JLabel lblMeta = new JLabel(subtituloMeta);
            lblMeta.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblMeta.setForeground(new Color(30, 41, 59));
            metaRow.add(lblMeta);

            body.add(metaRow);
            body.add(Box.createVerticalStrut(14));
        }

        JTextArea areaTexto = new JTextArea(textoCompleto != null ? textoCompleto : "");
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setOpaque(false);
        areaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaTexto.setForeground(new Color(15, 23, 42));
        areaTexto.setBorder(new EmptyBorder(6, 8, 6, 8));
        areaTexto.setCaretPosition(0);

        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        scrollTexto.setOpaque(false);
        scrollTexto.getViewport().setOpaque(false);
        scrollTexto.setBorder(BorderFactory.createEmptyBorder());
        ComponenteUtil.formatarScrollInvisivel(scrollTexto);

        Container boxTexto = new Container();
        boxTexto.removeShadow();
        boxTexto.setBackground(new Color(248, 250, 252));
        boxTexto.setBorderRadius(14);
        boxTexto.setBorder(new Color(203, 213, 225), 1);
        boxTexto.setLayout(new BorderLayout());
        boxTexto.setBorder(new EmptyBorder(8, 10, 8, 10));
        boxTexto.add(scrollTexto, BorderLayout.CENTER);
        boxTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
        boxTexto.setPreferredSize(new Dimension(500, 195));

        body.add(boxTexto);
        root.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(231, 246, 236));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(209, 235, 218)),
                new EmptyBorder(12, 24, 12, 24)
        ));

        JButton btnFechar = new JButton("Fechar");
        estilizarBotaoConfirmarSubtela(btnFechar, "Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        footer.add(btnFechar, BorderLayout.EAST);

        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setMinimumSize(new Dimension(560, 390));
        dialog.setPreferredSize(new Dimension(560, 390));
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
}
