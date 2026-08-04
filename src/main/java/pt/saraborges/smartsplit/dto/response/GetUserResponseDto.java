package pt.saraborges.smartsplit.dto.response;

import java.util.Date;

public record GetUserResponseDto(String name,
                                 String email,
                                 String createdBy,
                                 Date createdAt) {
}
