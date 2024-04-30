package com.example.batchconfig;

import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class PersonAgeBiggerThan30Dto {
    private Long id;
    private int age ;
    private Long personId;
    private Long brandId;
}
