package br.com.gdam.screenmatch.model;

public enum Categoria {
  ACAO("Action"),
  ROMANCE("Romance"),
  DRAMA("Drama"),
  CRIME("Crime"),
  COMEDIA("Comedy"),
  FANTASIA("Fantasy"),
  AVENTURA("Adventure");

  private String categoriaOmdb;

  Categoria(String categoriaOmdb) {
    this.categoriaOmdb = categoriaOmdb;
  }

  public static Categoria fromString(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    throw new IllegalArgumentException("Nenhuma categoria correspondente enconrtada!");
  }
}
