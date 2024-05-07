package com.example.batchconfig.customer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@Getter
@Setter
@Table(name = "tblCustomer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private BigDecimal lastName;
    private String address;
}
