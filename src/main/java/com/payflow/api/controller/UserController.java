package com.payflow.api.controller;

import com.payflow.api.dto.CreateUserRequest;
import com.payflow.api.dto.UserResponse;
import com.payflow.application.usecase.CreateUserCommand;
import com.payflow.application.usecase.CreateUserUseCase;
import com.payflow.application.usecase.FindUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserUseCase findUserUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(
                request.fullName(), request.cpf(), request.email(), request.password(), request.type()
        );
        return UserResponse.from(createUserUseCase.execute(command));
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.from(findUserUseCase.execute(id));
    }
}
