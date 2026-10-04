package pt.saraborges.smartsplit.entity.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.User;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {
    @Getter
    @ManyToOne
    private User recipient;

    @Getter
    private String message;

    @Getter
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
