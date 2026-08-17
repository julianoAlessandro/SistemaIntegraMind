package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Permissoes;
import java.util.List;

/**
 * @author Guilherme
 */
public interface PermissoesDAO {

    public void cadastrarPermissao(Permissoes permissao);

    public List listarPermissoes();

    public Permissoes buscarPorNome(String nome);

    public Permissoes buscarPorId(Long id);

    public void deletarPermissao(Permissoes permissao);

    public void atualizarPermissao(Permissoes permissao);
}