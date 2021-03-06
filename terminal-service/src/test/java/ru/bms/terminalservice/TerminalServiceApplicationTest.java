package ru.bms.terminalservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.api.HelloResponse;
import ru.bms.api.ITerminal;
import ru.bms.api.RuleUnit;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = {TerminalServiceApplication.class})
@ComponentScan(basePackages = {"ru.bms.terminalservice"})
public class TerminalServiceApplicationTest {

    public static final String TERMINAL_CODE = "10";
    public static final BigDecimal PERCENT = BigDecimal.valueOf(10);
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private TerminalManager terminalService;

    @Test
    public void testHello() throws Exception {
        webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello, my friend! I`m ITerminal Controller.").build());
    }


    @Test
    public void testGetTerminal() throws Exception {
        terminalService.add("10", BigDecimal.TEN);
        terminalService.add("20", BigDecimal.valueOf(20));
        webClient.post().uri("/getTerminal").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        ITerminal.builder().code(TERMINAL_CODE).build())
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(RuleUnit.class)
                .isEqualTo(RuleUnit.builder().percent(PERCENT).build());
    }

}

