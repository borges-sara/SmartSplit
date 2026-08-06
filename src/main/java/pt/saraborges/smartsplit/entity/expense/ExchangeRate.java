package pt.saraborges.smartsplit.entity.expense;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "exchange_rates")
@AllArgsConstructor
@NoArgsConstructor
// This table is meant to cache exchange rate values instead of always calculating them
// when making expenses between different coins
public class ExchangeRate extends BaseEntity {
    @Getter
    @Setter
    @Column(length = 3)
    private String baseCurrency;

    @Getter
    @Setter
    @Column(length = 3)
    private String targetCurrency;

    @Getter
    @Setter
    private BigDecimal rate;
}
