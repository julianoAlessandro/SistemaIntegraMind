package com.mycompany.sistemaintegramind.SegurancaLicenca;

import java.time.LocalDate;

public class LicencaService {

    private final ValidadorTokenRSA validadorTokenRSA;

    public LicencaService() {
        this.validadorTokenRSA = new ValidadorTokenRSA();
    }

    public boolean verificarLicenca(Licenca licenca) {

        if (licenca == null) {
            return false;
        }

        try {

            //2026-11-25 Juliano: Verifica a assinatura RSA
            if (!validadorTokenRSA.validarToken(licenca.getToken())) {
                return false;
            }

            //2026-11-25 Juliano: Extrai os dados assinados
            ValidadorTokenRSA.DadosLicenca dados
                    = validadorTokenRSA.extrairDadosToken(licenca.getToken());

            //2026-11-25 Juliano: Compara os identificadores
            if (!dados.clinicaId().equals(licenca.getClinicaId())) {
                return false;
            }

            if (!dados.pagamentoId().equals(licenca.getPagamentoId())) {
                return false;
            }

            //2026-11-25 Juliano: Compara as datas armazenadas no banco
            if (!dados.dataInicio().equals(licenca.getDataInicio())) {
                return false;
            }

            if (!dados.dataVencimento().equals(licenca.getDataVencimento())) {
                return false;
            }

            //2026-11-25 Juliano: Verifica o período de validade
            LocalDate hoje = LocalDate.now();

            if (hoje.isBefore(dados.dataInicio())) {
                return false;
            }

            if (hoje.isAfter(dados.dataVencimento())) {
                return false;
            }

            //2026-11-25 Juliano: Verifica o status
            return "ATIVA".equals(licenca.getStatus());

        } catch (Exception e) {
            return false;
        }
    }

    public boolean ativarLicenca(String token) {

        try {

            //2026-11-25 Juliano: Validar assinatura e extrair os dados do token
            ValidadorTokenRSA.DadosLicenca dados = validadorTokenRSA.extrairDadosToken(token);

            //2026-11-25 Juliano: Conferir as datas
            LocalDate hoje = LocalDate.now();

            if (hoje.isBefore(dados.dataInicio())
                    || hoje.isAfter(dados.dataVencimento())) {
                return false;
            }

            //2026-11-25 Juliano: Montar a licença
            Licenca licenca = new Licenca();

            licenca.setToken(token);
            licenca.setClinicaId(dados.clinicaId());
            licenca.setPagamentoId(dados.pagamentoId());
            licenca.setDataInicio(dados.dataInicio());
            licenca.setDataVencimento(dados.dataVencimento());
            licenca.setDataAtivacao(hoje);
            licenca.setStatus("ATIVA");

            
            LicencaRepository repository = new LicencaRepository();

            repository.salvarOuAtualizar(licenca);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

}
