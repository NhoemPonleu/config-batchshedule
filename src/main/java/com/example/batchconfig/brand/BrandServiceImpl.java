package com.example.batchconfig.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService{
    private final BrandRepository brandRepository;
    @Override
    public void brandOpenClose(BrandDTO brandDTO) {
        Brand brand = new Brand();
        brand.setName(brandDTO.getName());
        brand.setCloseYN(brandDTO.getCloseYN());
        brandRepository.save(brand);
    }

    @Override
    public List<Brand> getAllBrandForBatch() {
       List<Brand> listAllBrand= brandRepository.findAll();
       return listAllBrand;
    }
    public void updateBrandCloseYN(Long brandId, String closeYN) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);

        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();
            brand.setCloseYN(closeYN);
            brandRepository.save(brand);
        } else {
            throw new RuntimeException("Brand not found with ID: " + brandId);
        }
    }
}
