package org.dandan.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthUserDto {

    @Schema(example = "user3")
    private String username;

    @Schema(example = "user3")
    private String password;

    private String code;

    private String uuid = "";
}
