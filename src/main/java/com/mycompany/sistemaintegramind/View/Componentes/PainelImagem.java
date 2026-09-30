package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PainelImagem extends JPanel {

    private Image imagemOriginal;

    public PainelImagem() {
        setOpaque(false);
    }

    public void setImagem(ImageIcon icon) {
        if (icon != null) {
            this.imagemOriginal = icon.getImage();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagemOriginal != null) {
            // Redimensiona a imagem para ocupar todo o componente
            g.drawImage(
                imagemOriginal,
                0,
                0,
                getWidth(),
                getHeight(),
                this
            );
        }
    }
}
