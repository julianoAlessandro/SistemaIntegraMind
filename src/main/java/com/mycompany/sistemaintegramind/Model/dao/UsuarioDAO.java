package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Usuarios;
import java.util.List;

/**
 * @author Guilherme
 */
public interface UsuarioDAO {

    public void cadastrarUsuario(Usuarios usuario);
    public List<Usuarios> listarUsuarios();
    
    public Usuarios login(String usuario, String senha); 
    
    public void deletarUsuario(Usuarios usuario);
    public void atualizarUsuario(Usuarios usuario);
}