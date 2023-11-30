package com.siseth.management.module.file.service;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.component.entity.D_Tag;
import com.siseth.management.component.entity.T_File2Tag;
import com.siseth.management.component.fedora.PathGenerator;
import com.siseth.management.component.image.ImageSize;
import com.siseth.management.component.image.ImageUtil;
import com.siseth.management.component.paggingSorting.SortingConverter;
import com.siseth.management.component.repository.D_FileRepository;
import com.siseth.management.component.repository.D_TagRepository;
import com.siseth.management.component.repository.T_File2TagRepository;
import com.siseth.management.module.fedora.api.FedoraFileExistsDTO;
import com.siseth.management.module.fedora.model.GetImage;
import com.siseth.management.module.fedora.model.UploadImage;
import com.siseth.management.module.feign.dataSource.DataSourceService;
import com.siseth.management.module.feign.dataSource.dto.SourceInternalShortResDTO;
import com.siseth.management.module.file.dto.file.response.FileShortResDTO;
import com.siseth.management.module.file.dto.file.response.FileWithPageResDTO;
import com.siseth.management.module.file.dto.file.response.FileWithSourceResDTO;
import com.siseth.management.module.file.dto.file.response.TagResDTO;
import com.siseth.management.module.internal.api.requests.AddTagToFileReqDTO;
import com.siseth.management.module.internal.api.requests.AddTagsToFilesReqDTO;
import com.siseth.management.module.internal.api.requests.CreateTagReqDTO;
import com.siseth.management.module.internal.api.requests.UpdateTagReqDTO;
import com.siseth.management.module.internal.api.response.CreateTagResDTO;
import com.siseth.management.module.socket.WebSocketContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final D_FileRepository fileRepository;

    private final D_TagRepository dTagRepository;

    private final T_File2TagRepository tFile2TagRepository;

    private final DataSourceService dataSourceService;

    public CreateTagResDTO createTag(CreateTagReqDTO dto, String userId, String realm) {
        D_Tag dTag = new D_Tag(dto, userId, realm);
        dTagRepository.save(dTag);
        return new CreateTagResDTO(dTag.getId(), dTag.getName());
    }
    public UpdateTagReqDTO editTag(UpdateTagReqDTO dto, String userId, String realm) {
        D_Tag dTag = dTagRepository.findById(dto.getId()).orElseThrow( () -> new EntityNotFoundException("Tag not found"));
        dTag.update(dto,userId,realm);
        dTagRepository.save(dTag);
        return dto;
    }
    public String removeTag(Long tagId, String id, String realm) {
        D_Tag dTag = dTagRepository.findById(tagId).orElseThrow( () -> new EntityNotFoundException("Tag not found"));

        List<T_File2Tag> tFile2Tags = tFile2TagRepository.findAllByTagIdAndUserId(tagId, id);
        tFile2Tags.forEach(x -> x.setIsActive(false));
        tFile2TagRepository.saveAll(tFile2Tags);

        dTag.setIsActive(false);
        dTagRepository.save(dTag);
        return "Tag removed";
    }

    public String removeTagFromFile(Long tagId, Long fileId, String id, String realm) {
        List<T_File2Tag> tFile2Tags = tFile2TagRepository.findAllByTagIdAndFileIdAndUserId(tagId, fileId, id);
        tFile2Tags.forEach(x -> x.setIsActive(false));
        tFile2TagRepository.saveAll(tFile2Tags);
        return "Tag removed";
    }

    public Map<String, List<TagResDTO>> getUserTags(String id, String realm) {
        List<TagResDTO> systemTags = dTagRepository.findAllByOwner("").stream().map(TagResDTO::new).collect(Collectors.toList());
        List<TagResDTO> userTags = dTagRepository.findAllByOwnerAndRealm(id, realm).stream().map(TagResDTO::new).collect(Collectors.toList());

        Map<String, List<TagResDTO>> allTags = new HashMap<>();
        allTags.put("userTags", userTags);
        allTags.put("systemTags", systemTags);

        return allTags;
    }
    public  List<TagResDTO> getSourceTag(Long sourceId, String userId, String realm) {
        return dTagRepository.getAllTagToSource(sourceId, userId, realm)
                .stream()
                .map(TagResDTO::new).collect(Collectors.toList());
    }

    public Map<String, List<TagResDTO>> getFileTags(Long fileId, String id, String realm) {
        List<T_File2Tag> tFile2TagsUser = tFile2TagRepository.findAllByFileIdAndUserIdAndType(fileId, id, "USER");
        List<T_File2Tag> tFile2TagsOwner = tFile2TagRepository.findAllByFileIdAndUserIdAndType(fileId, id, "OWNER");

        List<TagResDTO> userTags = tFile2TagsUser.stream().map(x -> new TagResDTO(x.getTag())).collect(Collectors.toList());
        List<TagResDTO> ownerTags = tFile2TagsOwner.stream().map(x -> new TagResDTO(x.getTag())).collect(Collectors.toList());
        List<TagResDTO> systemTags = dTagRepository.findAllByOwner("").stream().map(TagResDTO::new).collect(Collectors.toList());

        Map<String, List<TagResDTO>> allTags = new HashMap<>();
        allTags.put("userTags", userTags);
        allTags.put("ownerTags", ownerTags);
        allTags.put("systemTags", systemTags);

        return allTags;
    }

    public String addTagToFile(AddTagToFileReqDTO dto, String userId, String realm) {
        D_File dFile = fileRepository.findByIdAndRealm(dto.getFileId(), realm).orElseThrow( () -> new RuntimeException("File not found!"));
        D_Tag dTag = dTagRepository.findById(dto.getTagId()).orElseThrow( () -> new EntityNotFoundException("Tag not found"));

        SourceInternalShortResDTO source = dataSourceService.getSource(dFile.getSourceId());

        T_File2Tag tFile2Tag = new T_File2Tag(dFile,dTag,userId, source.getUserId());


        tFile2TagRepository.save(tFile2Tag);
        return "Tag to file of id: " + dTag.getId() + " added to the database";
    }


    public String addTagsToFiles(AddTagsToFilesReqDTO dto, String userId, String realm) {
        List<D_File> dFiles = fileRepository.findAllByIdIn(dto.getFileIds());
        List<D_Tag> dTags = dTagRepository.findAllByIdIn(dto.getTagIds());

        List<T_File2Tag> tFile2Tags = new ArrayList<>();

        Map<Long, String> sourceIdToOwner =
                fileRepository.getSourceIdsByFileIds(dto.getFileIds())
                        .stream()
                        .map(dataSourceService::getSource)
                        .collect(Collectors.toMap(SourceInternalShortResDTO::getId, SourceInternalShortResDTO::getUserId));

        dFiles.forEach( file -> {
            dTags.forEach( tag -> {
                tFile2Tags.add(new T_File2Tag(file,tag,userId, sourceIdToOwner.get(file.getSourceId())));
            });
        });

        tFile2Tags
                .stream()
                .filter( x ->
                 !tFile2TagRepository.existsByFileAndTagAndUserIdAndType(x.getFile(),x.getTag(), x.getUserId(), x.getType()))
                .forEach(tFile2TagRepository::save);
        return "Tags were added to the files";
    }
}
