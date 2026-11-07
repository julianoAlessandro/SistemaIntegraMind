package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.AudioEvolucaoClinica;

public interface AudioEvolucaoClinicaDAO {

    public void cadastrarAudioEvolucaoClinica(AudioEvolucaoClinica audioevolucaoclinica);

    public AudioEvolucaoClinica buscarAudioPorEvolucao(Long idEvolucao);
}
