
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

            // 1. Verifica a assinatura RSA
            if (!validadorTokenRSA.validarToken(licenca.getToken())) {
                return false;
            }

            // 2. Extrai os dados assinados
            ValidadorTokenRSA.DadosLicenca dados =
                    validadorTokenRSA.extrairDadosToken(licenca.getToken());

            // 3. Compara os identificadores
            if (!dados.clinicaId().equals(licenca.getClinicaId())) {
                return false;
            }

            if (!dados.pagamentoId().equals(licenca.getPagamentoId())) {
                return false;
            }

            // 4. Compara as datas armazenadas no banco
            if (!dados.dataInicio().equals(licenca.getDataInicio())) {
                return false;
            }

            if (!dados.dataVencimento().equals(licenca.getDataVencimento())) {
                return false;
            }

            // 5. Verifica o período de validade
            LocalDate hoje = LocalDate.now();

            if (hoje.isBefore(dados.dataInicio())) {
                return false;
            }

            if (hoje.isAfter(dados.dataVencimento())) {
                return false;
            }

            // 6. Verifica o status
            return "ATIVA".equals(licenca.getStatus());

        } catch (Exception e) {
            return false;
        }
    }
}
