package br.com.gdam.screenmatch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gdam.screenmatch.dto.EpisodioDTO;
import br.com.gdam.screenmatch.dto.SerieDTO;
import br.com.gdam.screenmatch.service.SerieService;

@RestController
@RequestMapping("/series")
public class SerieController {

  @Autowired
  private SerieService service;

  @GetMapping
  public List<SerieDTO> obterSeries() {
    return service.obterTodasAsSeries();
  }

  @GetMapping("/top5")
  public List<SerieDTO> obterTop5Series() {
    return service.obterTop5Series();
  }

  @GetMapping("/lancamentos")
  public List<SerieDTO> obterLancamentos() {
    return service.obterLancamentos();
  }

  @GetMapping("/{id}")
  public SerieDTO obterPorId(@PathVariable Long id) {
    return service.obterPorId(id);
  }

  @GetMapping("/{id}/temporadas/todas")
  public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable Long id) {
    return service.obterTodasAsSeries(id);
  }

  @GetMapping("/{id}/temporadas/{temporada}")
  public List<EpisodioDTO> obterTemporadaPorId(@PathVariable Long id, @PathVariable Long temporada) {
    return service.obterTemporadaPorId(id, temporada);
  }

  @GetMapping("/categoria/{generos}")
  public List<SerieDTO> obterSeriesPorGenero(@PathVariable String generos) {
    return service.obterSeriesPorGeneros(generos);
  }

  @GetMapping("/{id}/temporadas/top")
  public List<EpisodioDTO> obterTop5EpisodiosPorSerie(@PathVariable Long id) {
    return service.obterTop5EpisodiosPorSerie(id);
  }
}