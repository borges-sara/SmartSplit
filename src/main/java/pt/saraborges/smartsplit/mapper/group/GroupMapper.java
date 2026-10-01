package pt.saraborges.smartsplit.mapper.group;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import pt.saraborges.smartsplit.dto.response.group.GroupResponseDto;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class GroupMapper {

    @Autowired
    protected UserMapper userMapper;

    public GroupResponseDto groupToGroupResponseDto(Group group){
        var users = group.getGroupMembers()
                .stream()
                .map(userMapper::userToGetUserDto)
                .toList();

        return new GroupResponseDto(group.getId(),
                group.getName(),
                group.getBaseCurrency(),
                users,
                group.getCategories());
    }
}
