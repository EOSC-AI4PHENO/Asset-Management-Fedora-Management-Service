package com.siseth.management.component.repository;

import com.siseth.management.component.entity.D_File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface D_FileRepository extends JpaRepository<D_File, Long> {
    Boolean existsByDirectoryAndNameAndRealm(String dir, String name, String realm);

    Optional<D_File> findByDirectoryAndNameAndRealm(String dir, String name, String realm);

    Boolean existsByDirectoryAndRealm(String dir, String realm);

    Optional<D_File> findByIdAndRealm(Long id, String realm);


    List<D_File> findAllByIdIn(List<Long> ids);

    List<D_File> findAllByOwnerAndRealmAndTypeAndSourceId(String owner, String realm, String type, Long sourceId);

    Optional<D_File> findByNameAndType(String name, String type);

    @Query("SELECT MAX(f.originCreatedAt) " +
            "FROM D_File f " +
            "WHERE f.sourceId = :sourceId AND " +
            "      f.isActive = TRUE AND " +
            "      f.type = :type ")
    LocalDateTime getLastPhotoDateToSourceIdAndType(@Param("sourceId") Long sourceId,
                                                    @Param("type") String type);

    boolean existsBySourceIdAndTypeAndOriginCreatedAt(Long sourceId, String type, LocalDateTime originCreatedAt);


    @Query("SELECT DISTINCT f.sourceId " +
            "FROM D_File  f " +
            "WHERE f.isActive = TRUE and f.id IN :fileIds ")
    List<Long> getSourceIdsByFileIds(@Param("fileIds") List<Long> fileIds);


    @Query("SELECT DISTINCT f " +
            "FROM D_File f " +
            "LEFT JOIN T_File2Tag f2t on f2t.file = f AND f2t.isActive = TRUE " +
            "LEFT JOIN D_Tag t on f2t.tag = t AND t.isActive = TRUE " +
            "WHERE f.isActive = TRUE AND " +
            "      (0L IN :tagIds OR t.id IN :tagIds) AND " +
            "      f.sourceId = :sourceId AND " +
            "      f.type = :type AND " +
            "      CAST(f.originCreatedAt AS LocalDate) >= :dateFrom AND " +
            "      CAST(f.originCreatedAt AS LocalDate) <= :dateTo " +
            "GROUP BY f " +
            "HAVING (:tagLength = 1 OR COUNT(DISTINCT t.id) = :tagLength )")
    Page<D_File> getAllBySourceId(@Param("sourceId") Long sourceId,
                                  @Param("type") String type,
                                  @Param("dateFrom") LocalDate dateFrom,
                                  @Param("dateTo") LocalDate dateTo,
                                  @Param("tagIds") List<Long> tagIds,
                                  @Param("tagLength") Integer tagLength,
                                  Pageable pageable);

    @Query("SELECT f " +
            "FROM D_File f " +
            "WHERE f.isActive = TRUE AND " +
            "      f.sourceId = :sourceId AND " +
            "      f.type = :type AND " +
            "      CAST(f.originCreatedAt AS LocalDate) >= :dateFrom AND " +
            "      CAST(f.originCreatedAt AS LocalDate) <= :dateTo AND " +
            "      f.realm = :realm ")
    List<D_File> getAllBySourceId(@Param("sourceId") Long sourceId,
                                  @Param("type") String type,
                                  @Param("dateFrom") LocalDate dateFrom,
                                  @Param("dateTo") LocalDate dateTo,
                                  @Param("realm") String realm);
    @Query("SELECT DISTINCT f.sourceId " +
            "FROM D_File f " +
            "WHERE f.isActive = TRUE AND " +
            "      f.type = :type AND " +
            "      CAST(f.originCreatedAt AS LocalDate) >= :dateFrom AND " +
            "      CAST(f.originCreatedAt AS LocalDate) <= :dateTo AND " +
            "      f.realm = :realm ")
    List<Long> getSourceIds(@Param("type") String type,
                                  @Param("dateFrom") LocalDate dateFrom,
                                  @Param("dateTo") LocalDate dateTo,
                                  @Param("realm") String realm);


}
