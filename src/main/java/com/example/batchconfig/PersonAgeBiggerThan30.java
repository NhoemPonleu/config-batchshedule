package com.example.batchconfig;

import com.example.batchconfig.brand.Brand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PersonAgeBiggerThan30 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int age ;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    private LocalDateTime registerDate;
    private Long transactionSeqNo;
    private LocalDate transactionDate;
    private String registerTellerId;
    private String registerTellerName;
    @ManyToOne
    private Brand brand;
}
