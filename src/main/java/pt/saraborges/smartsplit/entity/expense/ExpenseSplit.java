package pt.saraborges.smartsplit.entity.expense;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.User;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_splits")
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSplit extends BaseEntity {

    @Getter
    @ManyToOne
    private Expense expense;

    @Getter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    private BigDecimal amountToPay;

    @Getter
    @Setter
    private boolean settled;
}
