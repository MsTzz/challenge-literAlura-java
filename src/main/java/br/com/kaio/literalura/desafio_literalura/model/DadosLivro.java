package br.com.kaio.literalura.desafio_literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("title") String titulo,
                         @JsonAlias("authors") List<DadosAutor> dadosAutor,
                         @JsonAlias("languages") List<String> idiomas,
                         @JsonAlias("download_count") Integer qtdDownloads){

    @Override
    public String toString() {
        String autoresFormatados = (dadosAutor != null && !dadosAutor.isEmpty()) ? dadosAutor.toString() : "Nenhum autor disponível";
        String idiomasFormatados = (idiomas != null && !idiomas.isEmpty()) ? String.join(", ", idiomas) : "Nenhum idioma disponível";

        return String.format(
                "\nTítulo: %s | Autor(es): %s \n" +
                "Idioma(s): %s | Quantidade de Downloads: %d\n",
                titulo,
                autoresFormatados,
                idiomasFormatados.toUpperCase(),
                qtdDownloads
        );


    }
}
