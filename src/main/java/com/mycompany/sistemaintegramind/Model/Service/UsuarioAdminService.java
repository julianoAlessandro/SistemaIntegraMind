/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.Service;
import com.mycompany.sistemaintegramind.Model.dao.impl.CargoJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.PermissoesJPA;
import com.mycompany.sistemaintegramind.Model.dao.impl.UsuarioJPA;
import com.mycompany.sistemaintegramind.Model.entidades.Permissoes;

/**
 *
 * @author guilh
 */
public class UsuarioAdminService {

    private UsuarioJPA salvarusuario = new UsuarioJPA();
    private CargoJPA salvarcargo =  new CargoJPA();
    private PermissoesJPA salvarpermissoes = new PermissoesJPA(); 
    public void CadastrarUsuarioAdmin() {
        if(salvarusuario.listarUsuarios().isEmpty()){
           System.out.println("Cadastrando Usuario...");
           Permissoes ACESSAR_VENDAS = new Permissoes("ACESSAR_VENDAS");
           
           

            
        }

    }
}
