package br.com.kaio.literalura.desafio_literalura.repository;

import br.com.kaio.literalura.desafio_literalura.model.Autor;
import br.com.kaio.literalura.desafio_literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
@Repository
public interface Repositorio extends JpaRepository<Autor, Long> {

    Optional<Autor> findFirstByNomeContainingIgnoreCase(String nomeAutor);

    @Query("SELECT l FROM Livro l WHERE l.titulo ILIKE %:nomeLivro%")
    Optional<Livro> obterLivroPeloTutilo(String nomeLivro);

    @Query("SELECT l FROM Livro l WHERE l.autor.nome ILIKE %:autorEscolido%")
    List<Livro> buscarLivrosPorAutor(String autorEscolido);

    @Query("SELECT l FROM Livro l ORDER BY l.qtdDownloads DESC")
    List<Livro> obterTodosOsLivros();

    @Query("SELECT a FROM Autor a WHERE a.dataNascimento >= :anoEscolha")
    List<Autor> atorPorAno(int anoEscolha);

    @Query("SELECT DISTINCT l.idioma FROM Livro l")
    List<String> buscarIdiomasCadastrados();

    @Query("SELECT l FROM Livro l WHERE l.idioma = :idiomaEscolido")
    List<Livro> obterLivrosPorIdioma(String idiomaEscolido);

    @Query("SELECT l FROM Livro l ORDER BY l.qtdDownloads DESC LIMIT 5")
    List<Livro> buscarTop5Livros();

}
