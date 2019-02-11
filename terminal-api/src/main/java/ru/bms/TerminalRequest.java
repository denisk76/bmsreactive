package ru.bms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.Terminal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerminalRequest {
    private Terminal terminal;
}
