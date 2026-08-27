package pt.saraborges.smartsplit.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.request.group.GroupMembersDto;
import pt.saraborges.smartsplit.dto.request.group.NewGroupDto;
import pt.saraborges.smartsplit.dto.response.group.GroupResponseDto;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.event.group.GroupCreatedEvent;
import pt.saraborges.smartsplit.exception.ConflictException;
import pt.saraborges.smartsplit.exception.ResourceNotFoundException;
import pt.saraborges.smartsplit.mapper.group.GroupMapper;
import pt.saraborges.smartsplit.repository.GroupRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class GroupService {
    // Repositories
    private GroupRepository groupRepository;

    // Services
    private UserService userService;
    private ExpenseSplitService expenseSplitService;

    // Mappers
    private GroupMapper groupMapper;

    // Kafka
    private final ApplicationEventPublisher eventPublisher;

    public Optional<Group> getGroupByName(String name){
        return groupRepository.findGroupByName(name);
    }

    public List<GroupResponseDto> getAllGroups() {
        var groups = groupRepository.findAll();
        return groups
                .stream()
                .map(groupMapper :: groupToGroupResponseDto)
                .toList();
    }

    @Transactional
    public GroupResponseDto createGroup(NewGroupDto groupDto){
        var groupAdmin = userService
                .getUserByEmail(groupDto.adminEmail())
                .orElseThrow(()
                -> new ResourceNotFoundException("There is no user registered with the email '" + groupDto.adminEmail() + "'."));

        if(groupRepository.findByNameAndAdminEmail(groupDto.name(), Email.fromExisting(groupDto.adminEmail())).isPresent()){
            throw new ConflictException("The user '" + groupDto.adminEmail() + "' already has a group named '" + groupDto.name() + "'.");
        }

        var members = new ArrayList<User>();
        members.add(groupAdmin);

        var newGroup = new Group(
                groupDto.name(),
                groupAdmin,
                members,
                groupDto.currency(),
                Collections.emptyList(),
                groupDto.createdBy(),
                new Date());

        groupRepository.save(newGroup);

        // published Kafka event
        eventPublisher.publishEvent(new GroupCreatedEvent(
                newGroup.getId(), newGroup.getName(), groupAdmin.getId(), Instant.now()));

        return groupMapper.groupToGroupResponseDto(newGroup);
    }

    public GroupResponseDto addUsersToGroup(GroupMembersDto dto){
        if(dto == null)
            throw new NullPointerException("The dto cannot be null.");

        var group = validateGroupExistence(dto.groupName(), dto.adminEmail());

        var members = group.getGroupMembers();
        dto.membersToUpdate().forEach(u -> {
            var user = userService
                    .getUserByEmail(u)
                    .orElseThrow(()
                    -> new ResourceNotFoundException("There is no user registered with the email '" + u + "'."));

            if(!members.contains(user)){
                members.add(user);
            }
        });

        group.setGroupMembers(members);
        var updatedGroup = groupRepository.save(group);

        return groupMapper.groupToGroupResponseDto(updatedGroup);
    }

    // TODO: send a notification to members who couldn't be removed, alerting them of their pending balance
    public GroupResponseDto removeGroupMembers(GroupMembersDto dto){
        var group = validateGroupExistence(dto.groupName(), dto.adminEmail());

        var currentMemberEmails = group.getGroupMembers().stream()
                .map(u -> u.getEmail().getEmail())
                .toList();

        // only emails that are actually members can be requested for removal
        var requestedRemovals = dto.membersToUpdate().stream()
                .filter(currentMemberEmails::contains)
                .toList();

        // members with an unsettled split stay in the group until they settle up
        var emailsWithPendingSplits = expenseSplitService.getEmailsWithPendingSplits(group.getId(), requestedRemovals);

        var emailsToRemove = requestedRemovals.stream()
                .filter(email -> !emailsWithPendingSplits.contains(email))
                .collect(Collectors.toSet());

        var currentMembers = group.getGroupMembers();
        currentMembers.removeIf(u -> emailsToRemove.contains(u.getEmail().getEmail()));
        group.setGroupMembers(currentMembers);

        var updatedGroup = groupRepository.save(group);

        return groupMapper.groupToGroupResponseDto(updatedGroup);
    }

    private Group validateGroupExistence(String groupName, String adminEmail){
        return groupRepository
                .findByNameAndAdminEmail(groupName, Email.fromExisting(adminEmail))
                .orElseThrow(()
                        -> new ResourceNotFoundException("There is no group '" + groupName + "' registered by user '" + adminEmail + "'.")
                );
    }
}
