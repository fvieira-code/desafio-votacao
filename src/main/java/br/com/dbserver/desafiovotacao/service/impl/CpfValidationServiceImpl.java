package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.NotFoundException;
import br.com.dbserver.desafiovotacao.service.CpfValidationService;
import org.springframework.stereotype.Service;

@Service
public class CpfValidationServiceImpl implements CpfValidationService {

    @Override
    public void validateAbleToVote(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new NotFoundException("CPF é obrigado.");
        }

        if (!isValidCpf(cpf)) {
            throw new NotFoundException("CPF inválido.");
        }

    }

    private boolean isValidCpf(String cpf) {
        String c = cpf.replaceAll("\\D", "");

        if (c.length() != 11) return false;
        if (c.chars().distinct().count() == 1) return false;

        int d1 = calcDigit(c, 10);
        int d2 = calcDigit(c, 11);

        return c.charAt(9) - '0' == d1 && c.charAt(10) - '0' == d2;
    }

    private int calcDigit(String c, int weightStart) {
        int sum = 0;
        for (int i = 0; i < weightStart - 1; i++) {
            sum += (c.charAt(i) - '0') * (weightStart - i);
        }
        int mod = (sum * 10) % 11;
        return (mod == 10) ? 0 : mod;
    }

}
