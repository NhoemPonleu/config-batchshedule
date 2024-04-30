package com.example.batchconfig.brand.brandShedule;

import com.example.batchconfig.PersonDTO;
import com.example.batchconfig.brand.BrandDTO;
import com.example.batchconfig.brand.BrandService;
import com.example.batchconfig.brand.BrandTransactionEveryDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BrandSheduleTask {

    private final BrandTransactionEveryDayService brandTransactionEveryDayService;

    @Autowired
    public BrandSheduleTask(BrandTransactionEveryDayService brandTransactionEveryDayService) {
        this.brandTransactionEveryDayService = brandTransactionEveryDayService;
    }

    @Scheduled(cron = "00 00 17 * * *", zone = "Asia/Phnom_Penh")// Runs every day at 10:01 AM
    public void runBrandCloaseYN() {
        BrandTransactionEveryDayDTO brandTransactionEveryDayDTO = new BrandTransactionEveryDayDTO();
        // Set DTO properties if needed

        brandTransactionEveryDayService.brandCloaseYN(brandTransactionEveryDayDTO);
    }
    @Scheduled(cron = "00 00 7 * * *", zone = "Asia/Phnom_Penh")
    public void runBrandOpenYN() {
        BrandTransactionEveryDayDTO brandTransactionEveryDayDTO = new BrandTransactionEveryDayDTO();
        // Set DTO properties if needed

        brandTransactionEveryDayService.brandOpenYTN(brandTransactionEveryDayDTO);
    }
}