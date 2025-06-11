package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aluracursos.screenmatch.model.Serie;

import java.util.Optional;
import java.util.List;

public interface SerieRepository extends JpaRepository<Serie,Long> {

    // Este nombramiento del método no es al azar, si no más bien es que JPA nos permite hacer consulta así.
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);

}
