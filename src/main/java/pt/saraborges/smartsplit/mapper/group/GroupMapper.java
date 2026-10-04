package pt.saraborges.smartsplit.mapper.group;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import pt.saraborges.smartsplit.dto.response.group.GroupResponseDto;
import pt.saraborges.smartsplit.entity.group.Group;
import pt.saraborges.smartsplit.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface GroupMapper {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    default GroupResponseDto groupToGroupResponseDto(Group group){
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
