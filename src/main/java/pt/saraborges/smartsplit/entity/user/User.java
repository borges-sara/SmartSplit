package pt.saraborges.smartsplit.entity.user;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;

import java.util.Date;

@Table(name = "users")
public class User extends BaseEntity {

    @Getter
    @Setter
    private String name;

    @Getter
    private Email email;

    private Password password;


    public User(
                String name,
                Email email,
                Password password,
                String createdBy,
                Date createdAt) {
        super(createdAt, createdBy);

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String attempt){
        return password.matches(attempt);
    }
}
