package DTO.Response.Product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateResponse {
    private String message;

    @JsonProperty("_id")
    private String id;
}