package com.siseth.management.component.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "file", name = "`D_File`")
@Where(clause = "`isActive`")
public class D_File extends BaseEntity {

    @Column
    private String name;

    @Column
    private String directory;

    @Column(name = "`realm`")
    private String realm;

    @Column(name = "`originDirectory`")
    private String originDirectory;

    @Column(name = "`size`")
    private Long size;


    @Column
    private String type;

    @Column
    private String owner;

    @Column(name = "`isActive`")
    private Boolean isActive;

    @Column(name = "`originCreatedAt`")
    private LocalDateTime originCreatedAt;

    @Column(name = "`sourceId`")
    private Long sourceId;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @Where(clause = "`isActive`")
    private List<T_File2Tag> file2TagList;

    @Transient
    public String getPath() {
        return this.realm +
                (this.directory.startsWith("/") ? "" : "/") +
                this.directory +
                this.name;
    }

    public void delete() {
        this.isActive = false;
    }

    public D_File(String directory, String originDirectory, Long size,
                  Long sourceId, String name, String type, String realm, LocalDateTime originCreatedAt) {
        this.name = name;
        this.directory = directory;
        this.originDirectory = originDirectory;
        this.size = size;
        this.isActive = true;
        this.sourceId = sourceId;
        this.type = type;
        this.realm = realm;
        this.originCreatedAt = originCreatedAt;
    }

    public void checkOwner(String userId) {
        if(this.owner !=null && !this.owner.equals(userId)) {
            throw new RuntimeException("Not an owner!");
        }
    }
}
