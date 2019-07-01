package ru.bms.terminalservice;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.AddTerminalRequest;
import ru.bms.AddTerminalResponse;
import ru.bms.api.HelloResponse;
import ru.bms.api.ITerminal;
import ru.bms.api.RuleUnit;

import java.math.BigDecimal;

@RestController
@Log
public class TerminalController {

    @Autowired
    TerminalManager terminalManager;

    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello, my friend! I`m ITerminal Controller.").build());
    }

    @PostMapping("/getTerminal")
    public Mono<RuleUnit> getTerminal(@RequestBody ITerminal terminal) {
        log.info("post /getTerminal ");
        log.info(terminal.toString());
        String code = terminal.getCode();
        BigDecimal percent;
        try {
            percent = terminalManager.getByCode(code);
        } catch (Exception e) {
            percent = BigDecimal.ONE;
        }
        return Mono.just(RuleUnit.builder().percent(percent).build());
    }

    @PostMapping("/addTerminal")
    public Mono<AddTerminalResponse> addTerminal(@RequestBody AddTerminalRequest request) {
        terminalManager.add(request.getTerminalCode(), request.getPercent());
        return Mono.just(AddTerminalResponse.builder().state("SUCCESS").build());
    }
}
