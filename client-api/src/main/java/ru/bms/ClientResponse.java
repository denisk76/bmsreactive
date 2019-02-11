package ru.bms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {
    private Account account;
}
