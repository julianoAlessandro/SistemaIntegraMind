/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Classe utilitária para manipulação de imagens e ícones no Swing.
 *
 * @author guilh
 */
public class ImagemUtil {

    // =========================================================
    // 1. ALTERAR A COR DA IMAGEM (ÍCONE PRETO/MONOCROMÁTICO)
    // =========================================================
    /**
     * Altera a cor de uma imagem monocromática (ex: silhueta preta em PNG transparente)
     * preservando a transparência original do canal Alpha.
     *
     * @param imagemOriginal Imagem base
     * @param novaCor Cor desejada para a imagem
     * @return Nova Image com a cor aplicada
     */
    public static Image recolorir(Image imagemOriginal, Color novaCor) {
        if (imagemOriginal == null || novaCor == null) {
            return imagemOriginal;
        }

        BufferedImage bufferedOriginal = paraBufferedImage(imagemOriginal);
        int width = bufferedOriginal.getWidth();
        int height = bufferedOriginal.getHeight();

        if (width <= 0 || height <= 0) {
            return imagemOriginal;
        }

        BufferedImage imgColorida = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imgColorida.createGraphics();

        // Configurações de qualidade
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 1. Desenha a imagem original
        g2.drawImage(bufferedOriginal, 0, 0, null);

        // 2. Aplica o modo SRC_IN: pinta APENAS sobre os pixels visíveis/não-transparentes
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(novaCor);
        g2.fillRect(0, 0, width, height);

        g2.dispose();

        return imgColorida;
    }

    public static Icon recolorir(Icon icone, Color novaCor) {
        Image img = converterIconeParaImagem(icone);
        Image imgColorida = recolorir(img, novaCor);
        return new ImageIcon(imgColorida);
    }

    // =========================================================
    // 2. REDIMENSIONAR IMAGEM
    // =========================================================
    /**
     * Redimensiona uma imagem mantendo alta qualidade de renderização (Bicubic).
     *
     * @param imagemOriginal Imagem base
     * @param largura Nova largura em pixels
     * @param altura Nova altura em pixels
     * @return Imagem redimensionada
     */
    public static Image redimensionar(Image imagemOriginal, int largura, int altura) {
        if (imagemOriginal == null || largura <= 0 || altura <= 0) {
            return imagemOriginal;
        }

        BufferedImage imgRedimensionada = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imgRedimensionada.createGraphics();

        // Renderização com alta qualidade
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(imagemOriginal, 0, 0, largura, altura, null);
        g2.dispose();

        return imgRedimensionada;
    }

    public static Icon redimensionar(Icon icone, int largura, int altura) {
        Image img = converterIconeParaImagem(icone);
        Image imgRedim = redimensionar(img, largura, altura);
        return new ImageIcon(imgRedim);
    }

    // =========================================================
    // 3. COMBINAR REDIMENSIONAR + RECOLORIR
    // =========================================================
    /**
     * Altera a cor e redimensiona a imagem em uma única operação.
     */
    public static Image recolorirERedimensionar(Image imagemOriginal, Color novaCor, int largura, int altura) {
        Image imgColorida = recolorir(imagemOriginal, novaCor);
        return redimensionar(imgColorida, largura, altura);
    }

    public static Icon recolorirERedimensionar(Icon icone, Color novaCor, int largura, int altura) {
        Image img = converterIconeParaImagem(icone);
        Image imgProcessada = recolorirERedimensionar(img, novaCor, largura, altura);
        return new ImageIcon(imgProcessada);
    }

    // =========================================================
    // 4. MÉTODOS DE CONVERSÃO / HELPER
    // =========================================================
    /**
     * Converte um Icon/ImageIcon genérico em Image.
     */
    public static Image converterIconeParaImagem(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else if (icon != null) {
            BufferedImage img = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = img.createGraphics();
            icon.paintIcon(null, g2, 0, 0);
            g2.dispose();
            return img;
        }
        return null;
    }

    /**
     * Garante a conversão de Image para BufferedImage.
     */
    public static BufferedImage paraBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = bimage.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        return bimage;
    }
}