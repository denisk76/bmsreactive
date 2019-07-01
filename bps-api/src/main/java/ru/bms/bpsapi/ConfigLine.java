package ru.bms.bpsapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.ApiParamType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigLine {
    private ApiParamType paramType;
    private String url;
    private String method;
}
