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
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Tela de Anamnese do Paciente com modelos padrão (Infantil/Adolescente e Adulto),
 * menu dropdown sempre visível e botão único "Criar Personalizado" no padrão Google Forms.
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

    private static final Color VERDE_BOTAO_ATIVO = new Color(31, 122, 68);
    private static final Color CINZA_BOTAO_INATIVO = new Color(241, 243, 245);
    private static final Color TEXTO_BOTAO_INATIVO = new Color(67, 77, 89);
    private static final Color BORDA_CONTAINER_INTERNO = new Color(214, 222, 228);

    private Paciente paciente;
    private final AnamneseDAO anamneseDAO = new AnamneseJPA();

    private AbaBarraAnamnese abaAtiva = AbaBarraAnamnese.INFANTIL_ADOLESCENTE;
    private TipoModeloAnamnese modoAtual = TipoModeloAnamnese.INFANTIL_ADOLESCENTE;
    private boolean modoEdicaoQuestionario = false;
    private boolean atualizandoCombo = false;
    private boolean dropdownSelecionado = false;

    private Icon iconeSlidersBranco;
    private Icon iconeSlidersEscuro;

    private FormularioAnamnese formularioAtual;
    private final List<PerguntaEditorBinder> editoresPerguntas = new ArrayList<>();
    private final Map<Integer, String> respostasPreenchimento = new LinkedHashMap<>();

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
    }

    private void configurarVisualizacaoInicial() {
        container1.setCardStyle();

        containerFormulario.removeShadow();
        containerFormulario.setBackground(Color.WHITE);
        containerFormulario.setBorderRadius(14);
        containerFormulario.setBorder(BORDA_CONTAINER_INTERNO, 1);

        ComponenteUtil.formatarScrollInvisivel(scrPerguntas);
        scrPerguntas.getVerticalScrollBar().setUnitIncrement(16);

        iconeSlidersBranco = criarIconeSliders(Color.WHITE);
        iconeSlidersEscuro = criarIconeSliders(new Color(13, 82, 65));

        btnCriarPersonalizado.setIcon(iconeSlidersEscuro);
        btnExpandir.setIcon(criarIconeExpandir(new Color(120, 125, 130)));
        btnExpandir.setText("");

        estilizarBotaoArredondado(btnInfantilAdolescente);
        estilizarBotaoArredondado(btnAdulto);
        estilizarBotaoArredondado(btnCriarPersonalizado);
        estilizarBotaoArredondado(btnSalvarAnamnese);

        configurarEstiloDropdown();
        carregarDropdownFormularios(null);
        selecionarModeloPadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
    }

    private void estilizarBotaoArredondado(JButton botao) {
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
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
                    // Item exibido na barra principal do JComboBox
                    if (dropdownSelecionado) {
                        lbl.setBackground(VERDE_BOTAO_ATIVO);
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setBackground(CINZA_BOTAO_INATIVO);
                        lbl.setForeground(TEXTO_BOTAO_INATIVO);
                    }
                } else {
                    // Itens dentro da lista aberta do dropdown
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

        // Quando o usuário clica/abre o dropdown "Selecionar anamnese", ativa o mesmo efeito de selecionado
        cmbPersonalizados.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
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
                atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
            }
        });
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

        // Todos os 4 controles permanecem sempre visíveis
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

        // Estilo de selecionado para o botão único "Criar Personalizado"
        if (aba == AbaBarraAnamnese.CRIAR_PERSONALIZADO) {
            btnCriarPersonalizado.setBackground(VERDE_BOTAO_ATIVO);
            btnCriarPersonalizado.setForeground(Color.WHITE);
            btnCriarPersonalizado.setIcon(iconeSlidersBranco);
        } else {
            btnCriarPersonalizado.setBackground(CINZA_BOTAO_INATIVO);
            btnCriarPersonalizado.setForeground(TEXTO_BOTAO_INATIVO);
            btnCriarPersonalizado.setIcon(iconeSlidersEscuro);
        }

        // Estilo de selecionado para o dropdown "Selecionar anamnese"
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
                && ("Anamnese Infantil / Adolescente".equals(f.getTitulo()) || "Anamnese Adulto".equals(f.getTitulo()));
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

        String tituloPadrao = (tipo == TipoModeloAnamnese.INFANTIL_ADOLESCENTE)
                ? "Anamnese Infantil / Adolescente"
                : "Anamnese Adulto";

        txtTituloAnamnese.setText(tituloPadrao);
        txtTituloAnamnese.setEditable(false);
        txtTituloAnamnese.setBackground(Color.WHITE);

        formularioAtual = anamneseDAO.buscarModeloPadrao(tipo);
        pnlRodapeAcoes.setVisible(false);

        renderizarPreviewModeloPadrao(formularioAtual);
    }

    private void renderizarPreviewModeloPadrao(FormularioAnamnese form) {
        pnlConteudoAnamnese.removeAll();
        pnlConteudoAnamnese.setLayout(new BorderLayout());
        pnlConteudoAnamnese.setBackground(Color.WHITE);

        JPanel painelCentral = new JPanel();
        painelCentral.setOpaque(false);
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

        painelCentral.add(Box.createVerticalGlue());

        JLabel lblPreview = new JLabel("Preview da Anamnese", SwingConstants.CENTER);
        lblPreview.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPreview.setForeground(new Color(18, 22, 26));
        lblPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCentral.add(lblPreview);

        painelCentral.add(Box.createVerticalStrut(16));

        JPanel botoesAcaoPadrao = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        botoesAcaoPadrao.setOpaque(false);

        JButton btnPreencherPadrao = new JButton("Preencher Anamnese");
        btnPreencherPadrao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPreencherPadrao.setBackground(VERDE_BOTAO_ATIVO);
        btnPreencherPadrao.setForeground(Color.WHITE);
        btnPreencherPadrao.setFocusPainted(false);
        btnPreencherPadrao.setBorderPainted(false);
        btnPreencherPadrao.setOpaque(true);
        btnPreencherPadrao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPreencherPadrao.addActionListener(e -> abrirPreenchimentoFormulario(form, false));

        JButton btnEditarPadrao = new JButton("Personalizar este Modelo");
        btnEditarPadrao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnEditarPadrao.setBackground(CINZA_BOTAO_INATIVO);
        btnEditarPadrao.setForeground(TEXTO_BOTAO_INATIVO);
        btnEditarPadrao.setFocusPainted(false);
        btnEditarPadrao.setBorderPainted(false);
        btnEditarPadrao.setOpaque(true);
        btnEditarPadrao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditarPadrao.addActionListener(e -> {
            atualizarEstadoBarra(AbaBarraAnamnese.CRIAR_PERSONALIZADO);
            iniciarConstrutorComFormulario(form);
        });

        botoesAcaoPadrao.add(btnPreencherPadrao);
        botoesAcaoPadrao.add(btnEditarPadrao);
        botoesAcaoPadrao.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCentral.add(botoesAcaoPadrao);

        painelCentral.add(Box.createVerticalGlue());

        pnlConteudoAnamnese.add(painelCentral, BorderLayout.CENTER);
        pnlConteudoAnamnese.revalidate();
        pnlConteudoAnamnese.repaint();
    }

    private void iniciarModoCriarPersonalizado() {
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

        FormularioAnamnese novo = new FormularioAnamnese();
        novo.setTitulo("Titulo");
        novo.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
        novo.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

        novo.adicionarPergunta(new PerguntaAnamnese(
                "Pergunta",
                1,
                TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                new ArrayList<>(Arrays.asList("Opção 1", "Opção 2"))
        ));
        novo.adicionarPergunta(new PerguntaAnamnese(
                "Pergunta",
                2,
                TipoPerguntaAnamnese.ESTRELAS,
                Collections.emptyList()
        ));

        iniciarConstrutorComFormulario(novo);
    }

    private void iniciarConstrutorComFormulario(FormularioAnamnese base) {
        this.modoEdicaoQuestionario = true;
        this.formularioAtual = new FormularioAnamnese();
        this.formularioAtual.setTitulo(base != null && base.getTitulo() != null ? base.getTitulo() : "Titulo");
        this.formularioAtual.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
        this.formularioAtual.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

        if (base != null && base.getPerguntas() != null) {
            int ordem = 1;
            for (PerguntaAnamnese p : base.getPerguntas()) {
                this.formularioAtual.adicionarPergunta(new PerguntaAnamnese(
                        p.getEnunciado(),
                        ordem++,
                        p.getTipoPergunta(),
                        p.getOpcoes()
                ));
            }
        }

        txtTituloAnamnese.setText(this.formularioAtual.getTitulo());
        txtTituloAnamnese.setEditable(true);
        txtTituloAnamnese.requestFocusInWindow();

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
            pnlConteudoAnamnese.add(Box.createVerticalStrut(18));
        }

        pnlConteudoAnamnese.add(Box.createVerticalGlue());
        pnlConteudoAnamnese.revalidate();
        pnlConteudoAnamnese.repaint();
    }

    private JPanel criarCardEditorPergunta(int indiceZeroBased, PerguntaAnamnese pergunta) {
        int numero = indiceZeroBased + 1;

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(242, 244, 247)),
                new EmptyBorder(6, 4, 12, 4)
        ));

        JPanel linhaTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        linhaTopo.setOpaque(false);
        linhaTopo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNumero = new JLabel(numero + ".");
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblNumero.setForeground(new Color(45, 50, 56));

        JTextField txtEnunciado = new JTextField(pergunta.getEnunciado() != null ? pergunta.getEnunciado() : "Pergunta", 24);
        txtEnunciado.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEnunciado.setForeground(new Color(45, 50, 56));
        txtEnunciado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 224, 230)),
                new EmptyBorder(2, 4, 2, 4)
        ));

        JComboBox<TipoPerguntaAnamnese> cmbTipo = new JComboBox<>(TipoPerguntaAnamnese.values());
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbTipo.setBackground(new Color(246, 248, 250));
        cmbTipo.setSelectedItem(pergunta.getTipoPergunta());

        JButton btnRemoverPergunta = new JButton("×");
        btnRemoverPergunta.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRemoverPergunta.setForeground(new Color(160, 165, 175));
        btnRemoverPergunta.setBorder(new EmptyBorder(0, 6, 0, 6));
        btnRemoverPergunta.setContentAreaFilled(false);
        btnRemoverPergunta.setFocusPainted(false);
        btnRemoverPergunta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemoverPergunta.setToolTipText("Remover esta pergunta");

        linhaTopo.add(lblNumero);
        linhaTopo.add(txtEnunciado);
        linhaTopo.add(Box.createHorizontalStrut(10));
        linhaTopo.add(cmbTipo);
        if (formularioAtual.getPerguntas().size() > 1) {
            linhaTopo.add(btnRemoverPergunta);
        }

        card.add(linhaTopo);
        card.add(Box.createVerticalStrut(8));

        JPanel painelCorpo = new JPanel();
        painelCorpo.setLayout(new BoxLayout(painelCorpo, BoxLayout.Y_AXIS));
        painelCorpo.setOpaque(false);
        painelCorpo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCorpo.setBorder(new EmptyBorder(0, 18, 0, 0));

        List<JTextField> camposOpcoes = new ArrayList<>();

        TipoPerguntaAnamnese tipo = pergunta.getTipoPergunta();
        if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA || tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
            if (pergunta.getOpcoes() == null || pergunta.getOpcoes().isEmpty()) {
                pergunta.setOpcoes(new ArrayList<>(Arrays.asList("Opção 1", "Opção 2")));
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
                } else {
                    JCheckBox cb = new JCheckBox();
                    cb.setOpaque(false);
                    linhaOpcao.add(cb);
                }

                JTextField txtOpcao = new JTextField(opcoesAtuais.get(idxOp), 18);
                txtOpcao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                txtOpcao.setForeground(new Color(65, 70, 75));
                txtOpcao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 238, 242)),
                        new EmptyBorder(2, 2, 2, 2)
                ));
                camposOpcoes.add(txtOpcao);
                linhaOpcao.add(txtOpcao);

                if (opcoesAtuais.size() > 1) {
                    JButton btnRemOp = new JButton("–");
                    btnRemOp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    btnRemOp.setForeground(new Color(170, 175, 180));
                    btnRemOp.setBorder(new EmptyBorder(0, 4, 0, 4));
                    btnRemOp.setContentAreaFilled(false);
                    btnRemOp.setFocusPainted(false);
                    btnRemOp.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnRemOp.setToolTipText("Remover opção");
                    btnRemOp.addActionListener(e -> {
                        sincronizarEdicaoAtualNoFormulario();
                        pergunta.getOpcoes().remove(indiceOpcao);
                        renderizarConstrutorGoogleForms();
                    });
                    linhaOpcao.add(btnRemOp);
                }

                painelCorpo.add(linhaOpcao);
            }

            JPanel linhaAddOpcao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
            linhaAddOpcao.setOpaque(false);
            linhaAddOpcao.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton btnAddOpcao = new JButton("+ Adicionar opção");
            btnAddOpcao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnAddOpcao.setForeground(VERDE_BOTAO_ATIVO);
            btnAddOpcao.setBorder(new EmptyBorder(2, 4, 2, 4));
            btnAddOpcao.setContentAreaFilled(false);
            btnAddOpcao.setFocusPainted(false);
            btnAddOpcao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAddOpcao.addActionListener(e -> {
                sincronizarEdicaoAtualNoFormulario();
                int prox = pergunta.getOpcoes().size() + 1;
                pergunta.getOpcoes().add("Opção " + prox);
                renderizarConstrutorGoogleForms();
            });
            linhaAddOpcao.add(btnAddOpcao);
            painelCorpo.add(linhaAddOpcao);

        } else if (tipo == TipoPerguntaAnamnese.ESTRELAS) {
            JPanel linhaEstrelas = criarPainelEstrelasInterativo(indiceZeroBased, 0);
            linhaEstrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCorpo.add(linhaEstrelas);

        } else if (tipo == TipoPerguntaAnamnese.TEXTO_LIVRE) {
            JTextField txtExemplo = new JTextField("Texto de resposta curta/longa...", 28);
            txtExemplo.setEditable(false);
            txtExemplo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            txtExemplo.setForeground(new Color(140, 145, 150));
            txtExemplo.setBackground(new Color(250, 251, 252));
            txtExemplo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 220)));

            JPanel wrapperTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            wrapperTexto.setOpaque(false);
            wrapperTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapperTexto.add(txtExemplo);
            painelCorpo.add(wrapperTexto);
        }

        card.add(painelCorpo);

        PerguntaEditorBinder binder = new PerguntaEditorBinder(pergunta, txtEnunciado, cmbTipo, camposOpcoes);
        editoresPerguntas.add(binder);

        cmbTipo.addActionListener(e -> {
            sincronizarEdicaoAtualNoFormulario();
            TipoPerguntaAnamnese novoTipo = (TipoPerguntaAnamnese) cmbTipo.getSelectedItem();
            pergunta.setTipoPergunta(novoTipo);
            if ((novoTipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA || novoTipo == TipoPerguntaAnamnese.CAIXA_SELECAO)
                    && (pergunta.getOpcoes() == null || pergunta.getOpcoes().isEmpty())) {
                pergunta.setOpcoes(new ArrayList<>(Arrays.asList("Opção 1", "Opção 2")));
            }
            renderizarConstrutorGoogleForms();
        });

        btnRemoverPergunta.addActionListener(e -> {
            sincronizarEdicaoAtualNoFormulario();
            formularioAtual.getPerguntas().remove(indiceZeroBased);
            renderizarConstrutorGoogleForms();
        });

        return card;
    }

    private JPanel criarPainelEstrelasInterativo(final int indicePergunta, int notaInicial) {
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
        this.respostasPreenchimento.clear();

        if (selecionadoPeloDropdown) {
            this.modoAtual = TipoModeloAnamnese.PERSONALIZADO;
            atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        }

        txtTituloAnamnese.setText(form.getTitulo());
        txtTituloAnamnese.setEditable(false);

        btnAdicionarPergunta.setVisible(false);
        btnSalvarAnamnese.setText("SALVAR");
        pnlRodapeAcoes.setVisible(true);

        pnlConteudoAnamnese.removeAll();
        pnlConteudoAnamnese.setLayout(new BoxLayout(pnlConteudoAnamnese, BoxLayout.Y_AXIS));
        pnlConteudoAnamnese.setBackground(Color.WHITE);
        pnlConteudoAnamnese.setBorder(new EmptyBorder(12, 24, 12, 24));

        List<PerguntaAnamnese> perguntas = form.getPerguntas();
        for (int i = 0; i < perguntas.size(); i++) {
            final int idx = i;
            PerguntaAnamnese p = perguntas.get(i);

            JPanel bloco = new JPanel();
            bloco.setLayout(new BoxLayout(bloco, BoxLayout.Y_AXIS));
            bloco.setOpaque(false);
            bloco.setAlignmentX(Component.LEFT_ALIGNMENT);
            bloco.setBorder(new EmptyBorder(6, 4, 14, 4));

            JLabel lblPergunta = new JLabel((i + 1) + ". " + p.getEnunciado());
            lblPergunta.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblPergunta.setForeground(new Color(40, 45, 50));
            lblPergunta.setAlignmentX(Component.LEFT_ALIGNMENT);
            bloco.add(lblPergunta);
            bloco.add(Box.createVerticalStrut(8));

            JPanel corpo = new JPanel();
            corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
            corpo.setOpaque(false);
            corpo.setAlignmentX(Component.LEFT_ALIGNMENT);
            corpo.setBorder(new EmptyBorder(0, 18, 0, 0));

            if (p.getTipoPergunta() == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA) {
                ButtonGroup bg = new ButtonGroup();
                for (String op : p.getOpcoes()) {
                    JRadioButton rb = new JRadioButton(op);
                    rb.setOpaque(false);
                    rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    rb.setForeground(new Color(65, 70, 75));
                    rb.addActionListener(e -> respostasPreenchimento.put(idx, op));
                    bg.add(rb);
                    corpo.add(rb);
                    corpo.add(Box.createVerticalStrut(4));
                }
            } else if (p.getTipoPergunta() == TipoPerguntaAnamnese.CAIXA_SELECAO) {
                final List<JCheckBox> checks = new ArrayList<>();
                for (String op : p.getOpcoes()) {
                    JCheckBox cb = new JCheckBox(op);
                    cb.setOpaque(false);
                    cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    cb.setForeground(new Color(65, 70, 75));
                    checks.add(cb);
                    cb.addActionListener(e -> {
                        List<String> marcadas = new ArrayList<>();
                        for (JCheckBox c : checks) {
                            if (c.isSelected()) {
                                marcadas.add(c.getText());
                            }
                        }
                        respostasPreenchimento.put(idx, String.join(", ", marcadas));
                    });
                    corpo.add(cb);
                    corpo.add(Box.createVerticalStrut(4));
                }
            } else if (p.getTipoPergunta() == TipoPerguntaAnamnese.ESTRELAS) {
                JPanel estrelas = criarPainelEstrelasInterativo(idx, 0);
                estrelas.setAlignmentX(Component.LEFT_ALIGNMENT);
                corpo.add(estrelas);
            } else {
                JTextArea txtResp = new JTextArea(3, 36);
                txtResp.setLineWrap(true);
                txtResp.setWrapStyleWord(true);
                txtResp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                txtResp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 215, 220)),
                        new EmptyBorder(6, 8, 6, 8)
                ));
                txtResp.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                    @Override
                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        respostasPreenchimento.put(idx, txtResp.getText());
                    }

                    @Override
                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        respostasPreenchimento.put(idx, txtResp.getText());
                    }

                    @Override
                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        respostasPreenchimento.put(idx, txtResp.getText());
                    }
                });
                corpo.add(txtResp);
            }

            bloco.add(corpo);
            pnlConteudoAnamnese.add(bloco);
        }

        JPanel barraEditarEstrutura = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        barraEditarEstrutura.setOpaque(false);
        barraEditarEstrutura.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnEditarEstrutura = new JButton("Editar perguntas deste modelo");
        btnEditarEstrutura.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnEditarEstrutura.setForeground(VERDE_BOTAO_ATIVO);
        btnEditarEstrutura.setContentAreaFilled(false);
        btnEditarEstrutura.setBorderPainted(false);
        btnEditarEstrutura.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditarEstrutura.addActionListener(e -> {
            atualizarEstadoBarra(AbaBarraAnamnese.CRIAR_PERSONALIZADO);
            iniciarConstrutorComFormulario(form);
        });
        barraEditarEstrutura.add(btnEditarEstrutura);
        pnlConteudoAnamnese.add(barraEditarEstrutura);

        pnlConteudoAnamnese.revalidate();
        pnlConteudoAnamnese.repaint();
    }

    private void sincronizarEdicaoAtualNoFormulario() {
        if (!modoEdicaoQuestionario || formularioAtual == null || editoresPerguntas.isEmpty()) {
            return;
        }
        String tituloDigitado = txtTituloAnamnese.getText();
        if (tituloDigitado != null && !tituloDigitado.isBlank()) {
            formularioAtual.setTitulo(tituloDigitado.trim());
        }
        for (int i = 0; i < editoresPerguntas.size(); i++) {
            PerguntaEditorBinder binder = editoresPerguntas.get(i);
            binder.sincronizar(i + 1);
        }
    }

    private static class PerguntaEditorBinder {

        private final PerguntaAnamnese pergunta;
        private final JTextField txtEnunciado;
        private final JComboBox<TipoPerguntaAnamnese> cmbTipo;
        private final List<JTextField> txtOpcoes;

        public PerguntaEditorBinder(PerguntaAnamnese pergunta,
                JTextField txtEnunciado,
                JComboBox<TipoPerguntaAnamnese> cmbTipo,
                List<JTextField> txtOpcoes) {
            this.pergunta = pergunta;
            this.txtEnunciado = txtEnunciado;
            this.cmbTipo = cmbTipo;
            this.txtOpcoes = txtOpcoes;
        }

        public void sincronizar(int ordem) {
            pergunta.setOrdem(ordem);
            String enunciado = txtEnunciado.getText();
            pergunta.setEnunciado((enunciado == null || enunciado.isBlank()) ? "Pergunta " + ordem : enunciado.trim());
            TipoPerguntaAnamnese tipo = (TipoPerguntaAnamnese) cmbTipo.getSelectedItem();
            if (tipo != null) {
                pergunta.setTipoPergunta(tipo);
            }
            if (tipo == TipoPerguntaAnamnese.MULTIPLA_ESCOLHA || tipo == TipoPerguntaAnamnese.CAIXA_SELECAO) {
                List<String> novasOpcoes = new ArrayList<>();
                for (int i = 0; i < txtOpcoes.size(); i++) {
                    String val = txtOpcoes.get(i).getText();
                    novasOpcoes.add((val == null || val.isBlank()) ? "Opção " + (i + 1) : val.trim());
                }
                pergunta.setOpcoes(novasOpcoes);
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

        txtTituloAnamnese.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        txtTituloAnamnese.setForeground(new java.awt.Color(13, 17, 23));
        txtTituloAnamnese.setText("Anamnese Infantil / Adolescente");
        txtTituloAnamnese.setBorder(null);

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
        btnAdicionarPergunta.setToolTipText("Adicionar nova pergunta");
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
                        .addComponent(txtTituloAnamnese)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        atualizarEstadoBarra(AbaBarraAnamnese.DROPDOWN_SELECIONAR);
        Object item = cmbPersonalizados.getSelectedItem();
        if (item instanceof FormularioAnamnese) {
            FormularioAnamnese selecionado = (FormularioAnamnese) item;
            abrirPreenchimentoFormulario(selecionado, true);
        }
    }//GEN-LAST:event_cmbPersonalizadosActionPerformed

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
        renderizarConstrutorGoogleForms();
    }//GEN-LAST:event_btnAdicionarPerguntaActionPerformed

    private void btnSalvarAnamneseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarAnamneseActionPerformed
        if (formularioAtual == null) {
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

            formularioAtual.setTitulo(titulo.trim());
            formularioAtual.setTipoModelo(TipoModeloAnamnese.PERSONALIZADO);
            formularioAtual.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

            if (formularioAtual.getId() == null) {
                anamneseDAO.cadastrarFormulario(formularioAtual);
            } else {
                anamneseDAO.atualizarFormulario(formularioAtual);
            }

            carregarDropdownFormularios(formularioAtual);
            abrirPreenchimentoFormulario(formularioAtual, true);

            JOptionPane.showMessageDialog(
                    this,
                    "Formulário de anamnese \"" + formularioAtual.getTitulo() + "\" salvo e adicionado à lista!",
                    "Anamnese Salva",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            StringBuilder resumo = new StringBuilder();
            List<PerguntaAnamnese> perguntas = formularioAtual.getPerguntas();
            for (int i = 0; i < perguntas.size(); i++) {
                PerguntaAnamnese p = perguntas.get(i);
                String resp = respostasPreenchimento.getOrDefault(i, "Não respondida");
                resumo.append(i + 1).append(". ").append(p.getEnunciado())
                        .append(": ").append(resp).append("\n");
            }

            RespostaAnamnese resposta = new RespostaAnamnese(formularioAtual, paciente, resumo.toString());
            anamneseDAO.salvarResposta(resposta);

            JOptionPane.showMessageDialog(
                    this,
                    "Respostas da anamnese \"" + formularioAtual.getTitulo() + "\" registradas com sucesso!",
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

        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(janelaPai, "Anamnese - " + txtTituloAnamnese.getText(), JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(980, 720);
        dialog.setLocationRelativeTo(this);

        FichaAnaminaseView viewExpandida = new FichaAnaminaseView(this.paciente);
        if (this.modoEdicaoQuestionario) {
            viewExpandida.atualizarEstadoBarra(AbaBarraAnamnese.CRIAR_PERSONALIZADO);
            viewExpandida.iniciarConstrutorComFormulario(this.formularioAtual);
        } else if (this.abaAtiva == AbaBarraAnamnese.DROPDOWN_SELECIONAR) {
            viewExpandida.carregarDropdownFormularios(this.formularioAtual);
            viewExpandida.abrirPreenchimentoFormulario(this.formularioAtual, true);
        } else {
            viewExpandida.selecionarModeloPadrao(this.modoAtual);
        }

        dialog.setContentPane(viewExpandida);
        dialog.setVisible(true);

        carregarDropdownFormularios(this.formularioAtual);
    }//GEN-LAST:event_btnExpandirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarPergunta;
    private javax.swing.JButton btnAdulto;
    private javax.swing.JButton btnCriarPersonalizado;
    private javax.swing.JButton btnExpandir;
    private javax.swing.JButton btnInfantilAdolescente;
    private javax.swing.JButton btnSalvarAnamnese;
    private com.mycompany.sistemaintegramind.util.Utilitarios.CircularIconLabel circularIconHeader;
    private javax.swing.JComboBox<Object> cmbPersonalizados;
    private com.mycompany.sistemaintegramind.View.Componentes.Container container1;
    private com.mycompany.sistemaintegramind.View.Componentes.Container containerFormulario;
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
    private javax.swing.JTextField txtTituloAnamnese;
    // End of variables declaration//GEN-END:variables
}
