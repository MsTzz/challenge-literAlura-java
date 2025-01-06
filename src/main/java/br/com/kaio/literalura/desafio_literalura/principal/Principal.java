package br.com.kaio.literalura.desafio_literalura.principal;

import br.com.kaio.literalura.desafio_literalura.model.Autor;
import br.com.kaio.literalura.desafio_literalura.model.DadosLivro;
import br.com.kaio.literalura.desafio_literalura.model.Livro;
import br.com.kaio.literalura.desafio_literalura.repository.Repositorio;
import br.com.kaio.literalura.desafio_literalura.service.ConsumoApi;
import br.com.kaio.literalura.desafio_literalura.service.ConverteDados;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumoApi = new ConsumoApi(conversor);
    private Repositorio repositorio;

    public Principal(Repositorio repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0){

            do {
                System.out.println("\n\n=== MENU PRINCIPAL ===\n" +
                        "Escolha o número de sua opção:\n\n" +
                        "1 - Buscar livro pelo título ou autor\n" +
                        "2 - Listar livros registrados\n" +
                        "3 - Listar autores registrados\n" +
                        "4 - Listar autores vivos em um determinado ano\n" +
                        "5 - Listar livros em um determinado idioma\n" +
                        "6 - Listar top5 livros mais baixados\n" +
                        "7 - listar livros registrados de um determinado autor\n" +
                        "0 - Sair\n\n" +
                        "Digite sua opção:");

                try {
                    opcao = leitura.nextInt();
                    leitura.nextLine();

                    if (opcao < 0 || opcao > 7) {
                        System.out.println("Opção inválida! Tente novamente.");
                        continue;
                    }

                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, insira um número.");
                    leitura.nextLine();
                }
            } while (true);

            switch (opcao) {
                case 1:
                    buscarLivroPeloTituloOuAutor();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistados();
                    break;
                case 4:
                    buscarAutorPorAno();
                    break;
                case 5:
                    buscarLivrosPorIdioma();
                    break;
                case 6:
                    buscarTop5Livros();
                    break;
                case 7:
                    listarLivrosPorAutor();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
            }
        }
    }

    private void buscarLivroPeloTituloOuAutor() {
        DadosLivro dadosLivro = getDadosLivros();
        if (dadosLivro == null) {
            System.out.println("\nNenhum livro ou autor encontrado!");
        } else {
            salvarAutor(dadosLivro);
        }
    }

    private void salvarLivro(Autor autor, DadosLivro dadosLivro) {
        Optional<Livro> livroExistente = repositorio.obterLivroPeloTutilo(dadosLivro.titulo());
        if (livroExistente.isEmpty()){
            Livro livro = new Livro(dadosLivro);
            autor.addLivros(livro);
            System.out.println("\nSalvando o Livro: " + livro.getTitulo() +
                    "\nDo autor: " + autor.getNome());
            repositorio.save(autor);
        } else {
            System.out.println("\nO Livro: "+ dadosLivro + " já está cadastrado!");
        }

    }

    private void salvarAutor(DadosLivro dadosLivro) {
        var dadosAutor = dadosLivro.dadosAutor().get(0);
        Optional<Autor> autorExistente = repositorio.findFirstByNomeContainingIgnoreCase(dadosAutor.nome());
        Autor autor = autorExistente
                .orElseGet(() -> {
                    Autor novoAutor = new Autor(dadosAutor);
                    System.out.println("\nSalvando novo Autor!\n");
                    repositorio.save(novoAutor);
                    return novoAutor;
                });
        salvarLivro(autor, dadosLivro);
    }


    private DadosLivro getDadosLivros() {
        System.out.println("Qual o nome do livro ou Autor que gostaria de buscar?");
        var nomeLivro = leitura.nextLine().toLowerCase();
        List<DadosLivro> listaDeLivros = consumoApi.obterDados(nomeLivro);

        if (listaDeLivros.isEmpty()) {
            return null;
        }

        return switch (listaDeLivros.size()) {
            case 1 -> {
                System.out.println("\nLivro encontrado: " + listaDeLivros.get(0));
                yield listaDeLivros.get(0);
            }
            default -> {
                for (DadosLivro livro : listaDeLivros) {
                    System.out.println("ID: " + (listaDeLivros.indexOf(livro) + 1) + livro);
                }
                System.out.println("\nAtenção: a resposta da API pode conter dados repetidos. Verifique a quantidade de downloads para identificar o livro desejado.");
                int idEscolhido;
                do {
                    System.out.println("\nDigite o ID do livro desejado: ");
                    try {
                        idEscolhido = leitura.nextInt() - 1;
                        leitura.nextLine();

                        if (idEscolhido >= 0 && idEscolhido < listaDeLivros.size()) {
                            System.out.println("\nLivro Selecionado: \n" + listaDeLivros.get(idEscolhido));
                            yield listaDeLivros.get(idEscolhido);
                        } else {
                            System.out.println("ID inválido. Por favor, escolha um número entre 1 e " + listaDeLivros.size() + ".");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Por favor, insira um número.");
                        leitura.nextLine();  //Se não repetir aqui fica em loop no catch!
                    }
                } while (true);

            }
        };
    }

    private boolean validaRegistro(List<?> l){
        if (l.isEmpty()){
            System.out.println("\nNenhum Registro!\n");
            return false;
        } else {return true;}
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = repositorio.obterTodosOsLivros();
        if (!validaRegistro(livros)) return;
        livros.forEach(System.out::println);
    }

    private void listarAutoresRegistados() {
        List<Autor> autores = repositorio.findAll();
        if (!validaRegistro(autores)) return;
        autores.forEach(System.out::println);
    }

    private void buscarAutorPorAno() {
        do {
            System.out.println("\nA partir de Qual ano gostaria de ver?");
            try {
                var anoEscolha = leitura.nextInt();
                leitura.nextLine();

                List<Autor> autores = repositorio.atorPorAno(anoEscolha);
                if (!validaRegistro(autores)) break;
                autores.forEach(System.out::println);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                leitura.nextLine();
            }
        }while (true);
    }

    private void buscarLivrosPorIdioma() {
        List<String> idiomasCadastrados = repositorio.buscarIdiomasCadastrados();

        do {
            System.out.println("\nIdiomas Já Cadastrados:\n");
            idiomasCadastrados.forEach(i -> System.out.println("Idioma: " + i.toUpperCase()));
            System.out.println("\nEscolha um idioma:");
            var idiomaEscolido = leitura.nextLine().toLowerCase();

            if (idiomasCadastrados.contains(idiomaEscolido)){
                List<Livro> livros = repositorio.obterLivrosPorIdioma(idiomaEscolido);
                livros.forEach(System.out::println);
                break;
            } else {
                System.out.println("Opção invalida.");
            }
        } while (true);

    }

    private void buscarTop5Livros(){
        List<Livro> top5Livros = repositorio.buscarTop5Livros();
        if (!validaRegistro(top5Livros)) return;
        top5Livros.forEach(System.out::println);
    }

    private void listarLivrosPorAutor(){
        System.out.println("\nAutores Já Cadastrados:\n");
        listarAutoresRegistados();
        do {
            System.out.println("\nEscolha um Autor:\n");
            var autorEscolido = leitura.nextLine().toLowerCase();
            List<Livro> livrosDoAutor = repositorio.buscarLivrosPorAutor(autorEscolido);
            if (!validaRegistro(livrosDoAutor)) return;
            livrosDoAutor.forEach(System.out::println);
            break;
        } while (true);
    }

}
