package ru.bms.service;

import reactor.core.publisher.Mono;
import ru.bms.AddClientRequest;
import ru.bms.AddClientResponse;
import ru.bms.api.Account;
import ru.bms.api.IClient;

public interface ClientService {
    void setIpAddr(String ipAddr);

    Mono<Account> getClient(IClient client);

    Mono<AddClientResponse> addClient(AddClientRequest request);

    void addClients(String request);

}
