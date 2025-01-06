package br.com.kaio.literalura.desafio_literalura.service;

import br.com.kaio.literalura.desafio_literalura.model.DadosGutendex;
import br.com.kaio.literalura.desafio_literalura.model.DadosLivro;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsumoApi {
    private final String ENDERECO = "https://gutendex.com/books?search=";
    private ConverteDados conversor;

    public ConsumoApi(ConverteDados conversor) {
        this.conversor = conversor;
    }

    public List<DadosLivro> obterDados(String nomePesquisa) {
        String endereco = ENDERECO + URLEncoder.encode(nomePesquisa, StandardCharsets.UTF_8);
        List<DadosLivro> todosOsLivros = new ArrayList<>();

        do {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .header("User-Agent", "Java HttpClient")
                    .header("Accept", "application/json")
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    DadosGutendex gutendex = conversor.obterDados(response.body(), DadosGutendex.class);
                    todosOsLivros.addAll(gutendex.livros());
                    todosOsLivros = todosOsLivros.stream()
                            .sorted(Comparator.comparingInt(DadosLivro::qtdDownloads).reversed())
                            .collect(Collectors.toList());
                    endereco = gutendex.proximoEndereco();
                } else {
                    throw new RuntimeException("Erro na resposta da API: " + response.statusCode() +
                            "\nResposta do servidor: " + response.body());
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Erro ao consumir a API", e);
            }
        } while (endereco != null);

        return todosOsLivros;
    }
}
