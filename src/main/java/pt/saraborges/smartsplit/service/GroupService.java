package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.repository.GroupRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class GroupService {
    protected GroupRepository groupRepository;

    public Optional<Group> getGroupByName(String name){
        return groupRepository.findGroupByName(name);
    }
}
