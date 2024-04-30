package com.example.batchconfig.brand.brandShedule;

import com.example.batchconfig.brand.Brand;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class BrandTransactionEveryDayDTO {
    private Long id;
    private String name;
    private Long brandId;
    private String brandCloseYN;
}
