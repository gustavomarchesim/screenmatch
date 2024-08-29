package br.com.gdam.screenmatch.model;

public enum Categoria {
  ACAO("Action", "Ação"),
  ROMANCE("Romance", "Romance"),
  DRAMA("Drama", "Drama"),
  CRIME("Crime", "Crime"),
  COMEDIA("Comedy", "Comédia"),
  FANTASIA("Fantasy", "Fantasia"),
  AVENTURA("Adventure", "Aventura");

  private String categoriaOmdb;
  private String categoriaPortugues;

  Categoria(String categoriaOmdb, String categoriaPortugues) {
    this.categoriaOmdb = categoriaOmdb;
    this.categoriaPortugues = categoriaPortugues;
  }

  public static Categoria fromString(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    throw new IllegalArgumentException("Nenhuma categoria correspondente enconrtada!");
  }

  public static Categoria fromBrasileiro(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    throw new IllegalArgumentException("Nenhuma categoria correspondente enconrtada!");
  }
}
