/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.FichaMenuPaciente;

import com.mycompany.sistemaintegramind.Model.dao.AnamneseDAO;
import com.mycompany.sistemaintegramind.Model.dao.impl.AnamneseJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoModeloAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoPerguntaAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.FormularioAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.PerguntaAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.RespostaAnamnese;
import com.mycompany.sistemaintegramind.View.Componentes.ComponenteUtil;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.ListSelectionModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Tela de Anamnese do Paciente com dois modos:
 * 1 - Anamnese modelos: preenchimento e criação/edição de modelos com rascunho automático por paciente.
 * 2 - Anamnese Visualização: exibida ao salvar uma anamnese ou ao abrir paciente com anamnese salva,
 *     com visualização somente leitura, botão X para sair de uma anamnese desativada e voltar à principal,
 *     data e hora de desativação na frente do nome das anamneses desativadas, e botão "Editar" (na ativa)
 *     ou "Copiar e Colar" (na desativada, colando a estrutura no Criar Personalizado).
 *
 * @author guilh
 */
public class FichaAnaminaseView extends javax.swing.JPanel {

    private enum AbaBarraAnamnese {
        INFANTIL_ADOLESCENTE,
        ADULTO,
        DROPDOWN_SELECIONAR,
        CRIAR_PERSONALIZADO
    }

    /**
     * FlowLayout responsivo que calcula a quebra automática de linha (multi-row)
     * com base na largura disponível do container/scrollpane, evitando que perguntas
     * ou opções ultrapassem o limite lateral da tela.
     */
    private static class WrapFlowLayout extends FlowLayout {

        public WrapFlowLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                Container container = target;
                while (container.getSize().width == 0 && container.getParent() != null) {
                    container = container.getParent();
                }
                targetWidth = container.getSize().width;
                if (targetWidth <= 0) {
                    targetWidth = 780;
                }
                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = Math.max(220, targetWidth - horizontalInsetsAndGap - 24);

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;
                int nmembers = target.getComponentCount();

                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth && rowWidth > 0) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        if (rowWidth != 0) {
                            rowWidth += hgap;
                        }
                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                addRow(dim, rowWidth, rowHeight);
                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;

