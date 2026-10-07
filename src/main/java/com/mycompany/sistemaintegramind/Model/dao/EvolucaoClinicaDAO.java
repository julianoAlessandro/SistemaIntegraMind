/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.util.List;

/**
 *
 * @author Micro
 */
public interface EvolucaoClinicaDAO {
   public void cadastrarEvolucaoClinica(EvolucaoClinica evolucaoClinica);
   public List<EvolucaoClinica> listarEvolucoesClinicas();
}
