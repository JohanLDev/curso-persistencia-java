package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jdk.jfr.Category;

import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;
    private Categoria genero;
    private String actores;
    private String sinopsis;

    public Serie (DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        this.genero = Categoria.fromString(datosSerie.genero());
        this.actores = datosSerie.actores();
        this.sinopsis = datosSerie.sinopsis();
    }
}
