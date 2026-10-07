package com.mycompany.sistemaintegramind.util.Utilitarios;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class CircularIconLabel extends JLabel {

    private Color shadowColor = new Color(0, 0, 0, 40);
    private int shadowBlur = 8;
    private int shadowOffsetX = 0;
    private int shadowOffsetY = 3;
    private int customCircleSize = 0;

    // Propriedades do Ícone
    private Color iconColor = null;
    private int iconWidth = 0;
    private int iconHeight = 0;
    private int iconOffsetX = 0;
    private int iconOffsetY = 0;

    // Variável que guarda a nossa versão "modificada" para desenhar na tela
    private Icon processedIcon = null;
    
    // Variável secreta para enganar a pintura padrão do JLabel
    private boolean hideIconForSuper = false;

    public CircularIconLabel() {
        super();
        init();
    }

    public CircularIconLabel(Icon icon) {
        super(icon);
        init();
    }

    private void init() {
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    // =========================================================================
    // Segredo do NetBeans: setIcon() guarda o original, e nós criamos o nosso
    // =========================================================================
    @Override
    public void setIcon(Icon icon) {
        // 1. Deixa o JLabel (e o NetBeans) guardar o ícone original nativamente
        super.setIcon(icon); 
        
        // 2. Atualiza a nossa versão modificada que será desenhada na tela
        updateProcessedIcon();
    }

    // Se o Java perguntar pelo ícone e a flag estiver true, dizemos que não tem
    // para evitar que ele desenhe a versão original por cima do nosso círculo.
    @Override
    public Icon getIcon() {
        if (hideIconForSuper) {
            return null;
        }
        return super.getIcon();
    }

    // =========================================================================
    // Processamento do Ícone (Gerado apenas quando as propriedades mudam)
    // =========================================================================
    private void updateProcessedIcon() {
        Icon icon = super.getIcon();
        if (icon == null) {
            processedIcon = null;
            return;
        }

        // Força o carregamento no Runtime
        if (icon instanceof ImageIcon) {
            Image img = ((ImageIcon) icon).getImage();
            if (img != null) {
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(img, 0);
                try { tracker.waitForAll(); } catch (InterruptedException ex) {}
            }
        }

        int origW = icon.getIconWidth();
        int origH = icon.getIconHeight();

        int w = (iconWidth > 0) ? iconWidth : origW;
        int h = (iconHeight > 0) ? iconHeight : origH;

        if (w <= 0 || h <= 0 || origW <= 0 || origH <= 0) {
            processedIcon = icon; // Fallback segurança
            return;
        }

        BufferedImage imgFinal = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imgFinal.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        double scaleX = (double) w / origW;
        double scaleY = (double) h / origH;

        AffineTransform oldTransform = g2.getTransform();
        g2.scale(scaleX, scaleY);
        icon.paintIcon(this, g2, 0, 0);
        g2.setTransform(oldTransform);

        if (iconColor != null) {
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.setColor(iconColor);
            g2.fillRect(0, 0, w, h);
        }

        g2.dispose();
        processedIcon = new ImageIcon(imgFinal);
        repaint();
    }

    // =========================================================================
    // Propriedades Visíveis no GUI Builder
    // =========================================================================
    public int getIconOffsetX() { return iconOffsetX; }
    public void setIconOffsetX(int iconOffsetX) { 
        this.iconOffsetX = iconOffsetX; 
        repaint(); 
    }

    public int getIconOffsetY() { return iconOffsetY; }
    public void setIconOffsetY(int iconOffsetY) { 
        this.iconOffsetY = iconOffsetY; 
        repaint(); 
    }

    public Color getIconColor() { return iconColor; }
    public void setIconColor(Color iconColor) { 
        this.iconColor = iconColor; 
        updateProcessedIcon(); 
    }

    public int getIconWidth() { return iconWidth; }
    public void setIconWidth(int iconWidth) { 
        this.iconWidth = Math.max(0, iconWidth); 
        updateProcessedIcon(); 
    }

    public int getIconHeight() { return iconHeight; }
    public void setIconHeight(int iconHeight) { 
        this.iconHeight = Math.max(0, iconHeight); 
        updateProcessedIcon(); 
    }

    public Color getShadowColor() { return shadowColor; }
    public void setShadowColor(Color shadowColor) { 
        this.shadowColor = shadowColor; 
        repaint(); 
    }

    public int getShadowBlur() { return shadowBlur; }
    public void setShadowBlur(int shadowBlur) { 
        this.shadowBlur = Math.max(0, shadowBlur); 
        repaint(); revalidate(); 
    }

    public int getShadowOffsetX() { return shadowOffsetX; }
    public void setShadowOffsetX(int shadowOffsetX) { 
        this.shadowOffsetX = shadowOffsetX; 
        repaint(); revalidate(); 
    }

    public int getShadowOffsetY() { return shadowOffsetY; }
    public void setShadowOffsetY(int shadowOffsetY) { 
        this.shadowOffsetY = shadowOffsetY; 
        repaint(); revalidate(); 
    }

    public int getCustomCircleSize() { return customCircleSize; }
    public void setCustomCircleSize(int customCircleSize) { 
        this.customCircleSize = Math.max(0, customCircleSize); 
        repaint(); revalidate(); 
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension base = super.getPreferredSize();
        int circle = (customCircleSize > 0) ? customCircleSize : Math.max(base.width, base.height);
        int padding = (shadowBlur + Math.max(Math.abs(shadowOffsetX), Math.abs(shadowOffsetY))) * 2;
        return new Dimension(circle + padding, circle + padding);
    }

    // =========================================================================
    // Renderização Definitiva da Tela
    // =========================================================================
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int marginX = shadowBlur + Math.abs(shadowOffsetX);
        int marginY = shadowBlur + Math.abs(shadowOffsetY);

        int diameter;
        if (customCircleSize > 0) {
            diameter = customCircleSize;
        } else {
            int availW = getWidth() - (marginX * 2);
            int availH = getHeight() - (marginY * 2);
            diameter = Math.max(1, Math.min(availW, availH));
        }

        if (diameter > 0) {
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            float radius = diameter / 2.0f;

            // 1. SOMBRA
            if (shadowColor != null && shadowColor.getAlpha() > 0 && shadowBlur > 0) {
                float shadowRadius = radius + shadowBlur;
                float shadowCenterX = x + radius + shadowOffsetX;
                float shadowCenterY = y + radius + shadowOffsetY;

                Color transparentColor = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), 0);
                RadialGradientPaint p = new RadialGradientPaint(
                        shadowCenterX, shadowCenterY, shadowRadius,
                        new float[]{0.0f, 1.0f}, new Color[]{shadowColor, transparentColor}
                );
                g2.setPaint(p);
                g2.fillOval((int) (shadowCenterX - shadowRadius), (int) (shadowCenterY - shadowRadius), (int) (shadowRadius * 2), (int) (shadowRadius * 2));
            }

            // 2. CÍRCULO PRINCIPAL
            Color circleColor = getBackground();
            if (circleColor != null) {
                g2.setColor(circleColor);
                g2.fillOval(x, y, diameter, diameter);
            }
        }
        g2.dispose();

        // 3. O SEGREDO: Esconde o ícone original momentaneamente para o JLabel não estragar a tela
        hideIconForSuper = true;
        super.paintComponent(g); // Aqui ele desenha apenas o texto (se existir)
        hideIconForSuper = false;

        // 4. Desenha o NOSSO ícone processado (Escalado, colorido e com offset)
        if (processedIcon != null) {
            int px = (getWidth() - processedIcon.getIconWidth()) / 2 + iconOffsetX;
            int py = (getHeight() - processedIcon.getIconHeight()) / 2 + iconOffsetY;
            processedIcon.paintIcon(this, g, px, py);
        }
    }
}