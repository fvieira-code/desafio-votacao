package br.com.zukk.vivo.projectslicing.service.generator;

import java.io.ByteArrayInputStream;

public class ArquivoGerado {
    private String nomeArquivo;
    private ByteArrayInputStream conteudo;

    public ArquivoGerado(String nomeArquivo, ByteArrayInputStream conteudo) {
        this.nomeArquivo = nomeArquivo;
        this.conteudo = conteudo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public ByteArrayInputStream getConteudo() {
        return conteudo;
    }
}

