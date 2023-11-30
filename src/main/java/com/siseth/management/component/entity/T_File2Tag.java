package com.siseth.management.component.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "file", name = "`T_File2Tag`")
@Where(clause = "`isActive`")
public class T_File2Tag extends BaseEntity {

    @JoinColumn(name="`fileId`")
    @ManyToOne
    private D_File file;

    @JoinColumn(name="`tagId`")
    @ManyToOne
    private D_Tag tag;

    @Column
    private String value;

    @Column(name = "`userId`")
    private String userId;

    @Column(name = "`isActive`")
    private Boolean isActive;

    @Column(name = "`type`")
    private String type;

    public T_File2Tag(D_File file, D_Tag tag, String userId, String sourceUser) {
        this.file = file;
        this.tag = tag;
        this.userId = userId;
        this.isActive = true;

        if (userId.equals(sourceUser)) {
            this.type = "OWNER";
        }else {
            this.type = "USER";
        }
    }

    public boolean isUserTag(String id){
        return id != null && id.equals(this.userId);
    }

    public enum TagType {
        OWNER,
        USER
    }
}
