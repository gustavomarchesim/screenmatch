package br.com.gdam.screenmatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.gdam.screenmatch.principal.Principal;
import br.com.gdam.screenmatch.repository.SerieRepository;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {
		// Carregar o .env
		Dotenv dotenv = Dotenv.configure().load();
		// Garantir que as variáveis do .env sejam injetadas no ambiente
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.exibeMenu();
	}
}
