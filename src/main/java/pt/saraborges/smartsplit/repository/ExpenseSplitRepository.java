package pt.saraborges.smartsplit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.saraborges.smartsplit.entity.expense.ExpenseSplit;

import java.util.List;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> getExpenseSplitsByUser_Id(Long userId);
}
