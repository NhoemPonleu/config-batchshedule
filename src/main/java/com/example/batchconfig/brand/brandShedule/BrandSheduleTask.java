package com.example.batchconfig.brand.brandShedule;

import com.example.batchconfig.PersonDTO;
import com.example.batchconfig.brand.BrandDTO;
import com.example.batchconfig.brand.BrandService;
import com.example.batchconfig.brand.BrandTransactionEveryDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

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

    @Scheduled(cron = "0 51 11 * * *", zone = "Asia/Phnom_Penh")
    public void backupData() {
        try {
            // Define source and destination directories
            File sourceDir = new File("D:\\react app");
            File destinationDir = new File("D:\\Document");

            // Create a timestamp for the backup file
            String timestamp = LocalDateTime.now().toString().replace(":", "_");

            // Copy files from source directory to destination directory
            for (File file : sourceDir.listFiles()) {
                if (file.isFile()) {
                    Path sourcePath = file.toPath();
                    Path destinationPath = new File(destinationDir, file.getName() + "_" + timestamp).toPath();
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            System.out.println("Backup completed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred during backup process!");
        }
    }
}