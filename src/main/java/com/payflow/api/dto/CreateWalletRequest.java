package com.payflow.api.dto;

import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(

        @NotNull(message = "User ID is required")
        Long userId
) {
}
