package pt.saraborges.smartsplit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

    List<User> findAllByName(String name);

    User deleteDistinctByEmailAndPassword(Email email, Password password);

    boolean existsUserByEmail(String email);
}
