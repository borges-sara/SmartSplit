package pt.saraborges.smartsplit.mapper.expense;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import pt.saraborges.smartsplit.dto.response.expense.NewEqualExpenseResponse;
import pt.saraborges.smartsplit.entity.expense.Expense;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ExpenseMapper {
    public NewEqualExpenseResponse expenseToNewEqualExpenseDto(Expense expense){
        var splitDtos =
                expense
                .getSplits()
                .stream()
                .map(x -> x.toString())
                .toList();

        return new NewEqualExpenseResponse(
                expense.getDescription(),
                expense.getAmount(),
                splitDtos,
                expense.getCategory(),
                expense.getSplitType());
    }
}
