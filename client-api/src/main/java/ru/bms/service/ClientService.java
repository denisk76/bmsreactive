package ru.bms.service;

import reactor.core.publisher.Mono;
import ru.bms.AddClientRequest;
import ru.bms.AddClientResponse;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;

public interface ClientService {
    void setIpAddr(String ipAddr);

    Mono<ClientResponse> getClient(ClientRequest request);

    Mono<AddClientResponse> addClient(AddClientRequest request);

    void addClients(String request);
}
