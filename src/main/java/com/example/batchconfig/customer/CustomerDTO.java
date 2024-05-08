package com.example.batchconfig.customer;

import lombok.Data;

import java.util.List;
@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private List<VillageDTO> villages;
    // constructors, getters, setters
}