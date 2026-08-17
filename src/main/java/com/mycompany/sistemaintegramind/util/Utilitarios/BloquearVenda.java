/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.awt.Component;
import java.awt.Container;

/**
 *
 * @author Micro
 */
public class BloquearVenda {

    public static void  setEnabledRecursivo(Container container, boolean enabled) {

        for (Component component : container.getComponents()) {

            component.setEnabled(enabled);

            if (component instanceof Container) {
                setEnabledRecursivo((Container) component, enabled);
            }
        }
    }
}