                Container scrollParent = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
                if (scrollParent != null && target.isValid()) {
                    dim.width -= (hgap + 1);
                }
                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);
            if (dim.height > 0) {
                dim.height += getVgap();
            }
            dim.height += rowHeight;
        }
    }


    private static final String PREFIXO_COMPLEMENTO = "COMPLEMENTO:";
    private static final String PREFIXO_COLUNA = "COL:";
    private static final String PREFIXO_LINHA = "LIN:";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    private static final Color VERDE_BOTAO_ATIVO = new Color(31, 122, 68);
    private static final Color VERDE_ACAO_PRINCIPAL = new Color(18, 132, 43);
    private static final Color CINZA_BOTAO_INATIVO = new Color(241, 243, 245);
    private static final Color CINZA_DROPDOWN_DESATIVADAS = new Color(235, 237, 240);
    private static final Color TEXTO_BOTAO_INATIVO = new Color(67, 77, 89);
    private static final Color BORDA_CONTAINER_INTERNO = new Color(214, 222, 228);
    private static final Color FUNDO_TITULO_PRINCIPAL_AZUL_CLARO = new Color(219, 234, 254);
    private static final Color FUNDO_TITULO_DESATIVADA_VERMELHO_CLARO = new Color(254, 226, 226);

    // Cache persistente em disco e memória dos preenchimentos não salvos (isolado por paciente e por formulário)
    private static final File ARQUIVO_RASCUNHOS_DISCO = new File(
            System.getProperty("user.home"), ".integramind_preenchimento_anamnese.properties"
    );
    private static final Map<String, String> CACHE_PREENCHIMENTO = new ConcurrentHashMap<>();
    private static boolean cacheCarregadoDoDisco = false;

    static {
        carregarCachePreenchimentoDoDisco();
        Runtime.getRuntime().addShutdownHook(new Thread(FichaAnaminaseView::salvarCachePreenchimentoNoDisco));
    }

    private static synchronized void carregarCachePreenchimentoDoDisco() {
        if (cacheCarregadoDoDisco) {
            return;
        }
        cacheCarregadoDoDisco = true;
        if (!ARQUIVO_RASCUNHOS_DISCO.exists()) {
            return;
        }
        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(ARQUIVO_RASCUNHOS_DISCO), StandardCharsets.UTF_8)) {
            props.load(reader);
            for (String nome : props.stringPropertyNames()) {
                CACHE_PREENCHIMENTO.put(nome, props.getProperty(nome));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void salvarCachePreenchimentoNoDisco() {
        Properties props = new Properties();
        props.putAll(CACHE_PREENCHIMENTO);
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(ARQUIVO_RASCUNHOS_DISCO), StandardCharsets.UTF_8)) {
            props.store(writer, "Rascunhos de preenchimento de Anamnese por Paciente - SistemaIntegraMind");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ItemAnamneseDesativada {
        private final RespostaAnamnese resposta;

        public ItemAnamneseDesativada(RespostaAnamnese resposta) {
            this.resposta = resposta;
        }

        public RespostaAnamnese getResposta() {
            return resposta;
        }

        @Override
        public String toString() {
            if (resposta == null) {
                return "Anamneses Desativadas";
            }
            String titulo = (resposta.getFormulario() != null && resposta.getFormulario().getTitulo() != null)
                    ? resposta.getFormulario().getTitulo()
                    : "Anamnese";
            LocalDateTime dtDesativacao = resposta.getDataHoraDesativacao();
            if (dtDesativacao == null && resposta.getDataAtualizacao() != null) {
                dtDesativacao = resposta.getDataAtualizacao();
            }
            if (dtDesativacao != null) {
                return titulo + " - Desativada em " + dtDesativacao.format(FORMATO_DATA_HORA);
            }
            String data = resposta.getDataAbertura() != null
                    ? resposta.getDataAbertura().format(FORMATO_DATA)
                    : "";
            return titulo + (data.isEmpty() ? "" : " - " + data);
        }
    }

    private Paciente paciente;
    private final AnamneseDAO anamneseDAO = new AnamneseJPA();

    private AbaBarraAnamnese abaAtiva = AbaBarraAnamnese.INFANTIL_ADOLESCENTE;
    private TipoModeloAnamnese modoAtual = TipoModeloAnamnese.INFANTIL_ADOLESCENTE;
    private boolean modoEdicaoQuestionario = false;
    private boolean modoVisualizacaoAnamnese = false;
    private boolean desativadaAbertaAPartirDeModelos = false;
    private AbaBarraAnamnese abaAnteriorModelos = AbaBarraAnamnese.INFANTIL_ADOLESCENTE;
    private FormularioAnamnese formularioAnteriorModelos = null;
    private boolean estavaEditandoQuestionarioModelos = false;
    private boolean atualizandoCombo = false;
    private boolean atualizandoComboDesativadas = false;
    private boolean dropdownSelecionado = false;
    private boolean carregandoPreenchimentoUI = false;

    private String chaveFormularioOrigemEdicao = null;
    private Map<String, String> dadosPreenchimentoPendentesColagem = null;

    private Icon iconeSlidersBranco;
    private Icon iconeSlidersEscuro;
    private Icon iconeMaisBranco;
    private Icon iconeFecharX;
    private Icon iconeExpandirPadrao;
    private Icon iconeMinimizarPadrao;

    private boolean modoTelaCheiaExpandida = false;
    private JDialog dialogTelaCheia = null;

    // Estado estilo Microsoft Forms no construtor: -1 = todas em pré-visualização; >= 0 = índice da pergunta em edição
    private int indicePerguntaSelecionadaConstrutor = -1;
    private JTextField campoEnunciadoAtivoConstrutor = null;

    // Popup pesquisável para o dropdown "Anamneses Desativadas"
    private JPopupMenu popupBuscaDesativadas;
    private JTextField txtBuscaDesativadas;
    private DefaultListModel<ItemAnamneseDesativada> modeloListaDesativadas;
    private JList<ItemAnamneseDesativada> listaBuscaDesativadas;
    private int indiceHoverListaDesativadas = -1;

    private FormularioAnamnese formularioAtual;
    private FormularioAnamnese rascunhoPersonalizado = null;
    private RespostaAnamnese respostaVisualizada = null;

    private final List<PerguntaEditorBinder> editoresPerguntas = new ArrayList<>();
    private final Map<Integer, String> respostasPreenchimento = new LinkedHashMap<>();
    private Timer timerPersistenciaRascunho;

    public FichaAnaminaseView() {
        this(null);
    }

    public FichaAnaminaseView(Paciente paciente) {
        this.paciente = paciente;
        initComponents();
        configurarVisualizacaoInicial();
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        carregarEstadoInicialPaciente();
    }

    private void configurarVisualizacaoInicial() {
        container1.setCardStyle();

        containerFormulario.removeShadow();
        containerFormulario.setBackground(Color.WHITE);
        containerFormulario.setBorderRadius(14);
        containerFormulario.setBorder(BORDA_CONTAINER_INTERNO, 1);

        ComponenteUtil.formatarScrollInvisivel(scrPerguntas);
        scrPerguntas.getVerticalScrollBar().setUnitIncrement(20);

        timerPersistenciaRascunho = new Timer(200, e -> salvarCachePreenchimentoNoDisco());
        timerPersistenciaRascunho.setRepeats(false);

        iconeSlidersBranco = criarIconeSliders(Color.WHITE);
        iconeSlidersEscuro = criarIconeSliders(new Color(13, 82, 65));
        iconeMaisBranco = criarIconeMais(Color.WHITE);
        iconeFecharX = criarIconeFecharX(new Color(20, 24, 28));

        btnCriarPersonalizado.setIcon(iconeSlidersEscuro);
        btnCriarNovaAnamnese.setIcon(iconeMaisBranco);
        iconeExpandirPadrao = criarIconeExpandir(new Color(120, 125, 130));
        iconeMinimizarPadrao = criarIconeMinimizar(new Color(120, 125, 130));

        btnFecharVisualizacaoDesativada.setIcon(iconeFecharX);
        btnFecharVisualizacaoDesativada.setText("");
        btnExpandir.setIcon(iconeExpandirPadrao);
        btnExpandir.setText("");

        configurarBotaoLimparVassoura();

        txtDataAbertura.setText(LocalDate.now().format(FORMATO_DATA));
        txtDataAbertura.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(195, 224, 208), 1, true),
                new EmptyBorder(2, 8, 2, 8)
        ));
        txtDataAbertura.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aoAlterarDataAbertura();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aoAlterarDataAbertura();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aoAlterarDataAbertura();
            }
        });

        estilizarBotaoArredondado(btnInfantilAdolescente);
        estilizarBotaoArredondado(btnAdulto);
        estilizarBotaoArredondado(btnCriarPersonalizado);
        estilizarBotaoArredondado(btnSalvarAnamnese);
        estilizarBotaoArredondado(btnCriarNovaAnamnese);
        estilizarBotaoArredondado(btnBaixarAnamneseSalva);
        estilizarBotaoArredondado(btnEditarAnamneseSalva);

        btnCriarNovaAnamnese.setBackground(VERDE_ACAO_PRINCIPAL);
        btnCriarNovaAnamnese.setForeground(Color.WHITE);
        btnBaixarAnamneseSalva.setBackground(VERDE_ACAO_PRINCIPAL);
        btnBaixarAnamneseSalva.setForeground(Color.WHITE);
        btnEditarAnamneseSalva.setBackground(VERDE_ACAO_PRINCIPAL);
        btnEditarAnamneseSalva.setForeground(Color.WHITE);

        scrPerguntas.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pnlConteudoAnamnese.revalidate();
                pnlConteudoAnamnese.repaint();
            }
        });

        btnEditarPerguntasModelo.setOpaque(true);
        btnEditarPerguntasModelo.setContentAreaFilled(true);
        btnEditarPerguntasModelo.setFocusPainted(false);
        btnEditarPerguntasModelo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(195, 224, 208), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                salvarCachePreenchimentoNoDisco();

                rascunhoPersonalizado = null;
                dadosPreenchimentoPendentesColagem = null;
                if (!modoVisualizacaoAnamnese && abaAtiva == AbaBarraAnamnese.CRIAR_PERSONALIZADO && modoEdicaoQuestionario) {
                    formularioAtual = criarRascunhoPadraoPersonalizado();
                    txtTituloAnamnese.setText(formularioAtual.getTitulo());
                    txtDataAbertura.setText(LocalDate.now().format(FORMATO_DATA));
                    editoresPerguntas.clear();
                    renderizarConstrutorGoogleForms();
                }
            }
        });

        configurarEstiloDropdown();
        configurarEstiloDropdownDesativadas();
        carregarDropdownFormularios(null);
        carregarEstadoInicialPaciente();
    }

    private void carregarEstadoInicialPaciente() {
        RespostaAnamnese ativa = obterRespostaAtivaPaciente();
        if (ativa != null) {
            abrirVisualizacaoResposta(ativa);
        } else {
            modoVisualizacaoAnamnese = false;
            respostaVisualizada = null;
            atualizarDropdownDesativadas(null);
            selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        }
    }

    private RespostaAnamnese obterRespostaAtivaPaciente() {
        List<RespostaAnamnese> todas = anamneseDAO.listarRespostasPorPaciente(paciente);
        RespostaAnamnese ultimaAtiva = null;
        for (RespostaAnamnese r : todas) {
            if (r != null && r.isAtiva()) {
                ultimaAtiva = r;
            }
        }
        return ultimaAtiva;
    }

    private List<RespostaAnamnese> listarRespostasDesativadasPaciente() {
        List<RespostaAnamnese> todas = anamneseDAO.listarRespostasPorPaciente(paciente);
        List<RespostaAnamnese> desativadas = new ArrayList<>();
        for (int i = todas.size() - 1; i >= 0; i--) {
            RespostaAnamnese r = todas.get(i);
            if (r != null && !r.isAtiva()) {
                desativadas.add(r);
            }
        }
        return desativadas;
    }

    private void configurarEstiloDropdownDesativadas() {
        cmbAnamnesesDesativadas.setOpaque(true);
        cmbAnamnesesDesativadas.setFocusable(false);
        cmbAnamnesesDesativadas.setBackground(CINZA_DROPDOWN_DESATIVADAS);
        cmbAnamnesesDesativadas.setForeground(new Color(115, 122, 130));
        cmbAnamnesesDesativadas.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true));

        cmbAnamnesesDesativadas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (index == -1) {
                    lbl.setBackground(CINZA_DROPDOWN_DESATIVADAS);
                    lbl.setForeground(new Color(120, 126, 134));
                } else {
                    if (isSelected) {
                        lbl.setBackground(VERDE_BOTAO_ATIVO);
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setBackground(Color.WHITE);
                        lbl.setForeground(new Color(45, 52, 58));
                    }
                }
                return lbl;
            }
        });

        inicializarPopupPesquisaDesativadas();

        cmbAnamnesesDesativadas.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    cmbAnamnesesDesativadas.hidePopup();
                    mostrarPopupPesquisaDesativadas();
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private void inicializarPopupPesquisaDesativadas() {
        popupBuscaDesativadas = new JPopupMenu();
        popupBuscaDesativadas.setBackground(Color.WHITE);
        popupBuscaDesativadas.setBorder(BorderFactory.createLineBorder(new Color(195, 205, 212), 1));

        JPanel painelPopup = new JPanel(new BorderLayout(0, 4));
        painelPopup.setBackground(Color.WHITE);
        painelPopup.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel barraPesquisa = new JPanel(new BorderLayout(6, 0));
        barraPesquisa.setBackground(new Color(246, 248, 250));
        barraPesquisa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 222), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
        lblLupa.setForeground(new Color(100, 110, 120));

        txtBuscaDesativadas = new JTextField();
        txtBuscaDesativadas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscaDesativadas.setForeground(new Color(30, 35, 40));
        txtBuscaDesativadas.setOpaque(false);
        txtBuscaDesativadas.setBorder(null);
        txtBuscaDesativadas.setToolTipText("Pesquisar pelo nome ou data da anamnese (ex.: Adulto, 09/11/2026)");

        JLabel lblDicaBusca = new JLabel("Pesquisar por nome ou data...");
        lblDicaBusca.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblDicaBusca.setForeground(new Color(140, 148, 156));

        barraPesquisa.add(lblLupa, BorderLayout.WEST);
        barraPesquisa.add(txtBuscaDesativadas, BorderLayout.CENTER);
        barraPesquisa.add(lblDicaBusca, BorderLayout.EAST);

        txtBuscaDesativadas.getDocument().addDocumentListener(new DocumentListener() {
            private void atualizar() {
                String texto = txtBuscaDesativadas.getText();
                lblDicaBusca.setVisible(texto == null || texto.isEmpty());
                filtrarListaPopupDesativadas(texto);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizar();
            }
        });

        txtBuscaDesativadas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int prox = Math.min(modeloListaDesativadas.getSize() - 1, listaBuscaDesativadas.getSelectedIndex() + 1);
                    if (prox >= 0) {
                        listaBuscaDesativadas.setSelectedIndex(prox);
                        listaBuscaDesativadas.ensureIndexIsVisible(prox);
                    }
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int ant = Math.max(0, listaBuscaDesativadas.getSelectedIndex() - 1);
                    if (ant >= 0) {
                        listaBuscaDesativadas.setSelectedIndex(ant);
                        listaBuscaDesativadas.ensureIndexIsVisible(ant);
                    }
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ItemAnamneseDesativada sel = listaBuscaDesativadas.getSelectedValue();
                    if (sel != null && sel.getResposta() != null) {
                        popupBuscaDesativadas.setVisible(false);
                        selecionarItemAnamneseDesativada(sel);
                    }
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    popupBuscaDesativadas.setVisible(false);
                    e.consume();
                }
            }
        });

        modeloListaDesativadas = new DefaultListModel<>();
        listaBuscaDesativadas = new JList<>(modeloListaDesativadas);
        listaBuscaDesativadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaBuscaDesativadas.setBackground(Color.WHITE);
        listaBuscaDesativadas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        listaBuscaDesativadas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(8, 12, 8, 12));
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                boolean destacado = isSelected || (index == indiceHoverListaDesativadas);
                if (value instanceof ItemAnamneseDesativada && ((ItemAnamneseDesativada) value).getResposta() == null) {
                    lbl.setBackground(Color.WHITE);
                    lbl.setForeground(new Color(120, 128, 136));
                    lbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                } else if (destacado) {
                    lbl.setBackground(VERDE_BOTAO_ATIVO);
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(Color.WHITE);
                    lbl.setForeground(new Color(30, 36, 42));
                }
                return lbl;
            }
        });

        listaBuscaDesativadas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int idx = listaBuscaDesativadas.locationToIndex(e.getPoint());
                if (idx != indiceHoverListaDesativadas) {
                    indiceHoverListaDesativadas = idx;
                    listaBuscaDesativadas.repaint();
                }
            }
        });

        listaBuscaDesativadas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                indiceHoverListaDesativadas = -1;
                listaBuscaDesativadas.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = listaBuscaDesativadas.locationToIndex(e.getPoint());
                if (idx >= 0) {
                    ItemAnamneseDesativada item = modeloListaDesativadas.getElementAt(idx);
                    if (item != null && item.getResposta() != null) {
                        popupBuscaDesativadas.setVisible(false);
                        selecionarItemAnamneseDesativada(item);
                    }
                }
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaBuscaDesativadas);
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        ComponenteUtil.formatarScrollInvisivel(scrollLista);

        painelPopup.add(barraPesquisa, BorderLayout.NORTH);
        painelPopup.add(scrollLista, BorderLayout.CENTER);
        painelPopup.setPreferredSize(new Dimension(520, 310));

        popupBuscaDesativadas.add(painelPopup);
    }

    private void mostrarPopupPesquisaDesativadas() {
        List<RespostaAnamnese> desativadas = listarRespostasDesativadasPaciente();
        if (desativadas.isEmpty()) {
            return;
        }
        indiceHoverListaDesativadas = -1;
        txtBuscaDesativadas.setText("");
        filtrarListaPopupDesativadas("");

        int larguraPopup = Math.max(520, cmbAnamnesesDesativadas.getWidth());
        Component conteudo = popupBuscaDesativadas.getComponent(0);
        conteudo.setPreferredSize(new Dimension(larguraPopup, 310));
        popupBuscaDesativadas.pack();

        int xOffset = Math.min(0, cmbAnamnesesDesativadas.getWidth() - larguraPopup);
        popupBuscaDesativadas.show(cmbAnamnesesDesativadas, xOffset, cmbAnamnesesDesativadas.getHeight() + 2);
        SwingUtilities.invokeLater(() -> txtBuscaDesativadas.requestFocusInWindow());
    }

    private void filtrarListaPopupDesativadas(String termo) {
        modeloListaDesativadas.clear();
        List<RespostaAnamnese> desativadas = listarRespostasDesativadasPaciente();
        String filtro = termo != null ? termo.trim().toLowerCase() : "";

        for (RespostaAnamnese d : desativadas) {
            ItemAnamneseDesativada item = new ItemAnamneseDesativada(d);
            String textoItem = item.toString().toLowerCase();
            String dataAb = d.getDataAbertura() != null ? d.getDataAbertura().format(FORMATO_DATA).toLowerCase() : "";
            if (filtro.isEmpty() || textoItem.contains(filtro) || dataAb.contains(filtro)) {
                modeloListaDesativadas.addElement(item);
            }
        }

        if (modeloListaDesativadas.isEmpty()) {
            modeloListaDesativadas.addElement(new ItemAnamneseDesativada(null) {
                @Override
                public String toString() {
                    return "Nenhuma anamnese encontrada para \"" + (termo != null ? termo.trim() : "") + "\"";
                }
            });
        } else {
            listaBuscaDesativadas.setSelectedIndex(0);
        }
    }

    private void selecionarItemAnamneseDesativada(ItemAnamneseDesativada item) {
        if (item == null || item.getResposta() == null) {
            return;
        }
        if (!modoVisualizacaoAnamnese) {
            salvarRascunhoSeEstiverCriandoPersonalizado();
            salvarCachePreenchimentoNoDisco();
            this.desativadaAbertaAPartirDeModelos = true;
            this.abaAnteriorModelos = this.abaAtiva;
            this.formularioAnteriorModelos = this.formularioAtual;
            this.estavaEditandoQuestionarioModelos = this.modoEdicaoQuestionario;
        }
        abrirVisualizacaoResposta(item.getResposta());
    }

    private void atualizarDropdownDesativadas(RespostaAnamnese selecionada) {
        atualizandoComboDesativadas = true;
        try {
            List<RespostaAnamnese> desativadas = listarRespostasDesativadasPaciente();

            DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
            model.addElement("Anamneses Desativadas");

            int idxSelecionar = 0;
            for (RespostaAnamnese d : desativadas) {
                ItemAnamneseDesativada item = new ItemAnamneseDesativada(d);
                model.addElement(item);
                if (selecionada != null && !selecionada.isAtiva() && d.getId() != null && d.getId().equals(selecionada.getId())) {
                    idxSelecionar = model.getSize() - 1;
                }
            }

            cmbAnamnesesDesativadas.setModel(model);
            cmbAnamnesesDesativadas.setSelectedIndex(idxSelecionar);

            // O dropdown "Anamneses Desativadas" só será visível caso tenham anamneses desativadas
            cmbAnamnesesDesativadas.setVisible(!desativadas.isEmpty());
        } finally {
            atualizandoComboDesativadas = false;
        }
    }

    private void configurarBotaoLimparVassoura() {
        btnLimparPreenchimento.setOpaque(true);
        btnLimparPreenchimento.setContentAreaFilled(true);
        btnLimparPreenchimento.setFocusPainted(false);
        btnLimparPreenchimento.setBorder(BorderFactory.createLineBorder(new Color(235, 205, 205), 1, true));
        java.net.URL urlLimpar = getClass().getResource("/imagens/limpar.png");
        if (urlLimpar != null) {
            ImageIcon iconeOrig = new ImageIcon(urlLimpar);
            Image imgEscalada = iconeOrig.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            btnLimparPreenchimento.setIcon(new ImageIcon(imgEscalada));
            btnLimparPreenchimento.setText("");
        }
    }

    private void aoAlterarDataAbertura() {
        if (carregandoPreenchimentoUI || modoEdicaoQuestionario || modoVisualizacaoAnamnese || formularioAtual == null) {
            return;
        }
        String prefixo = obterPrefixoRascunho(formularioAtual);
        salvarValorRascunho(prefixo, "DATA_ABERTURA", txtDataAbertura.getText());
    }

    private String obterChavePaciente() {
        return AnamneseJPA.gerarChavePaciente(paciente);
    }

    private String obterChaveFormulario(FormularioAnamnese form) {
        if (form == null) {
            return "FORM_NULO";
        }
        if (form.getTipoModelo() == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
            return "MOD_INFANTIL_ADOLESCENTE";
        }
        if (form.getTipoModelo() == TipoModeloAnamnese.ADULTO) {
            return "MOD_ADULTO";
        }
        if (form.getId() != null) {
            return "MOD_PERS_ID_" + form.getId();
        }
        return "MOD_PERS_TIT_" + normalizarChave(form.getTitulo());
    }

    private String obterPrefixoRascunho(FormularioAnamnese form) {
        return obterChavePaciente() + "__" + obterChaveFormulario(form) + "__";
    }

    private String normalizarChave(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toLowerCase().replaceAll("[^a-z0-9à-ú]+", "_");
    }

    private String obterChavePergunta(PerguntaAnamnese p, int ocorrencia, int idxFallback) {
        TipoPerguntaAnamnese tipo = p.getTipoPergunta() != null ? p.getTipoPergunta() : TipoPerguntaAnamnese.TEXTO_LIVRE;
        return "Q_" + tipo.name() + "_" + normalizarChave(p.getEnunciado()) + "_OC" + ocorrencia;
    }

    private void salvarValorRascunho(String prefixo, String subChave, String valor) {
        if (prefixo == null || subChave == null) {
            return;
        }
        String chaveCompleta = prefixo + subChave;
        if (valor == null || valor.isEmpty()) {
            CACHE_PREENCHIMENTO.remove(chaveCompleta);
        } else {
            CACHE_PREENCHIMENTO.put(chaveCompleta, valor);
        }
        if (timerPersistenciaRascunho != null) {
            timerPersistenciaRascunho.restart();
        } else {
            salvarCachePreenchimentoNoDisco();
        }
    }

    private String obterValorRascunho(String prefixo, String subChave, String subChaveFallbackIdx) {
        if (prefixo == null) {
            return null;
        }
        if (subChave != null) {
            String val = CACHE_PREENCHIMENTO.get(prefixo + subChave);
            if (val != null) {
                return val;
            }
        }
        if (subChaveFallbackIdx != null) {
            return CACHE_PREENCHIMENTO.get(prefixo + subChaveFallbackIdx);
        }
        return null;
    }

    private String obterValorMapaCampos(Map<String, String> mapa, String subChave, String subChaveFallbackIdx) {
        if (mapa == null) {
            return null;
        }
        if (subChave != null) {
            String val = mapa.get(subChave);
            if (val != null) {
                return val;
            }
        }
        if (subChaveFallbackIdx != null) {
            return mapa.get(subChaveFallbackIdx);
        }
        return null;
    }

    private String serializarDadosCamposAtuais(FormularioAnamnese form, LocalDate dataAbertura) {
        Properties props = new Properties();
        if (dataAbertura != null) {
            props.setProperty("DATA_ABERTURA", dataAbertura.format(FORMATO_DATA));
        }
        if (form != null) {
            String prefixo = obterPrefixoRascunho(form);
            for (Map.Entry<String, String> entry : CACHE_PREENCHIMENTO.entrySet()) {
                if (entry.getKey().startsWith(prefixo)) {
                    String subChave = entry.getKey().substring(prefixo.length());
                    props.setProperty(subChave, entry.getValue());
                }
            }
        }
        try (StringWriter writer = new StringWriter()) {
            props.store(writer, null);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Map<String, String> desserializarDadosCampos(RespostaAnamnese resposta) {
        Map<String, String> mapa = new HashMap<>();
        if (resposta == null) {
            return mapa;
        }
        if (resposta.getDataAbertura() != null) {
            mapa.put("DATA_ABERTURA", resposta.getDataAbertura().format(FORMATO_DATA));
        }
        String raw = resposta.getDadosCampos();
        if (raw != null && !raw.isBlank()) {
            Properties props = new Properties();
            try (StringReader reader = new StringReader(raw)) {
                props.load(reader);
                for (String key : props.stringPropertyNames()) {
                    mapa.put(key, props.getProperty(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mapa;
    }

    private void copiarDadosRespostaParaRascunho(RespostaAnamnese resposta, FormularioAnamnese formDestino) {
        if (resposta == null || formDestino == null) {
            return;
        }
        limparRascunhoPreenchimentoFormulario(formDestino);
        String prefixoDestino = obterPrefixoRascunho(formDestino);
        Map<String, String> dados = desserializarDadosCampos(resposta);
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                CACHE_PREENCHIMENTO.put(prefixoDestino + entry.getKey(), entry.getValue());
            }
        }
        if (resposta.getDataAbertura() != null) {
            CACHE_PREENCHIMENTO.put(prefixoDestino + "DATA_ABERTURA", resposta.getDataAbertura().format(FORMATO_DATA));
        }
        salvarCachePreenchimentoNoDisco();
    }

    private void aplicarDadosMapaNoRascunho(Map<String, String> dados, FormularioAnamnese formDestino) {
        if (dados == null || dados.isEmpty() || formDestino == null) {
            return;
        }
        String prefixoDestino = obterPrefixoRascunho(formDestino);
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                CACHE_PREENCHIMENTO.put(prefixoDestino + entry.getKey(), entry.getValue());
            }
        }
        salvarCachePreenchimentoNoDisco();
    }

    private void copiarRascunhoEntreFormularios(String chaveFormOrigem, String chaveFormDestino) {
        if (chaveFormOrigem == null || chaveFormDestino == null || chaveFormOrigem.equals(chaveFormDestino)) {
            return;
        }
        String prefOrigem = obterChavePaciente() + "__" + chaveFormOrigem + "__";
        String prefDestino = obterChavePaciente() + "__" + chaveFormDestino + "__";

        Map<String, String> novos = new HashMap<>();
        for (Map.Entry<String, String> entry : CACHE_PREENCHIMENTO.entrySet()) {
            if (entry.getKey().startsWith(prefOrigem)) {
                String sufixo = entry.getKey().substring(prefOrigem.length());
                novos.put(prefDestino + sufixo, entry.getValue());
            }
        }
        if (!novos.isEmpty()) {
            CACHE_PREENCHIMENTO.putAll(novos);
            salvarCachePreenchimentoNoDisco();
        }
    }

    private void limparRascunhoPreenchimentoFormulario(FormularioAnamnese form) {
        if (form == null) {
            return;
        }
        String prefixo = obterPrefixoRascunho(form);
        List<String> chavesParaRemover = new ArrayList<>();
        for (String k : CACHE_PREENCHIMENTO.keySet()) {
            if (k.startsWith(prefixo)) {
                chavesParaRemover.add(k);
            }
        }
        for (String k : chavesParaRemover) {
            CACHE_PREENCHIMENTO.remove(k);
        }
        respostasPreenchimento.clear();
        salvarCachePreenchimentoNoDisco();
    }

    private void estilizarBotaoArredondado(JButton botao) {
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
    }

    private LocalDate obterDataAberturaValida() {
        String texto = txtDataAbertura.getText();
        if (texto == null || texto.isBlank()) {
            return null;
        }
        String limpo = texto.trim();
        try {
            return LocalDate.parse(limpo, FORMATO_DATA);
        } catch (DateTimeParseException ex) {
            try {
                return LocalDate.parse(limpo);
            } catch (DateTimeParseException ex2) {
                return null;
            }
        }
    }

    private void configurarEstiloDropdown() {
        cmbPersonalizados.setOpaque(true);
        cmbPersonalizados.setFocusable(false);

        cmbPersonalizados.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

                if (index == -1) {
                    if (dropdownSelecionado) {
                        lbl.setBackground(VERDE_BOTAO_ATIVO);
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setBackground(CINZA_BOTAO_INATIVO);
                        lbl.setForeground(TEXTO_BOTAO_INATIVO);
                    }
                } else {
                    if (isSelected) {
                        lbl.setBackground(VERDE_BOTAO_ATIVO);
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setBackground(Color.WHITE);
                        lbl.setForeground(new Color(40, 45, 50));
                    }
                }
                return lbl;
            }
        });

        cmbPersonalizados.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (modoVisualizacaoAnamnese) {
                    return;
                }
                salvarRascunhoSeEstiverCriandoPersonalizado();
                atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        cmbPersonalizados.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (modoVisualizacaoAnamnese) {
                    return;
                }
                salvarRascunhoSeEstiverCriandoPersonalizado();
                atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
            }
        });
    }

    private void salvarRascunhoSeEstiverCriandoPersonalizado() {
        if (!modoVisualizacaoAnamnese && modoEdicaoQuestionario && formularioAtual != null && formularioAtual.getId() == null) {
            sincronizarEdicaoAtualNoFormulario();
            rascunhoPersonalizado = copiarFormularioComoRascunho(formularioAtual, formularioAtual.getTitulo());
        }
    }

    private FormularioAnamnese criarRascunhoPadraoPersonalizado() {
        FormularioAnamnese novo = new FormularioAnamnese();
        novo.setId(null);
        novo.setTitulo("Titulo");
        novo.setDataAbertura(LocalDate.now());
        novo.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
        novo.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

        novo.adicionarPergunta(new PerguntaAnamnese(
                "1 - Identificação",
                1,
                TipoPerguntaAnamnese.SECAO_TITULO,
                Collections.emptyList()
        ));
        novo.adicionarPergunta(new PerguntaAnamnese(
                "Pergunta",
                2,
                TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                new ArrayList<>(Arrays.asList("Opção 1", "Opção 2"))
        ));
        novo.adicionarPergunta(new PerguntaAnamnese(
                "Pergunta com complemento",
                3,
                TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                new ArrayList<>(Arrays.asList("sim", "não", PREFIXO_COMPLEMENTO + "Qual?"))
        ));
        return novo;
    }

    private FormularioAnamnese copiarFormularioComoRascunho(FormularioAnamnese origem, String tituloPersonalizado) {
        FormularioAnamnese copia = new FormularioAnamnese();
        copia.setId(null);
        copia.setTitulo(tituloPersonalizado != null && !tituloPersonalizado.isBlank() ? tituloPersonalizado : "Titulo");
        copia.setDataAbertura(origem != null && origem.getDataAbertura() != null ? origem.getDataAbertura() : LocalDate.now());
        copia.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
        copia.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

        if (origem != null && origem.getPerguntas() != null) {
            int ordem = 1;
            for (PerguntaAnamnese p : origem.getPerguntas()) {
                List<String> opcoesCopia = p.getOpcoes() != null
                        ? new ArrayList<>(p.getOpcoes())
                        : new ArrayList<>();
                copia.adicionarPergunta(new PerguntaAnamnese(
                        p.getEnunciado(),
                        ordem++,
                        p.getTipoPergunta(),
                        opcoesCopia
                ));
            }
        }
        return copia;
    }

    private Icon criarIconeFecharX(Color cor) {
        int w = 20;
        int h = 20;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(3, 3, 17, 17);
        g2.drawLine(17, 3, 3, 17);
        g2.dispose();
        return new ImageIcon(img);
    }

    private Icon criarIconeMais(Color cor) {
        int w = 16;
        int h = 16;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(8, 2, 8, 14);
        g2.drawLine(2, 8, 14, 8);
        g2.dispose();
        return new ImageIcon(img);
    }

    private Icon criarIconeSliders(Color cor) {
        int w = 18;
        int h = 18;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int[] ys = {4, 9, 14};
        int[] xs = {6, 12, 8};
        for (int i = 0; i < 3; i++) {
            g2.drawLine(2, ys[i], 16, ys[i]);
            g2.fillOval(xs[i] - 2, ys[i] - 2, 5, 5);
        }
        g2.dispose();
        return new ImageIcon(img);
    }

    private Icon criarIconeExpandir(Color cor) {
        int w = 18;
        int h = 18;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.drawLine(3, 7, 3, 3);
        g2.drawLine(3, 3, 7, 3);
        g2.drawLine(3, 3, 7, 7);

        g2.drawLine(15, 7, 15, 3);
        g2.drawLine(15, 3, 11, 3);
        g2.drawLine(15, 3, 11, 7);

        g2.drawLine(3, 11, 3, 15);
        g2.drawLine(3, 15, 7, 15);
        g2.drawLine(3, 15, 7, 11);

        g2.drawLine(15, 11, 15, 15);
        g2.drawLine(15, 15, 11, 15);
        g2.drawLine(15, 15, 11, 11);

        g2.dispose();
        return new ImageIcon(img);
    }

    private Icon criarIconeMinimizar(Color cor) {
        int w = 18;
        int h = 18;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Setas apontando para o centro (minimizar / sair da tela cheia)
        g2.drawLine(2, 2, 7, 7);
        g2.drawLine(7, 3, 7, 7);
        g2.drawLine(3, 7, 7, 7);

        g2.drawLine(16, 2, 11, 7);
        g2.drawLine(11, 3, 11, 7);
        g2.drawLine(15, 7, 11, 7);

        g2.drawLine(2, 16, 7, 11);
        g2.drawLine(7, 15, 7, 11);
        g2.drawLine(3, 11, 7, 11);

        g2.drawLine(16, 16, 11, 11);
        g2.drawLine(11, 15, 11, 11);
        g2.drawLine(15, 11, 11, 11);

        g2.dispose();
        return new ImageIcon(img);
    }

    private void atualizarEstadoBarra(AbaBarraAnamnese aba) {
        this.abaAtiva = aba;
        this.dropdownSelecionado = (aba == AbaBarraAnamnese.DROPDOWN_SELECIONAR);

        if (modoVisualizacaoAnamnese) {
            lblTituloModelos.setText("Anamnese Visualização");
            btnInfantilAdolescente.setVisible(false);
            btnAdulto.setVisible(false);
            cmbPersonalizados.setVisible(false);
            btnCriarPersonalizado.setVisible(false);
            // Quando uma anamnese desativada for aberta a partir da tela de anamnese modelos,
            // não mostrar o botão de "Criar Nova Anamnese" da anamnese visualização
            btnCriarNovaAnamnese.setVisible(!desativadaAbertaAPartirDeModelos);
            atualizarDropdownDesativadas(respostaVisualizada);
            container1.revalidate();
            container1.repaint();
            return;
        }

        lblTituloModelos.setText("Anamnese modelos");
        btnCriarNovaAnamnese.setVisible(false);
        btnFecharVisualizacaoDesativada.setVisible(false);
        atualizarDropdownDesativadas(null);

        btnInfantilAdolescente.setVisible(true);
        btnAdulto.setVisible(true);
        cmbPersonalizados.setVisible(true);
        btnCriarPersonalizado.setVisible(true);

        aplicarCorBotaoModelo(
                btnInfantilAdolescente,
                aba == AbaBarraAnamnese.INFANTIL_ADOLESCENTE,
                "/imagens/MenuPacienteIcons/paciente_white.png",
                "/imagens/MenuPacienteIcons/paciente_green.png"
        );
        aplicarCorBotaoModelo(
                btnAdulto,
                aba == AbaBarraAnamnese.ADULTO,
                "/imagens/MenuPacienteIcons/paciente_white.png",
                "/imagens/MenuPacienteIcons/paciente_green.png"
        );

        if (aba == AbaBarraAnamnese.CRIAR_PERSONALIZADO) {
            btnCriarPersonalizado.setBackground(VERDE_BOTAO_ATIVO);
            btnCriarPersonalizado.setForeground(Color.WHITE);
            btnCriarPersonalizado.setIcon(iconeSlidersBranco);
        } else {
            btnCriarPersonalizado.setBackground(CINZA_BOTAO_INATIVO);
            btnCriarPersonalizado.setForeground(TEXTO_BOTAO_INATIVO);
            btnCriarPersonalizado.setIcon(iconeSlidersEscuro);
        }

        aplicarEstiloComboSelecionado(this.dropdownSelecionado);

        container1.revalidate();
        container1.repaint();
    }

    private void aplicarEstiloComboSelecionado(boolean ativo) {
        Color corFundo = ativo ? VERDE_BOTAO_ATIVO : CINZA_BOTAO_INATIVO;
        Color corTexto = ativo ? Color.WHITE : TEXTO_BOTAO_INATIVO;

        cmbPersonalizados.setBackground(corFundo);
        cmbPersonalizados.setForeground(corTexto);
        cmbPersonalizados.setBorder(BorderFactory.createLineBorder(
                ativo ? VERDE_BOTAO_ATIVO : new Color(218, 222, 228),
                1,
                true
        ));

        for (Component comp : cmbPersonalizados.getComponents()) {
            comp.setBackground(corFundo);
            comp.setForeground(corTexto);
        }

        cmbPersonalizados.repaint();
    }

    private void aplicarCorBotaoModelo(JButton botao, boolean ativo, String iconeAtivo, String iconeInativo) {
        if (ativo) {
            botao.setBackground(VERDE_BOTAO_ATIVO);
            botao.setForeground(Color.WHITE);
            java.net.URL url = getClass().getResource(iconeAtivo);
            if (url != null) {
                botao.setIcon(new ImageIcon(url));
            }
        } else {
            botao.setBackground(CINZA_BOTAO_INATIVO);
            botao.setForeground(TEXTO_BOTAO_INATIVO);
            java.net.URL url = getClass().getResource(iconeInativo);
            if (url != null) {
                botao.setIcon(new ImageIcon(url));
            }
        }
    }

    private void carregarDropdownFormularios(FormularioAnamnese selecionarAoFinal) {
        atualizandoCombo = true;
        try {
            DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
            List<FormularioAnamnese> personalizados = anamneseDAO.listarFormulariosPorTipo(TipoModeloAnamnese.PERSONALIZADO);
            List<FormularioAnamnese> todos = anamneseDAO.listarTodosFormularios();

            List<FormularioAnamnese> itensDropdown = new ArrayList<>(personalizados);
            for (FormularioAnamnese f : todos) {
                if (f.getTipoModelo() != TipoModeloAnamnese.PERSONALIZADO
                        && f.getTitulo() != null
                        && !ehModeloPadraoNaoSalvo(f)
                        && !contemFormulario(itensDropdown, f)) {
                    itensDropdown.add(f);
                }
            }

            model.addElement("Selecionar Anamnese...");
            for (FormularioAnamnese form : itensDropdown) {
                model.addElement(form);
            }

            cmbPersonalizados.setModel(model);

            if (selecionarAoFinal != null) {
                for (int i = 0; i < model.getSize(); i++) {
                    Object elem = model.getElementAt(i);
                    if (elem instanceof FormularioAnamnese) {
                        FormularioAnamnese f = (FormularioAnamnese) elem;
                        if ((f.getId() != null && f.getId().equals(selecionarAoFinal.getId()))
                                || f.getTitulo().equalsIgnoreCase(selecionarAoFinal.getTitulo())) {
                            cmbPersonalizados.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            } else {
                cmbPersonalizados.setSelectedIndex(0);
            }
        } finally {
            atualizandoCombo = false;
        }
    }

    private boolean ehModeloPadraoNaoSalvo(FormularioAnamnese f) {
        return (f.getId() != null && (f.getId() == 1L || f.getId() == 2L))
                && (f.getTipoModelo() == TipoModeloAnamnese.INFANTIL_ADOLESCENTE
                || f.getTipoModelo() == TipoModeloAnamnese.ADULTO);
    }

    private boolean contemFormulario(List<FormularioAnamnese> lista, FormularioAnamnese alvo) {
        for (FormularioAnamnese f : lista) {
            if (f.getId() != null && f.getId().equals(alvo.getId())) {
                return true;
            }
        }
        return false;
    }

    private void selecionarModeloPadrao(TipoModeloAnamnese tipo) {
        salvarRascunhoSeEstiverCriandoPersonalizado();
        salvarCachePreenchimentoNoDisco();

        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;
        this.modoAtual = tipo;
        this.modoEdicaoQuestionario = false;

        atualizandoCombo = true;
        try {
            if (cmbPersonalizados.getItemCount() > 0) {
                cmbPersonalizados.setSelectedIndex(0);
            }
        } finally {
            atualizandoCombo = false;
        }

        atualizarEstadoBarra(tipo == TipoModeloAnamnese.INFANTIL_ADOLESCENTE
                ? AbaBarraAnamnese.INFANTIL_ADOLESCENTE
                : AbaBarraAnamnese.ADULTO);

        formularioAtual = anamneseDAO.buscarModeloPadrao(tipo);
        abrirPreenchimentoFormulario(formularioAtual, false);
    }

    private void iniciarModoCriarPersonalizado() {
        salvarRascunhoSeEstiverCriandoPersonalizado();
        salvarCachePreenchimentoNoDisco();

        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;
        this.chaveFormularioOrigemEdicao = null;
        this.dadosPreenchimentoPendentesColagem = null;
        this.modoAtual = TipoModeloAnamnese.PERSONALIZADO;
        this.modoEdicaoQuestionario = true;

        atualizandoCombo = true;
        try {
            if (cmbPersonalizados.getItemCount() > 0) {
                cmbPersonalizados.setSelectedIndex(0);
            }
        } finally {
            atualizandoCombo = false;
        }

        atualizarEstadoBarra(AbaBarraAnamnese.CRIAR_PERSONALIZADO);

        if (rascunhoPersonalizado == null) {
            rascunhoPersonalizado = criarRascunhoPadraoPersonalizado();
        }

        iniciarConstrutorComFormulario(rascunhoPersonalizado);
    }

    private void iniciarConstrutorAPartirDeModeloPadrao(FormularioAnamnese modeloPadrao) {
        salvarCachePreenchimentoNoDisco();

        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;
        this.chaveFormularioOrigemEdicao = obterChaveFormulario(modeloPadrao);
        this.dadosPreenchimentoPendentesColagem = null;
        this.modoAtual = TipoModeloAnamnese.PERSONALIZADO;
        this.modoEdicaoQuestionario = true;

        atualizandoCombo = true;
        try {
            if (cmbPersonalizados.getItemCount() > 0) {
                cmbPersonalizados.setSelectedIndex(0);
            }
        } finally {
            atualizandoCombo = false;
        }

        atualizarEstadoBarra(AbaBarraAnamnese.CRIAR_PERSONALIZADO);

        String tituloSugerido = (modeloPadrao != null && modeloPadrao.getTitulo() != null)
                ? modeloPadrao.getTitulo() + " (Personalizado)"
                : "Titulo";

        rascunhoPersonalizado = copiarFormularioComoRascunho(modeloPadrao, tituloSugerido);
        iniciarConstrutorComFormulario(rascunhoPersonalizado);
    }

    private void iniciarConstrutorComFormulario(FormularioAnamnese base) {
        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;
        this.modoEdicaoQuestionario = true;
        this.editoresPerguntas.clear();
        // Inicia com todas as perguntas em Pré-visualização (estilo Microsoft Forms),
        // ou se tiver poucas perguntas recém-criadas, permite clicar em qualquer uma para editar
        this.indicePerguntaSelecionadaConstrutor = -1;

        if (base != null && base.getId() != null) {
            this.chaveFormularioOrigemEdicao = obterChaveFormulario(base);
        }

        this.formularioAtual = new FormularioAnamnese();
        if (base != null) {
            this.formularioAtual.setId(base.getId());
            this.formularioAtual.setTitulo(base.getTitulo() != null ? base.getTitulo() : "Titulo");
            this.formularioAtual.setDataAbertura(base.getDataAbertura() != null ? base.getDataAbertura() : LocalDate.now());
            this.formularioAtual.setTipoModelo(base.getTipoModelo() != null ? base.getTipoModelo() : TipoModeloAnamnese.PERSONALIZADO);
            this.formularioAtual.setStatusFormulario(base.getStatusFormulario() != null ? base.getStatusFormulario() : StatusPacienteAgendamentoDocumento.ATIVO);
        } else {
            this.formularioAtual.setTitulo("Titulo");
            this.formularioAtual.setDataAbertura(LocalDate.now());
            this.formularioAtual.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
            this.formularioAtual.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);
        }

        if (base != null && base.getPerguntas() != null) {
            int ordem = 1;
            for (PerguntaAnamnese p : base.getPerguntas()) {
                List<String> opcoesCopia = p.getOpcoes() != null
                        ? new ArrayList<>(p.getOpcoes())
                        : new ArrayList<>();
                this.formularioAtual.adicionarPergunta(new PerguntaAnamnese(
                        p.getEnunciado(),
                        ordem++,
                        p.getTipoPergunta(),
                        opcoesCopia
                ));
            }
        }

        carregandoPreenchimentoUI = true;
        try {
            txtTituloAnamnese.setText(this.formularioAtual.getTitulo());
            txtTituloAnamnese.setEditable(true);
            txtTituloAnamnese.setOpaque(true);
            txtTituloAnamnese.setBackground(Color.WHITE);
            txtTituloAnamnese.setBorder(null);
            txtDataAbertura.setEditable(true);
            txtDataAbertura.setText(this.formularioAtual.getDataAbertura().format(FORMATO_DATA));
        } finally {
            carregandoPreenchimentoUI = false;
        }
        txtTituloAnamnese.requestFocusInWindow();

        btnFecharVisualizacaoDesativada.setVisible(false);
        btnEditarPerguntasModelo.setVisible(false);
        btnLimparPreenchimento.setVisible(false);
        btnBaixarAnamneseSalva.setVisible(false);
        btnEditarAnamneseSalva.setVisible(false);
        btnAdicionarPergunta.setVisible(true);
        btnSalvarAnamnese.setText("SALVAR");
        pnlRodapeAcoes.setVisible(true);

        renderizarConstrutorGoogleForms();
    }

    private void selecionarPerguntaConstrutor(int novoIndice) {
        if (!modoEdicaoQuestionario || formularioAtual == null) {
            return;
        }
        sincronizarEdicaoAtualNoFormulario();
        final int scrollAtual = scrPerguntas.getVerticalScrollBar().getValue();
        this.indicePerguntaSelecionadaConstrutor = novoIndice;
        editoresPerguntas.clear();
        renderizarConstrutorGoogleForms();
        SwingUtilities.invokeLater(() -> {
            scrPerguntas.getVerticalScrollBar().setValue(scrollAtual);
            if (campoEnunciadoAtivoConstrutor != null) {
                campoEnunciadoAtivoConstrutor.requestFocusInWindow();
            }
        });
    }

    private void renderizarConstrutorGoogleForms() {
        sincronizarEdicaoAtualNoFormulario();

        editoresPerguntas.clear();
        campoEnunciadoAtivoConstrutor = null;
        pnlConteudoAnamnese.removeAll();
        pnlConteudoAnamnese.setLayout(new BoxLayout(pnlConteudoAnamnese, BoxLayout.Y_AXIS));
        pnlConteudoAnamnese.setBackground(Color.WHITE);
        pnlConteudoAnamnese.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Clicar no fundo branco fora dos cards coloca todas as perguntas em Pré-visualização (como no Microsoft Forms)
        pnlConteudoAnamnese.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (modoEdicaoQuestionario && indicePerguntaSelecionadaConstrutor != -1) {
                    selecionarPerguntaConstrutor(-1);
                }
            }
        });

        List<PerguntaAnamnese> perguntas = formularioAtual.getPerguntas();
        if (indicePerguntaSelecionadaConstrutor >= perguntas.size()) {
            indicePerguntaSelecionadaConstrutor = perguntas.size() - 1;
        }

        for (int i = 0; i < perguntas.size(); i++) {
            PerguntaAnamnese pergunta = perguntas.get(i);
            pergunta.setOrdem(i + 1);
            if (i == indicePerguntaSelecionadaConstrutor) {
                JPanel cardEdicao = criarCardEditorPergunta(i, pergunta);
                pnlConteudoAnamnese.add(cardEdicao);
            } else {
                JPanel cardPreview = criarCardPreVisualizacaoConstrutor(i, pergunta);
                pnlConteudoAnamnese.add(cardPreview);
            }
            pnlConteudoAnamnese.add(Box.createVerticalStrut(10));
        }

        pnlConteudoAnamnese.add(Box.createVerticalGlue());
        pnlConteudoAnamnese.revalidate();
        pnlConteudoAnamnese.repaint();
    }

    /**
     * Estado 1 (Microsoft Forms - Pré-visualização):
     * Quando a pergunta NÃO está selecionada no construtor, ela é exibida exatamente na forma
     * que ficará na hora do preenchimento. Ao clicar nela, entra no Estado 2 (Edição).
     */
    private JPanel criarCardPreVisualizacaoConstrutor(final int indiceZeroBased, PerguntaAnamnese p) {
        TipoPerguntaAnamnese tipo = p.getTipoPergunta() != null
                ? p.getTipoPergunta()
                : TipoPerguntaAnamnese.TEXTO_LIVRE;

        if (tipo == TipoPerguntaAnamnese.SECAO_TITULO) {
            JPanel bannerSecao = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 10, 6)) {
                @Override
                public Dimension getMaximumSize() {
                    Dimension pref = getPreferredSize();
                    return new Dimension(Integer.MAX_VALUE, pref.height);
                }
            };
            bannerSecao.setBackground(new Color(236, 246, 240));
            bannerSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
            bannerSecao.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, VERDE_BOTAO_ATIVO));
            bannerSecao.setToolTipText("Clique para editar esta seção");

            JLabel lblSecao = new JLabel(p.getEnunciado());
            lblSecao.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblSecao.setForeground(new Color(13, 82, 65));
            bannerSecao.add(lblSecao);

            instalarCliqueParaEditarRecursivo(bannerSecao, indiceZeroBased, new Color(236, 246, 240), new Color(224, 240, 231));
            return bannerSecao;
        }

        JPanel bloco = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        bloco.setLayout(new BoxLayout(bloco, BoxLayout.Y_AXIS));
        bloco.setBackground(Color.WHITE);
        bloco.setOpaque(true);
        bloco.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloco.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 243, 246)),
                new EmptyBorder(8, 8, 10, 8)
        ));
        bloco.setToolTipText("Clique para editar esta pergunta");

        JPanel linhaPergunta = new JPanel(new BorderLayout());
        linhaPergunta.setOpaque(false);
        linhaPergunta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPergunta = new JLabel(formatarEnunciadoResponsivoHtml(p.getEnunciado()));
        lblPergunta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPergunta.setForeground(new Color(35, 42, 48));
        linhaPergunta.add(lblPergunta, BorderLayout.CENTER);
        bloco.add(linhaPergunta);
        bloco.add(Box.createVerticalStrut(4));

        JPanel corpo = new JPanel();
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setOpaque(false);
        corpo.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.setBorder(new EmptyBorder(0, 8, 0, 0));

        if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA) {
            JPanel linhaOpcoes = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 14, 4));
            linhaOpcoes.setOpaque(false);
            linhaOpcoes.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (String opBruta : p.getOpcoes()) {
                JRadioButton rb = new JRadioButton(limparPrefixoParentesesOpcao(opBruta));
                rb.setOpaque(false);
                rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                rb.setForeground(new Color(55, 62, 68));
                rb.setFocusable(false);
                linhaOpcoes.add(rb);
            }
            corpo.add(linhaOpcoes);

        } else if (tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
            JPanel linhaChecks = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 14, 4));
            linhaChecks.setOpaque(false);
            linhaChecks.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (String opBruta : p.getOpcoes()) {
                JCheckBox cb = new JCheckBox(limparPrefixoParentesesOpcao(opBruta));
                cb.setOpaque(false);
                cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                cb.setForeground(new Color(55, 62, 68));
                cb.setFocusable(false);
                linhaChecks.add(cb);
            }
            corpo.add(linhaChecks);

        } else if (tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
            JPanel linhaCampos = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 12, 6));
            linhaCampos.setOpaque(false);
            linhaCampos.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (String rotuloCampo : p.getOpcoes()) {
                JLabel lbl = new JLabel(rotuloCampo + ":");
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lbl.setForeground(new Color(60, 68, 75));
                JTextField txtCampo = new JTextField("", 13);
                txtCampo.setEditable(false);
                txtCampo.setFocusable(false);
                txtCampo.setBackground(Color.WHITE);
                txtCampo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(175, 185, 195)),
                        new EmptyBorder(2, 4, 2, 4)
                ));
                linhaCampos.add(lbl);
                linhaCampos.add(txtCampo);
            }
            corpo.add(linhaCampos);

        } else if (tipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
            JPanel linhaMista = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 12, 6));
            linhaMista.setOpaque(false);
            linhaMista.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (String item : p.getOpcoes()) {
                if (item.startsWith(PREFIXO_COMPLEMENTO)) {
                    String rotuloComp = item.substring(PREFIXO_COMPLEMENTO.length()).trim();
                    JLabel lblC = new JLabel(rotuloComp.endsWith(":") ? rotuloComp : (rotuloComp + ":"));
                    lblC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lblC.setForeground(new Color(60, 68, 75));
                    JTextField txtComp = new JTextField("", 14);
                    txtComp.setEditable(false);
                    txtComp.setFocusable(false);
                    txtComp.setBackground(Color.WHITE);
                    txtComp.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(175, 185, 195)),
                            new EmptyBorder(2, 4, 2, 4)
                    ));
                    linhaMista.add(lblC);
                    linhaMista.add(txtComp);
                } else if (!item.startsWith(PREFIXO_COLUNA) && !item.startsWith(PREFIXO_LINHA)) {
                    JRadioButton rb = new JRadioButton(limparPrefixoParentesesOpcao(item));
                    rb.setOpaque(false);
                    rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    rb.setForeground(new Color(55, 62, 68));
                    rb.setFocusable(false);
                    linhaMista.add(rb);
                }
            }
            corpo.add(linhaMista);

        } else if (tipo == TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE) {
            List<String> colunas = new ArrayList<>();
            List<String> linhas = new ArrayList<>();
            for (String item : p.getOpcoes()) {
                if (item.startsWith(PREFIXO_COLUNA)) {
                    colunas.add(item.substring(PREFIXO_COLUNA.length()));
                } else if (item.startsWith(PREFIXO_LINHA)) {
                    linhas.add(item.substring(PREFIXO_LINHA.length()));
                }
            }
            if (colunas.isEmpty()) {
                colunas = Arrays.asList("2ª-feira", "3ª-feira", "4ª-feira", "5ª-feira", "6ª-feira");
            }
            if (linhas.isEmpty()) {
                linhas = Arrays.asList("Manhã", "Tarde");
            }
            JPanel tabelaGrid = new JPanel(new GridLayout(linhas.size() + 1, colunas.size() + 1, 0, 0));
            tabelaGrid.setBackground(Color.WHITE);
            tabelaGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
            tabelaGrid.setMaximumSize(new Dimension(720, (linhas.size() + 1) * 34));
            tabelaGrid.add(criarCelulaCabecalhoTabela("Dia da semana"));
            for (String col : colunas) {
                tabelaGrid.add(criarCelulaCabecalhoTabela(col));
            }
            for (String lin : linhas) {
                tabelaGrid.add(criarCelulaCabecalhoTabela(lin));
                for (int c = 0; c < colunas.size(); c++) {
                    JPanel celula = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
                    celula.setBackground(Color.WHITE);
                    celula.setBorder(BorderFactory.createLineBorder(new Color(205, 212, 220)));
                    JCheckBox cb = new JCheckBox("X");
                    cb.setOpaque(false);
                    cb.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    cb.setFocusable(false);
                    celula.add(cb);
                    tabelaGrid.add(celula);
                }
            }
            corpo.add(tabelaGrid);

        } else if (tipo == TipoPerguntaAnamnese.ESTRELAS) {
            JPanel estrelas = criarPainelEstrelasInterativo(indiceZeroBased, null, null, 0, true);
            estrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
            corpo.add(estrelas);

        } else {
            JTextArea txtResp = new JTextArea("", 2, 30);
            txtResp.setEditable(false);
            txtResp.setFocusable(false);
            txtResp.setBackground(Color.WHITE);
            txtResp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtResp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 215, 220)),
                    new EmptyBorder(4, 8, 4, 8)
            ));
            JPanel wrapperArea = new JPanel(new BorderLayout());
            wrapperArea.setOpaque(false);
            wrapperArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapperArea.setBorder(new EmptyBorder(0, 0, 0, 12));
            wrapperArea.add(txtResp, BorderLayout.CENTER);
            corpo.add(wrapperArea);
        }

        bloco.add(corpo);
        instalarCliqueParaEditarRecursivo(bloco, indiceZeroBased, Color.WHITE, new Color(246, 251, 248));
        return bloco;
    }

    private void instalarCliqueParaEditarRecursivo(
            final JPanel cardRaiz,
            final int indicePergunta,
            final Color corNormal,
            final Color corHover) {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
                selecionarPerguntaConstrutor(indicePergunta);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cardRaiz.setBackground(corHover);
                cardRaiz.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), cardRaiz);
                if (!cardRaiz.contains(p)) {
                    cardRaiz.setBackground(corNormal);
                    cardRaiz.repaint();
                }
            }
        };
        adicionarMouseListenerRecursivo(cardRaiz, adapter);
    }

    private void adicionarMouseListenerRecursivo(Component comp, MouseAdapter adapter) {
        comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comp.addMouseListener(adapter);
        if (comp instanceof Container) {
            for (Component filho : ((Container) comp).getComponents()) {
                adicionarMouseListenerRecursivo(filho, adapter);
            }
        }
    }

    /**
     * Estado 2 (Microsoft Forms - Edição):
     * Quando a pergunta está selecionada no construtor, exibe o card completo de edição
     * com destaque superior verde, seletor de tipo, reordenação (↑/↓), exclusão e edição de opções.
     */
    private JPanel criarCardEditorPergunta(int indiceZeroBased, PerguntaAnamnese pergunta) {
        int numero = indiceZeroBased + 1;
        TipoPerguntaAnamnese tipo = pergunta.getTipoPergunta() != null
                ? pergunta.getTipoPergunta()
                : TipoPerguntaAnamnese.MULTIPLA_ESCOLHA;

        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(249, 252, 250));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(3, 0, 0, 0, VERDE_BOTAO_ATIVO),
                        BorderFactory.createLineBorder(new Color(185, 218, 200), 1)
                ),
                new EmptyBorder(10, 12, 12, 12)
        ));

        JPanel linhaTopo = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 6, 4));
        linhaTopo.setOpaque(false);
        linhaTopo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNumero = new JLabel(tipo == TipoPerguntaAnamnese.SECAO_TITULO ? "Seção:" : (numero + "."));
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNumero.setForeground(VERDE_BOTAO_ATIVO);

        JTextField txtEnunciado = new JTextField(pergunta.getEnunciado() != null ? pergunta.getEnunciado() : "Pergunta", 26);
        txtEnunciado.setFont(new Font("Segoe UI", tipo == TipoPerguntaAnamnese.SECAO_TITULO ? Font.BOLD : Font.PLAIN, 15));
        txtEnunciado.setForeground(new Color(30, 35, 40));
        txtEnunciado.setBackground(Color.WHITE);
        txtEnunciado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, VERDE_BOTAO_ATIVO),
                new EmptyBorder(3, 6, 3, 6)
        ));
        this.campoEnunciadoAtivoConstrutor = txtEnunciado;

        JComboBox<TipoPerguntaAnamnese> cmbTipo = new JComboBox<>(TipoPerguntaAnamnese.values());
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbTipo.setBackground(Color.WHITE);
        cmbTipo.setSelectedItem(tipo);

        JButton btnSubir = criarBotaoAdicionarItem("↑");
        btnSubir.setToolTipText("Mover pergunta para cima");
        btnSubir.setEnabled(indiceZeroBased > 0);
        btnSubir.addActionListener(e -> {
            if (indiceZeroBased > 0) {
                sincronizarEdicaoAtualNoFormulario();
                Collections.swap(formularioAtual.getPerguntas(), indiceZeroBased, indiceZeroBased - 1);
                indicePerguntaSelecionadaConstrutor = indiceZeroBased - 1;
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            }
        });

        JButton btnDescer = criarBotaoAdicionarItem("↓");
        btnDescer.setToolTipText("Mover pergunta para baixo");
        btnDescer.setEnabled(indiceZeroBased < formularioAtual.getPerguntas().size() - 1);
        btnDescer.addActionListener(e -> {
            if (indiceZeroBased < formularioAtual.getPerguntas().size() - 1) {
                sincronizarEdicaoAtualNoFormulario();
                Collections.swap(formularioAtual.getPerguntas(), indiceZeroBased, indiceZeroBased + 1);
                indicePerguntaSelecionadaConstrutor = indiceZeroBased + 1;
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            }
        });

        JButton btnConcluirItem = criarBotaoAdicionarItem("✓ Pré-visualizar");
        btnConcluirItem.setToolTipText("Concluir edição desta pergunta e voltar à pré-visualização");
        btnConcluirItem.addActionListener(e -> selecionarPerguntaConstrutor(-1));

        JButton btnRemoverPergunta = new JButton("Excluir item");
        btnRemoverPergunta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRemoverPergunta.setForeground(new Color(185, 60, 60));
        btnRemoverPergunta.setBorder(new EmptyBorder(2, 8, 2, 8));
        btnRemoverPergunta.setContentAreaFilled(false);
        btnRemoverPergunta.setFocusPainted(false);
        btnRemoverPergunta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        linhaTopo.add(lblNumero);
        linhaTopo.add(txtEnunciado);
        linhaTopo.add(Box.createHorizontalStrut(6));
        linhaTopo.add(cmbTipo);
        linhaTopo.add(btnSubir);
        linhaTopo.add(btnDescer);
        linhaTopo.add(btnConcluirItem);
        if (formularioAtual.getPerguntas().size() > 1) {
            linhaTopo.add(btnRemoverPergunta);
        }

        card.add(linhaTopo);
        card.add(Box.createVerticalStrut(6));

        JPanel painelCorpo = new JPanel();
        painelCorpo.setLayout(new BoxLayout(painelCorpo, BoxLayout.Y_AXIS));
        painelCorpo.setOpaque(false);
        painelCorpo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCorpo.setBorder(new EmptyBorder(0, 18, 0, 0));

        List<JTextField> camposOpcoes = new ArrayList<>();
        List<JTextField> camposComplementos = new ArrayList<>();
        List<JTextField> camposColunas = new ArrayList<>();
        List<JTextField> camposLinhas = new ArrayList<>();

        if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA
                || tipo == TipoPerguntaAnamnese.CAIXA_SELECAO
                || tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {

            if (pergunta.getOpcoes() == null || pergunta.getOpcoes().isEmpty()) {
                if (tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
                    pergunta.setOpcoes(new ArrayList<>(Arrays.asList("Campo 1", "Campo 2")));
                } else {
                    pergunta.setOpcoes(new ArrayList<>(Arrays.asList("Opção 1", "Opção 2")));
                }
            }

            ButtonGroup grupoPreview = new ButtonGroup();
            List<String> opcoesAtuais = pergunta.getOpcoes();

            for (int idxOp = 0; idxOp < opcoesAtuais.size(); idxOp++) {
                final int indiceOpcao = idxOp;
                JPanel linhaOpcao = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
                linhaOpcao.setOpaque(false);
                linhaOpcao.setAlignmentX(Component.LEFT_ALIGNMENT);

                if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA) {
                    JRadioButton rb = new JRadioButton();
                    rb.setOpaque(false);
                    grupoPreview.add(rb);
                    linhaOpcao.add(rb);
                } else if (tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
                    JCheckBox cb = new JCheckBox();
                    cb.setOpaque(false);
                    linhaOpcao.add(cb);
                } else {
                    JLabel lblCampo = new JLabel("Campo " + (idxOp + 1) + ":");
                    lblCampo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    lblCampo.setForeground(new Color(90, 95, 100));
                    linhaOpcao.add(lblCampo);
                }

                String valorLimpo = opcoesAtuais.get(idxOp).replaceFirst("^(" + PREFIXO_COMPLEMENTO + "|" + PREFIXO_COLUNA + "|" + PREFIXO_LINHA + ")", "");
                valorLimpo = limparPrefixoParentesesOpcao(valorLimpo);
                JTextField txtOpcao = new JTextField(valorLimpo, 20);
                txtOpcao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                txtOpcao.setForeground(new Color(65, 70, 75));
                txtOpcao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 218, 224)),
                        new EmptyBorder(2, 4, 2, 4)
                ));
                camposOpcoes.add(txtOpcao);
                linhaOpcao.add(txtOpcao);

                JButton btnRemOp = criarBotaoExcluirItem("× Excluir");
                btnRemOp.addActionListener(e -> {
                    sincronizarEdicaoAtualNoFormulario();
                    if (pergunta.getOpcoes().size() <= 1) {
                        JOptionPane.showMessageDialog(this, "O item deve possuir pelo menos uma opção/campo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    pergunta.getOpcoes().remove(indiceOpcao);
                    editoresPerguntas.clear();
                    renderizarConstrutorGoogleForms();
                });
                linhaOpcao.add(btnRemOp);
                painelCorpo.add(linhaOpcao);
            }

            JPanel linhaAddOpcao = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
            linhaAddOpcao.setOpaque(false);
            linhaAddOpcao.setAlignmentX(Component.LEFT_ALIGNMENT);

            String rotuloAdd = tipo == TipoPerguntaAnamnese.CAMPOS_LINHA ? "+ Adicionar campo na linha" : "+ Adicionar opção";
            JButton btnAddOpcao = criarBotaoAdicionarItem(rotuloAdd);
            btnAddOpcao.addActionListener(e -> {
                sincronizarEdicaoAtualNoFormulario();
                int prox = pergunta.getOpcoes().size() + 1;
                pergunta.getOpcoes().add((tipo == TipoPerguntaAnamnese.CAMPOS_LINHA ? "Campo " : "Opção ") + prox);
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            });
            linhaAddOpcao.add(btnAddOpcao);
            painelCorpo.add(linhaAddOpcao);

        } else if (tipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
            List<String> escolhas = new ArrayList<>();
            List<String> complementos = new ArrayList<>();
            if (pergunta.getOpcoes() != null) {
                for (String item : pergunta.getOpcoes()) {
                    if (item.startsWith(PREFIXO_COMPLEMENTO)) {
                        complementos.add(item.substring(PREFIXO_COMPLEMENTO.length()));
                    } else if (!item.startsWith(PREFIXO_COLUNA) && !item.startsWith(PREFIXO_LINHA)) {
                        escolhas.add(limparPrefixoParentesesOpcao(item));
                    }
                }
            }
            if (escolhas.isEmpty() && complementos.isEmpty()) {
                escolhas.add("sim");
                escolhas.add("não");
                complementos.add("Qual?");
                atualizarListaEscolhaComComplemento(pergunta, escolhas, complementos);
            }

            JLabel lblSubEscolhas = new JLabel("Opções de seleção única (ex.: sim / não, Particular / Estadual / Outro):");
            lblSubEscolhas.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblSubEscolhas.setForeground(new Color(70, 80, 85));
            lblSubEscolhas.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(lblSubEscolhas);

            ButtonGroup grupoPreviewEsc = new ButtonGroup();
            for (int i = 0; i < escolhas.size(); i++) {
                final int idxEsc = i;
                JPanel linhaEsc = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
                linhaEsc.setOpaque(false);
                linhaEsc.setAlignmentX(Component.LEFT_ALIGNMENT);

                JRadioButton rb = new JRadioButton();
                rb.setOpaque(false);
                grupoPreviewEsc.add(rb);
                linhaEsc.add(rb);

                JTextField txtEsc = new JTextField(escolhas.get(i), 16);
                txtEsc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                camposOpcoes.add(txtEsc);
                linhaEsc.add(txtEsc);

                JButton btnRemEsc = criarBotaoExcluirItem("× Excluir opção");
                btnRemEsc.addActionListener(e -> {
                    sincronizarEdicaoAtualNoFormulario();
                    escolhas.remove(idxEsc);
                    atualizarListaEscolhaComComplemento(pergunta, escolhas, complementos);
                    editoresPerguntas.clear();
                    renderizarConstrutorGoogleForms();
                });
                linhaEsc.add(btnRemEsc);
                painelCorpo.add(linhaEsc);
            }

            JPanel linhaAddEsc = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
            linhaAddEsc.setOpaque(false);
            linhaAddEsc.setAlignmentX(Component.LEFT_ALIGNMENT);
            JButton btnAddEsc = criarBotaoAdicionarItem("+ Adicionar opção");
            btnAddEsc.addActionListener(e -> {
                sincronizarEdicaoAtualNoFormulario();
                escolhas.add("Opção " + (escolhas.size() + 1));
                atualizarListaEscolhaComComplemento(pergunta, escolhas, complementos);
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            });
            linhaAddEsc.add(btnAddEsc);
            painelCorpo.add(linhaAddEsc);

            painelCorpo.add(Box.createVerticalStrut(4));
            JLabel lblSubComp = new JLabel("Campos de texto complementares (ex.: Qual?, Desde:, Outro):");
            lblSubComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblSubComp.setForeground(new Color(70, 80, 85));
            lblSubComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(lblSubComp);

            for (int i = 0; i < complementos.size(); i++) {
                final int idxComp = i;
                JPanel linhaComp = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
                linhaComp.setOpaque(false);
                linhaComp.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel lblTag = new JLabel("Rótulo:");
                lblTag.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                linhaComp.add(lblTag);

                JTextField txtComp = new JTextField(complementos.get(i), 18);
                txtComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                camposComplementos.add(txtComp);
                linhaComp.add(txtComp);

                JButton btnRemComp = criarBotaoExcluirItem("× Excluir campo");
                btnRemComp.addActionListener(e -> {
                    sincronizarEdicaoAtualNoFormulario();
                    complementos.remove(idxComp);
                    atualizarListaEscolhaComComplemento(pergunta, escolhas, complementos);
                    editoresPerguntas.clear();
                    renderizarConstrutorGoogleForms();
                });
                linhaComp.add(btnRemComp);
                painelCorpo.add(linhaComp);
            }

            JPanel linhaAddComp = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
            linhaAddComp.setOpaque(false);
            linhaAddComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            JButton btnAddComp = criarBotaoAdicionarItem("+ Adicionar campo complementar");
            btnAddComp.addActionListener(e -> {
                sincronizarEdicaoAtualNoFormulario();
                complementos.add("Qual?");
                atualizarListaEscolhaComComplemento(pergunta, escolhas, complementos);
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            });
            linhaAddComp.add(btnAddComp);
            painelCorpo.add(linhaAddComp);

        } else if (tipo == TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE) {
            List<String> colunas = new ArrayList<>();
            List<String> linhas = new ArrayList<>();
            if (pergunta.getOpcoes() != null) {
                for (String item : pergunta.getOpcoes()) {
                    if (item.startsWith(PREFIXO_COLUNA)) {
                        colunas.add(item.substring(PREFIXO_COLUNA.length()));
                    } else if (item.startsWith(PREFIXO_LINHA)) {
                        linhas.add(item.substring(PREFIXO_LINHA.length()));
                    }
                }
            }
            if (colunas.isEmpty() || linhas.isEmpty()) {
                colunas = new ArrayList<>(Arrays.asList("2ª-feira", "3ª-feira", "4ª-feira", "5ª-feira", "6ª-feira"));
                linhas = new ArrayList<>(Arrays.asList("Manhã", "Tarde"));
                atualizarListaGrade(pergunta, colunas, linhas);
            }

            final List<String> refColunas = colunas;
            final List<String> refLinhas = linhas;

            JLabel lblCols = new JLabel("Colunas da Tabela (ex.: 2ª-feira a 6ª-feira):");
            lblCols.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblCols.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(lblCols);

            JPanel painelCols = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 6, 2));
            painelCols.setOpaque(false);
            painelCols.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (int i = 0; i < refColunas.size(); i++) {
                final int idxC = i;
                JTextField txtCol = new JTextField(refColunas.get(i), 8);
                camposColunas.add(txtCol);
                painelCols.add(txtCol);
                if (refColunas.size() > 1) {
                    JButton btnRemCol = criarBotaoExcluirItem("×");
                    btnRemCol.addActionListener(e -> {
                        sincronizarEdicaoAtualNoFormulario();
                        refColunas.remove(idxC);
                        atualizarListaGrade(pergunta, refColunas, refLinhas);
                        editoresPerguntas.clear();
                        renderizarConstrutorGoogleForms();
                    });
                    painelCols.add(btnRemCol);
                }
            }
            JButton btnAddCol = criarBotaoAdicionarItem("+ Coluna");
            btnAddCol.addActionListener(e -> {
                sincronizarEdicaoAtualNoFormulario();
                refColunas.add("Coluna " + (refColunas.size() + 1));
                atualizarListaGrade(pergunta, refColunas, refLinhas);
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            });
            painelCols.add(btnAddCol);
            painelCorpo.add(painelCols);

            JLabel lblRows = new JLabel("Linhas da Tabela (ex.: Manhã, Tarde):");
            lblRows.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblRows.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(lblRows);

            JPanel painelRows = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 6, 2));
            painelRows.setOpaque(false);
            painelRows.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (int i = 0; i < refLinhas.size(); i++) {
                final int idxL = i;
                JTextField txtLin = new JTextField(refLinhas.get(i), 8);
                camposLinhas.add(txtLin);
                painelRows.add(txtLin);
                if (refLinhas.size() > 1) {
                    JButton btnRemLin = criarBotaoExcluirItem("×");
                    btnRemLin.addActionListener(e -> {
                        sincronizarEdicaoAtualNoFormulario();
                        refLinhas.remove(idxL);
                        atualizarListaGrade(pergunta, refColunas, refLinhas);
                        editoresPerguntas.clear();
                        renderizarConstrutorGoogleForms();
                    });
                    painelRows.add(btnRemLin);
                }
            }
            JButton btnAddLin = criarBotaoAdicionarItem("+ Linha");
            btnAddLin.addActionListener(e -> {
                sincronizarEdicaoAtualNoFormulario();
                refLinhas.add("Linha " + (refLinhas.size() + 1));
                atualizarListaGrade(pergunta, refColunas, refLinhas);
                editoresPerguntas.clear();
                renderizarConstrutorGoogleForms();
            });
            painelRows.add(btnAddLin);
            painelCorpo.add(painelRows);

        } else if (tipo == TipoPerguntaAnamnese.ESTRELAS) {
            JPanel linhaEstrelas = criarPainelEstrelasInterativo(indiceZeroBased, null, null, 0, false);
            linhaEstrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(linhaEstrelas);

        } else if (tipo == TipoPerguntaAnamnese.TEXTO_LIVRE) {
            JTextField txtExemplo = new JTextField("Linha de resposta aberta (_______________________________)", 36);
            txtExemplo.setEditable(false);
            txtExemplo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            txtExemplo.setForeground(new Color(135, 140, 145));
            txtExemplo.setBackground(new Color(250, 251, 252));
            txtExemplo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 220)));

            JPanel wrapperTexto = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 8, 2));
            wrapperTexto.setOpaque(false);
            wrapperTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapperTexto.add(txtExemplo);
            painelCorpo.add(wrapperTexto);
        }

        if (tipo != TipoPerguntaAnamnese.SECAO_TITULO) {
            card.add(painelCorpo);
        }

        PerguntaEditorBinder binder = new PerguntaEditorBinder(
                pergunta, txtEnunciado, cmbTipo, camposOpcoes, camposComplementos, camposColunas, camposLinhas
        );
        editoresPerguntas.add(binder);

        cmbTipo.addActionListener(e -> {
            sincronizarEdicaoAtualNoFormulario();
            TipoPerguntaAnamnese novoTipo = (TipoPerguntaAnamnese) cmbTipo.getSelectedItem();
            pergunta.setTipoPergunta(novoTipo);
            if (novoTipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA || novoTipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
                pergunta.setOpcoes(new ArrayList<>(Arrays.asList("Opção 1", "Opção 2")));
            } else if (novoTipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
                pergunta.setOpcoes(new ArrayList<>(Arrays.asList("Campo 1", "Campo 2")));
            } else if (novoTipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
                pergunta.setOpcoes(new ArrayList<>(Arrays.asList("sim", "não", PREFIXO_COMPLEMENTO + "Qual?")));
            } else if (novoTipo == TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE) {
                pergunta.setOpcoes(new ArrayList<>(Arrays.asList(
                        PREFIXO_COLUNA + "2ª-feira", PREFIXO_COLUNA + "3ª-feira", PREFIXO_COLUNA + "4ª-feira",
                        PREFIXO_COLUNA + "5ª-feira", PREFIXO_COLUNA + "6ª-feira",
                        PREFIXO_LINHA + "Manhã", PREFIXO_LINHA + "Tarde"
                )));
            }
            editoresPerguntas.clear();
            renderizarConstrutorGoogleForms();
        });

        btnRemoverPergunta.addActionListener(e -> {
            sincronizarEdicaoAtualNoFormulario();
            formularioAtual.getPerguntas().remove(indiceZeroBased);
            indicePerguntaSelecionadaConstrutor = -1;
            editoresPerguntas.clear();
            renderizarConstrutorGoogleForms();
        });

        return card;
    }

    private void atualizarListaEscolhaComComplemento(PerguntaAnamnese pergunta, List<String> escolhas, List<String> complementos) {
        List<String> combinada = new ArrayList<>();
        for (String esc : escolhas) {
            combinada.add(esc);
        }
        for (String comp : complementos) {
            combinada.add(PREFIXO_COMPLEMENTO + comp);
        }
        pergunta.setOpcoes(combinada);
    }

    private void atualizarListaGrade(PerguntaAnamnese pergunta, List<String> colunas, List<String> linhas) {
        List<String> combinada = new ArrayList<>();
        for (String c : colunas) {
            combinada.add(PREFIXO_COLUNA + c);
        }
        for (String l : linhas) {
            combinada.add(PREFIXO_LINHA + l);
        }
        pergunta.setOpcoes(combinada);
    }

    private JButton criarBotaoExcluirItem(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(new Color(185, 60, 60));
        btn.setBorder(new EmptyBorder(2, 6, 2, 6));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton criarBotaoAdicionarItem(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(VERDE_BOTAO_ATIVO);
        btn.setBorder(new EmptyBorder(2, 4, 2, 4));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel criarPainelEstrelasInterativo(
            final int indicePergunta,
            final String prefixoRascunho,
            final String chavePergunta,
            int notaInicial,
            boolean somenteLeitura) {
        JPanel painelEstrelas = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 10, 2));
        painelEstrelas.setOpaque(false);

        final JButton[] botoesEstrela = new JButton[5];
        for (int i = 0; i < 5; i++) {
            final int nota = i + 1;
            JButton btnEstrela = new JButton(nota <= notaInicial ? "★" : "☆");
            btnEstrela.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 22));
            btnEstrela.setForeground(nota <= notaInicial ? new Color(234, 179, 8) : new Color(110, 115, 120));
            btnEstrela.setBorder(new EmptyBorder(0, 2, 0, 2));
            btnEstrela.setContentAreaFilled(false);
            btnEstrela.setFocusPainted(false);

            if (!somenteLeitura) {
                btnEstrela.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEstrela.addActionListener(e -> {
                    respostasPreenchimento.put(indicePergunta, nota + " estrela(s)");
                    if (prefixoRascunho != null && chavePergunta != null) {
                        salvarValorRascunho(prefixoRascunho, chavePergunta + "__ESTRELAS", String.valueOf(nota));
                        salvarValorRascunho(prefixoRascunho, "IDX_" + indicePergunta + "__ESTRELAS", String.valueOf(nota));
                    }
                    for (int j = 0; j < 5; j++) {
                        boolean preenchida = (j + 1) <= nota;
                        botoesEstrela[j].setText(preenchida ? "★" : "☆");
                        botoesEstrela[j].setForeground(preenchida ? new Color(234, 179, 8) : new Color(110, 115, 120));
                    }
                });
            }

            botoesEstrela[i] = btnEstrela;
            painelEstrelas.add(btnEstrela);
        }
        return painelEstrelas;
    }

    /**
     * Abre a tela "Anamnese Visualização" exibindo uma anamnese salva (ativa ou desativada)
     * em modo somente leitura:
     * - Se ativa: oculta o botão X e exibe o botão "Editar".
     * - Se desativada: exibe o botão X (para voltar à anamnese principal), exibe a data/hora de desativação
     *   na frente do nome e altera o botão para "Copiar e Colar".
     */
    private void abrirVisualizacaoResposta(RespostaAnamnese resposta) {
        if (resposta == null) {
            return;
        }
        if (resposta.isAtiva()) {
            this.desativadaAbertaAPartirDeModelos = false;
        }
        this.modoVisualizacaoAnamnese = true;
        this.modoEdicaoQuestionario = false;
        this.respostaVisualizada = resposta;
        this.editoresPerguntas.clear();

        FormularioAnamnese form = resolverFormularioDaResposta(resposta);
        this.formularioAtual = form;

        atualizarEstadoBarra(this.abaAtiva);

        boolean ehDesativada = !resposta.isAtiva();
        Map<String, String> dadosSalvos = desserializarDadosCampos(resposta);

        carregandoPreenchimentoUI = true;
        try {
            String tituloBase = (form != null && form.getTitulo() != null) ? form.getTitulo() : "Anamnese";
            if (ehDesativada) {
                LocalDateTime dtDesat = resposta.getDataHoraDesativacao();
                if (dtDesat == null && resposta.getDataAtualizacao() != null) {
                    dtDesat = resposta.getDataAtualizacao();
                }
                String strDataHora = dtDesat != null ? dtDesat.format(FORMATO_DATA_HORA) : "";
                txtTituloAnamnese.setText(strDataHora.isEmpty() ? (tituloBase + " - Desativada") : (tituloBase + " - Desativada em " + strDataHora));
            } else {
                txtTituloAnamnese.setText(tituloBase);
            }
            txtTituloAnamnese.setEditable(false);
            txtTituloAnamnese.setOpaque(true);
            txtTituloAnamnese.setBackground(
                    ehDesativada ? FUNDO_TITULO_DESATIVADA_VERMELHO_CLARO : FUNDO_TITULO_PRINCIPAL_AZUL_CLARO
            );
            txtTituloAnamnese.setBorder(new EmptyBorder(0, 10, 0, 10));

            LocalDate dataAb = resposta.getDataAbertura() != null ? resposta.getDataAbertura() : LocalDate.now();
            txtDataAbertura.setText(dataAb.format(FORMATO_DATA));
            txtDataAbertura.setEditable(false);
        } finally {
            carregandoPreenchimentoUI = false;
        }

        // Quando uma anamnese desativada for selecionada, mostra o botão X para sair dela e voltar à principal,
        // e o botão de ação se chama sempre "Retificar"
        btnFecharVisualizacaoDesativada.setVisible(ehDesativada);
        btnBaixarAnamneseSalva.setVisible(true);
        btnEditarAnamneseSalva.setText("Retificar");
        btnEditarAnamneseSalva.setVisible(true);

        btnEditarPerguntasModelo.setVisible(false);
        btnLimparPreenchimento.setVisible(false);
        btnAdicionarPergunta.setVisible(false);
        pnlRodapeAcoes.setVisible(false);

        renderizarFormularioPreenchimentoOuVisualizacao(form, null, dadosSalvos, true);
    }

    private FormularioAnamnese resolverFormularioDaResposta(RespostaAnamnese resposta) {
        if (resposta == null) {
            return anamneseDAO.buscarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        }
        FormularioAnamnese form = resposta.getFormulario();
        if (form != null && form.getPerguntas() != null && !form.getPerguntas().isEmpty()) {
            return form;
        }
        if (form != null && form.getTipoModelo() == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
            return anamneseDAO.buscarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        }
        if (form != null && form.getTipoModelo() == TipoModeloAnamnese.ADULTO) {
            return anamneseDAO.buscarModeloPadrao(TipoModeloAnamnese.ADULTO);
        }
        if (form != null && form.getId() != null) {
            FormularioAnamnese buscado = anamneseDAO.buscarPorId(form.getId());
            if (buscado != null) {
                return buscado;
            }
        }
        return anamneseDAO.buscarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
    }

    private void abrirPreenchimentoFormulario(FormularioAnamnese form, boolean selecionadoPeloDropdown) {
        if (form == null) {
            return;
        }
        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;
        this.formularioAtual = form;
        this.modoEdicaoQuestionario = false;
        this.editoresPerguntas.clear();
        this.respostasPreenchimento.clear();

        if (selecionadoPeloDropdown) {
            this.modoAtual = TipoModeloAnamnese.PERSONALIZADO;
            atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        } else {
            atualizarEstadoBarra(this.abaAtiva);
        }

        final String prefixoRascunho = obterPrefixoRascunho(form);
        carregandoPreenchimentoUI = true;
        try {
            txtTituloAnamnese.setText(form.getTitulo());
            txtTituloAnamnese.setEditable(false);
            txtTituloAnamnese.setOpaque(true);
            txtTituloAnamnese.setBackground(Color.WHITE);
            txtTituloAnamnese.setBorder(null);
            txtDataAbertura.setEditable(true);

            String dataRascunho = obterValorRascunho(prefixoRascunho, "DATA_ABERTURA", null);
            if (dataRascunho != null && !dataRascunho.isBlank()) {
                txtDataAbertura.setText(dataRascunho);
            } else {
                LocalDate dataAb = form.getDataAbertura() != null ? form.getDataAbertura() : LocalDate.now();
                txtDataAbertura.setText(dataAb.format(FORMATO_DATA));
            }
        } finally {
            carregandoPreenchimentoUI = false;
        }

        btnFecharVisualizacaoDesativada.setVisible(false);
        btnEditarPerguntasModelo.setVisible(true);
        btnLimparPreenchimento.setVisible(true);
        btnBaixarAnamneseSalva.setVisible(false);
        btnEditarAnamneseSalva.setVisible(false);
        btnAdicionarPergunta.setVisible(false);
        btnSalvarAnamnese.setText("SALVAR");
        pnlRodapeAcoes.setVisible(true);

        renderizarFormularioPreenchimentoOuVisualizacao(form, prefixoRascunho, null, false);
    }

    private String limparPrefixoParentesesOpcao(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().replaceFirst("^\\(\\s*[xX]?\\s*\\)\\s*", "");
    }

    private String formatarEnunciadoResponsivoHtml(String texto) {
        if (texto == null) {
            return "";
        }
        String escapado = texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<html><body style='width: 100%;'>" + escapado + "</body></html>";
    }

    private void renderizarFormularioPreenchimentoOuVisualizacao(
            FormularioAnamnese form,
            final String prefixoRascunho,
            final Map<String, String> dadosFixosVisualizacao,
            final boolean somenteLeitura) {

        pnlConteudoAnamnese.removeAll();
        pnlConteudoAnamnese.setLayout(new BoxLayout(pnlConteudoAnamnese, BoxLayout.Y_AXIS));
        pnlConteudoAnamnese.setBackground(Color.WHITE);
        pnlConteudoAnamnese.setBorder(new EmptyBorder(10, 22, 14, 22));

        Map<String, Integer> contadorEnunciados = new HashMap<>();
        List<PerguntaAnamnese> perguntas = form.getPerguntas();

        for (int i = 0; i < perguntas.size(); i++) {
            final int idx = i;
            PerguntaAnamnese p = perguntas.get(i);
            TipoPerguntaAnamnese tipo = p.getTipoPergunta() != null
                    ? p.getTipoPergunta()
                    : TipoPerguntaAnamnese.TEXTO_LIVRE;

            String chaveBase = tipo.name() + "_" + normalizarChave(p.getEnunciado());
            int ocorrencia = contadorEnunciados.getOrDefault(chaveBase, 0) + 1;
            contadorEnunciados.put(chaveBase, ocorrencia);

            final String qKey = obterChavePergunta(p, ocorrencia, idx);
            final String idxKey = "IDX_" + idx;

            if (tipo == TipoPerguntaAnamnese.SECAO_TITULO) {
                JPanel bannerSecao = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 10, 6)) {
                    @Override
                    public Dimension getMaximumSize() {
                        Dimension pref = getPreferredSize();
                        return new Dimension(Integer.MAX_VALUE, pref.height);
                    }
                };
                bannerSecao.setBackground(new Color(236, 246, 240));
                bannerSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
                bannerSecao.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, VERDE_BOTAO_ATIVO));

                JLabel lblSecao = new JLabel(p.getEnunciado());
                lblSecao.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblSecao.setForeground(new Color(13, 82, 65));
                bannerSecao.add(lblSecao);

                pnlConteudoAnamnese.add(Box.createVerticalStrut(8));
                pnlConteudoAnamnese.add(bannerSecao);
                pnlConteudoAnamnese.add(Box.createVerticalStrut(6));
                continue;
            }

            JPanel bloco = new JPanel() {
                @Override
                public Dimension getMaximumSize() {
                    Dimension pref = getPreferredSize();
                    return new Dimension(Integer.MAX_VALUE, pref.height);
                }
            };
            bloco.setLayout(new BoxLayout(bloco, BoxLayout.Y_AXIS));
            bloco.setOpaque(false);
            bloco.setAlignmentX(Component.LEFT_ALIGNMENT);
            bloco.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(244, 246, 248)),
                    new EmptyBorder(6, 4, 10, 4)
            ));

            JPanel linhaPergunta = new JPanel(new BorderLayout());
            linhaPergunta.setOpaque(false);
            linhaPergunta.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblPergunta = new JLabel(formatarEnunciadoResponsivoHtml(p.getEnunciado()));
            lblPergunta.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblPergunta.setForeground(new Color(35, 42, 48));
            linhaPergunta.add(lblPergunta, BorderLayout.CENTER);
            bloco.add(linhaPergunta);
            bloco.add(Box.createVerticalStrut(4));

            JPanel corpo = new JPanel();
            corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
            corpo.setOpaque(false);
            corpo.setAlignmentX(Component.LEFT_ALIGNMENT);
            corpo.setBorder(new EmptyBorder(0, 8, 0, 0));

            if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA) {
                ButtonGroup bg = new ButtonGroup();
                JPanel linhaOpcoes = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 14, 4));
                linhaOpcoes.setOpaque(false);
                linhaOpcoes.setAlignmentX(Component.LEFT_ALIGNMENT);

                final String opcaoSalva = somenteLeitura
                        ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__OPCAO", idxKey + "__OPCAO")
                        : obterValorRascunho(prefixoRascunho, qKey + "__OPCAO", idxKey + "__OPCAO");

                for (String opBruta : p.getOpcoes()) {
                    final String op = limparPrefixoParentesesOpcao(opBruta);
                    JRadioButton rb = new JRadioButton(op);
                    rb.setOpaque(false);
                    rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    rb.setForeground(new Color(55, 62, 68));
                    if (opcaoSalva != null && limparPrefixoParentesesOpcao(opcaoSalva).equalsIgnoreCase(op)) {
                        rb.setSelected(true);
                        if (!somenteLeitura) {
                            respostasPreenchimento.put(idx, op);
                        }
                    }
                    if (!somenteLeitura) {
                        rb.addActionListener(e -> {
                            respostasPreenchimento.put(idx, op);
                            salvarValorRascunho(prefixoRascunho, qKey + "__OPCAO", op);
                            salvarValorRascunho(prefixoRascunho, idxKey + "__OPCAO", op);
                        });
                        bg.add(rb);
                    } else {
                        final boolean deveFicarSelecionado = (opcaoSalva != null && limparPrefixoParentesesOpcao(opcaoSalva).equalsIgnoreCase(op));
                        rb.addActionListener(e -> rb.setSelected(deveFicarSelecionado));
                    }
                    linhaOpcoes.add(rb);
                }
                corpo.add(linhaOpcoes);

            } else if (tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
                final List<JCheckBox> checks = new ArrayList<>();
                JPanel linhaChecks = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 14, 4));
                linhaChecks.setOpaque(false);
                linhaChecks.setAlignmentX(Component.LEFT_ALIGNMENT);

                List<String> inicialmenteMarcadas = new ArrayList<>();
                for (String opBruta : p.getOpcoes()) {
                    final String op = limparPrefixoParentesesOpcao(opBruta);
                    final String opKey = normalizarChave(op);
                    JCheckBox cb = new JCheckBox(op);
                    cb.setOpaque(false);
                    cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    cb.setForeground(new Color(55, 62, 68));

                    String marcadoSalvo = somenteLeitura
                            ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__CHK_" + opKey, idxKey + "__CHK_" + opKey)
                            : obterValorRascunho(prefixoRascunho, qKey + "__CHK_" + opKey, idxKey + "__CHK_" + opKey);
                    final boolean estadoFixo = "true".equals(marcadoSalvo);
                    if (estadoFixo) {
                        cb.setSelected(true);
                        inicialmenteMarcadas.add(op);
                    }

                    checks.add(cb);
                    if (!somenteLeitura) {
                        cb.addActionListener(e -> {
                            salvarValorRascunho(prefixoRascunho, qKey + "__CHK_" + opKey, cb.isSelected() ? "true" : "");
                            salvarValorRascunho(prefixoRascunho, idxKey + "__CHK_" + opKey, cb.isSelected() ? "true" : "");
                            List<String> marcadas = new ArrayList<>();
                            for (JCheckBox c : checks) {
                                if (c.isSelected()) {
                                    marcadas.add(c.getText());
                                }
                            }
                            respostasPreenchimento.put(idx, String.join(", ", marcadas));
                        });
                    } else {
                        cb.addActionListener(e -> cb.setSelected(estadoFixo));
                    }
                    linhaChecks.add(cb);
                }
                if (!somenteLeitura && !inicialmenteMarcadas.isEmpty()) {
                    respostasPreenchimento.put(idx, String.join(", ", inicialmenteMarcadas));
                }
                corpo.add(linhaChecks);

            } else if (tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
                JPanel linhaCampos = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 12, 6));
                linhaCampos.setOpaque(false);
                linhaCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

                final Map<String, JTextField> mapaCampos = new LinkedHashMap<>();
                for (String rotuloCampo : p.getOpcoes()) {
                    final String campoKey = normalizarChave(rotuloCampo);
                    JLabel lbl = new JLabel(rotuloCampo + ":");
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lbl.setForeground(new Color(60, 68, 75));

                    String valSalvo = somenteLeitura
                            ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__CAMPO_" + campoKey, idxKey + "__CAMPO_" + campoKey)
                            : obterValorRascunho(prefixoRascunho, qKey + "__CAMPO_" + campoKey, idxKey + "__CAMPO_" + campoKey);
                    JTextField txtCampo = new JTextField(valSalvo != null ? valSalvo : "", 13);
                    txtCampo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    txtCampo.setEditable(!somenteLeitura);
                    txtCampo.setBackground(somenteLeitura ? new Color(250, 252, 251) : Color.WHITE);
                    txtCampo.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(175, 185, 195)),
                            new EmptyBorder(2, 4, 2, 4)
                    ));
                    mapaCampos.put(rotuloCampo, txtCampo);

                    if (!somenteLeitura) {
                        txtCampo.getDocument().addDocumentListener(criarListenerMapaCampos(
                                idx, prefixoRascunho, qKey, idxKey, "__CAMPO_" + campoKey, txtCampo, null, mapaCampos
                        ));
                    }
                    linhaCampos.add(lbl);
                    linhaCampos.add(txtCampo);
                }
                if (!somenteLeitura) {
                    atualizarRespostaEscolhaComComplemento(idx, null, mapaCampos);
                }
                corpo.add(linhaCampos);

            } else if (tipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
                final List<AbstractButton> opcoesMarcaveis = new ArrayList<>();
                final Map<String, JTextField> mapaComplementos = new LinkedHashMap<>();

                JPanel linhaMista = new JPanel(new WrapFlowLayout(FlowLayout.LEFT, 12, 6));
                linhaMista.setOpaque(false);
                linhaMista.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Separa opções de marcação e campos de complemento
                List<String> opcoesLimpa = new ArrayList<>();
                for (String item : p.getOpcoes()) {
                    if (!item.startsWith(PREFIXO_COMPLEMENTO)
                            && !item.startsWith(PREFIXO_COLUNA)
                            && !item.startsWith(PREFIXO_LINHA)) {
                        opcoesLimpa.add(limparPrefixoParentesesOpcao(item));
                    }
                }

                // Verifica se possui o par sim/não e se é múltipla marcação (ex.: "Quem mora na casa?" ou "Os pais já tiveram...")
                boolean temSim = false;
                boolean temNao = false;
                for (String op : opcoesLimpa) {
                    if (op.equalsIgnoreCase("sim")) {
                        temSim = true;
                    } else if (op.equalsIgnoreCase("não") || op.equalsIgnoreCase("nao")) {
                        temNao = true;
                    }
                }
                boolean possuiParSimNao = temSim && temNao;
                String enunciadoNorm = normalizarChave(p.getEnunciado());
                boolean multiplaMarcacao = enunciadoNorm.contains("quem_mora_na_casa")
                        || enunciadoNorm.contains("os_pais_j_tiveram")
                        || enunciadoNorm.contains("atendimento_especializado");

                final ButtonGroup grupoPrincipalRadio = new ButtonGroup();
                final ButtonGroup grupoSecundarioRadio = new ButtonGroup();
                final List<AbstractButton> botoesGrupoPrincipal = new ArrayList<>();
                final List<AbstractButton> botoesGrupoSecundario = new ArrayList<>();

                // Primeiro cria os campos complementares no mapa para poder vinculá-los à seleção de "Outro" / "sim"
                for (String item : p.getOpcoes()) {
                    if (item.startsWith(PREFIXO_COMPLEMENTO)) {
                        String rotuloComp = item.substring(PREFIXO_COMPLEMENTO.length()).trim();
                        final String compKey = normalizarChave(rotuloComp);
                        JLabel lblC = new JLabel(rotuloComp.endsWith(":") ? rotuloComp : (rotuloComp + ":"));
                        lblC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        lblC.setForeground(new Color(60, 68, 75));

                        String valSalvo = somenteLeitura
                                ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__COMP_" + compKey, idxKey + "__COMP_" + compKey)
                                : obterValorRascunho(prefixoRascunho, qKey + "__COMP_" + compKey, idxKey + "__COMP_" + compKey);
                        JTextField txtComp = new JTextField(valSalvo != null ? valSalvo : "", 14);
                        txtComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        txtComp.setEditable(!somenteLeitura);
                        txtComp.setBackground(somenteLeitura ? new Color(250, 252, 251) : Color.WHITE);
                        txtComp.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(175, 185, 195)),
                                new EmptyBorder(2, 4, 2, 4)
                        ));
                        mapaComplementos.put(rotuloComp, txtComp);
                        if (!somenteLeitura) {
                            txtComp.getDocument().addDocumentListener(criarListenerMapaCampos(
                                    idx, prefixoRascunho, qKey, idxKey, "__COMP_" + compKey, txtComp, opcoesMarcaveis, mapaComplementos
                            ));
                        }
                    }
                }

                // Renderiza na ordem exata definida na pergunta (sem "()" na frente das opções)
                for (String item : p.getOpcoes()) {
                    if (item.startsWith(PREFIXO_COMPLEMENTO)) {
                        String rotuloComp = item.substring(PREFIXO_COMPLEMENTO.length()).trim();
                        JLabel lblC = new JLabel(rotuloComp.endsWith(":") ? rotuloComp : (rotuloComp + ":"));
                        lblC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        lblC.setForeground(new Color(60, 68, 75));
                        JTextField txtComp = mapaComplementos.get(rotuloComp);
                        if (txtComp != null) {
                            linhaMista.add(lblC);
                            linhaMista.add(txtComp);
                        }
                    } else if (!item.startsWith(PREFIXO_COLUNA) && !item.startsWith(PREFIXO_LINHA)) {
                        final String textoOpcao = limparPrefixoParentesesOpcao(item);
                        final String escKey = normalizarChave(textoOpcao);

                        boolean ehSimOuNao = textoOpcao.equalsIgnoreCase("sim")
                                || textoOpcao.equalsIgnoreCase("não")
                                || textoOpcao.equalsIgnoreCase("nao");

                        AbstractButton botaoOpcao;
                        if (ehSimOuNao || !multiplaMarcacao) {
                            JRadioButton rb = new JRadioButton(textoOpcao);
                            rb.setOpaque(false);
                            rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                            rb.setForeground(new Color(55, 62, 68));
                            botaoOpcao = rb;

                            if (!somenteLeitura) {
                                if (possuiParSimNao && !ehSimOuNao) {
                                    grupoSecundarioRadio.add(rb);
                                    botoesGrupoSecundario.add(rb);
                                } else {
                                    grupoPrincipalRadio.add(rb);
                                    botoesGrupoPrincipal.add(rb);
                                }
                            } else {
                                if (possuiParSimNao && !ehSimOuNao) {
                                    botoesGrupoSecundario.add(rb);
                                } else {
                                    botoesGrupoPrincipal.add(rb);
                                }
                            }
                        } else {
                            JCheckBox cb = new JCheckBox(textoOpcao);
                            cb.setOpaque(false);
                            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                            cb.setForeground(new Color(55, 62, 68));
                            botaoOpcao = cb;
                            if (possuiParSimNao && !ehSimOuNao) {
                                botoesGrupoSecundario.add(cb);
                            } else {
                                botoesGrupoPrincipal.add(cb);
                            }
                        }

                        String escSalvo = somenteLeitura
                                ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__ESC_" + escKey, idxKey + "__ESC_" + escKey)
                                : obterValorRascunho(prefixoRascunho, qKey + "__ESC_" + escKey, idxKey + "__ESC_" + escKey);
                        final boolean estadoFixo = "true".equals(escSalvo);
                        if (estadoFixo) {
                            botaoOpcao.setSelected(true);
                        }

                        opcoesMarcaveis.add(botaoOpcao);
                        linhaMista.add(botaoOpcao);
                    }
                }

                // Configura a habilitação condicional do campo "Outro" / "outros" (só habilita se a opção Outro/outros estiver marcada)
                final Runnable sincronizarEstadoOpcoesEOutro = () -> {
                    AbstractButton botaoOutro = null;
                    for (AbstractButton b : opcoesMarcaveis) {
                        String t = b.getText() != null ? b.getText().trim() : "";
                        if (t.equalsIgnoreCase("outro") || t.equalsIgnoreCase("outros")) {
                            botaoOutro = b;
                            break;
                        }
                    }

                    for (Map.Entry<String, JTextField> entry : mapaComplementos.entrySet()) {
                        String nomeCampo = entry.getKey() != null ? entry.getKey().trim() : "";
                        JTextField campo = entry.getValue();
                        if (nomeCampo.equalsIgnoreCase("outro") || nomeCampo.equalsIgnoreCase("outros")) {
                            if (botaoOutro != null) {
                                boolean outroAtivo = botaoOutro.isSelected();
                                if (!somenteLeitura) {
                                    campo.setEnabled(outroAtivo);
                                    campo.setEditable(outroAtivo);
                                    campo.setBackground(outroAtivo ? Color.WHITE : new Color(243, 245, 247));
                                    if (!outroAtivo && !campo.getText().isEmpty()) {
                                        campo.setText("");
                                    }
                                } else {
                                    campo.setEnabled(true);
                                    campo.setEditable(false);
                                }
                            }
                        }
                    }

                    if (!somenteLeitura) {
                        for (AbstractButton b : opcoesMarcaveis) {
                            String bKey = normalizarChave(b.getText());
                            salvarValorRascunho(prefixoRascunho, qKey + "__ESC_" + bKey, b.isSelected() ? "true" : "");
                            salvarValorRascunho(prefixoRascunho, idxKey + "__ESC_" + bKey, b.isSelected() ? "true" : "");
                        }
                        atualizarRespostaEscolhaComComplemento(idx, opcoesMarcaveis, mapaComplementos);
                    }
                };

                for (AbstractButton b : opcoesMarcaveis) {
                    final boolean estadoInicial = b.isSelected();
                    if (!somenteLeitura) {
                        b.addActionListener(e -> {
                            sincronizarEstadoOpcoesEOutro.run();
                            String t = b.getText() != null ? b.getText().trim() : "";
                            if ((t.equalsIgnoreCase("outro") || t.equalsIgnoreCase("outros")) && b.isSelected()) {
                                for (Map.Entry<String, JTextField> entry : mapaComplementos.entrySet()) {
                                    if (entry.getKey().trim().equalsIgnoreCase(t)) {
                                        entry.getValue().requestFocusInWindow();
                                    }
                                }
                            }
                        });
                    } else {
                        b.addActionListener(e -> b.setSelected(estadoInicial));
                    }
                }

                // Aplica o estado inicial (por exemplo, deixando o campo "Outro:" desabilitado se "Outro" não estiver marcado)
                sincronizarEstadoOpcoesEOutro.run();
                corpo.add(linhaMista);

            } else if (tipo == TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE) {
                List<String> colunas = new ArrayList<>();
                List<String> linhas = new ArrayList<>();
                for (String item : p.getOpcoes()) {
                    if (item.startsWith(PREFIXO_COLUNA)) {
                        colunas.add(item.substring(PREFIXO_COLUNA.length()));
                    } else if (item.startsWith(PREFIXO_LINHA)) {
                        linhas.add(item.substring(PREFIXO_LINHA.length()));
                    }
                }
                if (colunas.isEmpty()) {
                    colunas = Arrays.asList("2ª-feira", "3ª-feira", "4ª-feira", "5ª-feira", "6ª-feira");
                }
                if (linhas.isEmpty()) {
                    linhas = Arrays.asList("Manhã", "Tarde");
                }

                JPanel tabelaGrid = new JPanel(new GridLayout(linhas.size() + 1, colunas.size() + 1, 0, 0));
                tabelaGrid.setBackground(Color.WHITE);
                tabelaGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
                tabelaGrid.setMaximumSize(new Dimension(720, (linhas.size() + 1) * 34));

                JLabel lblCanto = criarCelulaCabecalhoTabela("Dia da semana");
                tabelaGrid.add(lblCanto);
                for (String col : colunas) {
                    tabelaGrid.add(criarCelulaCabecalhoTabela(col));
                }

                final List<JCheckBox> checksGrade = new ArrayList<>();
                List<String> dispIniciais = new ArrayList<>();
                for (String lin : linhas) {
                    tabelaGrid.add(criarCelulaCabecalhoTabela(lin));
                    for (String col : colunas) {
                        final String chaveHorario = lin + " - " + col;
                        final String gradeKey = normalizarChave(chaveHorario);

                        JPanel celula = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
                        celula.setBackground(Color.WHITE);
                        celula.setBorder(BorderFactory.createLineBorder(new Color(205, 212, 220)));

                        JCheckBox cbHorario = new JCheckBox("X");
                        cbHorario.setOpaque(false);
                        cbHorario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        cbHorario.putClientProperty("chaveHorario", chaveHorario);

                        String celSalva = somenteLeitura
                                ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__GRADE_" + gradeKey, idxKey + "__GRADE_" + gradeKey)
                                : obterValorRascunho(prefixoRascunho, qKey + "__GRADE_" + gradeKey, idxKey + "__GRADE_" + gradeKey);
                        final boolean estadoFixo = "true".equals(celSalva);
                        if (estadoFixo) {
                            cbHorario.setSelected(true);
                            dispIniciais.add(chaveHorario);
                        }

                        checksGrade.add(cbHorario);
                        if (!somenteLeitura) {
                            cbHorario.addActionListener(e -> {
                                salvarValorRascunho(prefixoRascunho, qKey + "__GRADE_" + gradeKey, cbHorario.isSelected() ? "true" : "");
                                salvarValorRascunho(prefixoRascunho, idxKey + "__GRADE_" + gradeKey, cbHorario.isSelected() ? "true" : "");
                                List<String> disp = new ArrayList<>();
                                for (JCheckBox c : checksGrade) {
                                    if (c.isSelected()) {
                                        disp.add(String.valueOf(c.getClientProperty("chaveHorario")));
                                    }
                                }
                                respostasPreenchimento.put(idx, String.join("; ", disp));
                            });
                        } else {
                            cbHorario.addActionListener(e -> cbHorario.setSelected(estadoFixo));
                        }
                        celula.add(cbHorario);
                        tabelaGrid.add(celula);
                    }
                }
                if (!somenteLeitura && !dispIniciais.isEmpty()) {
                    respostasPreenchimento.put(idx, String.join("; ", dispIniciais));
                }
                corpo.add(tabelaGrid);

            } else if (tipo == TipoPerguntaAnamnese.ESTRELAS) {
                String estSalva = somenteLeitura
                        ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__ESTRELAS", idxKey + "__ESTRELAS")
                        : obterValorRascunho(prefixoRascunho, qKey + "__ESTRELAS", idxKey + "__ESTRELAS");
                int notaIni = 0;
                if (estSalva != null) {
                    try {
                        notaIni = Integer.parseInt(estSalva.trim());
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (!somenteLeitura && notaIni > 0) {
                    respostasPreenchimento.put(idx, notaIni + " estrela(s)");
                }
                JPanel estrelas = criarPainelEstrelasInterativo(idx, prefixoRascunho, qKey, notaIni, somenteLeitura);
                estrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
                corpo.add(estrelas);

            } else {
                String textoSalvo = somenteLeitura
                        ? obterValorMapaCampos(dadosFixosVisualizacao, qKey + "__TEXTO", idxKey + "__TEXTO")
                        : obterValorRascunho(prefixoRascunho, qKey + "__TEXTO", idxKey + "__TEXTO");
                JTextArea txtResp = new JTextArea(textoSalvo != null ? textoSalvo : "", 2, 30);
                if (!somenteLeitura && textoSalvo != null && !textoSalvo.isBlank()) {
                    respostasPreenchimento.put(idx, textoSalvo);
                }
                txtResp.setLineWrap(true);
                txtResp.setWrapStyleWord(true);
                txtResp.setEditable(!somenteLeitura);
                txtResp.setBackground(somenteLeitura ? new Color(250, 252, 251) : Color.WHITE);
                txtResp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                txtResp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 215, 220)),
                        new EmptyBorder(4, 8, 4, 8)
                ));
                if (!somenteLeitura) {
                    txtResp.getDocument().addDocumentListener(new DocumentListener() {
                        private void salvar() {
                            String val = txtResp.getText();
                            respostasPreenchimento.put(idx, val);
                            salvarValorRascunho(prefixoRascunho, qKey + "__TEXTO", val);
                            salvarValorRascunho(prefixoRascunho, idxKey + "__TEXTO", val);
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            salvar();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            salvar();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            salvar();
                        }
                    });
                }
                JPanel wrapperArea = new JPanel(new BorderLayout());
                wrapperArea.setOpaque(false);
                wrapperArea.setAlignmentX(Component.LEFT_ALIGNMENT);
                wrapperArea.setBorder(new EmptyBorder(0, 0, 0, 12));
                wrapperArea.add(txtResp, BorderLayout.CENTER);
                corpo.add(wrapperArea);
            }

            bloco.add(corpo);
            pnlConteudoAnamnese.add(bloco);
        }

        // Evita espaçamento exagerado entre perguntas em formulários curtos (2 ou 3 perguntas)
        pnlConteudoAnamnese.add(Box.createVerticalGlue());
        pnlConteudoAnamnese.revalidate();
        pnlConteudoAnamnese.repaint();
    }

    private JLabel criarCelulaCabecalhoTabela(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(242, 247, 244));
        lbl.setForeground(new Color(25, 65, 50));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setBorder(BorderFactory.createLineBorder(new Color(205, 212, 220)));
        return lbl;
    }

    private DocumentListener criarListenerMapaCampos(
            final int idxPergunta,
            final String prefixoRascunho,
            final String qKey,
            final String idxKey,
            final String sufixoCampo,
            final JTextField campoTexto,
            final List<? extends AbstractButton> opcoesMarcaveis,
            final Map<String, JTextField> mapaCampos) {
        return new DocumentListener() {
            private void atualizar() {
                String valor = campoTexto.getText();
                salvarValorRascunho(prefixoRascunho, qKey + sufixoCampo, valor);
                salvarValorRascunho(prefixoRascunho, idxKey + sufixoCampo, valor);
                atualizarRespostaEscolhaComComplemento(idxPergunta, opcoesMarcaveis, mapaCampos);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizar();
            }
        };
    }

    private void atualizarRespostaEscolhaComComplemento(
            int idxPergunta,
            List<? extends AbstractButton> opcoesMarcaveis,
            Map<String, JTextField> mapaCampos) {
        List<String> partes = new ArrayList<>();
        if (opcoesMarcaveis != null) {
            for (AbstractButton cb : opcoesMarcaveis) {
                if (cb.isSelected()) {
                    partes.add(limparPrefixoParentesesOpcao(cb.getText()));
                }
            }
        }
        if (mapaCampos != null) {
            for (Map.Entry<String, JTextField> entry : mapaCampos.entrySet()) {
                String val = entry.getValue().getText();
                if (val != null && !val.isBlank()) {
                    partes.add(entry.getKey() + ": " + val.trim());
                }
            }
        }
        if (partes.isEmpty()) {
            respostasPreenchimento.remove(idxPergunta);
        } else {
            respostasPreenchimento.put(idxPergunta, String.join(" | ", partes));
        }
    }

    private void sincronizarEdicaoAtualNoFormulario() {
        if (!modoEdicaoQuestionario || formularioAtual == null || editoresPerguntas.isEmpty()) {
            return;
        }
        String tituloDigitado = txtTituloAnamnese.getText();
        if (tituloDigitado != null && !tituloDigitado.isBlank()) {
            formularioAtual.setTitulo(tituloDigitado.trim());
        }
        LocalDate dataAb = obterDataAberturaValida();
        if (dataAb != null) {
            formularioAtual.setDataAbertura(dataAb);
        }
        for (int i = 0; i < editoresPerguntas.size(); i++) {
            PerguntaEditorBinder binder = editoresPerguntas.get(i);
            int ord = formularioAtual.getPerguntas().indexOf(binder.pergunta) + 1;
            binder.sincronizar(ord > 0 ? ord : (i + 1));
        }
        if (formularioAtual.getId() == null) {
            rascunhoPersonalizado = copiarFormularioComoRascunho(formularioAtual, formularioAtual.getTitulo());
        }
    }

    private static class PerguntaEditorBinder {

        private final PerguntaAnamnese pergunta;
        private final JTextField txtEnunciado;
        private final JComboBox<TipoPerguntaAnamnese> cmbTipo;
        private final List<JTextField> txtOpcoes;
        private final List<JTextField> txtComplementos;
        private final List<JTextField> txtColunas;
        private final List<JTextField> txtLinhas;

        public PerguntaEditorBinder(
                PerguntaAnamnese pergunta,
                JTextField txtEnunciado,
                JComboBox<TipoPerguntaAnamnese> cmbTipo,
                List<JTextField> txtOpcoes,
                List<JTextField> txtComplementos,
                List<JTextField> txtColunas,
                List<JTextField> txtLinhas) {
            this.pergunta = pergunta;
            this.txtEnunciado = txtEnunciado;
            this.cmbTipo = cmbTipo;
            this.txtOpcoes = txtOpcoes;
            this.txtComplementos = txtComplementos;
            this.txtColunas = txtColunas;
            this.txtLinhas = txtLinhas;
        }

        public void sincronizar(int ordem) {
            pergunta.setOrdem(ordem);
            String enunciado = txtEnunciado.getText();
            pergunta.setEnunciado((enunciado == null || enunciado.isBlank()) ? "Pergunta " + ordem : enunciado.trim());
            TipoPerguntaAnamnese tipo = (TipoPerguntaAnamnese) cmbTipo.getSelectedItem();
            if (tipo != null) {
                pergunta.setTipoPergunta(tipo);
            }
            if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA
                    || tipo == TipoPerguntaAnamnese.CAIXA_SELECAO
                    || tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
                List<String> novasOpcoes = new ArrayList<>();
                for (int i = 0; i < txtOpcoes.size(); i++) {
                    String val = txtOpcoes.get(i).getText();
                    novasOpcoes.add((val == null || val.isBlank()) ? "Opção " + (i + 1) : val.trim());
                }
                pergunta.setOpcoes(novasOpcoes);
            } else if (tipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
                List<String> combinada = new ArrayList<>();
                for (int i = 0; i < txtOpcoes.size(); i++) {
                    String val = txtOpcoes.get(i).getText();
                    combinada.add((val == null || val.isBlank()) ? "Opção " + (i + 1) : val.trim());
                }
                for (int i = 0; i < txtComplementos.size(); i++) {
                    String val = txtComplementos.get(i).getText();
                    combinada.add(PREFIXO_COMPLEMENTO + ((val == null || val.isBlank()) ? "Complemento " + (i + 1) : val.trim()));
                }
                pergunta.setOpcoes(combinada);
            } else if (tipo == TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE) {
                List<String> combinada = new ArrayList<>();
                for (int i = 0; i < txtColunas.size(); i++) {
                    String val = txtColunas.get(i).getText();
                    combinada.add(PREFIXO_COLUNA + ((val == null || val.isBlank()) ? "Coluna " + (i + 1) : val.trim()));
                }
                for (int i = 0; i < txtLinhas.size(); i++) {
                    String val = txtLinhas.get(i).getText();
                    combinada.add(PREFIXO_LINHA + ((val == null || val.isBlank()) ? "Linha " + (i + 1) : val.trim()));
                }
                pergunta.setOpcoes(combinada);
            }
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

        pnlCabecalho = new javax.swing.JPanel();
        circularIconHeader = new com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel();
        lblTituloPagina = new javax.swing.JLabel();
        lblSubtituloPagina = new javax.swing.JLabel();
        container1 = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        lblTituloModelos = new javax.swing.JLabel();
        pnlIndicadorVerde = new javax.swing.JPanel();
        cmbAnamnesesDesativadas = new javax.swing.JComboBox<>();
        btnCriarNovaAnamnese = new javax.swing.JButton();
        sepModelos = new javax.swing.JSeparator();
        btnInfantilAdolescente = new javax.swing.JButton();
        btnAdulto = new javax.swing.JButton();
        cmbPersonalizados = new javax.swing.JComboBox<>();
        btnCriarPersonalizado = new javax.swing.JButton();
        containerFormulario = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        btnFecharVisualizacaoDesativada = new javax.swing.JButton();
        txtTituloAnamnese = new javax.swing.JTextField();
        lblDataAbertura = new javax.swing.JLabel();
        txtDataAbertura = new javax.swing.JTextField();
        btnEditarPerguntasModelo = new javax.swing.JButton();
        btnLimparPreenchimento = new javax.swing.JButton();
        btnBaixarAnamneseSalva = new javax.swing.JButton();
        btnEditarAnamneseSalva = new javax.swing.JButton();
        btnExpandir = new javax.swing.JButton();
        sepTituloFormulario = new javax.swing.JSeparator();
        scrPerguntas = new javax.swing.JScrollPane();
        pnlConteudoAnamnese = new javax.swing.JPanel();
        pnlRodapeAcoes = new javax.swing.JPanel();
        btnAdicionarPergunta = new javax.swing.JButton();
        btnSalvarAnamnese = new javax.swing.JButton();

        setBackground(new java.awt.Color(244, 249, 247));

        pnlCabecalho.setBackground(new java.awt.Color(249, 252, 251));

        circularIconHeader.setBackground(new java.awt.Color(214, 237, 226));
        circularIconHeader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuPacienteIcons/stethoscope_green.png"))); // NOI18N
        circularIconHeader.setCustomCircleSize(64);
        circularIconHeader.setIconHeight(36);
        circularIconHeader.setIconWidth(36);
        circularIconHeader.setShadowBlur(0);

        lblTituloPagina.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTituloPagina.setForeground(new java.awt.Color(13, 82, 65));
        lblTituloPagina.setText("Anamnese");

        lblSubtituloPagina.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSubtituloPagina.setForeground(new java.awt.Color(82, 107, 97));
        lblSubtituloPagina.setText("Histórico e informações do paciente");

        javax.swing.GroupLayout pnlCabecalhoLayout = new javax.swing.GroupLayout(pnlCabecalho);
        pnlCabecalho.setLayout(pnlCabecalhoLayout);
        pnlCabecalhoLayout.setHorizontalGroup(
            pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(circularIconHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTituloPagina)
                    .addComponent(lblSubtituloPagina))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCabecalhoLayout.setVerticalGroup(
            pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(circularIconHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblTituloPagina)
                        .addGap(2, 2, 2)
                        .addComponent(lblSubtituloPagina)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        container1.setBackground(new java.awt.Color(255, 255, 255));

        lblTituloModelos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTituloModelos.setForeground(new java.awt.Color(13, 82, 65));
        lblTituloModelos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuPacienteIcons/medical-report_green.png"))); // NOI18N
        lblTituloModelos.setText("Anamnese modelos");
        lblTituloModelos.setIconTextGap(12);

        pnlIndicadorVerde.setBackground(new java.awt.Color(46, 150, 96));
        pnlIndicadorVerde.setPreferredSize(new java.awt.Dimension(125, 2));

        javax.swing.GroupLayout pnlIndicadorVerdeLayout = new javax.swing.GroupLayout(pnlIndicadorVerde);
        pnlIndicadorVerde.setLayout(pnlIndicadorVerdeLayout);
        pnlIndicadorVerdeLayout.setHorizontalGroup(
            pnlIndicadorVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        pnlIndicadorVerdeLayout.setVerticalGroup(
            pnlIndicadorVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        cmbAnamnesesDesativadas.setBackground(new java.awt.Color(235, 237, 240));
        cmbAnamnesesDesativadas.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        cmbAnamnesesDesativadas.setForeground(new java.awt.Color(120, 126, 134));
        cmbAnamnesesDesativadas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Anamneses Desativadas" }));
        cmbAnamnesesDesativadas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbAnamnesesDesativadas.addActionListener(this::cmbAnamnesesDesativadasActionPerformed);

        btnCriarNovaAnamnese.setBackground(new java.awt.Color(18, 132, 43));
        btnCriarNovaAnamnese.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCriarNovaAnamnese.setForeground(new java.awt.Color(255, 255, 255));
        btnCriarNovaAnamnese.setText("Criar Nova Anamnese");
        btnCriarNovaAnamnese.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCriarNovaAnamnese.setFocusPainted(false);
        btnCriarNovaAnamnese.setIconTextGap(10);
        btnCriarNovaAnamnese.addActionListener(this::btnCriarNovaAnamneseActionPerformed);

        sepModelos.setForeground(new java.awt.Color(235, 242, 238));

        btnInfantilAdolescente.setBackground(new java.awt.Color(31, 122, 68));
        btnInfantilAdolescente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnInfantilAdolescente.setForeground(new java.awt.Color(255, 255, 255));
        btnInfantilAdolescente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuPacienteIcons/paciente_white.png"))); // NOI18N
        btnInfantilAdolescente.setText("Infantil / Adolescente");
        btnInfantilAdolescente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInfantilAdolescente.setFocusPainted(false);
        btnInfantilAdolescente.setIconTextGap(10);
        btnInfantilAdolescente.addActionListener(this::btnInfantilAdolescenteActionPerformed);

        btnAdulto.setBackground(new java.awt.Color(241, 243, 245));
        btnAdulto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdulto.setForeground(new java.awt.Color(67, 77, 89));
        btnAdulto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/MenuPacienteIcons/paciente_green.png"))); // NOI18N
        btnAdulto.setText("Adulto");
        btnAdulto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdulto.setFocusPainted(false);
        btnAdulto.setIconTextGap(10);
        btnAdulto.addActionListener(this::btnAdultoActionPerformed);

        cmbPersonalizados.setBackground(new java.awt.Color(241, 243, 245));
        cmbPersonalizados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cmbPersonalizados.setForeground(new java.awt.Color(67, 77, 89));
        cmbPersonalizados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecionar Anamnese..." }));
        cmbPersonalizados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbPersonalizados.addActionListener(this::cmbPersonalizadosActionPerformed);

        btnCriarPersonalizado.setBackground(new java.awt.Color(241, 243, 245));
        btnCriarPersonalizado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCriarPersonalizado.setForeground(new java.awt.Color(67, 77, 89));
        btnCriarPersonalizado.setText("Criar Personalizado");
        btnCriarPersonalizado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCriarPersonalizado.setFocusPainted(false);
        btnCriarPersonalizado.setIconTextGap(10);
        btnCriarPersonalizado.addActionListener(this::btnCriarPersonalizadoActionPerformed);

        containerFormulario.setBackground(new java.awt.Color(255, 255, 255));

        btnFecharVisualizacaoDesativada.setBackground(new java.awt.Color(255, 255, 255));
        btnFecharVisualizacaoDesativada.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnFecharVisualizacaoDesativada.setForeground(new java.awt.Color(20, 24, 28));
        btnFecharVisualizacaoDesativada.setText("✕");
        btnFecharVisualizacaoDesativada.setToolTipText("Sair da anamnese desativada e voltar para a anamnese principal");
        btnFecharVisualizacaoDesativada.setBorder(null);
        btnFecharVisualizacaoDesativada.setContentAreaFilled(false);
        btnFecharVisualizacaoDesativada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFecharVisualizacaoDesativada.setFocusPainted(false);
        btnFecharVisualizacaoDesativada.addActionListener(this::btnFecharVisualizacaoDesativadaActionPerformed);

        txtTituloAnamnese.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        txtTituloAnamnese.setForeground(new java.awt.Color(13, 17, 23));
        txtTituloAnamnese.setText("ANAMNESE – INFANTIL E ADOLESCENTE");
        txtTituloAnamnese.setBorder(null);

        lblDataAbertura.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDataAbertura.setForeground(new java.awt.Color(13, 82, 65));
        lblDataAbertura.setText("Data de abertura:");

        txtDataAbertura.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtDataAbertura.setForeground(new java.awt.Color(30, 34, 38));
        txtDataAbertura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataAbertura.setText("27/10/2026");
        txtDataAbertura.setToolTipText("Data de abertura da anamnese (dd/mm/aaaa)");

        btnEditarPerguntasModelo.setBackground(new java.awt.Color(242, 248, 245));
        btnEditarPerguntasModelo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEditarPerguntasModelo.setForeground(new java.awt.Color(31, 122, 68));
        btnEditarPerguntasModelo.setText("Editar perguntas deste modelo");
        btnEditarPerguntasModelo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarPerguntasModelo.setFocusPainted(false);
        btnEditarPerguntasModelo.addActionListener(this::btnEditarPerguntasModeloActionPerformed);

        btnLimparPreenchimento.setBackground(new java.awt.Color(253, 244, 244));
        btnLimparPreenchimento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        btnLimparPreenchimento.setToolTipText("Limpar dados preenchidos nesta anamnese");
        btnLimparPreenchimento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparPreenchimento.setFocusPainted(false);
        btnLimparPreenchimento.addActionListener(this::btnLimparPreenchimentoActionPerformed);

        btnBaixarAnamneseSalva.setBackground(new java.awt.Color(18, 132, 43));
        btnBaixarAnamneseSalva.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBaixarAnamneseSalva.setForeground(new java.awt.Color(255, 255, 255));
        btnBaixarAnamneseSalva.setText("Baixar");
        btnBaixarAnamneseSalva.setToolTipText("Salvar / Baixar anamnese em arquivo (PDF, Word/DOCX, DOC, TXT)");
        btnBaixarAnamneseSalva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBaixarAnamneseSalva.setFocusPainted(false);
        btnBaixarAnamneseSalva.addActionListener(this::btnBaixarAnamneseSalvaActionPerformed);

        btnEditarAnamneseSalva.setBackground(new java.awt.Color(18, 132, 43));
        btnEditarAnamneseSalva.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEditarAnamneseSalva.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarAnamneseSalva.setText("Retificar");
        btnEditarAnamneseSalva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarAnamneseSalva.setFocusPainted(false);
        btnEditarAnamneseSalva.addActionListener(this::btnEditarAnamneseSalvaActionPerformed);

        btnExpandir.setBackground(new java.awt.Color(255, 255, 255));
        btnExpandir.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        btnExpandir.setForeground(new java.awt.Color(120, 120, 120));
        btnExpandir.setText("⛶");
        btnExpandir.setToolTipText("Expandir visualização");
        btnExpandir.setBorder(null);
        btnExpandir.setContentAreaFilled(false);
        btnExpandir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExpandir.setFocusPainted(false);
        btnExpandir.addActionListener(this::btnExpandirActionPerformed);

        sepTituloFormulario.setForeground(new java.awt.Color(210, 217, 226));

        scrPerguntas.setBorder(null);

        pnlConteudoAnamnese.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlConteudoAnamneseLayout = new javax.swing.GroupLayout(pnlConteudoAnamnese);
        pnlConteudoAnamnese.setLayout(pnlConteudoAnamneseLayout);
        pnlConteudoAnamneseLayout.setHorizontalGroup(
            pnlConteudoAnamneseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 880, Short.MAX_VALUE)
        );
        pnlConteudoAnamneseLayout.setVerticalGroup(
            pnlConteudoAnamneseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        scrPerguntas.setViewportView(pnlConteudoAnamnese);

        pnlRodapeAcoes.setBackground(new java.awt.Color(255, 255, 255));

        btnAdicionarPergunta.setBackground(new java.awt.Color(255, 255, 255));
        btnAdicionarPergunta.setFont(new java.awt.Font("Segoe UI", 0, 34)); // NOI18N
        btnAdicionarPergunta.setForeground(new java.awt.Color(30, 30, 30));
        btnAdicionarPergunta.setText("+");
        btnAdicionarPergunta.setToolTipText("Adicionar nova pergunta ou seção");
        btnAdicionarPergunta.setBorder(null);
        btnAdicionarPergunta.setContentAreaFilled(false);
        btnAdicionarPergunta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionarPergunta.setFocusPainted(false);
        btnAdicionarPergunta.addActionListener(this::btnAdicionarPerguntaActionPerformed);

        btnSalvarAnamnese.setBackground(new java.awt.Color(14, 138, 37));
        btnSalvarAnamnese.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSalvarAnamnese.setForeground(new java.awt.Color(255, 255, 255));
        btnSalvarAnamnese.setText("SALVAR");
        btnSalvarAnamnese.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalvarAnamnese.setFocusPainted(false);
        btnSalvarAnamnese.addActionListener(this::btnSalvarAnamneseActionPerformed);

        javax.swing.GroupLayout pnlRodapeAcoesLayout = new javax.swing.GroupLayout(pnlRodapeAcoes);
        pnlRodapeAcoes.setLayout(pnlRodapeAcoesLayout);
        pnlRodapeAcoesLayout.setHorizontalGroup(
            pnlRodapeAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRodapeAcoesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlRodapeAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalvarAnamnese, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRodapeAcoesLayout.createSequentialGroup()
                        .addComponent(btnAdicionarPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRodapeAcoesLayout.setVerticalGroup(
            pnlRodapeAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRodapeAcoesLayout.createSequentialGroup()
                .addComponent(btnAdicionarPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnSalvarAnamnese, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout containerFormularioLayout = new javax.swing.GroupLayout(containerFormulario);
        containerFormulario.setLayout(containerFormularioLayout);
        containerFormularioLayout.setHorizontalGroup(
            containerFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerFormularioLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(containerFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrPerguntas)
                    .addComponent(sepTituloFormulario)
                    .addGroup(containerFormularioLayout.createSequentialGroup()
                        .addComponent(btnFecharVisualizacaoDesativada, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTituloAnamnese, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDataAbertura)
                        .addGap(6, 6, 6)
                        .addComponent(txtDataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditarPerguntasModelo)
                        .addGap(6, 6, 6)
                        .addComponent(btnLimparPreenchimento, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnBaixarAnamneseSalva, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnEditarAnamneseSalva, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnExpandir, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlRodapeAcoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        containerFormularioLayout.setVerticalGroup(
            containerFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerFormularioLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(containerFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFecharVisualizacaoDesativada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTituloAnamnese, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(lblDataAbertura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDataAbertura)
                    .addComponent(btnEditarPerguntasModelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimparPreenchimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBaixarAnamneseSalva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditarAnamneseSalva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExpandir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(sepTituloFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(scrPerguntas, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(pnlRodapeAcoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout container1Layout = new javax.swing.GroupLayout(container1);
        container1.setLayout(container1Layout);
        container1Layout.setHorizontalGroup(
            container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sepModelos)
            .addGroup(container1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(container1Layout.createSequentialGroup()
                        .addGroup(container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTituloModelos)
                            .addComponent(pnlIndicadorVerde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbAnamnesesDesativadas, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCriarNovaAnamnese, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(container1Layout.createSequentialGroup()
                        .addComponent(containerFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(22, 22, 22))
                    .addGroup(container1Layout.createSequentialGroup()
                        .addComponent(btnInfantilAdolescente, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnAdulto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(cmbPersonalizados, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnCriarPersonalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(80, Short.MAX_VALUE))))
        );
        container1Layout.setVerticalGroup(
            container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(container1Layout.createSequentialGroup()
                        .addComponent(lblTituloModelos)
                        .addGap(10, 10, 10)
                        .addComponent(pnlIndicadorVerde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbAnamnesesDesativadas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCriarNovaAnamnese, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(sepModelos, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInfantilAdolescente, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdulto, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPersonalizados, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCriarPersonalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(containerFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCabecalho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(container1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlCabecalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(container1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnInfantilAdolescenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfantilAdolescenteActionPerformed
        selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
    }//GEN-LAST:event_btnInfantilAdolescenteActionPerformed

    private void btnAdultoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdultoActionPerformed
        selecionarModeloPadrao(TipoModeloAnamnese.ADULTO);
    }//GEN-LAST:event_btnAdultoActionPerformed

    private void btnCriarPersonalizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriarPersonalizadoActionPerformed
        iniciarModoCriarPersonalizado();
    }//GEN-LAST:event_btnCriarPersonalizadoActionPerformed

    private void cmbPersonalizadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPersonalizadosActionPerformed
        if (atualizandoCombo || modoVisualizacaoAnamnese) {
            return;
        }
        salvarRascunhoSeEstiverCriandoPersonalizado();
        salvarCachePreenchimentoNoDisco();
        atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        Object item = cmbPersonalizados.getSelectedItem();
        if (item instanceof FormularioAnamnese) {
            FormularioAnamnese selecionado = (FormularioAnamnese) item;
            abrirPreenchimentoFormulario(selecionado, true);
        }
    }//GEN-LAST:event_cmbPersonalizadosActionPerformed

    private void cmbAnamnesesDesativadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAnamnesesDesativadasActionPerformed
        if (atualizandoComboDesativadas) {
            return;
        }
        Object selecionado = cmbAnamnesesDesativadas.getSelectedItem();
        if (selecionado instanceof ItemAnamneseDesativada) {
            ItemAnamneseDesativada item = (ItemAnamneseDesativada) selecionado;
            if (item.getResposta() != null) {
                if (!modoVisualizacaoAnamnese) {
                    salvarRascunhoSeEstiverCriandoPersonalizado();
                    salvarCachePreenchimentoNoDisco();
                    this.desativadaAbertaAPartirDeModelos = true;
                    this.abaAnteriorModelos = this.abaAtiva;
                    this.formularioAnteriorModelos = this.formularioAtual;
                    this.estavaEditandoQuestionarioModelos = this.modoEdicaoQuestionario;
                }
                abrirVisualizacaoResposta(item.getResposta());
            }
        }
    }//GEN-LAST:event_cmbAnamnesesDesativadasActionPerformed

    private void btnFecharVisualizacaoDesativadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharVisualizacaoDesativadaActionPerformed
        if (!desativadaAbertaAPartirDeModelos) {
            RespostaAnamnese ativa = obterRespostaAtivaPaciente();
            if (ativa != null) {
                abrirVisualizacaoResposta(ativa);
                return;
            }
        }
        restaurarTelaModelosAnterior();
    }//GEN-LAST:event_btnFecharVisualizacaoDesativadaActionPerformed

    private void restaurarTelaModelosAnterior() {
        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;

        if (estavaEditandoQuestionarioModelos && formularioAnteriorModelos != null) {
            atualizarEstadoBarra(abaAnteriorModelos);
            iniciarConstrutorComFormulario(formularioAnteriorModelos);
        } else if (abaAnteriorModelos == AbaBarraAnamnese.DROPDOWN_SELECIONAR && formularioAnteriorModelos != null) {
            carregarDropdownFormularios(formularioAnteriorModelos);
            abrirPreenchimentoFormulario(formularioAnteriorModelos, true);
        } else if (abaAnteriorModelos == AbaBarraAnamnese.ADULTO) {
            selecionarModeloPadrao(TipoModeloAnamnese.ADULTO);
        } else {
            selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        }
    }

    private void btnCriarNovaAnamneseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriarNovaAnamneseActionPerformed
        Object[] opcoes = {"Continuar", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "A anamnese atual sera desativada",
                "Criar Nova Anamnese",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
        if (escolha == 0) {
            anamneseDAO.desativarRespostasPorPaciente(paciente);
            modoVisualizacaoAnamnese = false;
            respostaVisualizada = null;
            selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        }
    }//GEN-LAST:event_btnCriarNovaAnamneseActionPerformed

    private void btnBaixarAnamneseSalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaixarAnamneseSalvaActionPerformed
        if (!modoVisualizacaoAnamnese || respostaVisualizada == null) {
            return;
        }
        abrirDialogoEscolhaFormatoExportacao(respostaVisualizada);
    }//GEN-LAST:event_btnBaixarAnamneseSalvaActionPerformed

    private void abrirDialogoEscolhaFormatoExportacao(RespostaAnamnese resposta) {
        String[] formatos = {
            "Documento PDF (.pdf)",
            "Documento Word Moderno (.docx)",
            "Documento Word 97-2003 (.doc)",
            "Arquivo de Texto (.txt)"
        };

        JPanel painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new BoxLayout(painelOpcoes, BoxLayout.Y_AXIS));
        painelOpcoes.setBorder(new EmptyBorder(6, 6, 6, 6));

        JLabel lblInstrucao = new JLabel("Escolha o formato no qual a anamnese será salva:");
        lblInstrucao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInstrucao.setForeground(new Color(13, 82, 65));
        lblInstrucao.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelOpcoes.add(lblInstrucao);
        painelOpcoes.add(Box.createVerticalStrut(10));

        ButtonGroup grupoFormatos = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[formatos.length];
        for (int i = 0; i < formatos.length; i++) {
            radios[i] = new JRadioButton(formatos[i]);
            radios[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            radios[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                radios[i].setSelected(true);
            }
            grupoFormatos.add(radios[i]);
            painelOpcoes.add(radios[i]);
            painelOpcoes.add(Box.createVerticalStrut(4));
        }

        Object[] botoes = {"Salvar", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                painelOpcoes,
                "Salvar / Baixar Anamnese",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                botoes,
                botoes[0]
        );
        if (escolha != 0) {
            return;
        }

        int idxFormato = 0;
        for (int i = 0; i < radios.length; i++) {
            if (radios[i].isSelected()) {
                idxFormato = i;
                break;
            }
        }

        String extensao;
        String descricaoFiltro;
        switch (idxFormato) {
            case 1:
                extensao = "docx";
                descricaoFiltro = "Documento Microsoft Word (*.docx)";
                break;
            case 2:
                extensao = "doc";
                descricaoFiltro = "Documento Word 97-2003 (*.doc)";
                break;
            case 3:
                extensao = "txt";
                descricaoFiltro = "Arquivo de Texto (*.txt)";
                break;
            default:
                extensao = "pdf";
                descricaoFiltro = "Documento PDF (*.pdf)";
                break;
        }

        FormularioAnamnese form = resolverFormularioDaResposta(resposta);
        String tituloBase = (form != null && form.getTitulo() != null) ? form.getTitulo() : "Anamnese";
        String nomePac = (paciente != null && paciente.getNome() != null && !paciente.getNome().isBlank())
                ? paciente.getNome().trim()
                : "Paciente";
        String dataStr = (resposta.getDataAbertura() != null ? resposta.getDataAbertura() : LocalDate.now())
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String nomeSugerido = (tituloBase + "_" + nomePac + "_" + dataStr)
                .replaceAll("[\\\\/:*?\"<>|\\s]+", "_") + "." + extensao;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar Anamnese como ." + extensao.toUpperCase());
        chooser.setFileFilter(new FileNameExtensionFilter(descricaoFiltro, extensao));
        chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), nomeSugerido));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File destino = chooser.getSelectedFile();
        if (!destino.getName().toLowerCase().endsWith("." + extensao)) {
            destino = new File(destino.getParentFile(), destino.getName() + "." + extensao);
        }

        try {
            List<String[]> linhasRelatorio = montarLinhasExportacao(resposta, form);
            switch (extensao) {
                case "docx":
                    exportarParaDocx(destino, linhasRelatorio);
                    break;
                case "doc":
                    exportarParaDoc(destino, linhasRelatorio);
                    break;
                case "txt":
                    exportarParaTxt(destino, linhasRelatorio);
                    break;
                default:
                    exportarParaPdf(destino, linhasRelatorio);
                    break;
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Anamnese salva com sucesso em:\n" + destino.getAbsolutePath(),
                    "Arquivo Salvo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar o arquivo: " + ex.getMessage(),
                    "Erro na Exportação",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private List<String[]> montarLinhasExportacao(RespostaAnamnese resposta, FormularioAnamnese form) {
        List<String[]> linhas = new ArrayList<>();
        String titulo = (form != null && form.getTitulo() != null) ? form.getTitulo() : "Anamnese";
        linhas.add(new String[]{"TITULO", titulo});

        String nomePac = (paciente != null && paciente.getNome() != null && !paciente.getNome().isBlank())
                ? paciente.getNome()
                : "Não informado";
        String dataAb = (resposta.getDataAbertura() != null ? resposta.getDataAbertura() : LocalDate.now()).format(FORMATO_DATA);
        linhas.add(new String[]{"META", "Paciente: " + nomePac + "   |   Data de abertura: " + dataAb});

        if (!resposta.isAtiva()) {
            LocalDateTime dtDesat = resposta.getDataHoraDesativacao();
            if (dtDesat != null) {
                linhas.add(new String[]{"META", "Status: Desativada em " + dtDesat.format(FORMATO_DATA_HORA)});
            } else {
                linhas.add(new String[]{"META", "Status: Desativada"});
            }
        }

        Map<String, String> dados = desserializarDadosCampos(resposta);
        Map<String, Integer> contadorEnunciados = new HashMap<>();
        List<PerguntaAnamnese> perguntas = form != null ? form.getPerguntas() : Collections.emptyList();

        for (int i = 0; i < perguntas.size(); i++) {
            PerguntaAnamnese p = perguntas.get(i);
            TipoPerguntaAnamnese tipo = p.getTipoPergunta() != null ? p.getTipoPergunta() : TipoPerguntaAnamnese.TEXTO_LIVRE;

            String chaveBase = tipo.name() + "_" + normalizarChave(p.getEnunciado());
            int ocorrencia = contadorEnunciados.getOrDefault(chaveBase, 0) + 1;
            contadorEnunciados.put(chaveBase, ocorrencia);

            String qKey = obterChavePergunta(p, ocorrencia, i);
            String idxKey = "IDX_" + i;

            if (tipo == TipoPerguntaAnamnese.SECAO_TITULO) {
                linhas.add(new String[]{"SECAO", p.getEnunciado()});
                continue;
            }

            String valorResposta = extrairTextoRespostaSalva(p, tipo, dados, qKey, idxKey);
            linhas.add(new String[]{"PERGUNTA", p.getEnunciado(), valorResposta});
        }
        return linhas;
    }

    private String extrairTextoRespostaSalva(
            PerguntaAnamnese p,
            TipoPerguntaAnamnese tipo,
            Map<String, String> dados,
            String qKey,
            String idxKey) {
        if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA) {
            String op = obterValorMapaCampos(dados, qKey + "__OPCAO", idxKey + "__OPCAO");
            return (op != null && !op.isBlank()) ? limparPrefixoParentesesOpcao(op) : "Não preenchido";
        }
        if (tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
            List<String> marcadas = new ArrayList<>();
            for (String opBruta : p.getOpcoes()) {
                String op = limparPrefixoParentesesOpcao(opBruta);
                String opKey = normalizarChave(op);
                String v = obterValorMapaCampos(dados, qKey + "__CHK_" + opKey, idxKey + "__CHK_" + opKey);
                if ("true".equals(v)) {
                    marcadas.add(op);
                }
            }
            return marcadas.isEmpty() ? "Não preenchido" : String.join(", ", marcadas);
        }
        if (tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
            List<String> partes = new ArrayList<>();
            for (String rotulo : p.getOpcoes()) {
                String cKey = normalizarChave(rotulo);
                String v = obterValorMapaCampos(dados, qKey + "__CAMPO_" + cKey, idxKey + "__CAMPO_" + cKey);
                if (v != null && !v.isBlank()) {
                    partes.add(rotulo + ": " + v.trim());
                }
            }
            return partes.isEmpty() ? "Não preenchido" : String.join(" | ", partes);
        }
        if (tipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
            List<String> partes = new ArrayList<>();
            for (String item : p.getOpcoes()) {
                if (item.startsWith(PREFIXO_COMPLEMENTO)) {
                    String rot = item.substring(PREFIXO_COMPLEMENTO.length()).trim();
                    String compKey = normalizarChave(rot);
                    String v = obterValorMapaCampos(dados, qKey + "__COMP_" + compKey, idxKey + "__COMP_" + compKey);
                    if (v != null && !v.isBlank()) {
                        partes.add(rot + ": " + v.trim());
                    }
                } else if (!item.startsWith(PREFIXO_COLUNA) && !item.startsWith(PREFIXO_LINHA)) {
                    String op = limparPrefixoParentesesOpcao(item);
                    String escKey = normalizarChave(op);
                    String v = obterValorMapaCampos(dados, qKey + "__ESC_" + escKey, idxKey + "__ESC_" + escKey);
                    if ("true".equals(v)) {
                        partes.add(op);
                    }
                }
            }
            return partes.isEmpty() ? "Não preenchido" : String.join(" | ", partes);
        }
        if (tipo == TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE) {
            List<String> colunas = new ArrayList<>();
            List<String> linhas = new ArrayList<>();
            for (String item : p.getOpcoes()) {
                if (item.startsWith(PREFIXO_COLUNA)) {
                    colunas.add(item.substring(PREFIXO_COLUNA.length()));
                } else if (item.startsWith(PREFIXO_LINHA)) {
                    linhas.add(item.substring(PREFIXO_LINHA.length()));
                }
            }
            List<String> marcados = new ArrayList<>();
            for (String lin : linhas) {
                for (String col : colunas) {
                    String ch = lin + " - " + col;
                    String gKey = normalizarChave(ch);
                    String v = obterValorMapaCampos(dados, qKey + "__GRADE_" + gKey, idxKey + "__GRADE_" + gKey);
                    if ("true".equals(v)) {
                        marcados.add(ch);
                    }
                }
            }
            return marcados.isEmpty() ? "Nenhum horário marcado" : String.join("; ", marcados);
        }
        if (tipo == TipoPerguntaAnamnese.ESTRELAS) {
            String v = obterValorMapaCampos(dados, qKey + "__ESTRELAS", idxKey + "__ESTRELAS");
            return (v != null && !v.isBlank()) ? (v.trim() + " estrela(s)") : "Não preenchido";
        }
        String v = obterValorMapaCampos(dados, qKey + "__TEXTO", idxKey + "__TEXTO");
        return (v != null && !v.isBlank()) ? v.trim() : "Não preenchido";
    }

    private void exportarParaTxt(File destino, List<String[]> linhas) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String[] item : linhas) {
            String tipo = item[0];
            if ("TITULO".equals(tipo)) {
                sb.append("================================================================\n");
                sb.append("  ").append(item[1]).append("\n");
                sb.append("================================================================\n");
            } else if ("META".equals(tipo)) {
                sb.append(item[1]).append("\n");
            } else if ("SECAO".equals(tipo)) {
                sb.append("\n----------------------------------------------------------------\n");
                sb.append("[ ").append(item[1]).append(" ]\n");
                sb.append("----------------------------------------------------------------\n");
            } else if ("PERGUNTA".equals(tipo)) {
                sb.append("• ").append(item[1]).append("\n");
                sb.append("  Resposta: ").append(item[2]).append("\n\n");
            }
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8)) {
            writer.write(sb.toString());
        }
    }

    private String escaparXml(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private void exportarParaDoc(File destino, List<String[]> linhas) throws Exception {
        StringBuilder html = new StringBuilder();
        html.append("<html xmlns:o='urn:schemas-microsoft-com:office:office' ")
            .append("xmlns:w='urn:schemas-microsoft-com:office:word' ")
            .append("xmlns='http://www.w3.org/TR/REC-html40'>")
            .append("<head><meta charset='utf-8'><title>Anamnese</title>")
            .append("<style>")
            .append("body { font-family: 'Segoe UI', Arial, sans-serif; font-size: 11pt; color: #1e2428; }")
            .append("h1 { color: #0d5241; font-size: 18pt; border-bottom: 2px solid #1f7a44; padding-bottom: 6px; }")
            .append("h2 { color: #0d5241; background-color: #ecf6f0; padding: 6px 10px; font-size: 13pt; margin-top: 16px; }")
            .append(".meta { color: #434d59; font-size: 10pt; margin-bottom: 6px; }")
            .append(".pergunta { font-weight: bold; margin-top: 10px; }")
            .append(".resposta { margin-left: 12px; margin-bottom: 8px; color: #222; }")
            .append("</style></head><body>");

        for (String[] item : linhas) {
            String tipo = item[0];
            if ("TITULO".equals(tipo)) {
                html.append("<h1>").append(escaparXml(item[1])).append("</h1>");
            } else if ("META".equals(tipo)) {
                html.append("<div class='meta'><b>").append(escaparXml(item[1])).append("</b></div>");
            } else if ("SECAO".equals(tipo)) {
                html.append("<h2>").append(escaparXml(item[1])).append("</h2>");
            } else if ("PERGUNTA".equals(tipo)) {
                html.append("<div class='pergunta'>").append(escaparXml(item[1])).append("</div>");
                html.append("<div class='resposta'>").append(escaparXml(item[2])).append("</div>");
            }
        }
        html.append("</body></html>");

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8)) {
            writer.write(html.toString());
        }
    }

    private void exportarParaDocx(File destino, List<String[]> linhas) throws Exception {
        String contentTypes = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>"
                + "</Types>";

        String rels = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>"
                + "</Relationships>";

        StringBuilder body = new StringBuilder();
        body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
            .append("<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">")
            .append("<w:body>");

        for (String[] item : linhas) {
            String tipo = item[0];
            if ("TITULO".equals(tipo)) {
                body.append("<w:p><w:r><w:rPr><w:b/><w:color w:val=\"0D5241\"/><w:sz w:val=\"34\"/></w:rPr><w:t>")
                    .append(escaparXml(item[1]))
                    .append("</w:t></w:r></w:p>");
            } else if ("META".equals(tipo)) {
                body.append("<w:p><w:r><w:rPr><w:b/><w:color w:val=\"434D59\"/><w:sz w:val=\"20\"/></w:rPr><w:t>")
                    .append(escaparXml(item[1]))
                    .append("</w:t></w:r></w:p>");
            } else if ("SECAO".equals(tipo)) {
                body.append("<w:p><w:pPr><w:spacing w:before=\"200\" w:after=\"80\"/></w:pPr>")
                    .append("<w:r><w:rPr><w:b/><w:color w:val=\"1F7A44\"/><w:sz w:val=\"26\"/></w:rPr><w:t>")
                    .append(escaparXml(item[1]))
                    .append("</w:t></w:r></w:p>");
            } else if ("PERGUNTA".equals(tipo)) {
                body.append("<w:p><w:pPr><w:spacing w:before=\"100\" w:after=\"40\"/></w:pPr>")
                    .append("<w:r><w:rPr><w:b/><w:sz w:val=\"22\"/></w:rPr><w:t>")
                    .append(escaparXml(item[1]))
                    .append("</w:t></w:r></w:p>");
                body.append("<w:p><w:pPr><w:ind w:left=\"240\"/><w:spacing w:after=\"120\"/></w:pPr>")
                    .append("<w:r><w:rPr><w:sz w:val=\"21\"/></w:rPr><w:t>")
                    .append(escaparXml(item[2]))
                    .append("</w:t></w:r></w:p>");
            }
        }

        body.append("<w:sectPr><w:pgSz w:w=\"11906\" w:h=\"16838\"/>")
            .append("<w:pgMar w:top=\"1134\" w:right=\"1134\" w:bottom=\"1134\" w:left=\"1134\"/></w:sectPr>")
            .append("</w:body></w:document>");

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destino))) {
            zos.putNextEntry(new ZipEntry("[Content_Types].xml"));
            zos.write(contentTypes.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("_rels/.rels"));
            zos.write(rels.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("word/document.xml"));
            zos.write(body.toString().getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }
    }

    private String escaparTextoPdfWinAnsi(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '\u2013' || c == '\u2014') {
                c = '-';
            } else if (c == '\u2605') {
                c = '*';
            }
            if (c == '\\' || c == '(' || c == ')') {
                sb.append('\\').append(c);
            } else if (c >= 32 && c <= 255) {
                sb.append(c);
            } else {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private List<String> quebrarLinhasPdf(String texto, int maxChars) {
        List<String> resultado = new ArrayList<>();
        if (texto == null || texto.isEmpty()) {
            resultado.add("");
            return resultado;
        }
        for (String paragrafo : texto.split("\\r?\\n")) {
            String[] palavras = paragrafo.split("\\s+");
            StringBuilder linhaAtual = new StringBuilder();
            for (String p : palavras) {
                if (linhaAtual.length() + p.length() + 1 > maxChars && linhaAtual.length() > 0) {
                    resultado.add(linhaAtual.toString());
                    linhaAtual.setLength(0);
                }
                if (linhaAtual.length() > 0) {
                    linhaAtual.append(' ');
                }
                linhaAtual.append(p);
            }
            if (linhaAtual.length() > 0) {
                resultado.add(linhaAtual.toString());
            }
        }
        return resultado;
    }

    private void exportarParaPdf(File destino, List<String[]> linhas) throws Exception {
        List<List<String>> paginasComandos = new ArrayList<>();
        List<String> paginaAtual = new ArrayList<>();
        int y = 790;

        for (String[] item : linhas) {
            String tipo = item[0];
            if ("TITULO".equals(tipo)) {
                if (y < 90) {
                    paginasComandos.add(paginaAtual);
                    paginaAtual = new ArrayList<>();
                    y = 790;
                }
                paginaAtual.add("BT /F2 15 Tf 45 " + y + " Td (" + escaparTextoPdfWinAnsi(item[1]) + ") Tj ET");
                y -= 22;
            } else if ("META".equals(tipo)) {
                paginaAtual.add("BT /F2 10 Tf 45 " + y + " Td (" + escaparTextoPdfWinAnsi(item[1]) + ") Tj ET");
                y -= 16;
            } else if ("SECAO".equals(tipo)) {
                y -= 6;
                if (y < 80) {
                    paginasComandos.add(paginaAtual);
                    paginaAtual = new ArrayList<>();
                    y = 790;
                }
                paginaAtual.add("BT /F2 12 Tf 45 " + y + " Td ([" + escaparTextoPdfWinAnsi(item[1]) + "]) Tj ET");
                y -= 18;
            } else if ("PERGUNTA".equals(tipo)) {
                List<String> qWrap = quebrarLinhasPdf(item[1], 88);
                List<String> rWrap = quebrarLinhasPdf("Resposta: " + item[2], 92);
                int alturaBloco = (qWrap.size() + rWrap.size()) * 14 + 6;
                if (y - alturaBloco < 50) {
                    paginasComandos.add(paginaAtual);
                    paginaAtual = new ArrayList<>();
                    y = 790;
                }
                for (String ql : qWrap) {
                    paginaAtual.add("BT /F2 10 Tf 45 " + y + " Td (" + escaparTextoPdfWinAnsi(ql) + ") Tj ET");
                    y -= 13;
                }
                for (String rl : rWrap) {
                    paginaAtual.add("BT /F1 10 Tf 55 " + y + " Td (" + escaparTextoPdfWinAnsi(rl) + ") Tj ET");
                    y -= 13;
                }
                y -= 5;
            }
        }
        if (!paginaAtual.isEmpty()) {
            paginasComandos.add(paginaAtual);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();
        out.write("%PDF-1.4\n".getBytes(StandardCharsets.ISO_8859_1));

        int numPaginas = paginasComandos.size();
        int totalObjs = 4 + (numPaginas * 2);

        // 1: Catalog, 2: Pages, 3: Font Regular, 4: Font Bold
        offsets.add(out.size());
        out.write("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        StringBuilder kids = new StringBuilder();
        for (int i = 0; i < numPaginas; i++) {
            int pageObjId = 5 + (i * 2);
            kids.append(pageObjId).append(" 0 R ");
        }
        offsets.add(out.size());
        out.write(("2 0 obj\n<< /Type /Pages /Kids [ " + kids + "] /Count " + numPaginas + " >>\nendobj\n").getBytes(StandardCharsets.ISO_8859_1));

        offsets.add(out.size());
        out.write("3 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        offsets.add(out.size());
        out.write("4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

        for (int i = 0; i < numPaginas; i++) {
            int pageObjId = 5 + (i * 2);
            int contentObjId = pageObjId + 1;
            String streamContent = String.join("\n", paginasComandos.get(i)) + "\n";
            byte[] streamBytes = streamContent.getBytes(StandardCharsets.ISO_8859_1);

            offsets.add(out.size());
            out.write((pageObjId + " 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                    + "/Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents " + contentObjId + " 0 R >>\nendobj\n")
                    .getBytes(StandardCharsets.ISO_8859_1));

            offsets.add(out.size());
            out.write((contentObjId + " 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n")
                    .getBytes(StandardCharsets.ISO_8859_1));
            out.write(streamBytes);
            out.write("endstream\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));
        }

        int xrefOffset = out.size();
        out.write(("xref\n0 " + (totalObjs + 1) + "\n0000000000 65535 f \n").getBytes(StandardCharsets.ISO_8859_1));
        for (int off : offsets) {
            out.write(String.format("%010d 00000 n \n", off).getBytes(StandardCharsets.ISO_8859_1));
        }
        out.write(("trailer\n<< /Size " + (totalObjs + 1) + " /Root 1 0 R >>\nstartxref\n" + xrefOffset + "\n%%EOF\n")
                .getBytes(StandardCharsets.ISO_8859_1));

        try (FileOutputStream fos = new FileOutputStream(destino)) {
            fos.write(out.toByteArray());
        }
    }

    private void btnEditarAnamneseSalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarAnamneseSalvaActionPerformed
        if (respostaVisualizada == null) {
            return;
        }
        Object[] opcoes = {"Continuar", "Cancelar"};
        boolean ehDesativada = !respostaVisualizada.isAtiva();
        boolean temAtivaPrincipal = (obterRespostaAtivaPaciente() != null && !desativadaAbertaAPartirDeModelos);

        String mensagemAviso;
        if (ehDesativada) {
            if (temAtivaPrincipal) {
                mensagemAviso = "Para retificar esta anamnese desativada, os dados dela serao copiados e colados na tela anamneses modelos e a anamnese principal atual sera desativada.";
            } else {
                mensagemAviso = "Para retificar esta anamnese desativada, os dados dela serao copiados e colados na tela anamneses modelos.";
            }
        } else {
            mensagemAviso = "Essa anamnese ja foi salva, para retificar essa anamnese ela sera copiada e colada na tela anamneses modelos e a anamnese atual sera desativada";
        }

        int escolha = JOptionPane.showOptionDialog(
                this,
                mensagemAviso,
                "Retificar Anamnese",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
        if (escolha != 0) {
            return;
        }

        RespostaAnamnese respostaAlvo = this.respostaVisualizada;
        anamneseDAO.desativarRespostasPorPaciente(paciente);

        FormularioAnamnese formOrigem = resolverFormularioDaResposta(respostaAlvo);
        this.desativadaAbertaAPartirDeModelos = false;
        this.modoVisualizacaoAnamnese = false;
        this.respostaVisualizada = null;

        if (formOrigem.getTipoModelo() == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
            FormularioAnamnese padraoInfantil = anamneseDAO.buscarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
            copiarDadosRespostaParaRascunho(respostaAlvo, padraoInfantil);
            selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        } else if (formOrigem.getTipoModelo() == TipoModeloAnamnese.ADULTO) {
            FormularioAnamnese padraoAdulto = anamneseDAO.buscarModeloPadrao(TipoModeloAnamnese.ADULTO);
            copiarDadosRespostaParaRascunho(respostaAlvo, padraoAdulto);
            selecionarModeloPadrao(TipoModeloAnamnese.ADULTO);
        } else {
            carregarDropdownFormularios(formOrigem);
            copiarDadosRespostaParaRascunho(respostaAlvo, formOrigem);
            abrirPreenchimentoFormulario(formOrigem, true);
        }
    }//GEN-LAST:event_btnEditarAnamneseSalvaActionPerformed

    private void btnEditarPerguntasModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPerguntasModeloActionPerformed
        if (formularioAtual == null || modoVisualizacaoAnamnese) {
            return;
        }
        salvarCachePreenchimentoNoDisco();
        if (formularioAtual.getTipoModelo() == TipoModeloAnamnese.INFANTIL_ADOLESCENTE
                || formularioAtual.getTipoModelo() == TipoModeloAnamnese.ADULTO) {
            iniciarConstrutorAPartirDeModeloPadrao(formularioAtual);
        } else {
            atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
            iniciarConstrutorComFormulario(formularioAtual);
        }
    }//GEN-LAST:event_btnEditarPerguntasModeloActionPerformed

    private void btnLimparPreenchimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparPreenchimentoActionPerformed
        if (formularioAtual == null || modoEdicaoQuestionario || modoVisualizacaoAnamnese) {
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente apagar todos os dados de preenchimento desta anamnese para este paciente?",
                "Confirmar Limpeza de Preenchimento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirmacao == JOptionPane.YES_OPTION) {
            limparRascunhoPreenchimentoFormulario(formularioAtual);
            abrirPreenchimentoFormulario(formularioAtual, abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        }
    }//GEN-LAST:event_btnLimparPreenchimentoActionPerformed

    private void btnAdicionarPerguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarPerguntaActionPerformed
        if (!modoEdicaoQuestionario || formularioAtual == null) {
            return;
        }
        sincronizarEdicaoAtualNoFormulario();

        int proximaOrdem = formularioAtual.getPerguntas().size() + 1;
        PerguntaAnamnese nova = new PerguntaAnamnese(
                "Pergunta",
                proximaOrdem,
                TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                new ArrayList<>(Arrays.asList("Opção 1", "Opção 2"))
        );
        formularioAtual.adicionarPergunta(nova);
        this.indicePerguntaSelecionadaConstrutor = formularioAtual.getPerguntas().size() - 1;
        editoresPerguntas.clear();
        renderizarConstrutorGoogleForms();

        // Scroll automático até a nova pergunta criada fora da tela e foco no enunciado
        SwingUtilities.invokeLater(() -> {
            pnlConteudoAnamnese.revalidate();
            JScrollBar vbar = scrPerguntas.getVerticalScrollBar();
            if (vbar != null) {
                vbar.setValue(vbar.getMaximum());
            }
            if (campoEnunciadoAtivoConstrutor != null) {
                campoEnunciadoAtivoConstrutor.requestFocusInWindow();
                campoEnunciadoAtivoConstrutor.selectAll();
            }
        });
    }//GEN-LAST:event_btnAdicionarPerguntaActionPerformed

    private void btnSalvarAnamneseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarAnamneseActionPerformed
        if (formularioAtual == null || modoVisualizacaoAnamnese) {
            return;
        }

        LocalDate dataAbertura = obterDataAberturaValida();
        if (dataAbertura == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe uma Data de abertura válida no formato dd/mm/aaaa (ex.: 27/10/2026).",
                    "Data de abertura obrigatória",
                    JOptionPane.WARNING_MESSAGE
            );
            txtDataAbertura.requestFocusInWindow();
            return;
        }

        if (modoEdicaoQuestionario) {
            sincronizarEdicaoAtualNoFormulario();

            String titulo = txtTituloAnamnese.getText();
            if (titulo == null || titulo.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe um título para o formulário de anamnese.",
                        "Título obrigatório",
                        JOptionPane.WARNING_MESSAGE
                );
                txtTituloAnamnese.requestFocusInWindow();
                return;
            }

            boolean editandoExistente = (formularioAtual.getId() != null);
            formularioAtual.setTitulo(titulo.trim());
            formularioAtual.setDataAbertura(dataAbertura);
            formularioAtual.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
            if (formularioAtual.getStatusFormulario() == null) {
                formularioAtual.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);
            }

            if (!editandoExistente) {
                anamneseDAO.cadastrarFormulario(formularioAtual);
                rascunhoPersonalizado = null;
            } else {
                anamneseDAO.atualizarFormulario(formularioAtual);
            }

            if (chaveFormularioOrigemEdicao != null) {
                copiarRascunhoEntreFormularios(chaveFormularioOrigemEdicao, obterChaveFormulario(formularioAtual));
                chaveFormularioOrigemEdicao = null;
            }
            if (dadosPreenchimentoPendentesColagem != null) {
                aplicarDadosMapaNoRascunho(dadosPreenchimentoPendentesColagem, formularioAtual);
                dadosPreenchimentoPendentesColagem = null;
            }

            carregarDropdownFormularios(formularioAtual);
            abrirPreenchimentoFormulario(formularioAtual, true);

            String msgSucesso = editandoExistente
                    ? "Formulário de anamnese \"" + formularioAtual.getTitulo() + "\" (Data de abertura: " + dataAbertura.format(FORMATO_DATA) + ") atualizado com sucesso!"
                    : "Formulário de anamnese \"" + formularioAtual.getTitulo() + "\" (Data de abertura: " + dataAbertura.format(FORMATO_DATA) + ") salvo e adicionado à lista!";

            JOptionPane.showMessageDialog(
                    this,
                    msgSucesso,
                    editandoExistente ? "Anamnese Atualizada" : "Anamnese Salva",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            StringBuilder resumo = new StringBuilder();
            resumo.append("Data de abertura: ").append(dataAbertura.format(FORMATO_DATA)).append("\n");
            resumo.append("Modelo: ").append(formularioAtual.getTitulo()).append("\n\n");

            List<PerguntaAnamnese> perguntas = formularioAtual.getPerguntas();
            for (int i = 0; i < perguntas.size(); i++) {
                PerguntaAnamnese p = perguntas.get(i);
                if (p.getTipoPergunta() == TipoPerguntaAnamnese.SECAO_TITULO) {
                    resumo.append("\n=== ").append(p.getEnunciado()).append(" ===\n");
                } else {
                    String resp = respostasPreenchimento.getOrDefault(i, "Não respondida");
                    resumo.append("- ").append(p.getEnunciado()).append(" ").append(resp).append("\n");
                }
            }

            String dadosCampos = serializarDadosCamposAtuais(formularioAtual, dataAbertura);

            // Desativa qualquer anamnese ativa anterior do paciente antes de ativar a nova salva
            anamneseDAO.desativarRespostasPorPaciente(paciente);

            RespostaAnamnese resposta = new RespostaAnamnese(
                    formularioAtual,
                    paciente,
                    dataAbertura,
                    resumo.toString(),
                    dadosCampos,
                    Boolean.TRUE
            );
            resposta.setChavePaciente(obterChavePaciente());
            anamneseDAO.salvarResposta(resposta);

            // Somente após salvar a anamnese preenchida limpa os dados de rascunho desta anamnese para este paciente
            limparRascunhoPreenchimentoFormulario(formularioAtual);

            // Quando uma anamnese for salva a tela de anamnese modelos muda para a tela de anamnese visualização
            abrirVisualizacaoResposta(resposta);

            JOptionPane.showMessageDialog(
                    this,
                    "Respostas da anamnese \"" + formularioAtual.getTitulo() + "\" (Data de abertura: " + dataAbertura.format(FORMATO_DATA) + ") salvas com sucesso!",
                    "Anamnese Salva",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSalvarAnamneseActionPerformed

    private void ativarModoTelaCheia(JDialog dialog) {
        this.modoTelaCheiaExpandida = true;
        this.dialogTelaCheia = dialog;

        // Oculta o cabeçalho geral e a barra de seleção de modelos superior,
        // exibindo na janela em tela cheia APENAS a anamnese (containerFormulario)
        pnlCabecalho.setVisible(false);
        lblTituloModelos.setVisible(false);
        pnlIndicadorVerde.setVisible(false);
        sepModelos.setVisible(false);
        btnInfantilAdolescente.setVisible(false);
        btnAdulto.setVisible(false);
        cmbPersonalizados.setVisible(false);
        btnCriarPersonalizado.setVisible(false);
        cmbAnamnesesDesativadas.setVisible(false);
        btnCriarNovaAnamnese.setVisible(false);

        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(8, 12, 8, 12));
        this.add(containerFormulario, BorderLayout.CENTER);

        // Muda o ícone de expandir para minimizar (que fecha a tela cheia)
        btnExpandir.setIcon(iconeMinimizarPadrao);
        btnExpandir.setToolTipText("Minimizar / Fechar tela cheia");

        this.revalidate();
        this.repaint();
    }

    private void btnExpandirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpandirActionPerformed
        if (modoTelaCheiaExpandida && dialogTelaCheia != null) {
            sincronizarEdicaoAtualNoFormulario();
            salvarCachePreenchimentoNoDisco();
            dialogTelaCheia.dispose();
            return;
        }

        if (formularioAtual == null) {
            return;
        }
        sincronizarEdicaoAtualNoFormulario();
        salvarCachePreenchimentoNoDisco();

        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(janelaPai, "Anamnese - " + txtTituloAnamnese.getText(), JDialog.ModalityType.APPLICATION_MODAL);
        Rectangle limitesTela = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        dialog.setBounds(limitesTela);

        FichaAnaminaseView viewExpandida = new FichaAnaminaseView(this.paciente);
        viewExpandida.ativarModoTelaCheia(dialog);

        if (this.modoVisualizacaoAnamnese && this.respostaVisualizada != null) {
            viewExpandida.desativadaAbertaAPartirDeModelos = this.desativadaAbertaAPartirDeModelos;
            viewExpandida.abaAnteriorModelos = this.abaAnteriorModelos;
            viewExpandida.formularioAnteriorModelos = this.formularioAnteriorModelos;
            viewExpandida.estavaEditandoQuestionarioModelos = this.estavaEditandoQuestionarioModelos;
            viewExpandida.abrirVisualizacaoResposta(this.respostaVisualizada);
        } else if (this.modoEdicaoQuestionario) {
            viewExpandida.abaAtiva = this.abaAtiva;
            viewExpandida.modoAtual = this.modoAtual;
            viewExpandida.chaveFormularioOrigemEdicao = this.chaveFormularioOrigemEdicao;
            viewExpandida.iniciarConstrutorComFormulario(this.formularioAtual);
            if (this.indicePerguntaSelecionadaConstrutor >= 0) {
                viewExpandida.selecionarPerguntaConstrutor(this.indicePerguntaSelecionadaConstrutor);
            }
        } else if (this.abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR) {
            viewExpandida.carregarDropdownFormularios(this.formularioAtual);
            viewExpandida.abrirPreenchimentoFormulario(this.formularioAtual, true);
        } else {
            viewExpandida.selecionarModeloPadrao(this.modoAtual);
        }

        dialog.setContentPane(viewExpandida);
        dialog.setVisible(true);

        // Sincroniza de volta o estado da janela em tela cheia para a tela principal ao minimizar/fechar
        viewExpandida.sincronizarEdicaoAtualNoFormulario();
        salvarCachePreenchimentoNoDisco();

        if (viewExpandida.modoVisualizacaoAnamnese && viewExpandida.respostaVisualizada != null) {
            this.desativadaAbertaAPartirDeModelos = viewExpandida.desativadaAbertaAPartirDeModelos;
            carregarDropdownFormularios(null);
            abrirVisualizacaoResposta(viewExpandida.respostaVisualizada);
        } else if (viewExpandida.modoEdicaoQuestionario && viewExpandida.formularioAtual != null) {
            this.abaAtiva = viewExpandida.abaAtiva;
            this.modoAtual = viewExpandida.modoAtual;
            this.rascunhoPersonalizado = viewExpandida.rascunhoPersonalizado;
            this.chaveFormularioOrigemEdicao = viewExpandida.chaveFormularioOrigemEdicao;
            carregarDropdownFormularios(null);
            atualizarEstadoBarra(this.abaAtiva);
            iniciarConstrutorComFormulario(viewExpandida.formularioAtual);
        } else if (viewExpandida.formularioAtual != null) {
            this.abaAtiva = viewExpandida.abaAtiva;
            this.modoAtual = viewExpandida.modoAtual;
            boolean peloDropdown = (this.abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR);
            carregarDropdownFormularios(peloDropdown ? viewExpandida.formularioAtual : null);
            abrirPreenchimentoFormulario(viewExpandida.formularioAtual, peloDropdown);
        } else {
            carregarDropdownFormularios(null);
            carregarEstadoInicialPaciente();
        }
    }//GEN-LAST:event_btnExpandirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarPergunta;
    private javax.swing.JButton btnAdulto;
    private javax.swing.JButton btnBaixarAnamneseSalva;
    private javax.swing.JButton btnCriarNovaAnamnese;
    private javax.swing.JButton btnCriarPersonalizado;
    private javax.swing.JButton btnEditarAnamneseSalva;
    private javax.swing.JButton btnEditarPerguntasModelo;
    private javax.swing.JButton btnExpandir;
    private javax.swing.JButton btnFecharVisualizacaoDesativada;
    private javax.swing.JButton btnInfantilAdolescente;
    private javax.swing.JButton btnLimparPreenchimento;
    private javax.swing.JButton btnSalvarAnamnese;
    private com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel circularIconHeader;
    private javax.swing.JComboBox<Object> cmbAnamnesesDesativadas;
    private javax.swing.JComboBox<Object> cmbPersonalizados;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container1;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerFormulario;
    private javax.swing.JLabel lblDataAbertura;
    private javax.swing.JLabel lblSubtituloPagina;
    private javax.swing.JLabel lblTituloModelos;
    private javax.swing.JLabel lblTituloPagina;
    private javax.swing.JPanel pnlCabecalho;
    private javax.swing.JPanel pnlConteudoAnamnese;
    private javax.swing.JPanel pnlIndicadorVerde;
    private javax.swing.JPanel pnlRodapeAcoes;
    private javax.swing.JScrollPane scrPerguntas;
    private javax.swing.JSeparator sepModelos;
    private javax.swing.JSeparator sepTituloFormulario;
    private javax.swing.JTextField txtDataAbertura;
    private javax.swing.JTextField txtTituloAnamnese;
    // End of variables declaration//GEN-END:variables
}
