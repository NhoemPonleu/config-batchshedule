package com.example.batchconfig.sendBalance;

import com.example.batchconfig.sendBalance.dto.TransferRequest;
import com.example.batchconfig.sendBalance.dto.TransferResponse;
import com.example.batchconfig.sendBalance.dto.WithDrawalRequest;
import com.example.batchconfig.sendBalance.dto.WithdrawalResponse;

public interface TransferService {
    TransferResponse send(TransferRequest transferRequest);
    WithdrawalResponse withdrawalBalance(WithDrawalRequest withdrawalRequest);
}
