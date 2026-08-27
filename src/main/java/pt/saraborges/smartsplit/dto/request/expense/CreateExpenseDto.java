package pt.saraborges.smartsplit.dto.request.expense;

import pt.saraborges.smartsplit.entity.expense.SplitType;

import java.math.BigDecimal;
import java.util.List;

public record CreateExpenseDto(String description,
                               BigDecimal amount,
                               String paidByEmail,
                               List<String> dividedByEmails,
                               String groupName,
                               String currency,
                               SplitType splitType,
                               String category) {
}
