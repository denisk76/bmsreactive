package ru.bms.terminalservice;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.api.HelloResponse;
import ru.bms.api.RuleUnit;

import java.math.BigDecimal;

@RestController
@Log
public class TerminalController {

    public static final BigDecimal PERCENT = BigDecimal.valueOf(20);

    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello, my friend! I`m Terminal Controller.").build());
    }

    @PostMapping("/getTerminal")
    public Mono<TerminalResponse> getTerminal(@RequestBody TerminalRequest request) {
        log.info("post /getTerminal ");
        log.info(request.toString());
        return Mono.just(TerminalResponse.builder().ruleUnit(RuleUnit.builder().percent(PERCENT).build()).build());
    }
}
