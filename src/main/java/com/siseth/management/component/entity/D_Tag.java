package com.siseth.management.component.entity;

import com.siseth.management.module.internal.api.requests.CreateTagReqDTO;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "file", name = "`D_Tag`")
@Where(clause = "`isActive`")
public class D_Tag extends BaseEntity {

    @Column(name = "`name`")
    private String name;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "`realm`")
    private String realm;

    @Column(name = "`isPublic`")
    private Boolean isPublic;

    @Column(name = "`isRequired`")
    private Boolean isRequired;

    @Column
    private String owner;

    @Column
    private String type;

    @Column(name = "`isActive`")
    private Boolean isActive;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    @Where(clause = "`isActive`")
    private List<T_File2Tag> file2TagList;

    public D_Tag(CreateTagReqDTO dto, String userId, String realm) {
        this.name = dto.getName();
        this.realm = realm;
        this.isPublic = true;
        this.isRequired = true;
        this.owner = userId;
        this.isActive = true;
    }

    public void update(CreateTagReqDTO dto, String userId, String realm) {
        this.name = dto.getName();
        this.realm = realm;
        this.isPublic = true;
        this.isRequired = true;
        this.owner = userId;
        this.isActive = true;
    }
}
