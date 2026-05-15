package com.payflow.domain.model;

import com.payflow.domain.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WalletTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .fullName("João Silva")
                .cpf("12345678901")
                .email("joao@example.com")
                .password("hashed_password")
                .type(UserType.COMUM)
                .build();
    }

    private Wallet walletWithBalance(String balance) {
        return Wallet.builder()
                .id(1L)
                .user(user)
                .balance(new BigDecimal(balance))
                .build();
    }

    @Test
    void credit_shouldIncreaseBalance() {
        Wallet wallet = walletWithBalance("100.00");

        wallet.credit(new BigDecimal("50.00"));

        assertThat(wallet.getBalance()).isEqualByComparingTo("150.00");
    }

    @Test
    void credit_withZeroBalance_shouldSetBalance() {
        Wallet wallet = walletWithBalance("0.00");

        wallet.credit(new BigDecimal("200.00"));

        assertThat(wallet.getBalance()).isEqualByComparingTo("200.00");
    }

    @Test
    void debit_shouldDecreaseBalance_whenSufficientFunds() {
        Wallet wallet = walletWithBalance("100.00");

        wallet.debit(new BigDecimal("30.00"));

        assertThat(wallet.getBalance()).isEqualByComparingTo("70.00");
    }

    @Test
    void debit_shouldSucceed_whenAmountEqualsBalance() {
        Wallet wallet = walletWithBalance("100.00");

        wallet.debit(new BigDecimal("100.00"));

        assertThat(wallet.getBalance()).isEqualByComparingTo("0.00");
    }

    @Test
    void debit_shouldThrowInsufficientFundsException_whenInsufficientFunds() {
        Wallet wallet = walletWithBalance("50.00");

        assertThatThrownBy(() -> wallet.debit(new BigDecimal("100.00")))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds in wallet");
    }

    @Test
    void debit_shouldThrowInsufficientFundsException_whenBalanceIsZero() {
        Wallet wallet = walletWithBalance("0.00");

        assertThatThrownBy(() -> wallet.debit(new BigDecimal("0.01")))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds in wallet");
    }
}
