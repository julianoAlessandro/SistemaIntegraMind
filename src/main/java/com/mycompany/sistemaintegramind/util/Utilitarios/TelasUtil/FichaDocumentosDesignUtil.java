package com.mycompany.sistemaintegramind.util.Utilitarios.TelasUtil;

import com.mycompany.sistemaintegramind.View.Componentes.ComponenteUtil;
import com.mycompany.sistemaintegramind.View.Componentes.Container;
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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoDocumentoPaciente;
import com.mycompany.sistemaintegramind.View.Componentes.BotaoEstilo;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Utilitário responsável por isolar toda a estilização visual e montagem de layout
 * da tela {@code FichaDocumentosView} e de seus cards de documentos.
 */
public final class FichaDocumentosDesignUtil {

    private FichaDocumentosDesignUtil() {
    }

    /**
     * Aplica o design visual completo da tela FichaDocumentosView conforme image1.
     */
    public static void aplicarDesignTela(
            JPanel viewRoot,
            JLabel lblTituloPrincipal,
            JLabel lblTituloSecaoDocumentos,
            final JTextField txtPesquisarDocumentos,
            final JButton btnBuscarDocumentos,
            JButton btnLimparPesquisaDocumentos,
            JButton btnAdicionarDocumentos,
            final JScrollPane scroDocumentos,
            final JPanel pnlListarDocumentosPaciente
    ) {
        viewRoot.removeAll();
        viewRoot.setBackground(new Color(245, 248, 252));
        viewRoot.setLayout(new BorderLayout(0, 14));
        viewRoot.setBorder(new EmptyBorder(18, 24, 20, 24));

        // =====================================================================
        // 1. TOPO: Ícone verde + Título/Subtítulo + Linha divisória verde
        // =====================================================================
        JPanel painelTopoWrapper = new JPanel();
        painelTopoWrapper.setLayout(new BoxLayout(painelTopoWrapper, BoxLayout.Y_AXIS));
        painelTopoWrapper.setOpaque(false);

        JPanel painelHeader = new JPanel(new BorderLayout(16, 0));
        painelHeader.setOpaque(false);
        painelHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        Container tileIconePrincipal = new Container();
        tileIconePrincipal.removeShadow();
        tileIconePrincipal.setBackground(new Color(209, 242, 221));
        tileIconePrincipal.setBorderRadius(16);
        tileIconePrincipal.setPreferredSize(new Dimension(62, 62));
        tileIconePrincipal.setLayout(new BorderLayout());
        JLabel lblIconeGrande = new JLabel(criarIconeDocumentoPreenchido(new Color(11, 79, 48), 32, 36));
        lblIconeGrande.setHorizontalAlignment(SwingConstants.CENTER);
        tileIconePrincipal.add(lblIconeGrande, BorderLayout.CENTER);

        JPanel painelTextosHeader = new JPanel();
        painelTextosHeader.setLayout(new BoxLayout(painelTextosHeader, BoxLayout.Y_AXIS));
        painelTextosHeader.setOpaque(false);

        lblTituloPrincipal.setText("Documentos do Paciente");
        lblTituloPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTituloPrincipal.setForeground(new Color(15, 23, 42));
        lblTituloPrincipal.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Acesse e gerencie os documentos relacionados ao paciente.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(51, 65, 85));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelTextosHeader.add(Box.createVerticalStrut(4));
        painelTextosHeader.add(lblTituloPrincipal);
        painelTextosHeader.add(Box.createVerticalStrut(3));
        painelTextosHeader.add(lblSubtitulo);

        painelHeader.add(tileIconePrincipal, BorderLayout.WEST);
        painelHeader.add(painelTextosHeader, BorderLayout.CENTER);

        JPanel linhaVerde = new JPanel();
        linhaVerde.setBackground(new Color(46, 184, 92));
        linhaVerde.setPreferredSize(new Dimension(100, 2));
        linhaVerde.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        linhaVerde.setAlignmentX(Component.LEFT_ALIGNMENT);

