package br.com.gdam.screenmatch.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.gdam.screenmatch.model.DadosSerie;
import br.com.gdam.screenmatch.model.DadosTemporada;
import br.com.gdam.screenmatch.service.ConsumoApi;
import br.com.gdam.screenmatch.service.ConverteDados;

public class Principal {
  Scanner sc = new Scanner(System.in);

  private ConsumoApi consumoApi = new ConsumoApi();
  private ConverteDados converte = new ConverteDados();

  private final String ENDERECO = "https://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=e888d45d";

  public void exibeMenu() {

    var menu = """
        1- Buscar séries.
        2- Buscar episódios.
        0- Sair.
             """;
    System.out.print(menu);

    var opcao = sc.nextInt();
    sc.nextLine();
    switch (opcao) {
      case 1:
        buscarSerieWeb();
        break;
      case 2:
        buscarEpisodioPorSerie();
        break;
      case 0:
        System.out.println("Saindo...");
        return;
      default:
        System.out.println("Opção inválida");
        break;
    }

    // List<DadosTemporada> temporadas = new ArrayList<>();

    // temporadas.forEach(System.out::println);

    // temporadas.forEach(temporada -> temporada.episodios().forEach(episodio ->
    // System.out.println(episodio.titulo())));

    // List<DadosEpisodio> dadosEpisodios = temporadas.stream()
    // .flatMap(temporada -> temporada.episodios().stream())
    // .collect(Collectors.toList());

    // System.out.println("\nTop 5 episódios:");
    // dadosEpisodios.stream()
    // .filter(episodio -> !episodio.avaliacao().equalsIgnoreCase("N/A"))
    // .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
    // .limit(5)
    // .forEach(System.out::println);

    // List<Episodio> episodios = temporadas.stream()
    // .flatMap(temporada -> temporada.episodios().stream()
    // .map(episodio -> new Episodio(temporada.temporada(), episodio)))
    // .collect(Collectors.toList());

    // episodios.forEach(System.out::println);

    // System.out.println("Insira o nome para pesquisa");
    // var nomePesquisa = sc.nextLine();

    // Optional<Episodio> episodioBuscado = episodios.stream()
    // .filter(e ->
    // e.getTitulo().toUpperCase().contains(nomePesquisa.toUpperCase()))
    // .findFirst();

    // if (episodioBuscado.isPresent()) {
    // System.out.println("Episódio encontrado!");
    // System.out.println(episodioBuscado.get().getTemporada());
    // System.out.println(episodioBuscado.get().getNumeroEpisodio());
    // } else {
    // System.out.println("Episódio não encontrado!");
    // }

    // System.out.println("A partir de que ano você deseja ver os episodios?");
    // var ano = sc.nextInt();
    // sc.nextLine();

    // LocalDate dataBusca = LocalDate.of(ano, 1, 1);
    // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // episodios.stream()
    // .filter(E -> E.getDataLancamento() != null &&
    // E.getDataLancamento().isAfter(dataBusca))
    // .forEach(E -> System.out.println(
    // "Temporada: " + E.getTemporada() +
    // " Episódio: " + E.getTitulo() +
    // " Data lançamento: " + E.getDataLancamento().format(dtf)));

    // Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
    // .filter(E -> E.getAvaliacao() > 0.0)
    // .collect(Collectors.groupingBy(Episodio::getTemporada,
    // Collectors.averagingDouble(Episodio::getAvaliacao)));

    // System.out.println(avaliacaoPorTemporada);

    // DoubleSummaryStatistics est = episodios.stream().filter(E -> E.getAvaliacao()
    // > 0.0)
    // .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

    // System.out.println("Media: " + est.getAverage());
    // System.out.println("Minimo: " + est.getMin());
    // System.out.println("Maximo: " + est.getMax());
    // System.out.println("Quantidade: " + est.getCount());

  }

  public void buscarSerieWeb() {
    DadosSerie dados = getDadosSerie();
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

}
