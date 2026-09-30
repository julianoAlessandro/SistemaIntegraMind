/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.FinanceiroDAO;
import com.mycompany.sistemaintegramind.Model.dto.FinanceiroDTO;
import com.mycompany.sistemaintegramind.Model.entidades.FinanceiroFiltro;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Juliano
 */
public class FinanceiroJPA implements FinanceiroDAO {

    @Override
    public List<FinanceiroDTO> listarRelatorioFinanceiroDTO() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT new com.mycompany.sistemaintegramind.Model.dto.FinanceiroDTO("
                    + " a.id, "
                    + " p.nome, "
                    + " a.dataAtualizacao, "
                    + " a.valorDaConsulta) " // <- Fecha o parêntese do construtor DTO aqui
                    + " FROM Agendamento a JOIN a.paciente p " // <- Adicionada a cláusula FROM e o JOIN
                    + " WHERE a.statuspagamento = 'PAGO' " // <- Adicionado WHERE
                    + " AND a.statuspacienteagendamento = 'ATIVO'";

            return em.createQuery(jpql, FinanceiroDTO.class)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<FinanceiroDTO> FiltrarRelatorio(FinanceiroFiltro filtro) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