        // =====================================================================
        // 2. BARRA DE PESQUISA E BOTÕES DE AÇÃO
        // =====================================================================
        JPanel painelBarraAcoes = new JPanel(new BorderLayout(10, 0));
        painelBarraAcoes.setOpaque(false);
        painelBarraAcoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        Container containerInputBusca = new Container();
        containerInputBusca.removeShadow();
        containerInputBusca.setBackground(Color.WHITE);
        containerInputBusca.setBorderRadius(12);
        containerInputBusca.setBorder(new Color(203, 213, 225), 1);
        containerInputBusca.setLayout(new BorderLayout(8, 0));
        containerInputBusca.setBorder(new EmptyBorder(4, 12, 4, 12));
        containerInputBusca.setPreferredSize(new Dimension(400, 42));

        Icon icoLupaInput = carregarIconeRedimensionado("/imagens/lupa.png", new Color(15, 23, 42), 17, 17);
        JLabel lblLupaInput = new JLabel(icoLupaInput);
        txtPesquisarDocumentos.setOpaque(false);
        txtPesquisarDocumentos.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        txtPesquisarDocumentos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPesquisarDocumentos.setForeground(new Color(30, 41, 59));
        txtPesquisarDocumentos.putClientProperty("JTextField.placeholderText", "Digite o nome do documento que deseja encontrar...");
        txtPesquisarDocumentos.addActionListener(e -> btnBuscarDocumentos.doClick());

        containerInputBusca.add(lblLupaInput, BorderLayout.WEST);
        containerInputBusca.add(txtPesquisarDocumentos, BorderLayout.CENTER);

        JPanel painelBotoesDireita = new JPanel();
        painelBotoesDireita.setLayout(new BoxLayout(painelBotoesDireita, BoxLayout.X_AXIS));
        painelBotoesDireita.setOpaque(false);

        estilizarBotaoIconeQuadrado(
                btnBuscarDocumentos,
                carregarIconeRedimensionado("/imagens/lupa.png", new Color(15, 23, 42), 18, 18),
                "Pesquisar documento"
        );
        estilizarBotaoIconeQuadrado(
                btnLimparPesquisaDocumentos,
                carregarIconeRedimensionado("/imagens/limpar.png", new Color(15, 23, 42), 18, 18),
                "Limpar pesquisa"
        );
        estilizarBotaoAdicionarDocumento(btnAdicionarDocumentos);

        painelBotoesDireita.add(btnBuscarDocumentos);
        painelBotoesDireita.add(Box.createHorizontalStrut(8));
        painelBotoesDireita.add(btnLimparPesquisaDocumentos);
        painelBotoesDireita.add(Box.createHorizontalStrut(10));
        painelBotoesDireita.add(btnAdicionarDocumentos);

        painelBarraAcoes.add(containerInputBusca, BorderLayout.CENTER);
        painelBarraAcoes.add(painelBotoesDireita, BorderLayout.EAST);

        painelTopoWrapper.add(painelHeader);
        painelTopoWrapper.add(Box.createVerticalStrut(12));
        painelTopoWrapper.add(linhaVerde);
        painelTopoWrapper.add(Box.createVerticalStrut(14));
        painelTopoWrapper.add(painelBarraAcoes);

        viewRoot.add(painelTopoWrapper, BorderLayout.NORTH);

        // =====================================================================
        // 3. CONTAINER PRINCIPAL DE DOCUMENTOS
        // =====================================================================
        Container containerDocumentosCard = new Container();
        containerDocumentosCard.setCardStyle();
        containerDocumentosCard.setBackground(Color.WHITE);
        containerDocumentosCard.setBorderRadius(18);
        containerDocumentosCard.setLayout(new BorderLayout(0, 12));
        containerDocumentosCard.setBorder(new EmptyBorder(16, 18, 18, 18));

        JPanel painelTituloSecao = new JPanel();
        painelTituloSecao.setLayout(new BoxLayout(painelTituloSecao, BoxLayout.X_AXIS));
        painelTituloSecao.setOpaque(false);

