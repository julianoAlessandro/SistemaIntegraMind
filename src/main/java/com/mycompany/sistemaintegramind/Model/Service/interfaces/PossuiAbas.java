/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.Service.interfaces;

import com.mycompany.sistemaintegramind.View.MainMenu;
import javax.swing.JTabbedPane;

/**
 *
 * @author Micro
 */
public interface PossuiAbas {

    JTabbedPane getVenda();

    MainMenu getMainMenu();

    void bloquearVenda();

}
