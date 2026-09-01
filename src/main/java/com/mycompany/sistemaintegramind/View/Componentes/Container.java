/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;

/**
 *
 * @author guilh
 */
public class Container extends javax.swing.JPanel {

    // =========================================================
    // CONFIGURAÇÕES DO CONTAINER
    // =========================================================
    private int borderRadius = 30;

    // =========================================================
    // BORDA
    // =========================================================
    private Color borderColor = null;
    private int borderWidth = 0;

    // =========================================================
    // SOMBRA
    // =========================================================
    private Color shadowColor = null;
    private int shadowX = 0;
    private int shadowY = 0;
    private int shadowBlur = 0;

    /**
     * Creates new form Container
     */
    public Container() {

        initComponents();

        setOpaque(false);

        atualizarMargemSombra();
    }

    // =========================================================
    // PAINT
    // =========================================================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        // =====================================================
        // ESPAÇO RESERVADO PARA A SOMBRA
        // =====================================================
        int margem = shadowBlur + Math.max(
                Math.abs(shadowX),
                Math.abs(shadowY)
        );

        // =====================================================
        // TAMANHO REAL DO CARD
        // =====================================================
        int cardX = margem;
        int cardY = margem;

        int cardWidth = getWidth() - (margem * 2);
        int cardHeight = getHeight() - (margem * 2);

        if (cardWidth <= 0 || cardHeight <= 0) {
            g2.dispose();
            return;
        }

        // =====================================================
        // SOMBRA
        // =====================================================
        if (shadowColor != null && shadowBlur > 0) {

            for (int i = shadowBlur; i >= 1; i--) {

                /*
                 * Quanto mais longe do card,
                 * mais transparente fica.
                 */
                float porcentagem
                        = (float) (shadowBlur - i + 1)
                        / shadowBlur;

                int alpha = (int) (shadowColor.getAlpha()
                        * porcentagem
                        * 0.35f);

                if (alpha <= 0) {
                    continue;
                }

                Color sombra = new Color(
                        shadowColor.getRed(),
                        shadowColor.getGreen(),
                        shadowColor.getBlue(),
                        alpha
                );

                g2.setColor(sombra);

                /*
                 * A sombra cresce para fora do card.
                 */
                int x = cardX + shadowX - i;
                int y = cardY + shadowY - i;

                int width = cardWidth + (i * 2);
                int height = cardHeight + (i * 2);

                g2.fillRoundRect(
                        x,
                        y,
                        width,
                        height,
                        borderRadius + (i * 2),
                        borderRadius + (i * 2)
                );
            }
        }

        // =====================================================
        // FUNDO
        // =====================================================
        g2.setColor(getBackground());

        g2.fillRoundRect(
                cardX,
                cardY,
                cardWidth,
                cardHeight,
                borderRadius,
                borderRadius
        );

        // =====================================================
        // BORDA
        // =====================================================
        if (borderColor != null && borderWidth > 0) {

            g2.setColor(borderColor);

            float espessura = borderWidth;

            g2.setStroke(
                    new java.awt.BasicStroke(
                            espessura,
                            java.awt.BasicStroke.CAP_ROUND,
                            java.awt.BasicStroke.JOIN_ROUND
                    )
            );

            int offset = borderWidth / 2;

            g2.drawRoundRect(
                    cardX + offset,
                    cardY + offset,
                    cardWidth - borderWidth,
                    cardHeight - borderWidth,
                    borderRadius,
                    borderRadius
            );
        }

        g2.dispose();
    }

    // =========================================================
    // ATUALIZAR MARGEM DA SOMBRA
    // =========================================================
    private void atualizarMargemSombra() {

        int margem = shadowBlur + Math.max(
                Math.abs(shadowX),
                Math.abs(shadowY)
        );

        /*
         * Reserva espaço para a sombra.
         *
         * Isso impede que ela seja cortada
         * pelo limite do JPanel.
         */
        setBorder(
                BorderFactory.createEmptyBorder(
                        margem,
                        margem,
                        margem,
                        margem
                )
        );
    }

    // =========================================================
    // BORDER RADIUS
    // =========================================================
    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {

        this.borderRadius = Math.max(0, borderRadius);

        repaint();
    }

    // =========================================================
    // BORDA
    // =========================================================
    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorder(Color color, int width) {

        this.borderColor = color;
        this.borderWidth = Math.max(0, width);

        repaint();
    }

    public void removeBorder() {

        this.borderColor = null;
        this.borderWidth = 0;

        repaint();
    }

    // =========================================================
    // SOMBRA
    // =========================================================
    public void setShadow(
            Color color,
            int x,
            int y,
            int blur
    ) {

        this.shadowColor = color;
        this.shadowX = x;
        this.shadowY = y;
        this.shadowBlur = Math.max(0, blur);

        atualizarMargemSombra();

        revalidate();
        repaint();
    }

    // =========================================================
    // SOMBRA PADRÃO
    // =========================================================
    public void setShadow() {

        setShadow(
                new Color(0, 0, 0, 45),
                3,
                3,
                10
        );
    }

    // =========================================================
    // REMOVER SOMBRA
    // =========================================================
    public void removeShadow() {

        this.shadowColor = null;
        this.shadowX = 0;
        this.shadowY = 0;
        this.shadowBlur = 0;

        atualizarMargemSombra();

        revalidate();
        repaint();
    }
    
    public void setCardStyle() {

    setBackground(new Color(255, 255, 255));

    setBorderRadius(22);

    setBorder(
        new Color(230, 232, 235),
        1
    );

    setShadow(
        new Color(0, 0, 0, 15),
        2,
        3,
        9
    );
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(246, 245, 250));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 377, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
