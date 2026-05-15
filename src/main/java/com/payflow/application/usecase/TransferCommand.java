package com.payflow.application.usecase;

import java.math.BigDecimal;

public record TransferCommand(Long payerId, Long payeeId, BigDecimal amount) {
}
