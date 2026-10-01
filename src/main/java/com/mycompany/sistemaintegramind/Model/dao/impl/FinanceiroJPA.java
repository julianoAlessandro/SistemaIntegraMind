/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.FinanceiroDAO;
import com.mycompany.sistemaintegramind.Model.dto.FinanceiroDTO;
import com.mycompany.sistemaintegramind.Model.entidades.FinanceiroFiltro;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
           EntityManager em = JPAUtil.getEntityManager();

        List<FinanceiroDTO> filtrarRelatorio = new ArrayList<>();

        try {

            StringBuilder jpql = new StringBuilder();

            jpql.append(
                   "SELECT new com.mycompany.sistemaintegramind.Model.dto.FinanceiroDTO("
                    + " a.id, "
                    + " p.nome, "
                    + " a.dataAtualizacao, "
                    + " a.valorDaConsulta) " // <- Fecha o parêntese do construtor DTO aqui
                    + " FROM Agendamento a JOIN a.paciente p " // <- Adicionada a cláusula FROM e o JOIN
                    + " WHERE a.statuspagamento = 'PAGO' " // <- Adicionado WHERE
                    + " AND a.statuspacienteagendamento = 'ATIVO'"
            );

            if (filtro.getPaciente() != null && !filtro.getPaciente().trim().isEmpty()) {

                jpql.append(
                        "AND LOWER(p.nome) LIKE LOWER(:Paciente) "
                );
            }
            if (filtro.getDataInicial() != null) {

                jpql.append("AND a.dataAtualizacao >= :dataInicial ");
            }

            if (filtro.getDataFinal() != null) {

                jpql.append("AND a.dataAtualizacao <= :dataFinal ");
            }

          
            TypedQuery<FinanceiroDTO> query
                    = em.createQuery(
                            jpql.toString(),
                            FinanceiroDTO.class
                    );
           

            if (filtro.getPaciente() != null && !filtro.getPaciente().trim().isEmpty()) {

                query.setParameter(
                        "Paciente",
                        "%" + filtro.getPaciente().trim() + "%"
                );
            }
            if (filtro.getDataInicial() != null) {

                query.setParameter(
                        "dataInicial",
                        filtro.getDataInicial().atStartOfDay()
                );
            }

            if (filtro.getDataFinal() != null) {

                query.setParameter(
                        "dataFinal",
                        filtro.getDataFinal().atTime(LocalTime.MAX)
                );
            }

            filtrarRelatorio = query.getResultList();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            em.close();

        }

        return filtrarRelatorio;
    }

}
