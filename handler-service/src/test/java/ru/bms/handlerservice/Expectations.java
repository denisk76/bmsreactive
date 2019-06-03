package ru.bms.handlerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import ru.bms.ClientResponse;
import ru.bms.TerminalResponse;
import ru.bms.api.Account;
import ru.bms.api.Bill;
import ru.bms.api.RuleUnit;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Expectations {

    public static final String HEADER_CONTENT_TYPE = "application/json;charset=UTF-8";

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
        TerminalResponse response = TerminalResponse.builder()
                .ruleUnit(RuleUnit.builder().percent(BigDecimal.valueOf(20)).build())
                .build();
        addPostExpect(mockServer, "/getTerminal", response, "terminal");
    }

    private static void getClient(ClientAndServer mockServer) {
        ClientResponse response = ClientResponse.builder()
                .account(Account.builder().amount(BigDecimal.valueOf(10)).build())
                .build();
        addPostExpect(mockServer, "/getClient", response, "client");
    }

    private static void getPayment(ClientAndServer mockServer) {
        PaymentResponse response = PaymentResponse.builder()
                .account(Account.builder().amount(BigDecimal.valueOf(10)).build())
                .bill(Bill.builder().sum(BigDecimal.valueOf(10)).build())
                .earn(BigDecimal.valueOf(5))
                .spend(BigDecimal.ZERO)
                .build();
        addPostExpect(mockServer, "/getPayment", response, "payment");
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
