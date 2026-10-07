package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Componente de Painel com Sombra, Bordas Arredondadas e Imagem de Fundo
 *
 * @author guilh
 */
public class Container extends javax.swing.JPanel {

    // =========================================================
    // ENUM PARA POSICIONAMENTO DA IMAGEM
    // =========================================================
    public enum Alignment {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
    }

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

    // =========================================================
    // IMAGEM DE FUNDO
    // =========================================================
    private Image backgroundImage = null;
    private Alignment imageAlignment = Alignment.BOTTOM_RIGHT;
    private int imageWidth = -1;  // -1 para tamanho original
    private int imageHeight = -1; // -1 para tamanho original
    private int offsetX = 0;      // Deslocamento X para vazar a imagem
    private int offsetY = 0;      // Deslocamento Y para vazar a imagem
    private float imageAlpha = 1.0f; // Opacidade da imagem (0.0f a 1.0f)

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
        // CÁLCULO DE POSICIONAMENTO DO CARD
        // =====================================================
        int topSpace = Math.max(shadowBlur - shadowY, shadowBlur);
        int leftSpace = Math.max(shadowBlur - shadowX, shadowBlur);
        int bottomSpace = Math.max(shadowBlur + shadowY, shadowBlur);
        int rightSpace = Math.max(shadowBlur + shadowX, shadowBlur);

        int cardX = leftSpace;
        int cardY = topSpace;

        int cardWidth = getWidth() - leftSpace - rightSpace;
        int cardHeight = getHeight() - topSpace - bottomSpace;

        if (cardWidth <= 0 || cardHeight <= 0) {
            g2.dispose();
            return;
        }

        // =====================================================
        // SOMBRA
        // =====================================================
        if (shadowColor != null && shadowBlur > 0) {

            for (int i = shadowBlur; i >= 1; i--) {

                float porcentagem = (float) (shadowBlur - i + 1) / shadowBlur;

                int alpha = (int) (shadowColor.getAlpha() * porcentagem * 0.35f);

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
        // FUNDO DO CARD
        // =====================================================
        g2.setColor(getBackground());

        Shape cardShape = new RoundRectangle2D.Float(
                cardX,
                cardY,
                cardWidth,
                cardHeight,
                borderRadius,
                borderRadius
        );

        g2.fill(cardShape);

        // =====================================================
        // IMAGEM DE FUNDO
        // =====================================================
        if (backgroundImage != null) {
            Shape oldClip = g2.getClip();
            java.awt.Composite oldComposite = g2.getComposite();

            // Recorta a imagem dentro dos limites do card arredondado
            g2.setClip(cardShape);

            // Aplica opacidade/transparência se definida
            if (imageAlpha < 1.0f) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha));
            }

            int origWidth = backgroundImage.getWidth(null);
            int origHeight = backgroundImage.getHeight(null);

            int drawWidth = (imageWidth > 0) ? imageWidth : origWidth;
            int drawHeight = (imageHeight > 0) ? imageHeight : origHeight;

            if (drawWidth > 0 && drawHeight > 0) {
                int imgX = cardX;
                int imgY = cardY;

                switch (imageAlignment) {
                    case TOP_LEFT:
                        imgX = cardX - offsetX;
                        imgY = cardY - offsetY;
                        break;
                    case TOP_RIGHT:
                        imgX = cardX + cardWidth - drawWidth + offsetX;
                        imgY = cardY - offsetY;
                        break;
                    case BOTTOM_LEFT:
                        imgX = cardX - offsetX;
                        imgY = cardY + cardHeight - drawHeight + offsetY;
                        break;
                    case BOTTOM_RIGHT:
                        imgX = cardX + cardWidth - drawWidth + offsetX;
                        imgY = cardY + cardHeight - drawHeight + offsetY;
                        break;
                    case CENTER:
                        imgX = cardX + (cardWidth - drawWidth) / 2 + offsetX;
                        imgY = cardY + (cardHeight - drawHeight) / 2 + offsetY;
                        break;
                }

                g2.drawImage(backgroundImage, imgX, imgY, drawWidth, drawHeight, null);
            }

