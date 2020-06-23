package com.demo.vendingmachine.dao.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder(buildMethodName = "build", setterPrefix = "with", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ReportProductsByMachine {

    @Id
    private Integer vendingMachineId;

    @Column(name = "model")
    private String model;

    @Column(name = "location")
    private String location;

    private String productCode;

    private String productName;

    private Double productPrice;
}
