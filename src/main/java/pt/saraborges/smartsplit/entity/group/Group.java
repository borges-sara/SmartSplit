package pt.saraborges.smartsplit.entity.group;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseEntity {
    @Getter
    @Setter
    private String name;
}
