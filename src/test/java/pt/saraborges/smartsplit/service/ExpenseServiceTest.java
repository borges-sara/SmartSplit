package pt.saraborges.smartsplit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.saraborges.smartsplit.dto.request.expense.CreateExpenseDto;
import pt.saraborges.smartsplit.dto.response.expense.NewEqualExpenseResponse;
import pt.saraborges.smartsplit.entity.expense.Expense;
import pt.saraborges.smartsplit.entity.expense.ExpenseSplit;
import pt.saraborges.smartsplit.entity.expense.SplitType;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import pt.saraborges.smartsplit.exception.ResourceNotFoundException;
import pt.saraborges.smartsplit.mapper.expense.ExpenseMapper;
import pt.saraborges.smartsplit.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the "Create Equal-Split Expense" workflow orchestration.
 * <p>
 * Like UserServiceTest, this proves ExpenseService calls its collaborators (GroupService,
 * UserService, EqualExpenseCalculator, ExpenseMapper, ExpenseRepository) correctly and in
 * the right order - not that group/user lookup or the split maths themselves are correct
 * (those are covered by their own tests). All collaborators are mocked.
 * <p>
 * One behaviour gets its own dedicated test rather than being folded into the happy path:
 * ExpenseSplit.expense must be set on every split before the Expense is saved. That
 * back-reference is the owning side of the bidirectional JPA association (see
 * ExpenseSplit.expense's @JoinColumn), so forgetting to set it means every expense_splits
 * row would be persisted with a NULL expense_id - a real bug this project hit and fixed.
 */
@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private GroupService groupService;

    @Mock
    private EqualExpenseCalculator equalExpenseCalculator;

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private ExpenseRepository expenseRepository;

    private ExpenseService expenseService;

    private User alice;
    private User bob;
    private Group tripGroup;
    private CreateExpenseDto validDto;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseService(userService, groupService, equalExpenseCalculator, expenseMapper, expenseRepository);

        alice = new User("Alice", Email.fromExisting("alice@example.com"),
                Password.fromHash("hashed-value"), "system", new Date());
        bob = new User("Bob", Email.fromExisting("bob@example.com"),
                Password.fromHash("hashed-value"), "system", new Date());

        tripGroup = new Group();
        tripGroup.setName("Trip");

        // dividedByEmails must be mutable: getDividedByUsers() appends paidByEmail to it.
        validDto = new CreateExpenseDto(
                "Dinner",
                new BigDecimal("30.00"),
                "alice@example.com",
                new ArrayList<>(List.of("bob@example.com")),
                "Trip",
                "EUR",
                SplitType.EQUAL_SPLIT,
                "Food");
    }

    @Test
    void createEqualSplitExpense_savesExpenseAndReturnsMappedResponse_whenGroupAndAllUsersExist() {
        List<ExpenseSplit> calculatedSplits = List.of(
                new ExpenseSplit(bob, null, new BigDecimal("15.00"), false),
                new ExpenseSplit(alice, null, new BigDecimal("15.00"), false));
        NewEqualExpenseResponse expectedResponse =
                new NewEqualExpenseResponse("Dinner", new BigDecimal("30.00"), List.of(), "Food", "EQUAL_SPLIT");

        when(groupService.getGroupByName("Trip")).thenReturn(Optional.of(tripGroup));
        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(alice));
        when(userService.getUserByEmail("bob@example.com")).thenReturn(Optional.of(bob));
        when(equalExpenseCalculator.calculate(any(), eq(validDto.amount()))).thenReturn(calculatedSplits);
        when(expenseMapper.expenseToNewEqualExpenseDto(any(Expense.class))).thenReturn(expectedResponse);

        NewEqualExpenseResponse result = expenseService.createEqualSplitExpense(validDto);

        assertThat(result).isEqualTo(expectedResponse);

        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(expenseCaptor.capture());
        Expense savedExpense = expenseCaptor.getValue();

        assertThat(savedExpense.getDescription()).isEqualTo("Dinner");
        assertThat(savedExpense.getAmount()).isEqualTo(new BigDecimal("30.00"));
        assertThat(savedExpense.getPaidBy()).isEqualTo(alice);
        assertThat(savedExpense.getGroup()).isEqualTo(tripGroup);
        assertThat(savedExpense.getCurrency()).isEqualTo("EUR");
        assertThat(savedExpense.getCategory()).isEqualTo("Food");
        assertThat(savedExpense.getSplitType()).isEqualTo("EQUAL_SPLIT");
        assertThat(savedExpense.getSplits()).isEqualTo(calculatedSplits);
    }

    @Test
    void createEqualSplitExpense_setsExpenseReferenceOnEverySplit_beforePersisting() {
        List<ExpenseSplit> calculatedSplits = List.of(
                new ExpenseSplit(bob, null, new BigDecimal("15.00"), false),
                new ExpenseSplit(alice, null, new BigDecimal("15.00"), false));

        when(groupService.getGroupByName("Trip")).thenReturn(Optional.of(tripGroup));
        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(alice));
        when(userService.getUserByEmail("bob@example.com")).thenReturn(Optional.of(bob));
        when(equalExpenseCalculator.calculate(any(), eq(validDto.amount()))).thenReturn(calculatedSplits);
        when(expenseMapper.expenseToNewEqualExpenseDto(any(Expense.class))).thenReturn(
                new NewEqualExpenseResponse("Dinner", new BigDecimal("30.00"), List.of(), "Food", "EQUAL_SPLIT"));

        expenseService.createEqualSplitExpense(validDto);

        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(expenseCaptor.capture());
        Expense savedExpense = expenseCaptor.getValue();

        assertThat(calculatedSplits)
                .as("every split must reference the parent Expense so its expense_id FK is populated on save")
                .allSatisfy(split -> assertThat(split.getExpense()).isSameAs(savedExpense));
    }

    @Test
    void createEqualSplitExpense_includesBothPaidByAndDividedByUsers_whenCalculatingSplits() {
        when(groupService.getGroupByName("Trip")).thenReturn(Optional.of(tripGroup));
        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(alice));
        when(userService.getUserByEmail("bob@example.com")).thenReturn(Optional.of(bob));
        when(equalExpenseCalculator.calculate(any(), eq(validDto.amount()))).thenReturn(List.of());
        when(expenseMapper.expenseToNewEqualExpenseDto(any(Expense.class))).thenReturn(
                new NewEqualExpenseResponse("Dinner", new BigDecimal("30.00"), List.of(), "Food", "EQUAL_SPLIT"));

        expenseService.createEqualSplitExpense(validDto);

        ArgumentCaptor<List<User>> usersCaptor = ArgumentCaptor.forClass(List.class);
        verify(equalExpenseCalculator).calculate(usersCaptor.capture(), eq(validDto.amount()));
        assertThat(usersCaptor.getValue()).containsExactlyInAnyOrder(alice, bob);
    }

    @Test
    void createEqualSplitExpense_throwsResourceNotFound_whenGroupDoesNotExist() {
        when(groupService.getGroupByName("Trip")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.createEqualSplitExpense(validDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Trip");

        verifyNoInteractions(userService, equalExpenseCalculator, expenseMapper, expenseRepository);
    }

    @Test
    void createEqualSplitExpense_throwsResourceNotFound_whenPaidByUserDoesNotExist() {
        when(groupService.getGroupByName("Trip")).thenReturn(Optional.of(tripGroup));
        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.createEqualSplitExpense(validDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("alice@example.com");

        verifyNoInteractions(equalExpenseCalculator, expenseMapper, expenseRepository);
    }

    @Test
    void createEqualSplitExpense_throwsResourceNotFound_whenADividedByUserDoesNotExist() {
        when(groupService.getGroupByName("Trip")).thenReturn(Optional.of(tripGroup));
        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(alice));
        when(userService.getUserByEmail("bob@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.createEqualSplitExpense(validDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("bob@example.com");

        verifyNoInteractions(equalExpenseCalculator, expenseMapper, expenseRepository);
    }
}
