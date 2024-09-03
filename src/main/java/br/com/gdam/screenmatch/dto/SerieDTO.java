package br.com.gdam.screenmatch.dto;

import br.com.gdam.screenmatch.model.Categoria;

public record SerieDTO(
    Long id, String titulo,
    Integer totalTemporadas,
    Double avaliacao,
    Categoria generos,
    String atores,
    String poster,
    String sinopse) {
}
