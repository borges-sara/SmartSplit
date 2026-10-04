package pt.saraborges.smartsplit.service;

import org.junit.jupiter.api.Test;
import pt.saraborges.smartsplit.entity.expense.ExpenseSplit;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the actual "equal split" maths.
 * <p>
 * EqualExpenseCalculator divides a BigDecimal amount into 2-decimal shares. Because
 * division rarely comes out even (e.g. 10.00 / 3), the calculator has to decide who
 * absorbs the leftover cents - these tests pin down that behaviour: whole cents only,
 * and the shares must always sum back up to the original total (nobody's money should
 * silently vanish or appear from rounding).
 */
class EqualExpenseCalculatorTest {

    private final EqualExpenseCalculator calculator = new EqualExpenseCalculator();

    private User userNamed(String name) {
        return new User(name, Email.fromExisting(name.toLowerCase() + "@example.com"),
                Password.fromHash("hashed-value"), "system", new Date());
    }

    @Test
    void calculate_splitsEqually_whenAmountDividesEvenlyAmongUsers() {
        User alice = userNamed("Alice");
        User bob = userNamed("Bob");

        List<ExpenseSplit> splits = calculator.calculate(List.of(alice, bob), new BigDecimal("30.00"));

        assertThat(splits).extracting(ExpenseSplit::getAmountToPay)
                .containsExactly(new BigDecimal("15.00"), new BigDecimal("15.00"));
    }

    @Test
    void calculate_givesTheFullAmountToASingleUser() {
        User alice = userNamed("Alice");

        List<ExpenseSplit> splits = calculator.calculate(List.of(alice), new BigDecimal("42.37"));

        assertThat(splits).hasSize(1);
        assertThat(splits.get(0).getAmountToPay()).isEqualTo(new BigDecimal("42.37"));
    }

    @Test
    void calculate_splitsAreUnsettledAndMappedToTheRequestingUsers_inOrder() {
        User alice = userNamed("Alice");
        User bob = userNamed("Bob");

        List<ExpenseSplit> splits = calculator.calculate(List.of(alice, bob), new BigDecimal("10.00"));

        assertThat(splits).extracting(ExpenseSplit::getUser).containsExactly(alice, bob);
        assertThat(splits).allSatisfy(split -> assertThat(split.isSettled()).isFalse());
    }

    @Test
    void calculate_distributesRoundingRemainderInWholeCents_soSplitsSumToTheOriginalTotal() {
        User alice = userNamed("Alice");
        User bob = userNamed("Bob");
        User carol = userNamed("Carol");
        BigDecimal totalAmount = new BigDecimal("10.00");

        List<ExpenseSplit> splits = calculator.calculate(List.of(alice, bob, carol), totalAmount);

        BigDecimal sumOfSplits = splits.stream()
                .map(ExpenseSplit::getAmountToPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(sumOfSplits)
                .as("splits must fully account for the original amount - no cent should be lost to rounding")
                .isEqualByComparingTo(totalAmount);
    }
}
