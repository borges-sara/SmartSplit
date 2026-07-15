package pt.saraborges.smartsplit.common.entity;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseEntity{

    @Getter
    private final int id;

    @Getter
    @NonNull
    private final Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    @Getter
    @NonNull
    private final String createdBy;

    @Getter
    @Setter
    private String updatedBy;
}
