package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action","Acción"),
    ROMANCE("Romance", "Romance"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen"),
    COMEDIA("Comedy", "Comedia");

    private String categoriaOmdb;
    private String categoriaEspanol;

    Categoria(String generoOmdb, String categoriaEspanol) {
        this.categoriaOmdb = generoOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text) || categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }



    @Override
    public String toString() {
        return categoriaOmdb;
    }
}
