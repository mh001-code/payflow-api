package com.payflow.application.usecase;

import com.payflow.application.port.out.AuthorizationPort;
import com.payflow.application.port.out.NotificationPort;
import com.payflow.application.port.out.TransactionRepositoryPort;
import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.application.port.out.WalletRepositoryPort;
import com.payflow.domain.exception.InsufficientFundsException;
import com.payflow.domain.exception.UnauthorizedTransactionException;
import com.payflow.domain.exception.UserNotFoundException;
import com.payflow.domain.exception.WalletNotFoundException;
import com.payflow.domain.model.Transaction;
import com.payflow.domain.model.TransactionStatus;
import com.payflow.domain.model.User;
import com.payflow.domain.model.UserType;
import com.payflow.domain.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferUseCase {

    private final UserRepositoryPort userRepository;
    private final WalletRepositoryPort walletRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final AuthorizationPort authorizationPort;
    private final NotificationPort notificationPort;

    @Transactional
    public Transaction transfer(TransferCommand command) {
        User payer = userRepository.findById(command.payerId())
                .orElseThrow(() -> new UserNotFoundException(command.payerId()));

        if (payer.getType() == UserType.LOJISTA) {
            throw new UnauthorizedTransactionException();
        }

        Wallet payerWallet = walletRepository.findByUserId(command.payerId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + command.payerId()));

        Wallet payeeWallet = walletRepository.findByUserId(command.payeeId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + command.payeeId()));

        if (payerWallet.getBalance().compareTo(command.amount()) < 0) {
            throw new InsufficientFundsException(payerWallet.getId());
        }

        Transaction transaction = Transaction.builder()
                .payer(payerWallet)
                .payee(payeeWallet)
                .amount(command.amount())
                .status(TransactionStatus.PENDING)
                .build();

        if (!authorizationPort.authorize(transaction)) {
            transaction.setStatus(TransactionStatus.FAILED);
            return transactionRepository.save(transaction);
        }

        payerWallet.debit(command.amount());
        payeeWallet.credit(command.amount());

        transaction.setStatus(TransactionStatus.COMPLETED);
        Transaction completed = transactionRepository.save(transaction);
        notificationPort.notify(completed);
        return completed;
    }
}
