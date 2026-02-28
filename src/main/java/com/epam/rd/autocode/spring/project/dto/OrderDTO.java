package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderDTO{
    @NotBlank(message = "Client email is required")
    private String clientEmail;

    @NotBlank(message = "Employee email is required")
    private String employeeEmail;

    private LocalDateTime orderDate;
    private BigDecimal price;

    @NotEmpty(message = "An order must contain at least one book")
    @Valid
    private List<BookItemDTO> bookItems;
}
