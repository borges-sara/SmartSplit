package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.request.expense.CreateExpenseDto;
import pt.saraborges.smartsplit.dto.response.expense.NewEqualExpenseResponse;
import pt.saraborges.smartsplit.entity.expense.Expense;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.exception.ResourceNotFoundException;
import pt.saraborges.smartsplit.mapper.expense.ExpenseMapper;
import pt.saraborges.smartsplit.repository.ExpenseRepository;
import pt.saraborges.smartsplit.repository.GroupRepository;
import java.util.List;


@Service
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseService {
    // Services
    protected UserService userService;
    protected GroupService groupService;
    protected EqualExpenseCalculator equalExpenseCalculator;

    // Mappers
    private ExpenseMapper expenseMapper;

    // Repository
    private ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(GroupRepository groupRepository, ExpenseRepository expenseRepository, GroupService groupService) {
        this.expenseRepository = expenseRepository;
        this.groupService = groupService;
    }

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

        expenseSplits.forEach(split -> split.setExpense(expense));
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
