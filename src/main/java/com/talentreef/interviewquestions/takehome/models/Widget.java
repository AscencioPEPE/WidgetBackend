package com.talentreef.interviewquestions.takehome.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Table
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(toBuilder=true)
public class Widget {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(unique = true)
    private String name;

    @NotNull
    @Size(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
    @Column
    private String description;

    @NotNull
    @DecimalMin(value = "1.00", message = "Price must be at least 1")
    @DecimalMax(value = "20000.00", message = "Price must be less than or equal to 20,000")
    @Digits(integer = 5, fraction = 2, message = "Price must be a number with up to 2 decimal places")
    @Column(precision = 7, scale = 2)
    private BigDecimal price;
}
