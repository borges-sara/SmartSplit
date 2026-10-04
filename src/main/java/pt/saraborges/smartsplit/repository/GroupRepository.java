package pt.saraborges.smartsplit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findGroupByName(String name);
    @Query("""
    SELECT g from Group g
    WHERE (g.name = :name AND g.groupAdmin.email = :email)
    """)
    Optional<Group> findByNameAndAdminEmail(String name, Email email);
}
