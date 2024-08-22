package br.com.gdam.screenmatch.repository;

import br.com.gdam.screenmatch.model.Serie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
}
