package pt.saraborges.smartsplit.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.entity.expense.ExpenseSplit;
import pt.saraborges.smartsplit.entity.user.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class EqualExpenseCalculator implements ExpenseCalculator{
    private static final int SCALE = 2;    // decimal numbers
    private static final BigDecimal SMALLEST_UNIT = BigDecimal.ONE.scaleByPowerOfTen(-SCALE);

    @Override
    public List<ExpenseSplit> calculate(List<User> users, BigDecimal totalAmount) {
        var totalSplits = BigDecimal.valueOf(users.size());
        var baseAmount = totalAmount.divide(totalSplits, SCALE, RoundingMode.DOWN);
        var remainder = totalAmount.subtract(baseAmount.multiply(totalAmount));
        int remainderUnits = remainder.divide(SMALLEST_UNIT).intValue();

        var splits = new ArrayList<ExpenseSplit>();

        for(int i = 0; i < users.size(); i++) {
            var amountToPay =
                    (i < remainderUnits) ?
                    baseAmount.add(SMALLEST_UNIT) :
                    baseAmount;

            splits.add(new ExpenseSplit(users.get(i), null, amountToPay, false)); // expense set later, once the parent Expense exists
        }

        return splits;
    }
}
