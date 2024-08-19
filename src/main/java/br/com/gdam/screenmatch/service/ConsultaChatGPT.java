package br.com.gdam.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import io.github.cdimascio.dotenv.Dotenv;

public class ConsultaChatGPT {
  public static String obterTraducao(String texto) {
    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("OPENAI_API_KEY");

    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalArgumentException("Chave da API OpenAI não encontrada! Verifique o arquivo .env.");
    }

    OpenAiService service = new OpenAiService(apiKey);

    CompletionRequest requisicao = CompletionRequest.builder()
        .model("gpt-3.5-turbo-instruct")
        .prompt("traduza para o português o texto: " + texto)
        .maxTokens(1000)
        .temperature(0.7)
        .build();

    var resposta = service.createCompletion(requisicao);
    return resposta.getChoices().get(0).getText();
  }
}
