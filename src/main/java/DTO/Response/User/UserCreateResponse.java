package DTO.Response.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponse {
    private String message;

    @JsonProperty("_id")
    private String id;
}
