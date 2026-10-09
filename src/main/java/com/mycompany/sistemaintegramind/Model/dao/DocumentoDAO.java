/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Documento;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.util.List;

public interface DocumentoDAO {

    public void CadastrarDocumento(Documento documento);

     public List<Documento> listarDocumentosPorPaciente(Paciente paciente);
     public void atualizarPaciente(Documento documento);
     public Documento buscarPorId(Long id);

}
