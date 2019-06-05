package ru.bms.service;

import reactor.core.publisher.Mono;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;

public interface ClientService {
    Mono<ClientResponse> getClient(ClientRequest request);
}
