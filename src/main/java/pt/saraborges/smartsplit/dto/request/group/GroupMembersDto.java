package pt.saraborges.smartsplit.dto.request.group;

import java.util.List;

public record GroupMembersDto(String groupName,
                              String adminEmail,
                              List<String> membersToUpdate) {
}
