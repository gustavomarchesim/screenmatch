package br.com.gdam.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.gdam.screenmatch.model.DadosEpisodio;
import br.com.gdam.screenmatch.model.DadosSerie;
import br.com.gdam.screenmatch.service.ConsumoApi;
import br.com.gdam.screenmatch.service.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConverteDados converte = new ConverteDados();

		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=e888d45d");

		DadosSerie dados = converte.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&apikey=e888d45d");
		DadosEpisodio dadosEpisodio = converte.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);
	}
}
