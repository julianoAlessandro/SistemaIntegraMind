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
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
 * Tela de Anamnese do Paciente com salvamento automático de preenchimento por paciente
 * e por modelo (mesmo ao sair da ficha ou fechar/reabrir o sistema), preservação dos dados
 * preenchidos ao editar/adicionar perguntas em uma anamnese e botão de vassoura para limpar
 * o preenchimento com aviso de confirmação.
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

    private static final String PREFIXO_COMPLEMENTO = "COMPLEMENTO:";
    private static final String PREFIXO_COLUNA = "COL:";
    private static final String PREFIXO_LINHA = "LIN:";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final Color VERDE_BOTAO_ATIVO = new Color(31, 122, 68);
    private static final Color CINZA_BOTAO_INATIVO = new Color(241, 243, 245);
    private static final Color TEXTO_BOTAO_INATIVO = new Color(67, 77, 89);
    private static final Color BORDA_CONTAINER_INTERNO = new Color(214, 222, 228);

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

    private Paciente paciente;
    private final AnamneseDAO anamneseDAO = new AnamneseJPA();

    private AbaBarraAnamnese abaAtiva = AbaBarraAnamnese.INFANTIL_ADOLESCENTE;
    private TipoModeloAnamnese modoAtual = TipoModeloAnamnese.INFANTIL_ADOLESCENTE;
    private boolean modoEdicaoQuestionario = false;
    private boolean atualizandoCombo = false;
    private boolean dropdownSelecionado = false;
    private boolean carregandoPreenchimentoUI = false;

    // Guarda a chave do formulário de origem quando o usuário clica em "Editar perguntas deste modelo"
    // para que, ao finalizar a edição, os dados já preenchidos sejam mantidos.
    private String chaveFormularioOrigemEdicao = null;

    private Icon iconeSlidersBranco;
    private Icon iconeSlidersEscuro;

    private FormularioAnamnese formularioAtual;
    private FormularioAnamnese rascunhoPersonalizado = null;

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
        if (formularioAtual != null && !modoEdicaoQuestionario) {
            abrirPreenchimentoFormulario(formularioAtual, abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        }
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

        btnCriarPersonalizado.setIcon(iconeSlidersEscuro);
        btnExpandir.setIcon(criarIconeExpandir(new Color(120, 125, 130)));
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
                // Salva imediatamente no disco qualquer preenchimento feito pelo paciente antes de sair da tela
                salvarCachePreenchimentoNoDisco();

                // O rascunho de criação de perguntas não salvas na aba "Criar Personalizado" reseta ao mudar de tela
                rascunhoPersonalizado = null;
                if (abaAtiva == AbaBarraAnamnese.CRIAR_PERSONALIZADO && modoEdicaoQuestionario) {
                    formularioAtual = criarRascunhoPadraoPersonalizado();
                    txtTituloAnamnese.setText(formularioAtual.getTitulo());
                    txtDataAbertura.setText(LocalDate.now().format(FORMATO_DATA));
                    editoresPerguntas.clear();
                    renderizarConstrutorGoogleForms();
                }
            }
        });

        configurarEstiloDropdown();
        carregarDropdownFormularios(null);
        selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
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
        if (carregandoPreenchimentoUI || modoEdicaoQuestionario || formularioAtual == null) {
            return;
        }
        String prefixo = obterPrefixoRascunho(formularioAtual);
        salvarValorRascunho(prefixo, "DATA_ABERTURA", txtDataAbertura.getText());
    }

    private String obterChavePaciente() {
        if (paciente == null) {
            return "PAC_GERAL";
        }
        if (paciente.getId() != null) {
            return "PAC_ID_" + paciente.getId();
        }
        if (paciente.getCpf() != null && !paciente.getCpf().isBlank()) {
            return "PAC_CPF_" + normalizarChave(paciente.getCpf());
        }
        if (paciente.getNome() != null && !paciente.getNome().isBlank()) {
            return "PAC_NOME_" + normalizarChave(paciente.getNome());
        }
        return "PAC_GERAL";
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
                salvarRascunhoSeEstiverCriandoPersonalizado();
                atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
            }
        });
    }

    private void salvarRascunhoSeEstiverCriandoPersonalizado() {
        if (modoEdicaoQuestionario && formularioAtual != null && formularioAtual.getId() == null) {
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

    private void atualizarEstadoBarra(AbaBarraAnamnese aba) {
        this.abaAtiva = aba;
        this.dropdownSelecionado = (aba == AbaBarraAnamnese.DROPDOWN_SELECIONAR);

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

        this.chaveFormularioOrigemEdicao = null;
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

        this.chaveFormularioOrigemEdicao = obterChaveFormulario(modeloPadrao);
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
        this.modoEdicaoQuestionario = true;
        this.editoresPerguntas.clear();

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
            txtDataAbertura.setText(this.formularioAtual.getDataAbertura().format(FORMATO_DATA));
        } finally {
            carregandoPreenchimentoUI = false;
        }
        txtTituloAnamnese.requestFocusInWindow();

        btnEditarPerguntasModelo.setVisible(false);
        btnLimparPreenchimento.setVisible(false);
        btnAdicionarPergunta.setVisible(true);
        btnSalvarAnamnese.setText("SALVAR");
        pnlRodapeAcoes.setVisible(true);

        renderizarConstrutorGoogleForms();
    }

    private void renderizarConstrutorGoogleForms() {
        sincronizarEdicaoAtualNoFormulario();

        editoresPerguntas.clear();
        pnlConteudoAnamnese.removeAll();
        pnlConteudoAnamnese.setLayout(new BoxLayout(pnlConteudoAnamnese, BoxLayout.Y_AXIS));
        pnlConteudoAnamnese.setBackground(Color.WHITE);
        pnlConteudoAnamnese.setBorder(new EmptyBorder(12, 24, 12, 24));

        List<PerguntaAnamnese> perguntas = formularioAtual.getPerguntas();
        for (int i = 0; i < perguntas.size(); i++) {
            PerguntaAnamnese pergunta = perguntas.get(i);
            pergunta.setOrdem(i + 1);
            JPanel card = criarCardEditorPergunta(i, pergunta);
            pnlConteudoAnamnese.add(card);
            pnlConteudoAnamnese.add(Box.createVerticalStrut(14));
        }

        pnlConteudoAnamnese.add(Box.createVerticalGlue());
        pnlConteudoAnamnese.revalidate();
        pnlConteudoAnamnese.repaint();
    }

    private JPanel criarCardEditorPergunta(int indiceZeroBased, PerguntaAnamnese pergunta) {
        int numero = indiceZeroBased + 1;
        TipoPerguntaAnamnese tipo = pergunta.getTipoPergunta() != null
                ? pergunta.getTipoPergunta()
                : TipoPerguntaAnamnese.MULTIPLA_ESCOLHA;

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(tipo == TipoPerguntaAnamnese.SECAO_TITULO ? new Color(242, 249, 245) : Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0,
                        tipo == TipoPerguntaAnamnese.SECAO_TITULO ? 4 : 0,
                        1,
                        0,
                        tipo == TipoPerguntaAnamnese.SECAO_TITULO ? VERDE_BOTAO_ATIVO : new Color(240, 243, 246)
                ),
                new EmptyBorder(8, 8, 12, 8)
        ));

        JPanel linhaTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        linhaTopo.setOpaque(false);
        linhaTopo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNumero = new JLabel(tipo == TipoPerguntaAnamnese.SECAO_TITULO ? "Seção:" : (numero + "."));
        lblNumero.setFont(new Font("Segoe UI", tipo == TipoPerguntaAnamnese.SECAO_TITULO ? Font.BOLD : Font.PLAIN, 14));
        lblNumero.setForeground(tipo == TipoPerguntaAnamnese.SECAO_TITULO ? VERDE_BOTAO_ATIVO : new Color(45, 50, 56));

        JTextField txtEnunciado = new JTextField(pergunta.getEnunciado() != null ? pergunta.getEnunciado() : "Pergunta", 28);
        txtEnunciado.setFont(new Font("Segoe UI", tipo == TipoPerguntaAnamnese.SECAO_TITULO ? Font.BOLD : Font.PLAIN, 15));
        txtEnunciado.setForeground(new Color(30, 35, 40));
        txtEnunciado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 218, 224)),
                new EmptyBorder(2, 4, 2, 4)
        ));

        JComboBox<TipoPerguntaAnamnese> cmbTipo = new JComboBox<>(TipoPerguntaAnamnese.values());
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbTipo.setBackground(new Color(246, 248, 250));
        cmbTipo.setSelectedItem(tipo);

        JButton btnRemoverPergunta = new JButton("Excluir item");
        btnRemoverPergunta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRemoverPergunta.setForeground(new Color(185, 60, 60));
        btnRemoverPergunta.setBorder(new EmptyBorder(2, 8, 2, 8));
        btnRemoverPergunta.setContentAreaFilled(false);
        btnRemoverPergunta.setFocusPainted(false);
        btnRemoverPergunta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        linhaTopo.add(lblNumero);
        linhaTopo.add(txtEnunciado);
        linhaTopo.add(Box.createHorizontalStrut(8));
        linhaTopo.add(cmbTipo);
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
                JPanel linhaOpcao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
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
                JTextField txtOpcao = new JTextField(valorLimpo, 20);
                txtOpcao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                txtOpcao.setForeground(new Color(65, 70, 75));
                txtOpcao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 234, 238)),
                        new EmptyBorder(2, 2, 2, 2)
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

            JPanel linhaAddOpcao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
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
                        escolhas.add(item);
                    }
                }
            }
            if (escolhas.isEmpty() && complementos.isEmpty()) {
                escolhas.add("sim");
                escolhas.add("não");
                complementos.add("Qual?");
                atualizarListaEscolhaComComplemento(pergunta, escolhas, complementos);
            }

            JLabel lblSubEscolhas = new JLabel("Opções de marcação (ex.: sim, não):");
            lblSubEscolhas.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblSubEscolhas.setForeground(new Color(70, 80, 85));
            lblSubEscolhas.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(lblSubEscolhas);

            for (int i = 0; i < escolhas.size(); i++) {
                final int idxEsc = i;
                JPanel linhaEsc = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
                linhaEsc.setOpaque(false);
                linhaEsc.setAlignmentX(Component.LEFT_ALIGNMENT);

                JRadioButton rb = new JRadioButton();
                rb.setOpaque(false);
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

            JPanel linhaAddEsc = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
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
            JLabel lblSubComp = new JLabel("Campos de texto complementares (ex.: Qual?, Desde:, Razões dadas:):");
            lblSubComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblSubComp.setForeground(new Color(70, 80, 85));
            lblSubComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(lblSubComp);

            for (int i = 0; i < complementos.size(); i++) {
                final int idxComp = i;
                JPanel linhaComp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
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

            JPanel linhaAddComp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
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

            JPanel painelCols = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
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

            JPanel painelRows = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
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
            JPanel linhaEstrelas = criarPainelEstrelasInterativo(indiceZeroBased, null, null, 0);
            linhaEstrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(linhaEstrelas);

        } else if (tipo == TipoPerguntaAnamnese.TEXTO_LIVRE) {
            JTextField txtExemplo = new JTextField("Linha de resposta aberta (_______________________________)", 36);
            txtExemplo.setEditable(false);
            txtExemplo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            txtExemplo.setForeground(new Color(135, 140, 145));
            txtExemplo.setBackground(new Color(250, 251, 252));
            txtExemplo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 220)));

            JPanel wrapperTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
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
            int notaInicial) {
        JPanel painelEstrelas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
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

            botoesEstrela[i] = btnEstrela;
            painelEstrelas.add(btnEstrela);
        }
        return painelEstrelas;
    }

    private void abrirPreenchimentoFormulario(FormularioAnamnese form, boolean selecionadoPeloDropdown) {
        if (form == null) {
            return;
        }
        this.formularioAtual = form;
        this.modoEdicaoQuestionario = false;
        this.editoresPerguntas.clear();
        this.respostasPreenchimento.clear();

        if (selecionadoPeloDropdown) {
            this.modoAtual = TipoModeloAnamnese.PERSONALIZADO;
            atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        }

        final String prefixoRascunho = obterPrefixoRascunho(form);
        carregandoPreenchimentoUI = true;
        try {
            txtTituloAnamnese.setText(form.getTitulo());
            txtTituloAnamnese.setEditable(false);

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

        btnEditarPerguntasModelo.setVisible(true);
        btnLimparPreenchimento.setVisible(true);
        btnAdicionarPergunta.setVisible(false);
        btnSalvarAnamnese.setText("SALVAR");
        pnlRodapeAcoes.setVisible(true);

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
                JPanel bannerSecao = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
                bannerSecao.setBackground(new Color(236, 246, 240));
                bannerSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
                bannerSecao.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, VERDE_BOTAO_ATIVO));

                JLabel lblSecao = new JLabel(p.getEnunciado());
                lblSecao.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblSecao.setForeground(new Color(13, 82, 65));
                bannerSecao.add(lblSecao);

                pnlConteudoAnamnese.add(Box.createVerticalStrut(10));
                pnlConteudoAnamnese.add(bannerSecao);
                pnlConteudoAnamnese.add(Box.createVerticalStrut(8));
                continue;
            }

            JPanel bloco = new JPanel();
            bloco.setLayout(new BoxLayout(bloco, BoxLayout.Y_AXIS));
            bloco.setOpaque(false);
            bloco.setAlignmentX(Component.LEFT_ALIGNMENT);
            bloco.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(244, 246, 248)),
                    new EmptyBorder(6, 4, 10, 4)
            ));

            JLabel lblPergunta = new JLabel(p.getEnunciado());
            lblPergunta.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblPergunta.setForeground(new Color(35, 42, 48));
            lblPergunta.setAlignmentX(Component.LEFT_ALIGNMENT);
            bloco.add(lblPergunta);
            bloco.add(Box.createVerticalStrut(6));

            JPanel corpo = new JPanel();
            corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
            corpo.setOpaque(false);
            corpo.setAlignmentX(Component.LEFT_ALIGNMENT);
            corpo.setBorder(new EmptyBorder(0, 10, 0, 0));

            if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA) {
                ButtonGroup bg = new ButtonGroup();
                JPanel linhaOpcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 2));
                linhaOpcoes.setOpaque(false);
                linhaOpcoes.setAlignmentX(Component.LEFT_ALIGNMENT);

                String opcaoSalva = obterValorRascunho(prefixoRascunho, qKey + "__OPCAO", idxKey + "__OPCAO");

                for (String op : p.getOpcoes()) {
                    JRadioButton rb = new JRadioButton(op);
                    rb.setOpaque(false);
                    rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    rb.setForeground(new Color(55, 62, 68));
                    if (opcaoSalva != null && opcaoSalva.equals(op)) {
                        rb.setSelected(true);
                        respostasPreenchimento.put(idx, op);
                    }
                    rb.addActionListener(e -> {
                        respostasPreenchimento.put(idx, op);
                        salvarValorRascunho(prefixoRascunho, qKey + "__OPCAO", op);
                        salvarValorRascunho(prefixoRascunho, idxKey + "__OPCAO", op);
                    });
                    bg.add(rb);
                    linhaOpcoes.add(rb);
                }
                corpo.add(linhaOpcoes);

            } else if (tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
                final List<JCheckBox> checks = new ArrayList<>();
                JPanel linhaChecks = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 2));
                linhaChecks.setOpaque(false);
                linhaChecks.setAlignmentX(Component.LEFT_ALIGNMENT);

                List<String> inicialmenteMarcadas = new ArrayList<>();
                for (String op : p.getOpcoes()) {
                    final String opKey = normalizarChave(op);
                    JCheckBox cb = new JCheckBox(op);
                    cb.setOpaque(false);
                    cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    cb.setForeground(new Color(55, 62, 68));

                    String marcadoSalvo = obterValorRascunho(prefixoRascunho, qKey + "__CHK_" + opKey, idxKey + "__CHK_" + opKey);
                    if ("true".equals(marcadoSalvo)) {
                        cb.setSelected(true);
                        inicialmenteMarcadas.add(op);
                    }

                    checks.add(cb);
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
                    linhaChecks.add(cb);
                }
                if (!inicialmenteMarcadas.isEmpty()) {
                    respostasPreenchimento.put(idx, String.join(", ", inicialmenteMarcadas));
                }
                corpo.add(linhaChecks);

            } else if (tipo == TipoPerguntaAnamnese.CAMPOS_LINHA) {
                JPanel linhaCampos = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
                linhaCampos.setOpaque(false);
                linhaCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

                final Map<String, JTextField> mapaCampos = new LinkedHashMap<>();
                for (String rotuloCampo : p.getOpcoes()) {
                    final String campoKey = normalizarChave(rotuloCampo);
                    JLabel lbl = new JLabel(rotuloCampo + ":");
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lbl.setForeground(new Color(60, 68, 75));

                    String valSalvo = obterValorRascunho(prefixoRascunho, qKey + "__CAMPO_" + campoKey, idxKey + "__CAMPO_" + campoKey);
                    JTextField txtCampo = new JTextField(valSalvo != null ? valSalvo : "", 14);
                    txtCampo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    txtCampo.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(175, 185, 195)),
                            new EmptyBorder(2, 4, 2, 4)
                    ));
                    mapaCampos.put(rotuloCampo, txtCampo);

                    txtCampo.getDocument().addDocumentListener(criarListenerMapaCampos(
                            idx, prefixoRascunho, qKey, idxKey, "__CAMPO_" + campoKey, txtCampo, null, mapaCampos
                    ));
                    linhaCampos.add(lbl);
                    linhaCampos.add(txtCampo);
                }
                atualizarRespostaEscolhaComComplemento(idx, null, mapaCampos);
                corpo.add(linhaCampos);

            } else if (tipo == TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO) {
                final List<JCheckBox> opcoesMarcaveis = new ArrayList<>();
                final Map<String, JTextField> mapaComplementos = new LinkedHashMap<>();

                JPanel linhaMista = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
                linhaMista.setOpaque(false);
                linhaMista.setAlignmentX(Component.LEFT_ALIGNMENT);

                for (String item : p.getOpcoes()) {
                    if (item.startsWith(PREFIXO_COMPLEMENTO)) {
                        String rotuloComp = item.substring(PREFIXO_COMPLEMENTO.length());
                        final String compKey = normalizarChave(rotuloComp);
                        JLabel lblC = new JLabel(rotuloComp + ":");
                        lblC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        lblC.setForeground(new Color(60, 68, 75));

                        String valSalvo = obterValorRascunho(prefixoRascunho, qKey + "__COMP_" + compKey, idxKey + "__COMP_" + compKey);
                        JTextField txtComp = new JTextField(valSalvo != null ? valSalvo : "", 16);
                        txtComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        txtComp.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(175, 185, 195)),
                                new EmptyBorder(2, 4, 2, 4)
                        ));
                        mapaComplementos.put(rotuloComp, txtComp);
                        txtComp.getDocument().addDocumentListener(criarListenerMapaCampos(
                                idx, prefixoRascunho, qKey, idxKey, "__COMP_" + compKey, txtComp, opcoesMarcaveis, mapaComplementos
                        ));

                        linhaMista.add(lblC);
                        linhaMista.add(txtComp);
                    } else {
                        final String escKey = normalizarChave(item);
                        JCheckBox cb = new JCheckBox("( ) " + item);
                        cb.setOpaque(false);
                        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        cb.setForeground(new Color(55, 62, 68));

                        String escSalvo = obterValorRascunho(prefixoRascunho, qKey + "__ESC_" + escKey, idxKey + "__ESC_" + escKey);
                        if ("true".equals(escSalvo)) {
                            cb.setSelected(true);
                        }

                        opcoesMarcaveis.add(cb);
                        cb.addActionListener(e -> {
                            salvarValorRascunho(prefixoRascunho, qKey + "__ESC_" + escKey, cb.isSelected() ? "true" : "");
                            salvarValorRascunho(prefixoRascunho, idxKey + "__ESC_" + escKey, cb.isSelected() ? "true" : "");
                            atualizarRespostaEscolhaComComplemento(idx, opcoesMarcaveis, mapaComplementos);
                        });
                        linhaMista.add(cb);
                    }
                }
                atualizarRespostaEscolhaComComplemento(idx, opcoesMarcaveis, mapaComplementos);
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

                        String celSalva = obterValorRascunho(prefixoRascunho, qKey + "__GRADE_" + gradeKey, idxKey + "__GRADE_" + gradeKey);
                        if ("true".equals(celSalva)) {
                            cbHorario.setSelected(true);
                            dispIniciais.add(chaveHorario);
                        }

                        checksGrade.add(cbHorario);
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
                        celula.add(cbHorario);
                        tabelaGrid.add(celula);
                    }
                }
                if (!dispIniciais.isEmpty()) {
                    respostasPreenchimento.put(idx, String.join("; ", dispIniciais));
                }
                corpo.add(tabelaGrid);

            } else if (tipo == TipoPerguntaAnamnese.ESTRELAS) {
                String estSalva = obterValorRascunho(prefixoRascunho, qKey + "__ESTRELAS", idxKey + "__ESTRELAS");
                int notaIni = 0;
                if (estSalva != null) {
                    try {
                        notaIni = Integer.parseInt(estSalva.trim());
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (notaIni > 0) {
                    respostasPreenchimento.put(idx, notaIni + " estrela(s)");
                }
                JPanel estrelas = criarPainelEstrelasInterativo(idx, prefixoRascunho, qKey, notaIni);
                estrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
                corpo.add(estrelas);

            } else {
                String textoSalvo = obterValorRascunho(prefixoRascunho, qKey + "__TEXTO", idxKey + "__TEXTO");
                JTextArea txtResp = new JTextArea(textoSalvo != null ? textoSalvo : "", 2, 48);
                if (textoSalvo != null && !textoSalvo.isBlank()) {
                    respostasPreenchimento.put(idx, textoSalvo);
                }
                txtResp.setLineWrap(true);
                txtResp.setWrapStyleWord(true);
                txtResp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                txtResp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 215, 220)),
                        new EmptyBorder(4, 8, 4, 8)
                ));
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
                corpo.add(txtResp);
            }

            bloco.add(corpo);
            pnlConteudoAnamnese.add(bloco);
        }

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
            final List<JCheckBox> opcoesMarcaveis,
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
            List<JCheckBox> opcoesMarcaveis,
            Map<String, JTextField> mapaCampos) {
        List<String> partes = new ArrayList<>();
        if (opcoesMarcaveis != null) {
            for (JCheckBox cb : opcoesMarcaveis) {
                if (cb.isSelected()) {
                    partes.add(cb.getText().replace("( ) ", "(X) "));
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
            binder.sincronizar(i + 1);
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
        sepModelos = new javax.swing.JSeparator();
        btnInfantilAdolescente = new javax.swing.JButton();
        btnAdulto = new javax.swing.JButton();
        cmbPersonalizados = new javax.swing.JComboBox<>();
        btnCriarPersonalizado = new javax.swing.JButton();
        containerFormulario = new com.mycompany.sistemaintegramind.View.Componentes.Container();
        txtTituloAnamnese = new javax.swing.JTextField();
        lblDataAbertura = new javax.swing.JLabel();
        txtDataAbertura = new javax.swing.JTextField();
        btnEditarPerguntasModelo = new javax.swing.JButton();
        btnLimparPreenchimento = new javax.swing.JButton();
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
                        .addComponent(txtTituloAnamnese, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDataAbertura)
                        .addGap(6, 6, 6)
                        .addComponent(txtDataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditarPerguntasModelo)
                        .addGap(6, 6, 6)
                        .addComponent(btnLimparPreenchimento, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(txtTituloAnamnese, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(lblDataAbertura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDataAbertura)
                    .addComponent(btnEditarPerguntasModelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimparPreenchimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(containerFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(22, 22, 22))
                    .addGroup(container1Layout.createSequentialGroup()
                        .addGroup(container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTituloModelos)
                            .addComponent(pnlIndicadorVerde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(container1Layout.createSequentialGroup()
                                .addComponent(btnInfantilAdolescente, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(btnAdulto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(cmbPersonalizados, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(btnCriarPersonalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(80, Short.MAX_VALUE))))
        );
        container1Layout.setVerticalGroup(
            container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblTituloModelos)
                .addGap(10, 10, 10)
                .addComponent(pnlIndicadorVerde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(sepModelos, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInfantilAdolescente, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdulto, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPersonalizados, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCriarPersonalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
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
        if (atualizandoCombo) {
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

    private void btnEditarPerguntasModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPerguntasModeloActionPerformed
        if (formularioAtual == null) {
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
        if (formularioAtual == null || modoEdicaoQuestionario) {
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
        editoresPerguntas.clear();
        renderizarConstrutorGoogleForms();
    }//GEN-LAST:event_btnAdicionarPerguntaActionPerformed

    private void btnSalvarAnamneseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarAnamneseActionPerformed
        if (formularioAtual == null) {
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

            // Se o usuário estava editando uma anamnese que já possuía dados preenchidos (inclusive criada a partir de um modelo padrão),
            // preserva os dados preenchidos após finalizar a edição.
            if (chaveFormularioOrigemEdicao != null) {
                copiarRascunhoEntreFormularios(chaveFormularioOrigemEdicao, obterChaveFormulario(formularioAtual));
                chaveFormularioOrigemEdicao = null;
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

            RespostaAnamnese resposta = new RespostaAnamnese(formularioAtual, paciente, dataAbertura, resumo.toString());
            anamneseDAO.salvarResposta(resposta);

            // Somente após salvar a anamnese preenchida limpa os dados de preenchimento desta anamnese para este paciente
            limparRascunhoPreenchimentoFormulario(formularioAtual);
            abrirPreenchimentoFormulario(formularioAtual, abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR);

            JOptionPane.showMessageDialog(
                    this,
                    "Respostas da anamnese \"" + formularioAtual.getTitulo() + "\" (Data de abertura: " + dataAbertura.format(FORMATO_DATA) + ") registradas com sucesso!",
                    "Anamnese Preenchida",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSalvarAnamneseActionPerformed

    private void btnExpandirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpandirActionPerformed
        if (formularioAtual == null) {
            return;
        }
        sincronizarEdicaoAtualNoFormulario();
        salvarCachePreenchimentoNoDisco();

        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(janelaPai, "Anamnese - " + txtTituloAnamnese.getText(), JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(1040, 760);
        dialog.setLocationRelativeTo(this);

        FichaAnaminaseView viewExpandida = new FichaAnaminaseView(this.paciente);
        if (this.modoEdicaoQuestionario) {
            viewExpandida.atualizarEstadoBarra(this.abaAtiva);
            viewExpandida.iniciarConstrutorComFormulario(this.formularioAtual);
        } else if (this.abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR) {
            viewExpandida.carregarDropdownFormularios(this.formularioAtual);
            viewExpandida.abrirPreenchimentoFormulario(this.formularioAtual, true);
        } else {
            viewExpandida.selecionarModeloPadrao(this.modoAtual);
        }

        dialog.setContentPane(viewExpandida);
        dialog.setVisible(true);

        carregarDropdownFormularios(this.abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR ? this.formularioAtual : null);
        if (!this.modoEdicaoQuestionario && this.formularioAtual != null) {
            abrirPreenchimentoFormulario(this.formularioAtual, this.abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        }
    }//GEN-LAST:event_btnExpandirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarPergunta;
    private javax.swing.JButton btnAdulto;
    private javax.swing.JButton btnCriarPersonalizado;
    private javax.swing.JButton btnEditarPerguntasModelo;
    private javax.swing.JButton btnExpandir;
    private javax.swing.JButton btnInfantilAdolescente;
    private javax.swing.JButton btnLimparPreenchimento;
    private javax.swing.JButton btnSalvarAnamnese;
    private com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel circularIconHeader;
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
