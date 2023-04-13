package com.example.hlife.service;

import com.example.hlife.model.Currency;
import com.example.hlife.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public int saveCurrencyData(List<Currency> currencies) {
        currencyRepository.saveAll(currencies);
        return currencies.size();
    }

    public List<Currency> findByDate(LocalDate date) {
        return currencyRepository.findByaDate(date);
    }

    public List<Currency> findByDateAndCode(LocalDate date, String code) {
        return currencyRepository.findByaDateAndCode(date, code);
    }
}
