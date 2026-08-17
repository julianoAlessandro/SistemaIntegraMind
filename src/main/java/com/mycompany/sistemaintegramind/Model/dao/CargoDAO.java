package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Cargo;
import com.mycompany.sistemaintegramind.Model.entidades.Permissoes;
import java.util.List;

/**
 * @author Guilherme
 */
public interface CargoDAO {

    public void cadastrarCargo(Cargo cargo);
    public List<Cargo> listarCargos();
    public void deletarCargo(Cargo cargo);
    public void atualizarCargo(Cargo cargo);
    public Cargo buscarPorNome(String nome);
    
    //2026-07-18 Guilherme: Retorna a lista de permissões associadas a um cargo específico
    public List<Permissoes> listarPermissoesPorCargo(Cargo cargo);
}