package pt.saraborges.smartsplit.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.Date;

@Builder
@MappedSuperclass
@NoArgsConstructor(force = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class BaseEntity{

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
