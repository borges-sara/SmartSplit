package pt.saraborges.smartsplit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.saraborges.smartsplit.dto.request.expense.CreateExpenseDto;
import pt.saraborges.smartsplit.dto.response.expense.NewEqualExpenseResponse;
import pt.saraborges.smartsplit.service.ExpenseService;

@Controller
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @Operation(summary = "Create new equal-split expense")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "500", description = "Internal error.")
    })
    @PostMapping("/expenses/equalExpense")
    @ResponseBody
    public ResponseEntity<NewEqualExpenseResponse> createEqualExpense(CreateExpenseDto dto){
        var newExpense = expenseService.createEqualSplitExpense(dto);
        return ResponseEntity.ok(newExpense);
    }
}
