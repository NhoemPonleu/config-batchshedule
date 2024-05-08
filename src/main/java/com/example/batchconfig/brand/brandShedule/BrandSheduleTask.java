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

    @Scheduled(cron = "0 10 9 * * *", zone = "Asia/Phnom_Penh")// Runs every day at 9:10 AM
    public void runBrandCloaseYN() {
        BrandTransactionEveryDayDTO brandTransactionEveryDayDTO = new BrandTransactionEveryDayDTO();
        // Set DTO properties if needed

        brandTransactionEveryDayService.brandCloaseYN(brandTransactionEveryDayDTO);
    }
    @Scheduled(cron = "0 30 11 * * *", zone = "Asia/Phnom_Penh")// run every day at 9:40 AM
    public void runBrandOpenYN() {
        BrandTransactionEveryDayDTO brandTransactionEveryDayDTO = new BrandTransactionEveryDayDTO();
        // Set DTO properties if needed

        brandTransactionEveryDayService.brandOpenYTN(brandTransactionEveryDayDTO);
    }
}