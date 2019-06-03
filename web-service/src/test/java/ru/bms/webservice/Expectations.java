package ru.bms.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import ru.bms.webservice.api.PutPaymentResponse;

import java.math.BigDecimal;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Expectations {

    public static final String HEADER_CONTENT_TYPE = "application/json;charset=UTF-8";

    public static void createTerminalExpectations(ClientAndServer mockServer) {
        getHandler(mockServer);
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static void getHandler(ClientAndServer mockServer) {
        PutPaymentResponse response = PutPaymentResponse.builder()
                .amount(BigDecimal.valueOf(10))
                .earn(BigDecimal.valueOf(3))
                .spend(BigDecimal.valueOf(4))
                .build();
        addPostExpect(mockServer, "/payment", response, "handler");
    }

    private static void addPostExpect(ClientAndServer mockServer, String s, Object response, String name) {
        try {
            mockServer.when(request().withMethod("POST")
                    .withHeader("Content-Type", HEADER_CONTENT_TYPE)
                    .withPath(s)
            ).respond(response().withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(response))
                    .withHeader("Content-Type", HEADER_CONTENT_TYPE)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("mockServer " + name + " starting error.");
        }
    }
}
