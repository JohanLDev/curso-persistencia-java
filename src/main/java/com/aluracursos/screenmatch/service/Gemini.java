package com.aluracursos.screenmatch.service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


public class Gemini {



    public static String traducirTexto(String texto) {
        Client client = new Client.Builder()
                .apiKey("API-KEY")
                .build();

        String prompt = "Traduce el siguiente texto al español: " + texto;

        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-1.5-flash", // modelo Gemini que desees usar
                            prompt,
                            null); // Se pueden agregar configuraciones. Se debe investigar.

            if (response != null && response.text() != null) {
                return response.text();
            } else {
                System.out.println("La API de Gemini no devolvió texto para la traducción.");
                return "";
            }

        } catch (Exception e) {
            System.out.println("Error al traducir el texto: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }


}
