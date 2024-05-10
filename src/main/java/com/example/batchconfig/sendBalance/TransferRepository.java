package com.example.batchconfig.sendBalance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Transfer findByReceiverPhoneNumberAndPassword(String receiverPhoneNumber, Integer password);
}