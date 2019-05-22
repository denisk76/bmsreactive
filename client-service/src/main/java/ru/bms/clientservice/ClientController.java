package ru.bms.clientservice;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;
import ru.bms.api.Account;
import ru.bms.api.HelloResponse;
import ru.bms.clientservice.data.AccountData;

import java.math.BigDecimal;

@RestController
@Log
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public Mono<String> help() {
        return Mono.just("Hello!");
    }


    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello, my friend! I`m Client Controller.").build());
    }

    @PostMapping("/getClient")
    public Mono<ClientResponse> getClient(@RequestBody ClientRequest request) {
        log.info("post /getClient ");
        log.info(request.toString());
        AccountData accountData = clientService.findById(1);
        return Mono.just(ClientResponse.builder().account(Account.builder().amount(accountData.getAmount()).build()).build());
    }
}