            g2.setComposite(oldComposite);
            g2.setClip(oldClip);
        }

        // =====================================================
        // BORDA DO CARD
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

        int topSpace = Math.max(shadowBlur - shadowY, shadowBlur);
        int leftSpace = Math.max(shadowBlur - shadowX, shadowBlur);
        int bottomSpace = Math.max(shadowBlur + shadowY, shadowBlur);
        int rightSpace = Math.max(shadowBlur + shadowX, shadowBlur);

        setBorder(
                BorderFactory.createEmptyBorder(
                        topSpace,
                        leftSpace,
                        bottomSpace,
                        rightSpace
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
    public void setShadow(Color color, int x, int y, int blur) {
        this.shadowColor = color;
        this.shadowX = x;
        this.shadowY = y;
        this.shadowBlur = Math.max(0, blur);

        atualizarMargemSombra();
        revalidate();
        repaint();
    }

    public void setShadow() {
        setShadow(
                new Color(0, 0, 0, 45),
                3,
                3,
                10
        );
    }

    public void removeShadow() {
        this.shadowColor = null;
        this.shadowX = 0;
        this.shadowY = 0;
        this.shadowBlur = 0;

        atualizarMargemSombra();
        revalidate();
        repaint();
    }

    // =========================================================
    // ESTILOS DO CARD (SOBRECARGAS)
    // =========================================================
    public void setCardStyle() {
        setBackground(new Color(255, 255, 255));
        setBorderRadius(22);
        setBorder(new Color(230, 232, 235), 1);
        setShadow(new Color(0, 0, 0, 15), 2, 3, 9);
    }

    // 1. Método original preservado
    public void setCardStyle(Color backgroundColor) {
        setBackground(backgroundColor);
        setBorderRadius(22);

        Color borderColor = escurecerCor(backgroundColor, 0.90f);
        setBorder(borderColor, 2);

        setShadow(new Color(0, 0, 0, 10), 2, 3, 9);
    }

    // 2. Cor + Imagem padrão (Canto inferior direito, tamanho original)
    public void setCardStyle(Color backgroundColor, Image bgImage) {
        setCardStyle(backgroundColor, bgImage, Alignment.BOTTOM_RIGHT, -1, -1, 0, 0, 1.0f);
    }

    public void setCardStyle(Color backgroundColor, Icon bgIcon) {
        setCardStyle(backgroundColor, converterIconeParaImagem(bgIcon), Alignment.BOTTOM_RIGHT, -1, -1, 0, 0, 1.0f);
    }

    // 3. Cor + Imagem + Posição + Tamanho
    public void setCardStyle(Color backgroundColor, Image bgImage, Alignment alignment, int width, int height) {
        setCardStyle(backgroundColor, bgImage, alignment, width, height, 0, 0, 1.0f);
    }

    public void setCardStyle(Color backgroundColor, Icon bgIcon, Alignment alignment, int width, int height) {
        setCardStyle(backgroundColor, converterIconeParaImagem(bgIcon), alignment, width, height, 0, 0, 1.0f);
    }

    // 4. Cor + Imagem + Posição + Tamanho + Offsets (vazar borda)
    public void setCardStyle(Color backgroundColor, Image bgImage, Alignment alignment, int width, int height, int offsetX, int offsetY) {
        setCardStyle(backgroundColor, bgImage, alignment, width, height, offsetX, offsetY, 1.0f);
    }

    public void setCardStyle(Color backgroundColor, Icon bgIcon, Alignment alignment, int width, int height, int offsetX, int offsetY) {
        setCardStyle(backgroundColor, converterIconeParaImagem(bgIcon), alignment, width, height, offsetX, offsetY, 1.0f);
    }

    // 5. Método Mestre Completo (Com Opacidade)
    public void setCardStyle(Color backgroundColor, Image bgImage, Alignment alignment, int width, int height, int offsetX, int offsetY, float alpha) {
        this.backgroundImage = bgImage;
        this.imageAlignment = (alignment != null) ? alignment : Alignment.BOTTOM_RIGHT;
        this.imageWidth = width;
        this.imageHeight = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.imageAlpha = Math.max(0.0f, Math.min(1.0f, alpha));
        setCardStyle(backgroundColor);
    }

    public void setCardStyle(Color backgroundColor, Icon bgIcon, Alignment alignment, int width, int height, int offsetX, int offsetY, float alpha) {
        setCardStyle(backgroundColor, converterIconeParaImagem(bgIcon), alignment, width, height, offsetX, offsetY, alpha);
    }

    // Helper para converter ImageIcon/Icon em Image
    private Image converterIconeParaImagem(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else if (icon != null) {
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    java.awt.image.BufferedImage.TYPE_INT_ARGB
            );
            Graphics g = img.createGraphics();
            icon.paintIcon(this, g, 0, 0);
            g.dispose();
            return img;
        }
        return null;
    }

    // =========================================================
    // ESCURECER COR
    // =========================================================
    private Color escurecerCor(Color cor, float fator) {
        int red = Math.max(0, Math.min(255, (int) (cor.getRed() * fator)));
        int green = Math.max(0, Math.min(255, (int) (cor.getGreen() * fator)));
        int blue = Math.max(0, Math.min(255, (int) (cor.getBlue() * fator)));

        return new Color(red, green, blue);
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
