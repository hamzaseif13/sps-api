package com.hope.sps.customer;

import com.hope.sps.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterRequest extends RegisterRequest {

    private String phoneNumber;
}
