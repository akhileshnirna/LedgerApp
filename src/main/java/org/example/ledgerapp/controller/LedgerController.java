package org.example.ledgerapp.controller;

import ch.qos.logback.core.util.StringUtil;
import org.example.ledgerapp.domain.Entry;
import org.example.ledgerapp.domain.dto.TransactionDTO;
import org.example.ledgerapp.domain.enums.AccountType;
import org.example.ledgerapp.service.TransactionService;
import org.example.ledgerapp.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/ledger")
public class LedgerController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<String> recordTransaction(@RequestBody TransactionDTO transaction) {
        String error = ValidationUtils.validateTransaction(transaction);
        if (StringUtil.notNullNorEmpty(error)) {
            return ResponseEntity.badRequest().body(error);
        }
        transactionService.recordTransaction(transaction);
        return ResponseEntity.ok("Transactions recorded successfully");
    }

    @GetMapping("/balance/{accountType}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountType) {
        AccountType type = AccountType.valueOf(accountType);
        return ResponseEntity.ok(transactionService.getBalance(type));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Entry>> getTransactionHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(from, to));
    }

}
