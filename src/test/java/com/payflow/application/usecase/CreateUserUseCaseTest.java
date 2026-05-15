package com.payflow.application.usecase;

import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.domain.exception.DuplicateUserException;
import com.payflow.domain.model.User;
import com.payflow.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock private UserRepositoryPort userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private CreateUserCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateUserCommand(
                "João Silva",
                "12345678901",
                "joao@example.com",
                "senha123",
                UserType.COMUM
        );
    }

    @Test
    void execute_shouldCreateUser_withEncodedPassword() {
        when(userRepository.existsByCpf(command.cpf())).thenReturn(false);
        when(userRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$hashed");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = createUserUseCase.execute(command);

        assertThat(result.getPassword()).isEqualTo("$2a$hashed");
        assertThat(result.getCpf()).isEqualTo(command.cpf());
        assertThat(result.getEmail()).isEqualTo(command.email());
        assertThat(result.getType()).isEqualTo(UserType.COMUM);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void execute_shouldThrowDuplicateUserException_whenCpfAlreadyExists() {
        when(userRepository.existsByCpf(command.cpf())).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(command))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("CPF");

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowDuplicateUserException_whenEmailAlreadyExists() {
        when(userRepository.existsByCpf(command.cpf())).thenReturn(false);
        when(userRepository.existsByEmail(command.email())).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(command))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("email");

        verify(userRepository, never()).save(any());
    }

    @Test
    void execute_shouldNeverStoreRawPassword() {
        when(userRepository.existsByCpf(command.cpf())).thenReturn(false);
        when(userRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = createUserUseCase.execute(command);

        assertThat(result.getPassword()).isNotEqualTo("senha123");
        verify(passwordEncoder).encode("senha123");
    }
}
