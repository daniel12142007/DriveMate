package com.driver.driver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DriverRequest(
        @NotBlank(message = "Name must not be blank")
        @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "Name must only contain letters (Latin or Cyrillic)")
        String name,

        @NotBlank(message = "Car number must not be blank")
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Car number must only contain uppercase letters and digits")
        String carNumber,

        @NotBlank(message = "Phone number must not be blank")
        @Pattern(regexp = "^\\+\\d{3}\\s\\d{3}\\s\\d{3}\\s\\d{3}$",
                message = "The phone number must be in the format: +XXX XXX XXX XXX, where X is a digit")
        String phoneNumber,

        @NotBlank(message = "Car model must not be blank")
        String carModel
) {
}