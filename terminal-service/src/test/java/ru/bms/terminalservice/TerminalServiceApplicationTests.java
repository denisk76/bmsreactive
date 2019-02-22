package ru.bms.terminalservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.api.HelloResponse;
import ru.bms.api.RuleUnit;
import ru.bms.api.Terminal;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = {TerminalServiceApplication.class})
public class TerminalServiceApplicationTests {

    public static final String TERMINAL_CODE = "123";
    @Autowired
    private WebTestClient webClient;

    @Test
    public void testHello() throws Exception {
        webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello, my friend! I`m Terminal Controller.").build());
    }

    @Test
    public void testGetTerminal() throws Exception {
        webClient.post().uri("/getTerminal").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        TerminalRequest.builder()
                                .terminal(Terminal.builder().code(TERMINAL_CODE).build())
                                .build()
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TerminalResponse.class)
                .isEqualTo(TerminalResponse.builder()
                        .ruleUnit(RuleUnit.builder().percent(BigDecimal.valueOf(20)).build())
                        .build());
    }

}
