/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.util.Utilitarios;

/**
 *
 * @author guilh
 */
public class ValidarCpfCnpj {

    public static boolean isCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", ""); // Remove máscara
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int d1 = 0, d2 = 0;
            int digit1, digit2, resto;
            int num;

            for (int i = 1; i <= 9; i++) {
                num = (int) (cpf.charAt(i - 1) - 48);
                d1 = d1 + (11 - i) * num;
                d2 = d2 + (12 - i) * num;
            }

            resto = (d1 % 11);
            digit1 = (resto < 2) ? 0 : 11 - resto;
            d2 = d2 + 2 * digit1;
            resto = (d2 % 11);
            digit2 = (resto < 2) ? 0 : 11 - resto;

            return (digit1 == (int) (cpf.charAt(9) - 48)) && (digit2 == (int) (cpf.charAt(10) - 48));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int soma = 0, peso = 2;
            for (int i = 11; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                soma += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            int digito1 = ((soma % 11) < 2) ? 0 : 11 - (soma % 11);

            soma = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                soma += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            int digito2 = ((soma % 11) < 2) ? 0 : 11 - (soma % 11);

            return (digito1 == (int) (cnpj.charAt(12) - 48)) && (digito2 == (int) (cnpj.charAt(13) - 48));
        } catch (Exception e) {
            return false;
        }
    }
    private static int num; // Auxiliar
}
