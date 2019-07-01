package ru.bms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.ApiParamType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateConfigRequest {
    private String inputParamType;
    private String url;
}
