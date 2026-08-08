package pt.saraborges.smartsplit.entity.group;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.User;

import java.util.List;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseEntity {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> groupMembers;

    @Getter
    @Setter
    @Column(length = 3) //ISO codes
    private  String baseCurrency;

    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "group_categories", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "category")
    private List<String> categories;
}
