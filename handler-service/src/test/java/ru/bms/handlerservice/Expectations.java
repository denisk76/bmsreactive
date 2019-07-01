package ru.bms.handlerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import ru.bms.api.Account;
import ru.bms.api.Bill;
import ru.bms.api.Delta;
import ru.bms.api.RuleUnit;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Expectations {

    public static final String HEADER_CONTENT_TYPE = "application/json";

    public static void createTerminalExpectations(ClientAndServer mockServer) {
        getTerminal(mockServer);
    }

    public static void createClientExpectations(ClientAndServer mockServer) {
        getClient(mockServer);
    }

    public static void createPaymentExpectations(ClientAndServer mockServer) {
        getPayment(mockServer);
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static void getTerminal(ClientAndServer mockServer) {
        RuleUnit response = RuleUnit.builder().percent(BigDecimal.valueOf(20)).build();
        addPostExpect(mockServer, "/getTerminal", response, "terminal");
        addGetExpect(mockServer, "/hello", "Hello","hello");
    }

    private static void getClient(ClientAndServer mockServer) {
        Account response = Account.builder().amount(BigDecimal.valueOf(10)).build();
        addPostExpect(mockServer, "/getClient", response, "client");
        addGetExpect(mockServer, "/hello", "Hello", "hello");
    }

    private static void getPayment(ClientAndServer mockServer) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.add(PaymentResponse.ParamType.ACCOUNT, Account.builder().amount(BigDecimal.valueOf(10)).build());
        paymentResponse.add(PaymentResponse.ParamType.BILL, Bill.builder().sum(BigDecimal.valueOf(10)).build());
        paymentResponse.add(PaymentResponse.ParamType.DELTA, Delta.builder()
                .earn(BigDecimal.valueOf(5))
                .spend(BigDecimal.ZERO)
                .build());
        addPostExpect(mockServer, "/getPayment", paymentResponse, "payment");
        addGetExpect(mockServer, "/hello", "Hello", "hello");
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

    private static void addGetExpect(ClientAndServer mockServer, String s, Object response, String name) {

        try {
            mockServer.when(request().withMethod("GET")
                    .withPath(s)
            ).respond(response().withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(response))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("mockServer " + name + " starting error.");
        }
    }
}
