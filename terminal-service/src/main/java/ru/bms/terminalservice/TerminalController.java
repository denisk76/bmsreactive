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
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.api.HelloResponse;
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
    public Mono<TerminalResponse> getTerminal(@RequestBody TerminalRequest request) {
        log.info("post /getTerminal ");
        log.info(request.toString());
        String code = request.getTerminal().getCode();
        BigDecimal percent;
        try {
            percent = terminalManager.getByCode(code);
        } catch (Exception e) {
            percent = BigDecimal.ONE;
        }
        return Mono.just(TerminalResponse.builder().ruleUnit(RuleUnit.builder().percent(percent).build()).build());
    }

    @PostMapping("/addTerminal")
    public Mono<AddTerminalResponse> addTerminal(@RequestBody AddTerminalRequest request) {
        terminalManager.add(request.getTerminalCode(), request.getPercent());
        return Mono.just(AddTerminalResponse.builder().state("SUCCESS").build());
    }
}
