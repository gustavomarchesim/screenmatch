package br.com.gdam.screenmatch.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.gdam.screenmatch.model.Categoria;
import br.com.gdam.screenmatch.model.DadosSerie;
import br.com.gdam.screenmatch.model.DadosTemporada;
import br.com.gdam.screenmatch.model.Episodio;
import br.com.gdam.screenmatch.model.Serie;
import br.com.gdam.screenmatch.repository.SerieRepository;
import br.com.gdam.screenmatch.service.ConsumoApi;
import br.com.gdam.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

public class Principal {

    // Carrega as variáveis de ambiente do arquivo .env
    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("OMDB_API_KEY");

    // Cria um scanner para leitura de entrada do usuário
    Scanner sc = new Scanner(System.in);

    // Instancia os serviços para consumo de API e conversão de dados
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();

    // Constantes para a URL da API
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + apiKey;

    private SerieRepository repository;

    // Lista para armazenar séries localmente
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada;

    // Construtor que recebe o repositório de séries
    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    // Método para exibir o menu principal
    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            // Exibe o menu de opções para o usuário
            var menu = """
                    1- Buscar séries.
                    2- Buscar episódios.
                    3- Listar séries buscadas.
                    4- Buscar série por título.
                    5- Buscar séries por ator.
                    6- Buscar Top 5 séries.
                    7- Busca por gênero.
                    8- Busca por máximo de temporadas e avaliação minima.
                    9- Busca episódio por trecho.
                    10- Busca Top 5 episódios por Série.
                    11- Busca série por data.
                    0- Sair.
                    """;
            System.out.print(menu);

            // Lê a opção escolhida pelo usuário
            opcao = sc.nextInt();
            sc.nextLine();

