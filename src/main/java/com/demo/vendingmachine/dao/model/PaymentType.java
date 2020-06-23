package com.demo.vendingmachine.dao.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(buildMethodName = "build", setterPrefix = "with", toBuilder = true)
@Getter
@Setter
@ToString
@Table(name = "payment_type")
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private PaymentMethod type;

    @ManyToMany(mappedBy = "paymentTypes")
    @JsonBackReference
    private Set<VendingMachine> vendingMachines;
}
