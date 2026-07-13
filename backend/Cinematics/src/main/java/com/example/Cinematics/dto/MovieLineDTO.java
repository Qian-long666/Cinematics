package com.example.Cinematics.dto;

import lombok.Data;

@Data
public class MovieLineDTO {
    private Long id;
    private String quoteZh;
    private String quoteOri;
    private Integer quoteOrder;
}