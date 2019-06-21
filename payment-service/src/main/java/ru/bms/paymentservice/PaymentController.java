package ru.bms.paymentservice;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.api.Account;
import ru.bms.api.Delta;
import ru.bms.api.HelloResponse;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

@RestController
@Log
public class PaymentController {

    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello").build());
    }

    @PostMapping("/getPayment")
    public Mono<PaymentResponse> payment(@RequestBody PaymentRequest request) {
        log.info("post /getPayment");
        log.info(request.toString());
        BigDecimal sum = request.getBill().getSum()
                .multiply(request.getRuleUnit().getPercent())
                .divide(BigDecimal.valueOf(100));
        log.info("sum = " + sum);
        BigDecimal amount = request.getAccount().getAmount().add(sum);
        log.info("amount = " + amount);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.add(PaymentResponse.ParamType.ACCOUNT, Account.builder()
                .amount(amount)
                .build());
        paymentResponse.add(PaymentResponse.ParamType.BILL, request.getBill());
        paymentResponse.add(PaymentResponse.ParamType.DELTA, Delta.builder()
                .earn(sum)
                .spend(BigDecimal.ZERO)
        .build());
        return Mono.just(paymentResponse);
    }

}