/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.dto.FinanceiroDTO;
import com.mycompany.sistemaintegramind.Model.entidades.FinanceiroFiltro;
import java.util.List;

/**
 *
 * @author Juliano
 */
public interface FinanceiroDAO {
    public List<FinanceiroDTO> listarRelatorioFinanceiroDTO();
    public List<FinanceiroDTO> FiltrarRelatorio(FinanceiroFiltro filtro);
}
