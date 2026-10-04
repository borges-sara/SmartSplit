package pt.saraborges.smartsplit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.saraborges.smartsplit.entity.group.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findGroupByName(String name);
}
