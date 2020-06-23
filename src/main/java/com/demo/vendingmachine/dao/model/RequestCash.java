package com.demo.vendingmachine.dao.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(buildMethodName = "build", setterPrefix = "with", toBuilder = true)
@Getter
@Setter
@ToString
public class RequestCash {

    private Integer quantity;

    private Double amount;
}
