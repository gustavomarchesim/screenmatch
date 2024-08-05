package br.com.gdam.screenmatch.service;

public interface iConverteDados {
  <T> T obterDados(String json, Class<T> classe);
}
