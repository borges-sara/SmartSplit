package pt.saraborges.smartsplit.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class BaseEntity{
    @Getter
    private final int id;

    @Getter
    private final Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    @Getter
    private final String createdBy;

    @Getter
    @Setter
    private String updatedBy;

    public BaseEntity(Builder builder){
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.createdBy = builder.createdBy;
        this.updatedBy = builder.updatedBy;
    }

    public static Builder builder(int id, Date createdAt){
        return new Builder(id, createdAt);
    }

    private static class Builder{
        private final int id;
        private final Date createdAt;
        private Date updatedAt;
        private String createdBy;
        private String updatedBy;

        public Builder(int id, Date createdAt){
            this.id = id;
            this.createdAt = createdAt;
        }

        public Builder withUpdatedAt(Date updatedAt){
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder withCreatedBy(String createdBy){
            this.createdBy = createdBy;
            return this;
        }

        public Builder withUpdatedBy(String updatedBy){
            this.updatedBy = updatedBy;
            return this;
        }
    }
}
