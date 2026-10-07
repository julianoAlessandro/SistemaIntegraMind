/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.Color;

/**
 * Paleta de Cores Universal do Sistema (Design System Tokens)
 *
 * @author guilh
 */
public class PaletaCores {

    // =========================================================
    // 1. CORES BASE DE MARCA / BRAND COLORS
    // =========================================================
    public static final Color PRIMARIA = new Color(15, 23, 42);   // Azul Noturno / Slate Deep
    public static final Color PRIMARIA_HOVER = new Color(30, 41, 59);   // Tom mais claro para interações
    public static final Color SECUNDARIA = new Color(14, 165, 233); // Azul Sky / Accent
    public static final Color SECUNDARIA_HOVER = new Color(56, 189, 248);

    // =========================================================
    // 2. CORES SEMÂNTICAS (STATUS E ALERTAS)
    // =========================================================
    public static final Color SUCESSO = new Color(16, 185, 129);  // Verde (Pacientes, Confirmados)
    public static final Color ALERTA = new Color(245, 158, 11);  // Amarelo/Laranja (Pendentes)
    public static final Color ERRO = new Color(239, 68, 68);   // Vermelho (Cancelados/Atrasos)
    public static final Color INFORMAÇÃO = new Color(59, 130, 246);  // Azul Info

    // =========================================================
    // 3. CORES NEUTRAS E SUPERFÍCIES (MODO ESCURO / DARK THEME)
    // =========================================================
    public static class Dark {

        public static final Color FUNDO_TELA = new Color(11, 15, 25);    // Fundo geral da janela
        public static final Color CARD_PACIENTES = new Color(15, 41, 38);    // Card verde escuro
        public static final Color CARD_HOJE = new Color(18, 38, 70);    // Card azul escuro
        public static final Color CARD_RECEITA = new Color(50, 40, 20);    // Card dourado/escuro
        public static final Color CARD_PENDENTES = new Color(55, 25, 40);    // Card rosa/roxo escuro

        public static final Color TEXTO_PRINCIPAL = new Color(248, 250, 252);
        public static final Color TEXTO_SECUNDARIO = new Color(148, 163, 184);
        public static final Color BORDA = new Color(30, 41, 59);
    }

    // =========================================================
    // 4. CORES NEUTRAS E SUPERFÍCIES (MODO CLARO / LIGHT THEME)
    // =========================================================
    public static class Light {

        public static final Color FUNDO_TELA = new Color(246, 245, 250);
        public static final Color CARD_FUNDO = new Color(255, 255, 255);
        public static final Color TEXTO_PRINCIPAL = new Color(15, 23, 42);
        public static final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);
        public static final Color BORDA = new Color(226, 232, 240);
    }

    // =========================================================
    // 5. HELPER PARA TRANSPARÊNCIAS (ALPHA)
    // =========================================================
    /**
     * Aplica uma transparência/opacidade personalizada a qualquer cor da
     * paleta.
     *
     * @param cor Cor base
     * @param opacidade Valor entre 0 (totalmente transparente) e 255 (sólido)
     */
    public static Color comAlpha(Color cor, int opacidade) {
        int alpha = Math.max(0, Math.min(255, opacidade));
        return new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), alpha);
    }
}
