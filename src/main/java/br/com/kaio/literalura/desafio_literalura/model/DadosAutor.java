package br.com.kaio.literalura.desafio_literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(
        @JsonAlias("name") String nome,
        @JsonAlias("birth_year") Integer anoNascimento,
        @JsonAlias("death_year") Integer anoFalecimento
) {

    @Override
    public String toString() {
        String nascimento = (anoNascimento != null) ? String.valueOf(anoNascimento) : "Nascimento não informado";
        String falecimento = (anoFalecimento != null) ? String.valueOf(anoFalecimento) : "Falecimento não informado";

        return String.format(" %s | %s - %s ", nome, nascimento, falecimento);
    }
}
