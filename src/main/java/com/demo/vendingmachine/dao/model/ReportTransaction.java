package com.demo.vendingmachine.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;

@Builder(buildMethodName = "build", setterPrefix = "with", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ReportTransaction {

    @Id
    private Integer id;

    private String machineModel;

    private String productName;

    private String paymentType;

    private Double total;

    private Double moneyReceived;

    private Set<RequestCash> change;

    @JsonProperty("transactionDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;


}
