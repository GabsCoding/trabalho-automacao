package DTO.Request.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
    private String nome;

    private String email;

    private String password;

    private String administrador;
}