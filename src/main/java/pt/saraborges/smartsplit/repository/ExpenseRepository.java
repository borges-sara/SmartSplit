package pt.saraborges.smartsplit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.saraborges.smartsplit.entity.expense.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
