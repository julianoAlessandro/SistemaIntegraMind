package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MenuButtonUtils {

    public static void selecionarMenu(JButton botaoClicado, JButton[] botoes, String[] iconesBrancos, String[] iconesVerdes) {
        
        Color verdeTema = new Color(0, 102, 51); // Verde escuro (#006633)
        Color branco = Color.WHITE;

        for (int i = 0; i < botoes.length; i++) {
            JButton btn = botoes[i];
            boolean isAtivo = (btn == botaoClicado);

            // Seleciona o caminho correto de acordo com o estado do botão
            String caminhoIcone = isAtivo ? iconesBrancos[i] : iconesVerdes[i];

            // Ajusta as cores do texto e fundo
            if (isAtivo) {
                btn.setBackground(verdeTema);
                btn.setForeground(branco);
            } else {
                btn.setBackground(branco);
                btn.setForeground(verdeTema);
            }

            // Tenta carregar a imagem da pasta de recursos (root/src)
            try {
                // Remove a barra inicial caso exista para usar com o ContextClassLoader
                String path = caminhoIcone.startsWith("/") ? caminhoIcone.substring(1) : caminhoIcone;
                
                URL imgURL = Thread.currentThread().getContextClassLoader().getResource(path);
                
                if (imgURL != null) {
                    btn.setIcon(new ImageIcon(imgURL));
                } else {
                    System.err.println("ÍCONE NÃO ENCONTRADO NO CAMINHO: " + caminhoIcone);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar ícone: " + caminhoIcone + " | " + e.getMessage());
            }

            // Força a renderização correta das cores e estilos no Swing
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(false); // Remove a borda padrão se necessário
            btn.repaint();
            btn.revalidate();
        }
    }
}