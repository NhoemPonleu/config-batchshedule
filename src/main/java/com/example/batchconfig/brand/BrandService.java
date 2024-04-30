package com.example.batchconfig.brand;

import java.util.List;

public interface BrandService {
    void brandOpenClose(BrandDTO brandDTO);
    List<Brand> getAllBrandForBatch();
    void updateBrandCloseYN(Long brandId, String closeYN);
}
