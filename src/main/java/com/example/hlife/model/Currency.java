package com.example.hlife.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "R_CURRENCY")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 60)
    private String title;

    @Column(name = "CODE", nullable = false, length = 3)
    private String code;

    @Column(name = "VALUE", nullable = false, precision = 18, scale = 2)
    private BigDecimal value;

    @Column(name = "A_DATE", nullable = false)
    private LocalDate aDate;
}
