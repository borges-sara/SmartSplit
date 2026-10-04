package pt.saraborges.smartsplit.dto.response.user;

import java.util.Date;

public record GetUserResponseDto(String name,
                                 String email,
                                 String createdBy,
                                 Date createdAt) {
}
