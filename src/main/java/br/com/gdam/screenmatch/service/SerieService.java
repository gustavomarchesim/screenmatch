package br.com.gdam.screenmatch.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gdam.screenmatch.dto.EpisodioDTO;
import br.com.gdam.screenmatch.dto.SerieDTO;
import br.com.gdam.screenmatch.model.Categoria;
import br.com.gdam.screenmatch.model.Serie;
import br.com.gdam.screenmatch.repository.SerieRepository;

@Service
public class SerieService {

  @Autowired
  private SerieRepository repository;

  public List<SerieDTO> obterTodasAsSeries() {
    return convSerieDTOs(repository.findAll());

  }

  public List<SerieDTO> obterTop5Series() {
    return convSerieDTOs(repository.findTop5ByOrderByAvaliacaoDesc());
  }

  public List<SerieDTO> obterLancamentos() {
    return convSerieDTOs(repository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
  }

  private List<SerieDTO> convSerieDTOs(List<Serie> series) {
    return series.stream()
        .map(S -> new SerieDTO(S.getId(), S.getTitulo(), S.getTotalTemporadas(), S.getAvaliacao(), S.getGeneros(),
            S.getAtores(), S.getPoster(), S.getSinopse()))
        .collect(Collectors.toList());
  }

  public SerieDTO obterPorId(Long id) {
    Optional<Serie> serie = repository.findById(id);

    if (serie.isPresent()) {
      Serie S = serie.get();
      return new SerieDTO(S.getId(), S.getTitulo(), S.getTotalTemporadas(), S.getAvaliacao(), S.getGeneros(),
          S.getAtores(), S.getPoster(), S.getSinopse());
    }

    return null;
  }

  public List<EpisodioDTO> obterTodasAsSeries(Long id) {
    Optional<Serie> serie = repository.findById(id);

    if (serie.isPresent()) {
      Serie S = serie.get();
      return S.getEpisodios().stream().map(E -> new EpisodioDTO(E.getTemporada(), E.getNumeroEpisodio(), E.getTitulo()))
          .collect(Collectors.toList());
    }

    return null;
  }

  public List<EpisodioDTO> obterTemporadaPorId(Long id, Long temporada) {
    return repository.obterEpisodioPorTemporada(id, temporada).stream()
        .map(E -> new EpisodioDTO(E.getTemporada(), E.getNumeroEpisodio(), E.getTitulo()))
        .collect(Collectors.toList());
  }

  public List<SerieDTO> obterSeriesPorGeneros(String generos) {
    Categoria categoria = Categoria.fromBrasileiro(generos);
    return convSerieDTOs(repository.findByGeneros(categoria));
  }

  public List<EpisodioDTO> obterTop5EpisodiosPorSerie(Long id) {
    return repository.obterTop5EpisodiosPorId(id).stream()
        .map(E -> new EpisodioDTO(E.getTemporada(), E.getNumeroEpisodio(), E.getTitulo()))
        .collect(Collectors.toList());
  }
}
