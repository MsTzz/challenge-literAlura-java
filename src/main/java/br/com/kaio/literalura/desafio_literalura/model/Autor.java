package br.com.kaio.literalura.desafio_literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String nome;
    private Integer dataNascimento;
    private Integer dataFalecimento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();


    public Autor() {}

    public String getNome() {
        return nome;
    }

    public Autor(DadosAutor dadosAutor) {
        this.nome = dadosAutor.nome();
        this.dataNascimento = dadosAutor.anoNascimento();
        this.dataFalecimento = dadosAutor.anoFalecimento();
        validaNull();
    }

    private void validaNull(){
        this.nome = (nome != null) ? nome : "Nome não informado";
    }

    public void addLivros(Livro livro) {
        livro.setAutor(this);
        this.livros.add(livro);
    }


    @Override
    public String toString() {
        String nascimento = (dataNascimento != null) ? String.valueOf(dataNascimento) : "Nascimento não informado";
        String falecimento = (dataFalecimento != null) ? String.valueOf(dataFalecimento) : "Falecimento não informado";

        return String.format("\nAutor(a): '%s' | %s - %s ", nome, nascimento, falecimento);
    }

}
