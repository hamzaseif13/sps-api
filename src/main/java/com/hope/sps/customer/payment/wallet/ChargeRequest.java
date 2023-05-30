package com.hope.sps.customer.payment.wallet;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {

    @Min(value = 1,message = "Amount To Charge must be between 1 and 10 JOD")
    @Max(value = 10,message = "Amount To Charge must be between 1 and 10 JOD")
    private Integer amountToCharge;
}
