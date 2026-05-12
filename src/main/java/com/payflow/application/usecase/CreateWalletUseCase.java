package com.payflow.application.usecase;

import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.application.port.out.WalletRepositoryPort;
import com.payflow.domain.exception.DuplicateWalletException;
import com.payflow.domain.exception.UserNotFoundException;
import com.payflow.domain.model.User;
import com.payflow.domain.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateWalletUseCase {

    private final WalletRepositoryPort walletRepository;
    private final UserRepositoryPort userRepository;

    @Transactional
    public Wallet execute(CreateWalletCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        if (walletRepository.existsByUserId(command.userId())) {
            throw new DuplicateWalletException(command.userId());
        }

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        return walletRepository.save(wallet);
    }
}
