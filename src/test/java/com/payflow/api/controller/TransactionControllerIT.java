package com.payflow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.AbstractIntegrationTest;
import com.payflow.api.dto.TransferRequest;
import com.payflow.application.port.out.AuthorizationPort;
import com.payflow.application.port.out.NotificationPort;
import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.application.port.out.WalletRepositoryPort;
import com.payflow.domain.model.User;
import com.payflow.domain.model.UserType;
import com.payflow.domain.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorizationPort authorizationPort;

    @MockBean
    private NotificationPort notificationPort;

    @Autowired
    private UserRepositoryPort userRepository;

    @Autowired
    private WalletRepositoryPort walletRepository;

    private Long payerId;
    private Long payeeId;
    private Long lojistaId;

    @BeforeEach
    void setUp() {
        when(authorizationPort.authorize(any())).thenReturn(true);

        User payer = userRepository.save(User.builder()
                .fullName("João Payer")
                .cpf("12345678901")
                .email("payer@test.com")
                .password("$2a$10$mockpasswordhash")
                .type(UserType.COMUM)
                .build());

        User payee = userRepository.save(User.builder()
                .fullName("Maria Payee")
                .cpf("98765432100")
                .email("payee@test.com")
                .password("$2a$10$mockpasswordhash")
                .type(UserType.COMUM)
                .build());

        User lojista = userRepository.save(User.builder()
                .fullName("Loja XYZ")
                .cpf("11122233344")
                .email("loja@test.com")
                .password("$2a$10$mockpasswordhash")
                .type(UserType.LOJISTA)
                .build());

        walletRepository.save(Wallet.builder()
                .user(payer)
                .balance(new BigDecimal("200.00"))
                .build());

        walletRepository.save(Wallet.builder()
                .user(payee)
                .balance(new BigDecimal("100.00"))
                .build());

        walletRepository.save(Wallet.builder()
                .user(lojista)
                .balance(new BigDecimal("500.00"))
                .build());

        payerId = payer.getId();
        payeeId = payee.getId();
        lojistaId = lojista.getId();
    }

    @Test
    void postTransactions_shouldReturn201_withCompletedStatus_whenAuthorized() throws Exception {
        TransferRequest request = new TransferRequest(payerId, payeeId, new BigDecimal("50.00"));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.payerId").value(payerId))
                .andExpect(jsonPath("$.payeeId").value(payeeId))
                .andExpect(jsonPath("$.amount").value(50.00));
    }

    @Test
    void postTransactions_shouldReturn422_whenPayerIsLojista() throws Exception {
        TransferRequest request = new TransferRequest(lojistaId, payeeId, new BigDecimal("50.00"));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void postTransactions_shouldReturn422_whenInsufficientBalance() throws Exception {
        TransferRequest request = new TransferRequest(payerId, payeeId, new BigDecimal("999.00"));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}
