package br.com.kaio.literalura.desafio_literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titulo;
    private String idioma;
    private Integer qtdDownloads;

    @ManyToOne
    private Autor autor;

    public Livro() {
    }

    public String getTitulo() {
        return titulo;
    }

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.idioma = dadosLivro.idiomas().get(0);
        this.qtdDownloads = dadosLivro.qtdDownloads();
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return String.format("\nLivro :" +
                "\n Titulo: %s | " + "Idioma: %s," +
                "\n Autor(a): %s | " + "Quantidade de Downloads: %s,",
                titulo,idioma.toUpperCase(),autor.getNome(),qtdDownloads);
    }
}
