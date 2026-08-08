package pt.saraborges.smartsplit.entity.expense;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.entity.user.User;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_records")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(force = true)
public class PaymentRecord extends BaseEntity {
    @Getter
    private final BigDecimal amount;

    @Getter
    @ManyToOne
    private Group group;

    @Getter
    @ManyToOne
    private User payee;

    @Getter
    @ManyToOne
    private User payer;
}
