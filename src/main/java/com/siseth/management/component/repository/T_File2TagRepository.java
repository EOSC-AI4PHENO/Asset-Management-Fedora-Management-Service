package com.siseth.management.component.repository;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.component.entity.D_Tag;
import com.siseth.management.component.entity.T_File2Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Repository
public interface T_File2TagRepository extends JpaRepository<T_File2Tag, Long> {

    Optional<T_File2Tag> findByTagId(Long tagId);

    List<T_File2Tag> findAllByFileIdAndUserIdAndType(Long fileId, String userId, String type);

    List<T_File2Tag> findAllByTagIdAndUserId(Long tagId, String userId);

    List<T_File2Tag> findAllByTagIdAndFileIdAndUserId(Long tagId, Long fileId, String userId);

    boolean existsByFileAndTagAndUserIdAndType(D_File file, D_Tag tag, String userId, String type);
}
