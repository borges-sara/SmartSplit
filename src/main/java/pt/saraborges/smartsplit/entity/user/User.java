package pt.saraborges.smartsplit.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.entity.BaseEntity;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.mapper.user.EmailMapper;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import pt.saraborges.smartsplit.mapper.user.PasswordMapper;

import java.util.Date;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Getter
    @Setter
    private String name;

    @Getter
    @Convert(converter = EmailMapper.class)
    @Column(nullable = false, unique = true)
    private Email email;

    @Convert(converter = PasswordMapper.class)
    @Column(nullable = false)
    private Password password;


    protected User() {}

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
