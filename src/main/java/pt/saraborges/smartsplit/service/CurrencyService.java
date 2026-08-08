package pt.saraborges.smartsplit.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.response.CurrencyResponseDto;
import pt.saraborges.smartsplit.exception.ServiceUnavailableException;
import tools.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyService {
    @Getter
    private Map<String, String> currencies;

    @Value("${currency.file.path}")
    private String currencyFilePath;

    @PostConstruct
    public void registerCurrencies() {
        var file = new File(currencyFilePath);

        if(!file.exists())
            throw new ServiceUnavailableException("The currencies file does not exist.");

        this.currencies = new HashMap<>();
        var currencies = new ObjectMapper().readValue(file, CurrencyResponseDto[].class);

        for(var dto : currencies){
            this.currencies.put(dto.code(), dto.name());
        }
    }
}
