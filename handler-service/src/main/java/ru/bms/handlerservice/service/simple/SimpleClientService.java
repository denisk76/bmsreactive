package ru.bms.handlerservice.service.simple;

import reactor.core.publisher.Mono;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;
import ru.bms.api.Account;
import ru.bms.handlerservice.service.ClientService;

import java.math.BigDecimal;

public class SimpleClientService implements ClientService {
    @Override
    public Mono<ClientResponse> getClient(ClientRequest request) {
        return Mono.just(ClientResponse.builder()
                .account(Account.builder().amount(BigDecimal.TEN).build())
                .build()
        );
    }
}
