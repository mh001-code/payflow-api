package com.payflow.api.controller;

import com.payflow.api.dto.CreateWalletRequest;
import com.payflow.api.dto.WalletResponse;
import com.payflow.application.usecase.CreateWalletCommand;
import com.payflow.application.usecase.CreateWalletUseCase;
import com.payflow.application.usecase.FindWalletUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final FindWalletUseCase findWalletUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse create(@RequestBody @Valid CreateWalletRequest request) {
        return WalletResponse.from(createWalletUseCase.execute(new CreateWalletCommand(request.userId())));
    }

    @GetMapping("/{id}")
    public WalletResponse findById(@PathVariable Long id) {
        return WalletResponse.from(findWalletUseCase.execute(id));
    }
}
