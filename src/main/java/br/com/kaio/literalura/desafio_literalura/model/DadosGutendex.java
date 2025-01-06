package br.com.kaio.literalura.desafio_literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosGutendex(@JsonAlias("count") Integer qtdLivros,
                            @JsonAlias("next") String proximoEndereco,
                            @JsonAlias("results") List<DadosLivro> livros) {
}
