package br.com.gdam.screenmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ScreenmatchApplication {
	public static void main(String[] args) {
		// Carregar o .env
		Dotenv dotenv = Dotenv.configure().load();
		// Garantir que as variáveis do .env sejam injetadas no ambiente
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ScreenmatchApplication.class, args);
	}
}
