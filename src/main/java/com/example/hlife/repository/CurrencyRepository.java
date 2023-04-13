package com.example.hlife.repository;

import com.example.hlife.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findByaDate(LocalDate aDate);
    List<Currency> findByaDateAndCode(LocalDate aDate, String code);
}
