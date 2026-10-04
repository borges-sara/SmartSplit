package pt.saraborges.smartsplit.entity.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.User;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group extends BaseEntity {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private User groupAdmin;

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
    private String baseCurrency;

    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "group_categories", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "category")
    private List<String> categories;

    public Group(){}

    public Group(
            String name,
            User groupAdmin,
            List<User> groupMembers,
            String baseCurrency,
            List<String> categories,
            String createdBy,
            java.util.Date createdAt) {
        super(createdAt, createdBy);
        this.name = name;
        this.groupAdmin = groupAdmin;
        this.groupMembers = groupMembers;
        this.baseCurrency = baseCurrency;
        this.categories = categories;
    }
}
