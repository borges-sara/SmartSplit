package pt.saraborges.smartsplit.user.entity;

import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.common.entity.BaseEntity;
import pt.saraborges.smartsplit.user.entity.valueObjects.Email;
import pt.saraborges.smartsplit.user.entity.valueObjects.Password;

import java.util.Date;


public class User extends BaseEntity {

    @Getter
    @Setter
    private String name;

    @Getter
    private Email email;

    private Password password;


    public User(int id,
                String name,
                Email email,
                Password password,
                String createdBy,
                Date createdAt) {
        super(id, createdAt, createdBy);

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String attempt){
        return password.matches(attempt);
    }
}
