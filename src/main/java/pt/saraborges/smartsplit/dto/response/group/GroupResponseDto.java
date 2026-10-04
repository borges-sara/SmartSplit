package pt.saraborges.smartsplit.dto.response.group;

import pt.saraborges.smartsplit.dto.response.user.GetUserResponseDto;

import java.util.List;

public record GroupResponseDto(Long id,
                               String name,
                               String currency,
                               List<GetUserResponseDto> groupsMembers,
                               List<String> groupCategories) {
}
