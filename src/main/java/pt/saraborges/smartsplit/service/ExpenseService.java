package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.request.expense.CreateExpenseDto;
import pt.saraborges.smartsplit.dto.response.expense.NewEqualExpenseResponse;
import pt.saraborges.smartsplit.entity.expense.Expense;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.exception.ResourceNotFoundException;
import pt.saraborges.smartsplit.mapper.expense.ExpenseMapper;
import pt.saraborges.smartsplit.repository.ExpenseRepository;
import java.util.List;


@Service
@AllArgsConstructor
public class ExpenseService {
    // Services
    protected UserService userService;
    protected GroupService groupService;
    protected ExpenseSplitService expenseSplitService;
    protected EqualExpenseCalculator equalExpenseCalculator;

    // Mappers
    private ExpenseMapper expenseMapper;

    // Repository
    private ExpenseRepository expenseRepository;

    public NewEqualExpenseResponse createEqualSplitExpense(CreateExpenseDto dto) {
        var group = groupService
                .getGroupByName(dto.groupName())
                .orElseThrow(()
                        -> new ResourceNotFoundException("There is no registered Group with the name '" + dto.groupName() + "'."));

        var paidByUser = userService
                .getUserByEmail(dto.paidByEmail())
                .orElseThrow(()
                -> new ResourceNotFoundException("There is no registered User with the email '" + dto.paidByEmail() + "'."));

        var dividedBy = getDividedByUsers(dto.dividedByEmails(), dto.paidByEmail());

        var expenseSplits = equalExpenseCalculator.calculate(dividedBy, dto.amount());

        var expense = new Expense(
                dto.description(),
                dto.amount(),
                paidByUser,
                group,
                dto.currency(),
                dto.category(),
                dto.splitType().name(),
                expenseSplits);

        // save splits
        expenseSplits.forEach(split -> split.setExpense(expense));
        expenseSplitService.saveSplits(expenseSplits);

        // save expense
        expenseRepository.save(expense);
        return expenseMapper.expenseToNewEqualExpenseDto(expense);
    }

    private void createDifferentSplitExpense(CreateExpenseDto dto){
        throw new NotImplementedException();
    }

    private List<User> getDividedByUsers(List<String> dividedByEmails, String paidByEmail){
        dividedByEmails.add(paidByEmail);

        var usersList = dividedByEmails
                .stream()
                .map(userEmail -> userService
                        .getUserByEmail(userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("There is no user registered with the email '" + userEmail + "'."))
                ).toList();

        return usersList;
    }
}
