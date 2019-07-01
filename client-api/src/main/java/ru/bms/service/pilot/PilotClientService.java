package ru.bms.service.pilot;

import lombok.extern.java.Log;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.AddClientRequest;
import ru.bms.AddClientResponse;
import ru.bms.api.Account;
import ru.bms.api.IClient;
import ru.bms.service.ClientService;

@Log
public class PilotClientService implements ClientService {

    @Autowired
    @Qualifier("clientWebClient")
    WebClient webClient;

    @Override
    public void setIpAddr(String ipAddr) {
        log.info("set ip addr for client service: " + ipAddr);
        this.webClient = webClient.mutate().baseUrl(ipAddr).build();
    }

    @Override
    public Mono<Account> getClient(IClient request) {
        log.info("Pilot Client Service run getClient ...");
        log.info(request.toString());
        return webClient.post().uri("/getClient").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(Account.class);
    }

    @Override
    public Mono<AddClientResponse> addClient(AddClientRequest request) {
        log.info("Pilot Client Service run addClient ...");
        log.info(request.toString());
        return webClient.post().uri("/addClient").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(AddClientResponse.class);
    }

    @Override
    public void addClients(String request) {
        log.info("Pilot Client Service run addClient ...");
        log.info(request);
        JSONArray jsonArray = new JSONArray(request);
        for (int i = 0; i < jsonArray.length(); i++) {
            String json = jsonArray.get(i).toString();
            System.out.println("Send json: " + json);
            webClient.post().uri("/addClient").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(json))
                    .retrieve().bodyToMono(AddClientResponse.class).block();
        }
    }
}
