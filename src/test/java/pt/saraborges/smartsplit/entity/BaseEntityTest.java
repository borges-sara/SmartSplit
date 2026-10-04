package pt.saraborges.smartsplit.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * BaseEntity is the shared audit-field base for every domain entity (see CLAUDE.md); its
 * @NonNull createdAt/createdBy are the one invariant it enforces itself, via Lombok's
 * generated null checks on the @RequiredArgsConstructor. Subclass constructors (e.g.
 * User's) call super(createdAt, createdBy) without re-validating, relying on this.
 */
class BaseEntityTest {

    @Test
    void constructor_setsCreatedAtAndCreatedBy() {
        Date createdAt = new Date();
        BaseEntity entity = new BaseEntity(createdAt, "system");

        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getCreatedBy()).isEqualTo("system");
    }

    @Test
    void constructor_rejectsNullCreatedAt() {
        assertThatThrownBy(() -> new BaseEntity(null, "system"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void constructor_rejectsNullCreatedBy() {
        assertThatThrownBy(() -> new BaseEntity(new Date(), null))
                .isInstanceOf(NullPointerException.class);
    }
}