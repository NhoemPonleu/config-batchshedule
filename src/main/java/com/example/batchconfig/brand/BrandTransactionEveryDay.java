package com.example.batchconfig.brand;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity

public class BrandTransactionEveryDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Brand brand;
    private String brandCloseYN;
    private String brandOpenYN;
    private LocalDate registrationDate;
    private LocalDateTime timeRegistrationDate;
    private LocalTime registerTime;
}
