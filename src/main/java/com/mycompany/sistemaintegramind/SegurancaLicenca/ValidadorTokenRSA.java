
package com.mycompany.sistemaintegramind.SegurancaLicenca;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.Base64;

public class ValidadorTokenRSA {

    private static final String ALGORITMO = "RSA";
    private static final String ALGORITMO_ASSINATURA = "SHA256withRSA";

    private final PublicKey chavePublica;

    public ValidadorTokenRSA() {
        this.chavePublica = carregarChavePublica();
    }

    private PublicKey carregarChavePublica() {

        try (InputStream entrada = getClass()
                .getResourceAsStream("/Seguranca/chave_publica.key")) {

            if (entrada == null) {
                throw new IllegalStateException(
                        "Arquivo da chave pública não encontrado."
                );
            }

            byte[] bytes = entrada.readAllBytes();

            if (bytes.length == 0) {
                throw new IllegalStateException("Chave pública vazia.");
            }

            KeyFactory fabrica = KeyFactory.getInstance(ALGORITMO);

            X509EncodedKeySpec especificacao =
                    new X509EncodedKeySpec(bytes);

            return fabrica.generatePublic(especificacao);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível carregar a chave pública RSA.", e
            );
        }
    }

    public boolean validarToken(String token) {

        if (token == null || token.isBlank()) {
            return false;
        }

        try {

            String[] partes = token.split("\\.", -1);

            if (partes.length != 2
                    || partes[0].isEmpty()
                    || partes[1].isEmpty()) {
                return false;
            }

            byte[] dados = Base64.getUrlDecoder()
                    .decode(partes[0]);

            byte[] assinaturaDigital = Base64.getUrlDecoder()
                    .decode(partes[1]);

            Signature assinatura =
                    Signature.getInstance(ALGORITMO_ASSINATURA);

            assinatura.initVerify(chavePublica);
            assinatura.update(dados);

            return assinatura.verify(assinaturaDigital);

        } catch (Exception e) {
            return false;
        }
    }

    public static class DadosLicenca {

        private final Long clinicaId;
        private final Long pagamentoId;
        private final LocalDate dataInicio;
        private final LocalDate dataVencimento;

        public DadosLicenca(
                Long clinicaId,
                Long pagamentoId,
                LocalDate dataInicio,
                LocalDate dataVencimento) {

            this.clinicaId = clinicaId;
            this.pagamentoId = pagamentoId;
            this.dataInicio = dataInicio;
            this.dataVencimento = dataVencimento;
        }

        public Long clinicaId() {
            return clinicaId;
        }

        public Long pagamentoId() {
            return pagamentoId;
        }

        public LocalDate dataInicio() {
            return dataInicio;
        }

        public LocalDate dataVencimento() {
            return dataVencimento;
        }
    }

    public DadosLicenca extrairDadosToken(String token) {

        if (!validarToken(token)) {
            throw new SecurityException(
                    "Token inválido ou adulterado."
            );
        }

        try {

            String[] partes = token.split("\\.", -1);

            byte[] dados = Base64.getUrlDecoder()
                    .decode(partes[0]);

            String conteudo = new String(
                    dados,
                    StandardCharsets.UTF_8
            );

            String[] campos = conteudo.split("\\|", -1);

            if (campos.length != 5 || !"v1".equals(campos[0])) {
                throw new IllegalArgumentException(
                        "Formato de token inválido."
                );
            }

            if (!campos[1].startsWith("clinicaId=")
                    || !campos[2].startsWith("pagamentoId=")
                    || !campos[3].startsWith("inicio=")
                    || !campos[4].startsWith("vencimento=")) {

                throw new IllegalArgumentException(
                        "Dados do token inválidos."
                );
            }

            Long clinicaId = Long.parseLong(
                    campos[1].substring(10)
            );

            Long pagamentoId = Long.parseLong(
                    campos[2].substring(12)
            );

            LocalDate inicio = LocalDate.parse(
                    campos[3].substring(7)
            );

            LocalDate vencimento = LocalDate.parse(
                    campos[4].substring(11)
            );

            if (clinicaId <= 0 || pagamentoId <= 0) {
                throw new IllegalArgumentException(
                        "IDs da licença inválidos."
                );
            }

            if (vencimento.isBefore(inicio)) {
                throw new IllegalArgumentException(
                        "Período da licença inválido."
                );
            }

            return new DadosLicenca(
                    clinicaId,
                    pagamentoId,
                    inicio,
                    vencimento
            );

        } catch (Exception e) {

            throw new SecurityException(
                    "Não foi possível interpretar os dados do token.", e
            );
        }
    }
}
