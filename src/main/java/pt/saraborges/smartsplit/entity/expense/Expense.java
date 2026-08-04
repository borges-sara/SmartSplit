package pt.saraborges.smartsplit.entity.expense;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends BaseEntity {
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User paidBy;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Getter
    @Setter
    private String currency; //TODO: replace by currency API

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Category category;

    @Getter
    @Setter
    @OneToMany(mappedBy = "expense")
    private List<ExpenseSplit> splits;
}
