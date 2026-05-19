package com.ciclocare.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RegistroSintomaRequest {

    private List<String> humor;
    private List<String> sexoLibido;
    private List<String> sintomas;
    private List<String> secrecao;
    private String observacao;
}
