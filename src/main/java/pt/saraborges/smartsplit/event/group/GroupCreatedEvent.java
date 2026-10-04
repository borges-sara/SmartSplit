package pt.saraborges.smartsplit.event.group;

import java.time.Instant;

public record GroupCreatedEvent(
        Long groupId,
        String name,
        Long createdByUserId,
        Instant occurredAt
) {}
