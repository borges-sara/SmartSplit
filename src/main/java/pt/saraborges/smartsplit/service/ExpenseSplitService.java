package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.entity.expense.Expense;
import pt.saraborges.smartsplit.entity.expense.ExpenseSplit;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.repository.ExpenseRepository;
import pt.saraborges.smartsplit.repository.ExpenseSplitRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseSplitService {
    // Repositories
    private ExpenseSplitRepository expenseSplitRepository;
    private ExpenseRepository expenseRepository;

    public void saveSplits(List<ExpenseSplit> expenseSplits){
        expenseSplitRepository.saveAll(expenseSplits);
    }

    public List<ExpenseSplit> getPendingSplitsByUser(User user){
        return expenseSplitRepository
                .getExpenseSplitsByUser_Id(user.getId())
                .stream()
                .filter(s -> !s.isSettled())
                .toList();
    }

    // returns the subset of candidateEmails that still have an unsettled split among the given expenses
    public List<String> filterEmailsWithPendingSplits(List<Expense> expenses, List<String> candidateEmails){
        return expenses.stream()
                .flatMap(expense -> expense.getSplits().stream())
                .filter(split -> !split.isSettled())
                .map(split -> split.getUser().getEmail().getEmail())
                .distinct()
                .filter(candidateEmails::contains)
                .toList();
    }

    public List<String> getEmailsWithPendingSplits(Long groupId, List<String> candidateEmails){
        var expenses = expenseRepository.getExpensesByGroup_Id(groupId);
        return filterEmailsWithPendingSplits(expenses, candidateEmails);
    }
}
