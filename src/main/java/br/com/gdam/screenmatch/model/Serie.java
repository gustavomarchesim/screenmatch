package br.com.gdam.screenmatch.model;

import java.util.OptionalDouble;

public class Serie {
  private String titulo;

  private Integer totalTemporadas;

  private Double avaliacao;

  private Categoria generos;

  private String atores;

  private String poster;

  private String sinopse;

  public Serie(DadosSerie dadosSerie) {
    this.titulo = dadosSerie.titulo();
    this.totalTemporadas = dadosSerie.totalTemporadas();
    this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
    this.generos = Categoria.fromString(dadosSerie.generos().split(",")[0].trim());
    this.atores = dadosSerie.atores();
    this.poster = dadosSerie.poster();
    this.sinopse = dadosSerie.sinopse();
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Integer getTotalTemporadas() {
    return totalTemporadas;
  }

  public void setTotalTemporadas(Integer totalTemporadas) {
    this.totalTemporadas = totalTemporadas;
  }

  public Double getAvaliacao() {
    return avaliacao;
  }

  public void setAvaliacao(Double avaliacao) {
    this.avaliacao = avaliacao;
  }

  public Categoria getGeneros() {
    return generos;
  }

  public void setGeneros(Categoria generos) {
    this.generos = generos;
  }

  public String getAtores() {
    return atores;
  }

  public void setAtores(String atores) {
    this.atores = atores;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  public String getSinopse() {
    return sinopse;
  }

  public void setSinopse(String sinopse) {
    this.sinopse = sinopse;
  }

  @Override
  public String toString() {
    return "titulo=" + titulo + "\n"
        + "totalTemporadas=" + totalTemporadas + "\n"
        + "avaliacao=" + avaliacao + "\n"
        + "generos=" + generos + "\n"
        + "atores=" + atores + "\n"
        + "poster=" + poster + "\n"
        + "sinopse=" + sinopse + "\n";
  }
}
