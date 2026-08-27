package pt.saraborges.smartsplit.service;

import pt.saraborges.smartsplit.entity.expense.ExpenseSplit;
import pt.saraborges.smartsplit.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseCalculator {
    List<ExpenseSplit> calculate(List<User> users, BigDecimal totalAmount);
}
