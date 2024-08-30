package br.com.gdam.screenmatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.gdam.screenmatch.model.Categoria;
import br.com.gdam.screenmatch.model.Episodio;
import br.com.gdam.screenmatch.model.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
        Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

        List<Serie> findByAtoresContainingIgnoreCaseAndTotalTemporadasLessThanEqualAndAvaliacaoIsGreaterThanEqual(
                        String nomeAtor, Double temporada, Double avaliacao);

        List<Serie> findTop5ByOrderByAvaliacaoDesc();

        List<Serie> findByGeneros(Categoria categoria);

        @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
        List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

        @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
        List<Episodio> episodiosPorTrecho(@Param("trecho") String trecho);

        @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo ILIKE :serie ORDER BY e.avaliacao DESC LIMIT 5")
        List<Episodio> topEpisodiosPorSerie(String serie);

        @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE YEAR(e.dataLancamento) >= :anoBusca")
        List<Episodio> buscaEpisodioPorData(@Param("anoBusca") Integer anoBusca);

}
