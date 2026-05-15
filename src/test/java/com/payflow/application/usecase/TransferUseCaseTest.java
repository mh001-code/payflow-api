package com.payflow.application.usecase;

import com.payflow.application.port.out.AuthorizationPort;
import com.payflow.application.port.out.NotificationPort;
import com.payflow.application.port.out.TransactionRepositoryPort;
import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.application.port.out.WalletRepositoryPort;
import com.payflow.domain.exception.InsufficientFundsException;
import com.payflow.domain.exception.UnauthorizedTransactionException;
import com.payflow.domain.model.Transaction;
import com.payflow.domain.model.TransactionStatus;
import com.payflow.domain.model.User;
import com.payflow.domain.model.UserType;
import com.payflow.domain.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferUseCaseTest {

    @Mock private UserRepositoryPort userRepository;
    @Mock private WalletRepositoryPort walletRepository;
    @Mock private TransactionRepositoryPort transactionRepository;
    @Mock private AuthorizationPort authorizationPort;
    @Mock private NotificationPort notificationPort;

    @InjectMocks
    private TransferUseCase transferUseCase;

    private User payer;
    private Wallet payerWallet;
    private Wallet payeeWallet;
    private TransferCommand command;

    @BeforeEach
    void setUp() {
        payer = User.builder()
                .id(1L)
                .fullName("João Silva")
                .cpf("12345678901")
                .email("joao@example.com")
                .password("password")
                .type(UserType.COMUM)
                .build();

        User payee = User.builder()
                .id(2L)
                .fullName("Maria Oliveira")
                .cpf("98765432100")
                .email("maria@example.com")
                .password("password")
                .type(UserType.COMUM)
                .build();

        payerWallet = Wallet.builder()
                .id(1L)
                .user(payer)
                .balance(new BigDecimal("200.00"))
                .build();

        payeeWallet = Wallet.builder()
                .id(2L)
                .user(payee)
                .balance(new BigDecimal("100.00"))
                .build();

        command = new TransferCommand(1L, 2L, new BigDecimal("50.00"));
    }

    @Test
    void transfer_shouldCompleteSuccessfully_whenAuthorized() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(payeeWallet));
        when(authorizationPort.authorize(any())).thenReturn(true);
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = transferUseCase.transfer(command);

        assertThat(result.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(payerWallet.getBalance()).isEqualByComparingTo("150.00");
        assertThat(payeeWallet.getBalance()).isEqualByComparingTo("150.00");
        verify(notificationPort).notify(result);
    }

    @Test
    void transfer_shouldThrowUnauthorizedException_whenPayerIsLojista() {
        User lojista = User.builder()
                .id(1L)
                .fullName("Loja XYZ")
                .cpf("12345678901")
                .email("loja@example.com")
                .password("password")
                .type(UserType.LOJISTA)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(lojista));

        assertThatThrownBy(() -> transferUseCase.transfer(command))
                .isInstanceOf(UnauthorizedTransactionException.class);

        verifyNoInteractions(walletRepository, transactionRepository, authorizationPort, notificationPort);
    }

    @Test
    void transfer_shouldThrowInsufficientFundsException_whenInsufficientBalance() {
        Wallet walletWithLowBalance = Wallet.builder()
                .id(1L)
                .user(payer)
                .balance(new BigDecimal("10.00"))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(walletWithLowBalance));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(payeeWallet));

        assertThatThrownBy(() -> transferUseCase.transfer(command))
                .isInstanceOf(InsufficientFundsException.class);

        verifyNoInteractions(authorizationPort, notificationPort);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_shouldSaveAsFailed_whenAuthorizationDenied() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(payeeWallet));
        when(authorizationPort.authorize(any())).thenReturn(false);
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = transferUseCase.transfer(command);

        assertThat(result.getStatus()).isEqualTo(TransactionStatus.FAILED);
        assertThat(payerWallet.getBalance()).isEqualByComparingTo("200.00");
        assertThat(payeeWallet.getBalance()).isEqualByComparingTo("100.00");
        verifyNoInteractions(notificationPort);
    }

    @Test
    void transfer_shouldPropagateException_whenAuthorizationServiceFails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(payeeWallet));
        when(authorizationPort.authorize(any())).thenThrow(new RuntimeException("Authorization service unavailable"));

        assertThatThrownBy(() -> transferUseCase.transfer(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Authorization service unavailable");

        verify(transactionRepository, never()).save(any());
        verifyNoInteractions(notificationPort);
    }
}
