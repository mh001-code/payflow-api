package com.payflow.application.usecase;

import com.payflow.application.port.out.WalletRepositoryPort;
import com.payflow.domain.exception.WalletNotFoundException;
import com.payflow.domain.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindWalletUseCase {

    private final WalletRepositoryPort walletRepository;

    public Wallet execute(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException(id));
    }
}
