/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PainelImagemGradiente extends JPanel {

    private Image imagemOriginal;
    private BufferedImage imagemProcessada;

    public PainelImagemGradiente() {
        setOpaque(false); // Garante que o fundo do painel seja transparente
    }

    // Método para definir a imagem (pode ser usado via código ou propriedades)
    public void setImagem(ImageIcon icon) {
        if (icon != null) {
            this.imagemOriginal = icon.getImage();
            processarGradiente();
            repaint();
        }
    }

    private void processarGradiente() {
        if (imagemOriginal == null || getWidth() <= 0 || getHeight() <= 0) return;

        int w = getWidth();
        int h = getHeight();

        // Cria a imagem temporária no tamanho atual do componente
        imagemProcessada = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagemProcessada.createGraphics();

        // Desenha a imagem redimensionada para preencher o painel
        g2d.drawImage(imagemOriginal, 0, 0, w, h, null);

        // Define o gradiente de opacidade (Cima: Opaco -> Baixo: Transparente)
        GradientPaint gradienteAlpha = new GradientPaint(
            0, 0, new Color(0, 0, 0, 255), 
            0, h, new Color(0, 0, 0, 0)
        );

        g2d.setPaint(gradienteAlpha);
        g2d.setComposite(AlphaComposite.DstIn);
        g2d.fillRect(0, 0, w, h);

        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Se o tamanho mudou, reprocessa a imagem para não distorcer
        if (imagemProcessada == null || imagemProcessada.getWidth() != getWidth() || imagemProcessada.getHeight() != getHeight()) {
            processarGradiente();
        }

        if (imagemProcessada != null) {
            g.drawImage(imagemProcessada, 0, 0, null);
        }
    }
}