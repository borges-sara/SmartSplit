package pt.saraborges.smartsplit.dto.response.expense;

import java.math.BigDecimal;
import java.util.List;

public record NewEqualExpenseResponse(String description,
                                      BigDecimal amount,
                                      List<String> splits,
                                      String category,
                                      String splitType) {
}
