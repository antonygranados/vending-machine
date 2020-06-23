package com.demo.vendingmachine.dao.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "vending_machine")
public class VendingMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "model")
    private String model;

    @Column(name = "location")
    private String location;

    @ManyToMany
    @JoinTable(
            name = "machine_products",
            joinColumns = @JoinColumn(name = "vending_machine_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonManagedReference
    private Set<Product> products;

    @Column(name = "security_code")
    private String securityCode;

    @Column(name = "open_attempts")
    private Integer openAttempts;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MachineState status;

    @ManyToMany
    @JoinTable(
            name = "machine_payment_type",
            joinColumns = @JoinColumn(name = "vending_machine_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_type_id"))
    @JsonManagedReference
    private Set<PaymentType> paymentTypes;

}
