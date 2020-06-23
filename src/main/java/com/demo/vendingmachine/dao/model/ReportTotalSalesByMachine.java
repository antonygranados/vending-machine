package com.demo.vendingmachine.dao.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder(buildMethodName = "build", setterPrefix = "with", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ReportTotalSalesByMachine {

    @Id
    private Integer vendingMachineId;

    private String model;

    private Double totalSales;

}
