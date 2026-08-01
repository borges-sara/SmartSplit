package pt.saraborges.smartsplit.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.Date;

@Builder
@MappedSuperclass
public class BaseEntity{

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @NonNull
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    @Getter
    @NonNull
    private String createdBy;

    @Getter
    @Setter
    private String updatedBy;


    protected  BaseEntity(){}

    public BaseEntity(Date createdAt,
                      String createdBy){
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public BaseEntity(Date createdAt,
                      String createdBy,
                      Date updatedAt,
                      String updatedBy){
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    protected BaseEntity(Long id,
                          Date createdAt,
                          Date updatedAt,
                          String createdBy,
                          String updatedBy){
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
