package com.hope.sps.customer.payment.wallet;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {

    @Min(value = 900000, message = "amountToCharge at least must be 900000, 15 min")
    @NotNull(message = "amountToCharge is mandatory")
    private Integer amountToCharge;
}
