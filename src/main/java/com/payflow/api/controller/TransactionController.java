package com.payflow.api.controller;

import com.payflow.api.dto.TransactionResponse;
import com.payflow.api.dto.TransferRequest;
import com.payflow.application.usecase.FindTransactionUseCase;
import com.payflow.application.usecase.TransferCommand;
import com.payflow.application.usecase.TransferUseCase;
import com.payflow.domain.model.Transaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransferUseCase transferUseCase;
    private final FindTransactionUseCase findTransactionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse transfer(@RequestBody @Valid TransferRequest request) {
        TransferCommand command = new TransferCommand(request.payerId(), request.payeeId(), request.amount());
        Transaction transaction = transferUseCase.transfer(command);
        return TransactionResponse.from(transaction, request.payerId(), request.payeeId());
    }

    @GetMapping("/{id}")
    public TransactionResponse findById(@PathVariable Long id) {
        return TransactionResponse.from(findTransactionUseCase.findById(id));
    }

    @GetMapping("/history")
    public List<TransactionResponse> findHistory(@RequestParam @NotNull Long userId) {
        return findTransactionUseCase.findByUserId(userId).stream()
                .map(TransactionResponse::from)
                .toList();
    }
}