        JLabel lblIconeSecao = new JLabel(criarIconeDocumentoPreenchido(new Color(11, 79, 48), 18, 20));
        lblTituloSecaoDocumentos.setText("Documentos");
        lblTituloSecaoDocumentos.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTituloSecaoDocumentos.setForeground(new Color(15, 23, 42));

        painelTituloSecao.add(lblIconeSecao);
        painelTituloSecao.add(Box.createHorizontalStrut(8));
        painelTituloSecao.add(lblTituloSecaoDocumentos);

        pnlListarDocumentosPaciente.setOpaque(false);
        scroDocumentos.setOpaque(false);
        scroDocumentos.getViewport().setOpaque(false);
        scroDocumentos.setBorder(BorderFactory.createEmptyBorder());
        ComponenteUtil.formatarScrollInvisivel(scroDocumentos);
        scroDocumentos.getVerticalScrollBar().setUnitIncrement(16);
        scroDocumentos.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                pnlListarDocumentosPaciente.revalidate();
                pnlListarDocumentosPaciente.repaint();
            }
        });

        containerDocumentosCard.add(painelTituloSecao, BorderLayout.NORTH);
        containerDocumentosCard.add(scroDocumentos, BorderLayout.CENTER);

        viewRoot.add(containerDocumentosCard, BorderLayout.CENTER);
        viewRoot.revalidate();
        viewRoot.repaint();
    }

    /**
     * Monta o card visual de cada documento do paciente conforme image1.
     */
    public static JPanel montarCardDocumento(
            final JScrollPane scroDocumentos,
            JLabel lblImagemDocumento,
            String nomeArquivoLower,
            JLabel lblNomeArquivo,
            JLabel lblTipoDocumento,
            JLabel lblData,
            JButton btnVisualizar,
            JButton btnRemover
    ) {
        JPanel painelContainer = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int vw = (scroDocumentos != null && scroDocumentos.getViewport() != null)
                        ? scroDocumentos.getViewport().getWidth()
                        : 0;
                if (vw > 120) {
                    return new Dimension(vw, d.height);
                }
                return d;
            }

            @Override
            public Dimension getMaximumSize() {
                Dimension d = getPreferredSize();
                int vw = (scroDocumentos != null && scroDocumentos.getViewport() != null)
                        ? scroDocumentos.getViewport().getWidth()
                        : 0;
                return new Dimension(vw > 120 ? vw : Integer.MAX_VALUE, d.height);
            }
        };
        painelContainer.setOpaque(false);
        painelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelContainer.setBorder(new EmptyBorder(0, 0, 12, 4));

        Container card = new Container();
        card.removeShadow();
        card.setBackground(Color.WHITE);
        card.setBorderRadius(14);
        card.setBorder(new Color(226, 232, 240), 1);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(12, 16, 12, 18));

        // 1. Tile esquerdo com o ícone do tipo de arquivo (70x70 arredondado)
        Container tileIconeArquivo = new Container();
        tileIconeArquivo.removeShadow();
        tileIconeArquivo.setBackground(new Color(241, 245, 249));
        tileIconeArquivo.setBorderRadius(14);
        tileIconeArquivo.setBorder(new Color(226, 232, 240), 1);
        tileIconeArquivo.setPreferredSize(new Dimension(70, 70));
        tileIconeArquivo.setLayout(new BorderLayout());

        lblImagemDocumento.setText("");
        lblImagemDocumento.setPreferredSize(new Dimension(70, 70));
        lblImagemDocumento.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagemDocumento.setVerticalAlignment(SwingConstants.CENTER);
        lblImagemDocumento.setIcon(obterIconeTipoArquivo(nomeArquivoLower, 48, 48));
        tileIconeArquivo.add(lblImagemDocumento, BorderLayout.CENTER);

        card.add(tileIconeArquivo, BorderLayout.WEST);

        // 2. Centro: Nome do arquivo em negrito + ícone de calendário + "Adicionado em: ..."
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setOpaque(false);

        lblNomeArquivo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNomeArquivo.setForeground(new Color(15, 23, 42));
        lblNomeArquivo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel linhaData = new JPanel();
        linhaData.setLayout(new BoxLayout(linhaData, BoxLayout.X_AXIS));
        linhaData.setOpaque(false);
        linhaData.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIconeCalendario = new JLabel(criarIconeCalendarioEscuro(15, 15));
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblData.setForeground(new Color(30, 41, 59));

        linhaData.add(lblIconeCalendario);
        linhaData.add(Box.createHorizontalStrut(7));
        linhaData.add(lblData);

        painelCentro.add(Box.createVerticalGlue());
        painelCentro.add(lblNomeArquivo);
        painelCentro.add(Box.createVerticalStrut(7));
        painelCentro.add(linhaData);
        painelCentro.add(Box.createVerticalGlue());

        card.add(painelCentro, BorderLayout.CENTER);

        // 3. Direita: Badge verde-claro do tipo de documento + Botões Visualizar e Remover Arquivo
        JPanel painelDireita = new JPanel(new BorderLayout(16, 0));
        painelDireita.setOpaque(false);

        JLabel badgeTipo = criarBadgeTipoDocumento(lblTipoDocumento.getText());
        JPanel wrapperBadge = new JPanel(new BorderLayout());
        wrapperBadge.setOpaque(false);
        wrapperBadge.setBorder(new EmptyBorder(4, 0, 0, 0));
        wrapperBadge.add(badgeTipo, BorderLayout.NORTH);

        estilizarBotaoAcaoCardDocumento(
                btnVisualizar,
                "Visualizar",
                criarIconeOlhoVerdeEscuro(16, 13)
        );
        estilizarBotaoAcaoCardDocumento(
                btnRemover,
                "Remover Arquivo",
                criarIconeLixeiraVerdeEscura(14, 15)
        );

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.X_AXIS));
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnVisualizar);
        painelBotoes.add(Box.createHorizontalStrut(10));
        painelBotoes.add(btnRemover);

        JPanel wrapperBotoes = new JPanel(new BorderLayout());
        wrapperBotoes.setOpaque(false);
        wrapperBotoes.setBorder(new EmptyBorder(14, 0, 6, 0));
        wrapperBotoes.add(painelBotoes, BorderLayout.CENTER);

        painelDireita.add(wrapperBadge, BorderLayout.WEST);
        painelDireita.add(wrapperBotoes, BorderLayout.EAST);

        card.add(painelDireita, BorderLayout.EAST);
        painelContainer.add(card, BorderLayout.CENTER);

        return painelContainer;
    }

    private static Icon obterIconeTipoArquivo(String nomeArquivoLower, int w, int h) {
        String recurso = null;
        boolean removerFundoBranco = false;

        if (nomeArquivoLower != null) {
            String nome = nomeArquivoLower.trim().toLowerCase();
            if (nome.endsWith(".pdf")) {
                recurso = "/imagens/pdf.png";
            } else if (nome.endsWith(".txt")) {
                recurso = "/imagens/txt.jpeg";
                removerFundoBranco = true;
            } else if (nome.endsWith(".doc") || nome.endsWith(".docx")) {
                recurso = "/imagens/word.jpeg";
                removerFundoBranco = true;
            } else if (nome.endsWith(".png")) {
                recurso = "/imagens/png.png";
            } else if (nome.endsWith(".jpg") || nome.endsWith(".jpeg")) {
                recurso = "/imagens/jpg.jpeg";
                removerFundoBranco = true;
            }
        }

        if (recurso != null) {
            try {
                InputStream is = FichaDocumentosDesignUtil.class.getResourceAsStream(recurso);
                if (is == null) {
                    is = FichaDocumentosDesignUtil.class.getResourceAsStream(recurso.replace(".jpeg", ".JPEG"));
                }
                if (is != null) {
                    BufferedImage orig;
                    try (InputStream stream = is) {
                        orig = ImageIO.read(stream);
                    }
                    if (orig != null) {
                        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = buf.createGraphics();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.drawImage(orig, 0, 0, w, h, null);
                        g2.dispose();
                        if (removerFundoBranco) {
                            for (int y = 0; y < h; y++) {
                                for (int x = 0; x < w; x++) {
                                    int argb = buf.getRGB(x, y);
                                    int r = (argb >> 16) & 0xFF;
                                    int g = (argb >> 8) & 0xFF;
                                    int b = argb & 0xFF;
                                    if (r > 235 && g > 235 && b > 235) {
                                        buf.setRGB(x, y, 0x00FFFFFF);
                                    }
                                }
                            }
                        }
                        return new ImageIcon(buf);
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return criarIconeArquivoGenericoAzulCinza(w, h);
    }

    private static JLabel criarBadgeTipoDocumento(String texto) {
        JLabel badge = new JLabel(texto != null ? texto : "") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(216, 243, 224));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(new Color(15, 65, 42));
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(6, 14, 6, 14));
        return badge;
    }

    private static void estilizarBotaoIconeQuadrado(final JButton btn, Icon icone, String tooltip) {
        btn.setText("");
        if (icone != null) {
            btn.setIcon(icone);
        }
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(44, 42));
        btn.setMinimumSize(new Dimension(44, 42));
        btn.setMaximumSize(new Dimension(44, 42));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getModel().isRollover() ? new Color(241, 245, 249) : Color.WHITE);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.setColor(new Color(203, 213, 225));
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    private static void estilizarBotaoAdicionarDocumento(final JButton btn) {
        btn.setText("Adicionar Documento");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setIcon(criarIconeAdicionarDocumentoBranco(22, 22));
        btn.setIconTextGap(8);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setPreferredSize(new Dimension(205, 42));
        btn.setMinimumSize(new Dimension(205, 42));
        btn.setMaximumSize(new Dimension(215, 42));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = btn.getModel().isPressed()
                        ? new Color(9, 75, 40)
                        : (btn.getModel().isRollover() ? new Color(16, 118, 63) : new Color(15, 104, 55));
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 14, 14);
                g2.setColor(new Color(255, 255, 255, 180));
                g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    private static void estilizarBotaoAcaoCardDocumento(final JButton btn, String texto, Icon icone) {
        btn.setText(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(15, 23, 42));
        btn.setIcon(icone);
        btn.setIconTextGap(7);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 14, 7, 14));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getModel().isRollover() ? new Color(241, 245, 249) : new Color(250, 252, 255));
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.setColor(new Color(203, 213, 225));
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    private static Icon carregarIconeRedimensionado(String caminhoRecurso, Color corTint, int largura, int altura) {
        try {
            java.net.URL url = FichaDocumentosDesignUtil.class.getResource(caminhoRecurso);
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

    private static Icon criarIconeDocumentoPreenchido(Color corDoc, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int fold = Math.max(6, w / 3);
        int[] xs = {2, w - fold - 2, w - 2, w - 2, 2};
        int[] ys = {2, 2, fold + 2, h - 2, h - 2};
        g2.setColor(corDoc);
        g2.fillPolygon(xs, ys, 5);

        g2.setColor(new Color(148, 210, 178));
        int[] fxs = {w - fold - 2, w - fold - 2, w - 2};
        int[] fys = {2, fold + 2, fold + 2};
        g2.fillPolygon(fxs, fys, 3);

        g2.setColor(Color.WHITE);
        float strokeW = Math.max(1.6f, w / 14f);
        g2.setStroke(new BasicStroke(strokeW, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int x1 = Math.max(5, w / 4);
        int x2 = w - Math.max(5, w / 4);
        int yMid = h / 2;
        g2.drawLine(x1, yMid - 3, x2, yMid - 3);
        g2.drawLine(x1, yMid + 2, x2, yMid + 2);
        g2.drawLine(x1, yMid + 7, x2 - 3, yMid + 7);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeAdicionarDocumentoBranco(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(4, 2, w - 6, h - 4, 4, 4);
        g2.setColor(new Color(15, 104, 55));
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(10, 8, w - 5, 8);
        g2.drawLine(10, 11, w - 5, 11);
        g2.drawLine(10, 14, w - 7, 14);
        g2.setColor(new Color(15, 104, 55));
        g2.fillOval(1, 7, 9, 9);
        g2.setColor(Color.WHITE);
        g2.drawLine(3, 11, 8, 11);
        g2.drawLine(6, 9, 8, 11);
        g2.drawLine(6, 13, 8, 11);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeArquivoGenericoAzulCinza(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int fold = 12;
        int[] xs = {8, w - fold - 8, w - 8, w - 8, 8};
        int[] ys = {4, 4, fold + 4, h - 4, h - 4};
        g2.setColor(new Color(235, 241, 248));
        g2.fillPolygon(xs, ys, 5);

        g2.setColor(new Color(140, 158, 185));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawPolygon(xs, ys, 5);

        int[] fxs = {w - fold - 8, w - fold - 8, w - 8};
        int[] fys = {4, fold + 4, fold + 4};
        g2.drawPolyline(fxs, fys, 3);

        g2.drawLine(15, 20, w - 18, 20);
        g2.drawLine(15, 27, w - 15, 27);
        g2.drawLine(15, 34, w - 20, 34);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeCalendarioEscuro(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(30, 41, 59));
        g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawRoundRect(1, 2, w - 3, h - 3, 3, 3);
        g2.drawLine(1, 6, w - 2, 6);
        g2.drawLine(4, 1, 4, 3);
        g2.drawLine(w - 5, 1, w - 5, 3);
        g2.fillRect(4, 8, 2, 2);
        g2.fillRect(7, 8, 2, 2);
        g2.fillRect(10, 8, 2, 2);
        g2.fillRect(4, 11, 2, 2);
        g2.fillRect(7, 11, 2, 2);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeOlhoVerdeEscuro(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(11, 79, 48));
        g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(1, 1, w - 2, h - 2);
        g2.fillOval(w / 2 - 2, h / 2 - 2, 5, 5);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeLixeiraVerdeEscura(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(11, 79, 48));
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(2, 3, w - 2, 3);
        g2.drawLine(5, 1, w - 5, 1);
        g2.drawRoundRect(3, 3, w - 6, h - 4, 2, 2);
        g2.drawLine(6, 6, 6, h - 4);
        g2.drawLine(w - 6, 6, w - 6, h - 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    // =========================================================================
    // SUBTELA ADICIONAR DOCUMENTO (AdicionarDocumentoPacienteView)
    // =========================================================================

    /**
     * Aplica o design visual da subtela AdicionarDocumentoPacienteView conforme mockup.
     */
    public static void aplicarDesignAdicionarDocumento(
            JPanel viewRoot,
            JLabel lblTituloHeader,
            JLabel lblTipoDocumento,
            JLabel lblArquivo,
            JComboBox<TipoDocumentoPaciente> cmbTipoDocumento,
            JTextField txtArquivo,
            JButton btnSelecionarArquivo,
            JButton btnCancelar,
            JButton btnSalvar
    ) {
        viewRoot.removeAll();
        viewRoot.setBackground(Color.WHITE);
        viewRoot.setLayout(new BorderLayout());

        // 1. Banner verde no topo
        JPanel bannerHeader = criarBannerHeaderVerde("Adicionar Documento", criarIconeHeaderDocMais(42, 42));
        viewRoot.add(bannerHeader, BorderLayout.NORTH);

        // 2. Corpo branco
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(22, 28, 18, 28));

        // Seção 1: Ícone circular verde + "Tipo do Documento"
        JPanel rowLabelTipo = new JPanel();
        rowLabelTipo.setLayout(new BoxLayout(rowLabelTipo, BoxLayout.X_AXIS));
        rowLabelTipo.setOpaque(false);
        rowLabelTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        rowLabelTipo.add(criarCirculoIconeVerde(criarIconeDocumentoVerdePreenchido(16, 19), 38));
        rowLabelTipo.add(Box.createHorizontalStrut(14));
        lblTipoDocumento.setText("Tipo do Documento");
        lblTipoDocumento.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTipoDocumento.setForeground(new Color(15, 23, 42));
        rowLabelTipo.add(lblTipoDocumento);

        body.add(rowLabelTipo);
        body.add(Box.createVerticalStrut(10));

        // Dropdown arredondado em largura total (alinhado após o círculo de 38+14=52px)
        cmbTipoDocumento.setBackground(Color.WHITE);
        cmbTipoDocumento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTipoDocumento.setForeground(new Color(15, 23, 42));
        cmbTipoDocumento.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        cmbTipoDocumento.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
                return lbl;
            }
        });

        Container boxCombo = new Container();
        boxCombo.removeShadow();
        boxCombo.setBackground(new Color(248, 250, 252));
        boxCombo.setBorderRadius(12);
        boxCombo.setBorder(new Color(195, 207, 222), 1);
        boxCombo.setLayout(new BorderLayout());
        boxCombo.setBorder(new EmptyBorder(2, 6, 2, 6));
        boxCombo.add(cmbTipoDocumento, BorderLayout.CENTER);
        boxCombo.setPreferredSize(new Dimension(475, 40));
        boxCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel indentCombo = new JPanel(new BorderLayout());
        indentCombo.setOpaque(false);
        indentCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        indentCombo.setBorder(new EmptyBorder(0, 52, 0, 0));
        indentCombo.add(boxCombo, BorderLayout.CENTER);

        body.add(indentCombo);
        body.add(Box.createVerticalStrut(20));

        // Seção 2: Ícone circular verde + "Arquivo"
        JPanel rowLabelArquivo = new JPanel();
        rowLabelArquivo.setLayout(new BoxLayout(rowLabelArquivo, BoxLayout.X_AXIS));
        rowLabelArquivo.setOpaque(false);
        rowLabelArquivo.setAlignmentX(Component.LEFT_ALIGNMENT);

        rowLabelArquivo.add(criarCirculoIconeVerde(criarIconeCaixaArquivoVerde(18, 16), 38));
        rowLabelArquivo.add(Box.createHorizontalStrut(14));
        lblArquivo.setText("Arquivo");
        lblArquivo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblArquivo.setForeground(new Color(15, 23, 42));
        rowLabelArquivo.add(lblArquivo);

        body.add(rowLabelArquivo);
        body.add(Box.createVerticalStrut(10));

        // Linha do campo de arquivo + botão Selecionar Arquivo
        txtArquivo.setEditable(false);
        txtArquivo.setOpaque(false);
        txtArquivo.setBorder(new EmptyBorder(4, 10, 4, 8));
        txtArquivo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtArquivo.setForeground(new Color(30, 41, 59));
        txtArquivo.putClientProperty("JTextField.placeholderText", "Nenhum arquivo selecionado");

        Container boxArquivo = new Container();
        boxArquivo.removeShadow();
        boxArquivo.setBackground(new Color(248, 250, 252));
        boxArquivo.setBorderRadius(12);
        boxArquivo.setBorder(new Color(195, 207, 222), 1);
        boxArquivo.setLayout(new BorderLayout());
        boxArquivo.setBorder(new EmptyBorder(2, 6, 2, 6));
        boxArquivo.add(txtArquivo, BorderLayout.CENTER);
        boxArquivo.setPreferredSize(new Dimension(285, 42));

        estilizarBotaoSelecionarArquivo(btnSelecionarArquivo);

        JPanel rowArquivo = new JPanel(new BorderLayout(12, 0));
        rowArquivo.setOpaque(false);
        rowArquivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowArquivo.setBorder(new EmptyBorder(0, 52, 0, 0));
        rowArquivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        rowArquivo.add(boxArquivo, BorderLayout.CENTER);
        rowArquivo.add(btnSelecionarArquivo, BorderLayout.EAST);

        body.add(rowArquivo);
        body.add(Box.createVerticalStrut(26));

        // Linha de botões Cancelar e Salvar alinhados à direita
        JPanel rowBotoes = new JPanel();
        rowBotoes.setLayout(new BoxLayout(rowBotoes, BoxLayout.X_AXIS));
        rowBotoes.setOpaque(false);
        rowBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        estilizarBotaoCancelarDialogo(btnCancelar);
        estilizarBotaoSalvarDialogo(btnSalvar);

        rowBotoes.add(Box.createHorizontalGlue());
        rowBotoes.add(btnCancelar);
        rowBotoes.add(Box.createHorizontalStrut(12));
        rowBotoes.add(btnSalvar);

        body.add(rowBotoes);
        viewRoot.add(body, BorderLayout.CENTER);

        // 3. Faixa fina verde-clara no rodapé
        JPanel footer = new JPanel();
        footer.setBackground(new Color(231, 246, 236));
        footer.setPreferredSize(new Dimension(590, 24));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(209, 235, 218)));
        viewRoot.add(footer, BorderLayout.SOUTH);

        viewRoot.setPreferredSize(new Dimension(590, 395));
        viewRoot.revalidate();
        viewRoot.repaint();
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
        banner.setPreferredSize(new Dimension(590, 68));
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

    private static com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel criarCirculoIconeVerde(Icon icone, int diametro) {
        com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel circle = new com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel();
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

    private static void estilizarBotaoSelecionarArquivo(final JButton btn) {
        btn.setText("Selecionar Arquivo");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(13, 82, 44));
        btn.setIcon(criarIconePastaVerde(20, 17));
        btn.setIconTextGap(8);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(185, 42));
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getModel().isRollover() ? new Color(240, 253, 244) : Color.WHITE);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.setColor(new Color(18, 107, 56));
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    private static void estilizarBotaoCancelarDialogo(final JButton btn) {
        btn.setText("Cancelar");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(13, 82, 44));
        btn.setIcon(criarIconeCirculoXVerde(18, 18));
        btn.setIconTextGap(8);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(138, 40));
        btn.setBorder(new EmptyBorder(6, 18, 6, 18));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getModel().isRollover() ? new Color(231, 246, 236) : new Color(246, 253, 248));
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.setColor(new Color(18, 107, 56));
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    private static void estilizarBotaoSalvarDialogo(final JButton btn) {
        btn.setText("Salvar");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setIcon(criarIconeDisqueteBranco(17, 17));
        btn.setIconTextGap(8);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(132, 40));
        BotaoEstilo.aplicarEfeito(
                btn,
                new Color(14, 122, 60),
                new Color(11, 102, 49),
                new Color(9, 82, 39)
        );
        btn.setBorder(new EmptyBorder(6, 22, 6, 22));
    }

    private static Icon criarIconeHeaderDocMais(int w, int h) {
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
        g2.drawLine(29, 25, 29, 33);
        g2.drawLine(25, 29, 33, 29);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeDocumentoVerdePreenchido(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        int fold = 5;
        int[] xs = {1, w - fold - 1, w - 1, w - 1, 1};
        int[] ys = {1, 1, fold + 1, h - 1, h - 1};
        g2.fillPolygon(xs, ys, 5);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(4, h / 2 - 2, w - 5, h / 2 - 2);
        g2.drawLine(4, h / 2 + 2, w - 5, h / 2 + 2);
        g2.drawLine(4, h / 2 + 6, w - 7, h / 2 + 6);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconeCaixaArquivoVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.fillRoundRect(1, 1, w - 2, 4, 2, 2);
        g2.fillRoundRect(2, 6, w - 4, h - 7, 3, 3);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(w / 2 - 3, 9, 6, 2, 1, 1);
        g2.dispose();
        return new ImageIcon(img);
    }

    private static Icon criarIconePastaVerde(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(13, 115, 55));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[] xs = {2, 7, 9, w - 2, w - 2, 2};
        int[] ys = {2, 2, 5, 5, h - 2, h - 2};
        g2.drawPolygon(xs, ys, 6);
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

    private static Icon criarIconeDisqueteBranco(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(1, 1, w - 2, h - 2, 3, 3);
        g2.setColor(new Color(14, 122, 60));
        g2.fillRect(4, 2, w - 8, 5);
        g2.fillRoundRect(4, h - 7, w - 8, 5, 2, 2);
        g2.dispose();
        return new ImageIcon(img);
    }
}