            // Executa a ação correspondente à opção escolhida
            switch (opcao) {
                case 1:
                    buscarSerieWeb(); // Busca uma série na web
                    break;
                case 2:
                    buscarEpisodioPorSerie(); // Busca episódios de uma série
                    break;
                case 3:
                    listaTodasSeries(); // Lista todas as séries armazenadas
                    break;
                case 4:
                    buscarSeriePorTitulo(); // Busca uma série por título
                    break;
                case 5:
                    buscarSeriePorAtor(); // Busca séries por ator
                    break;
                case 6:
                    buscaTopSeries(); // Exibe as top 5 séries
                    break;
                case 7:
                    buscaPorGeneros(); // Busca séries por gênero
                    break;
                case 8:
                    buscaPorMinimoDeTemporadasEAvaliacaoMinima();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    buscaTopEpisodiosProSerie();
                    break;
                case 11:
                    buscaSeriePorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }
    }

    private void buscaSeriePorData() {
        System.out.println("Insira o ano da busca");
        var anoBusca = sc.nextInt();
        List<Episodio> buscaEpisodiosPorData = repository.buscaEpisodioPorData(anoBusca);
        buscaEpisodiosPorData.forEach(E -> System.out
                .println("Titulo: " + E.getSerie().getTitulo() + " - " + E.getTitulo() + ": " + E.getDataLancamento()));
    }

    private void buscaTopEpisodiosProSerie() {
        System.out.println("Insira a série para busca");
        var serie = sc.nextLine();
        List<Episodio> top5Episodios = repository.topEpisodiosPorSerie(serie);
        top5Episodios.forEach(E -> System.out
                .println("Série: " + E.getSerie().getTitulo() + " - " + E.getTitulo() + " - " + E.getAvaliacao()));

    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Insira o trecho do titulo de um episódio");
        var trecho = sc.nextLine();
        List<Episodio> episodiosPorTrecho = repository.episodiosPorTrecho(trecho);
        episodiosPorTrecho.forEach(
                E -> System.out.println(
                        E.getSerie().getTitulo() + " - " + E.getTitulo() + ", avaliação: " + E.getAvaliacao()));
    }

    // Método para buscar uma série na web e salvar no repositório
    public void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie(); // Obtém os dados da série a partir da API
        Serie serie = new Serie(dados); // Converte os dados em uma instância de Serie
        repository.save(serie); // Salva a série no repositório
    }

    // Método para obter os dados da série via API
    private DadosSerie getDadosSerie() {
        System.out.print("Digite o nome da série para busca: ");
        var nomeSerie = sc.nextLine();

        // Monta a URL da API com o nome da série
        var json = consumoApi.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        // Converte o JSON recebido em uma instância de DadosSerie
        DadosSerie dados = converte.obterDados(json, DadosSerie.class);
        return dados;
    }

    // Método para buscar episódios de uma série específica
    private void buscarEpisodioPorSerie() {
        listaTodasSeries(); // Lista todas as séries para o usuário escolher
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = sc.nextLine();

        // Busca a série pelo nome (ignorando maiúsculas/minúsculas)
        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            // Para cada temporada, busca os dados via API
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(
                        ENDERECO +
                                serieEncontrada
                                        .getTitulo()
                                        .replace(" ", "+")
                                        .toLowerCase()
                                +
                                "&season=" +
                                i +
                                API_KEY);

                // Converte o JSON em DadosTemporada e adiciona à lista
                DadosTemporada dadosTemporada = converte.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            // Exibe as temporadas no console
            temporadas.forEach(System.out::println);

            // Converte os dados de cada temporada em uma lista de episódios
            List<Episodio> episodios = temporadas
                    .stream()
                    .flatMap(d -> d.episodios().stream().map(e -> new Episodio(d.numero(), e))) // "Achata" a lista de
                                                                                                // listas de episódios10
                                                                                                // em uma única stream
                    .collect(Collectors.toList());

            // Define os episódios na série e salva no repositório
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    // Método para listar todas as séries armazenadas
    private void listaTodasSeries() {
        series = repository.findAll(); // Obtém todas as séries do repositório
        series
                .stream()
                .sorted(Comparator.comparing(Serie::getGeneros)) // Ordena as séries pelos gêneros
                .forEach(System.out::println); // Exibe as séries no console
    }

    // Método para buscar uma série por título
    public void buscarSeriePorTitulo() {
        System.out.print("Escolha uma série por nome: ");
        var nomeSerie = sc.nextLine();
        serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get()); // Exibe os dados da série encontrada
        } else {
            System.out.println("Série não encontrada!"); // Informa que a série não foi encontrada
        }
    }

    // Método para buscar séries por ator
    private void buscarSeriePorAtor() {
        System.out.print("Qual o nome para busca? ");
        var nomeAtor = sc.nextLine();
        System.out.println("Nota desejada: ");
        var avaliacao = sc.nextDouble();
        System.out.println("Quantidade maxima de temporadas");
        var temporada = sc.nextDouble();

        // Busca séries que contenham o ator e tenham a avaliação mínima informada
        List<Serie> seriesEncontradas = repository
                .findByAtoresContainingIgnoreCaseAndTotalTemporadasLessThanEqualAndAvaliacaoIsGreaterThanEqual(nomeAtor,
                        temporada, avaliacao);

        // Exibe as séries encontradas no console
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(S -> System.out.println(S.getTitulo() + ", avaliação: " + S.getAvaliacao()));
    }

    // Método para buscar as top 5 séries por avaliação
    private void buscaTopSeries() {
        List<Serie> melhoresSeries = repository.findTop5ByOrderByAvaliacaoDesc(); // Obtém as 5 séries com melhor
                                                                                  // avaliação
        melhoresSeries
                .forEach(S -> System.out.println("Titulo: " + S.getTitulo() + ", Avaliação: " + S.getAvaliacao()));
    }

    // Método para buscar séries por gênero
    private void buscaPorGeneros() {
        System.out.println("Qual categória deseja buscar? ");
        var nomeGenero = sc.nextLine();

        Categoria categoria = Categoria.fromBrasileiro(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGeneros(categoria);

        System.out.println("Séries da categoria " + nomeGenero);
        seriesPorCategoria.forEach(S -> System.out.println("Titulo: " + S.getTitulo() + ", Genero: " + S.getGeneros()));
    }

    private void buscaPorMinimoDeTemporadasEAvaliacaoMinima() {
        System.out.println("Informe o máximo de temporadas: ");
        var temporadas = sc.nextInt();
        System.out.println("Informe a avaliação minima: ");
        var avaliacao = sc.nextDouble();
        List<Serie> seriesPorTemporada = repository.seriesPorTemporadaEAvaliacao(temporadas, avaliacao);

        System.out.println("Séries encontadas: ");
        seriesPorTemporada.forEach(
                S -> System.out.println("Titulo: " + S.getTitulo() + ", Temporadas: " + S.getTotalTemporadas()
                        + ", Avaliação: " + S.getAvaliacao()));
    }
}
