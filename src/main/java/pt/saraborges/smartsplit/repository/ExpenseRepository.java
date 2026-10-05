package pt.saraborges.smartsplit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.saraborges.smartsplit.entity.expense.Expense;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> getExpensesByGroup_Id(Long groupId);
}
