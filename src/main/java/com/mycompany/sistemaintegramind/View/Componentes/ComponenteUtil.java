package com.mycompany.sistemaintegramind.View.Componentes;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import java.awt.Color;

public class ComponenteUtil {

    /**
     * Faz o componenteAlvo ter sempre a mesma largura do componenteReferencia e
     * imprime a alteração no terminal.
     *
    

    /**
     * Remove as bordas padrão do JScrollPane e torna o fundo transparente.
     * Ideal para containers com bordas arredondadas customizadas.
     *
     *
     * @param scroll
     */
    public static void formatarScrollInvisivel(JScrollPane scroll) {
        if (scroll == null) {
            return;
        }

        // 1. Remove a borda externa (aquela linha cinza/azul chata)
        scroll.setBorder(null);

        // 2. Remove a borda interna do viewport
        scroll.setViewportBorder(null);

        // 3. Configura transparência (Crucial para Java 11+)
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        // Força o fundo do viewport para transparente usando canal Alpha (0)
        scroll.getViewport().setBackground(new Color(0, 0, 0, 0));

        // 4. Estética: Oculta a barra horizontal se não for estritamente necessária
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

}
