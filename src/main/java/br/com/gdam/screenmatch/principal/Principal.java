package br.com.gdam.screenmatch.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.gdam.screenmatch.model.DadosSerie;
import br.com.gdam.screenmatch.model.DadosTemporada;
import br.com.gdam.screenmatch.model.Episodio;
import br.com.gdam.screenmatch.model.Serie;
import br.com.gdam.screenmatch.repository.SerieRepository;
import br.com.gdam.screenmatch.service.ConsumoApi;
import br.com.gdam.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

public class Principal {

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("OMDB_API_KEY");

    Scanner sc = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + apiKey;

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1- Buscar séries.
                    2- Buscar episódios.
                    3- Listar séries buscadas.
                    4- Buscar série por titulo.
                    5- Buscar séries por ator.
                    0- Sair.
                         """;
            System.out.print(menu);

            opcao = sc.nextInt();
            sc.nextLine();
            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listaTodasSeries();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
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

    public void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.print("Digite o nome da série para busca: ");
        var nomeSerie = sc.nextLine();
        var json = consumoApi.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = converte.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listaTodasSeries();
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = sc.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(
                nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

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

                DadosTemporada dadosTemporada = converte.obterDados(
                        json,
                        DadosTemporada.class);

                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas
                    .stream()
                    .flatMap(d -> d.episodios().stream().map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listaTodasSeries() {
        series = repository.findAll();
        series
                .stream()
                .sorted(Comparator.comparing(Serie::getGeneros))
                .forEach(System.out::println);
    }

    public void buscarSeriePorTitulo() {
        System.out.print("Escolha uma série por nome: ");
        var nomeSerie = sc.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.print("Qual o nome para busca? ");
        var nomeAtor = sc.nextLine();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCase(nomeAtor);
        System.out.println(seriesEncontradas);
    }
}
