package com.siseth.management.component.repository;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.component.entity.D_Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface D_TagRepository extends JpaRepository<D_Tag, Long> {
    Optional<D_Tag> findByIdAndRealm(Long id, String realm);

    List<D_Tag> findAllByOwnerAndRealm(String owner, String realm);

    List<D_Tag> findAllByOwner(String owner);

    List<D_Tag> findAllByIdIn(List<Long> ids);

    @Query("SELECT DISTINCT t " +
            "FROM D_Tag t " +
            "INNER JOIN T_File2Tag f2t on f2t.tag = t AND f2t.isActive = TRUE " +
            "INNER JOIN D_File f on f2t.file = f AND t.isActive = TRUE " +
            "WHERE f.sourceId = :sourceId AND " +
            "      f.isActive = TRUE AND " +
            "       f.realm = :realm AND " +
            "      (t.owner = '' OR f2t.type = 'OWNER' OR (f2t.type = 'USER' AND f2t.userId = :userId))")
    List<D_Tag> getAllTagToSource(Long sourceId, String userId, String realm);
}
