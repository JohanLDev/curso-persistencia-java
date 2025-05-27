package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action"),
    ROMANCE("Romance"),
    DRAMA("Drama"),
    CRIMEN("Crime"),
    COMEDIA("Comedy");

    private String categoriaOmdb;

    Categoria(String generoOmdb) {
        this.categoriaOmdb = generoOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
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
