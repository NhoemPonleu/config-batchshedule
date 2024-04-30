package com.example.batchconfig;

import com.example.batchconfig.brand.Brand;
import com.example.batchconfig.util.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class PersonAgeBiggerThan30 extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int age ;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
//    private LocalDateTime registerDate;
    private Long transactionSeqNo;
//    private LocalDate transactionDate;
    private String registerTellerId;
    private String registerTellerName;
//    private LocalTime registerTime;
    @ManyToOne
    private Brand brand;
}
