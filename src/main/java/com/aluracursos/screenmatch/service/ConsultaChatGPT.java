package com.aluracursos.screenmatch.service;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {

    public static String obtenerTraduccion(String texto) {
        OpenAiService service = new OpenAiService("API-KEY");

        CompletionRequest requisicion = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduce a español el siguiente texto: " + texto)
                .maxTokens(1000) // cantidad de token maximo para la respuesta
                .temperature(0.7) // variacion de las respuestas en base a una misma consulta.
                .build();

        var respuesta = service.createCompletion(requisicion);


        try {
            return respuesta.getChoices().get(0).getText();
        } catch (OpenAiHttpException e) {
            System.out.println("Ha ocurrido una excepcion al intentar usar la API: " + e.getMessage());
            return texto;
        }
    }

}
