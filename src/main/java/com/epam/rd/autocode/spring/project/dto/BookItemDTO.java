package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BookItemDTO {
    @NotNull(message = "Book selection is required")
    private String bookName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
