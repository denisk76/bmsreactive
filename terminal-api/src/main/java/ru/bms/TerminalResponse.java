package ru.bms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.RuleUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerminalResponse {
    private RuleUnit ruleUnit;
}
