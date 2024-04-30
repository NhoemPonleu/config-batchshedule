package com.example.batchconfig.brand.brandShedule;

import com.example.batchconfig.brand.Brand;
import com.example.batchconfig.brand.BrandService;
import com.example.batchconfig.brand.BrandTransactionEveryDay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandTransactionEveryServiceImpl implements BrandTransactionEveryDayService{
    private final BrandService brandService;
    private final BrandTransactionEveryDayRepository brandTransactionEveryDayRepository;
    @Override
    public void brandCloaseYN(BrandTransactionEveryDayDTO brandTransactionEveryDayDTO) {
       List<Brand> brands= brandService.getAllBrandForBatch();
       for (Brand brand : brands) {
           BrandTransactionEveryDay brandTransactionEveryDay = new BrandTransactionEveryDay();
           brandTransactionEveryDay.setBrand(brand);
           brandTransactionEveryDay.setBrandCloseYN("Y");
           brandTransactionEveryDay.setBrandOpenYN("N");
           brandTransactionEveryDay.setRegistrationDate(LocalDate.now());
           brandTransactionEveryDay.setTimeRegistrationDate(LocalDateTime.now());
           brandTransactionEveryDay.setRegisterTime(LocalTime.now());
           brandTransactionEveryDay.setName(brand.getName());
           brandTransactionEveryDayRepository.save(brandTransactionEveryDay);
           brandService.updateBrandCloseYN(brand.getId(),"Y");
       }
    }

    @Override
    public void brandOpenYTN(BrandTransactionEveryDayDTO brandTransactionEveryDayDTO) {
        List<Brand> brands= brandService.getAllBrandForBatch();
        for (Brand brand : brands) {
            BrandTransactionEveryDay brandTransactionEveryDay = new BrandTransactionEveryDay();
            brandTransactionEveryDay.setBrand(brand);
            brandTransactionEveryDay.setBrandCloseYN("N");
            brandTransactionEveryDay.setBrandOpenYN("Y");
            brandTransactionEveryDay.setRegistrationDate(LocalDate.now());
            brandTransactionEveryDay.setTimeRegistrationDate(LocalDateTime.now());
            brandTransactionEveryDay.setRegisterTime(LocalTime.now());
            brandTransactionEveryDay.setName(brand.getName());
            brandTransactionEveryDayRepository.save(brandTransactionEveryDay);
            brandService.updateBrandCloseYN(brand.getId(),"N");
        }
    }
}
