package br.com.gdam.screenmatch.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.gdam.screenmatch.model.DadosSerie;
import br.com.gdam.screenmatch.model.DadosTemporada;
import br.com.gdam.screenmatch.model.Serie;
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
  private List<DadosSerie> dadosSeries = new ArrayList<>();

  public void exibeMenu() {
    var opcao = -1;
    while (opcao != 0) {

      var menu = """
          1- Buscar séries.
          2- Buscar episódios.
          3- Listar séries buscadas.
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
    dadosSeries.add(dados);
    System.out.println(dados);
  }

  private DadosSerie getDadosSerie() {
    System.out.print("Digite o nome da série para busca: ");
    var nomeSerie = sc.nextLine();
    var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

    DadosSerie dados = converte.obterDados(json, DadosSerie.class);
    return dados;
  }

  private void buscarEpisodioPorSerie() {
    DadosSerie dados = getDadosSerie();
    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i <= dados.totalTemporadas(); i++) {
      var json = consumoApi.obterDados(ENDERECO + dados.titulo().replace(" ", "+") +
          "&season=" + i + API_KEY);
      DadosTemporada dadosTemporada = converte.obterDados(json,
          DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }
    temporadas.forEach(System.out::println);
  }

  private void listaTodasSeries() {
    List<Serie> series = dadosSeries.stream()
        .map(d -> new Serie(d))
        .collect(Collectors.toList());

    series.stream()
        .sorted(Comparator.comparing(Serie::getGeneros))
        .forEach(System.out::println);
  }
}
