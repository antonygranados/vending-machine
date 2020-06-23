package com.demo.vendingmachine.dao.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(buildMethodName = "build", setterPrefix = "with", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "report_collect_money")
public class ReportCollectMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Double amount;

    private LocalDateTime lastTimeCollected;
}
