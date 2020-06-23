package com.demo.vendingmachine.dao.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestSell {

    private Integer vendingMachineId;
    private Integer quantity;
    private PaymentMethod paymentMethod;
    private RequestCash[] requestCashes;
    private Integer productId;
}
