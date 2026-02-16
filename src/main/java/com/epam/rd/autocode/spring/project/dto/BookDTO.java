package com.epam.rd.autocode.spring.project.dto;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BookDTO{
    @NotBlank(message = "Title is required")
    private String name;
    private String genre;
    private AgeGroup ageGroup;
    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;
    private LocalDate publicationDate;
    @NotBlank(message = "Title is required")
    private String author;
    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "1", message = "Price must be greater than zero")
    private Integer pages;
    private String characteristics;
    private String description;
    private Language language;
}
