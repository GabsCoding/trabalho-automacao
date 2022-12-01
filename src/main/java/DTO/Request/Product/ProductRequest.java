package DTO.Request.Product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {
    private String nome;

    private int preco;

    private String descricao;

    private int quantidade;
}