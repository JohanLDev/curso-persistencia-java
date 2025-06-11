package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsultaChatGPT;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=cb12e773";;
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Serie> series = new ArrayList<>();

    private List<DatosSerie> datosSeries = new ArrayList<>();

    private SerieRepository serieRepository;

    private Optional<Serie> serie;

    public Principal(SerieRepository repository) {
        this.serieRepository = repository;
    }


    public void muestraElMenu() {


        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - Buscar Top 5 Mejores series
                    6 - Buscar series por categoria
                    7 - Filtrar series por temporada y evaluacion
                    8 - Buscar episodios
                    9 - Buscar Top 5 Mejores Episodios de una Serie                    
                    
                    
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;

                case 3 :
                    mostrarSeriesBuscadas();
                    break;

                case 4:
                    buscarSeriesPorTitulo();
                    break;

                case 6:
                    buscarSeriesPorCategoria();
                    break;

                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;

                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                buscarTop5Episodios();
                break;


                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios:");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream().
                filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase())).
                findFirst();

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                                    .map(e -> new Episodio(d.numero(), e)))
                                    .collect(Collectors.toList());


            serieEncontrada.setEpisodios(episodios);

            serieRepository.save(serieEncontrada);
        }



    }


    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //datosSeries.add(datos);
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas(){

        series = serieRepository.findAll();

        // Ordenar por género e imprimir
        series.stream().
                sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);


    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el titulo de la serie que deseas buscar:");
        var nombreSerie = teclado.nextLine();

        serie = serieRepository.findByTituloContainsIgnoreCase(nombreSerie);

        if(serie.isPresent()){
            System.out.println("Serie obtenida:" + serie.get());
        } else{
            System.out.println("Serie no encontrada.");
        }
    }

    private void buscarTop5Series(){
        List<Serie> topSeries = serieRepository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Titulo:" + s.getTitulo()));
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Escribe el genero/categoría de la serie que desea buscar:");
        var nombreCategoria = teclado.nextLine();

        Categoria categoria = Categoria.fromString(nombreCategoria);

        System.out.println(categoria.toString());

        List<Serie> seriesEncontradas = serieRepository.findByGenero(categoria);

        System.out.println("Series de la categoria  " + nombreCategoria + " encontradas: ");
        seriesEncontradas.forEach(System.out::println);
    }

    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Con evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = serieRepository.seriesPorTemporadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo(){
        System.out.println("Ingresa el nombre del episodio que deseas buscar: ");
        var nombreEpisodio = teclado.nextLine();

        List<Episodio> listaEpisodios = serieRepository.episodiosPorNombre(nombreEpisodio);

        listaEpisodios.forEach(e -> {
            System.out.printf("Serie: %s Temporada: %s Episodio: %s Evaluación: %s",e.getSerie(),e.getTemporada(),
                    e.getNumeroEpisodio(), e.getEvaluacion());
        });

    }

    private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();

        if(serie.isPresent()){
            Serie serieBuscada = serie.get();
            List<Episodio> episodios = serieRepository.top5Episodios(serieBuscada);
            episodios.forEach(e -> {
                System.out.printf("Serie: %s Temporada: %s Episodio: %s Evaluación: %s\n",e.getSerie(),e.getTemporada(),
                        e.getNumeroEpisodio(), e.getEvaluacion());
            });
        }

    }

}

