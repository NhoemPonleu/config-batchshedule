package com.example.batchconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchconfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchconfigApplication.class, args);
    }

}
