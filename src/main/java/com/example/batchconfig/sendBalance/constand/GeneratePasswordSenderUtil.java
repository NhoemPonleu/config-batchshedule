package com.example.batchconfig.sendBalance.constand;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class GeneratePasswordSenderUtil {
    public int generateRandomPassword() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // Generates a random 6-digit number
    }
}
