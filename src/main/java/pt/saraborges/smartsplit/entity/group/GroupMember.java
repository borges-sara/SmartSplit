package pt.saraborges.smartsplit.entity.group;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.User;

@Entity
@Table(name = "group_members")
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember extends BaseEntity {

    @Getter
    @Setter
    @ManyToOne
    private Group group;

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
