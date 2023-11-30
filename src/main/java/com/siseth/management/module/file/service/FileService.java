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
import com.siseth.management.component.zip.ZipUtil;
import com.siseth.management.module.fedora.api.FedoraFileExistsDTO;
import com.siseth.management.module.fedora.model.CreateContainer;
import com.siseth.management.module.fedora.model.DeleteImage;
import com.siseth.management.module.fedora.model.GetImage;
import com.siseth.management.module.fedora.model.UploadImage;
import com.siseth.management.module.feign.dataSource.DataSourceService;
import com.siseth.management.module.feign.dataSource.dto.SourceInternalShortResDTO;
import com.siseth.management.module.file.dto.file.response.*;
import com.siseth.management.module.internal.api.requests.AddTagToFileReqDTO;
import com.siseth.management.module.internal.api.requests.CreateContainerReqDTO;
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
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final D_FileRepository fileRepository;

    private final D_TagRepository dTagRepository;

    private final T_File2TagRepository tFile2TagRepository;



    private final DataSourceService dataSourceService;
    private final WebSocketContentService webSocketContentService;

    private ReentrantLock semaphoreImageUpload = new ReentrantLock();

    public LocalDateTime getLastPhotoDate(Long sourceId) {
        return fileRepository.getLastPhotoDateToSourceIdAndType(sourceId, "image");
    }

    public Map<LocalDateTime, Boolean> checkTimesToSource(Long sourceId, List<LocalDateTime> times) {
        return times.stream()
                .distinct()
                .collect(Collectors.toMap(x -> x, x-> fileRepository.existsBySourceIdAndTypeAndOriginCreatedAt(sourceId, "image", x)));
    }

    public byte[] getPublicImage(String path) {
        return new GetImage("public/" + path).getResponse();
    }


    public List<FileShortResDTO> testWebSocket() {
        //TODO test endpoint to WS
        List<D_File> files = fileRepository.findAllByIdIn(List.of(3001L, 3000L, 2999L, 2998L));
        String key = "RANDOM-KEY-TEST";
        webSocketContentService.sendContent(key, files);
        return files.stream().map(FileShortResDTO::new).collect(Collectors.toList());
    }



    public File zipFiles(List<Long> ids) {
        List<D_File> files = fileRepository.findAllByIdIn(ids);
        return new ZipUtil().zipFiles(files);
    }

    public FileWithPageResDTO getFileImageToSource(Long sourceId,
                                                      String _from,
                                                      String _to,
                                                      String imageSize,
                                                      boolean previewPhotoOnly,
                                                      Integer page,
                                                      Integer size,
                                                      String sorting,
                                                      String userId,
                                                      String tagIds) {

        Sort sort = SortingConverter.convert(sorting);
        LocalDate from = Optional.ofNullable(_from).map(LocalDate::parse).orElse(LocalDate.of(1900,1,1));
        LocalDate to = Optional.ofNullable(_to).map(LocalDate::parse).orElse(LocalDate.of(2100,1,1));
        List<Long> tagIdList = Arrays.stream(tagIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<D_File> filePage = fileRepository.getAllBySourceId(sourceId, "image", from, to, tagIdList, tagIdList.size(), pageable);

        Integer imgSize = ImageSize.getSize(imageSize);

        String content = previewPhotoOnly && filePage.getTotalElements() > 0 ?
                                ImageUtil.getBaseWithConvert(new GetImage(filePage.getContent().get(0).getPath()).getResponse(), imgSize, 0) :
                                null;

        List<FileWithSourceResDTO> fileShortResDTOS =
                filePage.stream()
                        .map(x -> new FileWithSourceResDTO(x,
                                previewPhotoOnly ?
                                        content :
                                        ImageUtil.getBaseWithConvert(new GetImage(x.getPath()).getResponse(), imgSize, 0), userId) )
                        .collect(Collectors.toList());

        return new FileWithPageResDTO(filePage, fileShortResDTOS);
    }
    public FileShortResDTO getFileById(Long imgId) {
        Optional<D_File> optionalDFile = fileRepository.findById(imgId);
        return optionalDFile.map(FileShortResDTO::new).orElse(new FileShortResDTO());
    }

    public List<FileShortResDTO> getFileImageToSource(Long sourceId,
                                                      LocalDate from,
                                                      LocalDate to,
                                                      String realm) {
        return fileRepository.getAllBySourceId(sourceId, "image", from, to, realm)
                .stream()
                .map(FileShortResDTO::new)
                .collect(Collectors.toList());
    }

    public void _createContainer(String dir, String realm){
        String[] dirs = dir.split("/");
        String strDirs = realm;
        for (int i = 0; i < dirs.length; i++) {
            createContainer(new CreateContainerReqDTO(strDirs, dirs[i]));
            strDirs += "/" + dirs[i];
        }


    }

    public String createContainer(CreateContainerReqDTO dto) {
        return new CreateContainer(dto).getResponse();
    }


    public Boolean checkImageExists(String originDirectory,
                                    Long sourceId,
                                    String name,
                                    String type,
                                    String realm) {
        String directory = PathGenerator.generate(type, originDirectory, sourceId, null );
        return fileRepository.findByDirectoryAndNameAndRealm(directory, name, realm).isPresent();
    }
    public FedoraFileExistsDTO uploadImage(MultipartFile file,
                                           String originDirectory,
                                           String userId,
                                           Long sourceId,
                                           String name,
                                           String type,
                                           String realm,
                                           String originCreatedAt) {
        semaphoreImageUpload.lock();

        try {
            String directory = PathGenerator.generate(type, originDirectory, sourceId, null);

            if(type.equals("image")) {
                boolean correctFile = ImageUtil.checkCorrect(file);
                if(!correctFile)
                    return new FedoraFileExistsDTO(null, true);
            }
            FedoraFileExistsDTO response =
                    fileRepository.findByDirectoryAndNameAndRealm(directory, name, realm)
                            .map(x -> new FedoraFileExistsDTO(x.getId(), true))
                            .orElse(null);
            if (response != null)
                return response;

            if (!fileRepository.existsByDirectoryAndRealm(directory, realm)) {
                _createContainer(directory, realm);
            }

            D_File dFile = new D_File(directory,
                    originDirectory,
                    file.getSize(),
                    sourceId,
                    name,
                    type,
                    realm,
                    Optional.ofNullable(originCreatedAt)
                            .map(LocalDateTime::parse)
                            .orElse(LocalDateTime.now()));

            dFile.setOwner(userId);

            log.info("Added new file " + new UploadImage(dFile, file, realm).getResponse());

            dFile = fileRepository.save(dFile);

            return new FedoraFileExistsDTO(dFile.getId(), false);

        } finally {
            semaphoreImageUpload.unlock();
        }

    }


    public FileContentResDTO getImageBase64(Long fileId) {
        D_File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return new FileContentResDTO(file, ImageUtil.convert(getImage(fileId)));
    }

    public byte[] getPdf(String name) {
        D_File file = fileRepository.findByNameAndType(name, "pdf")
                .orElseThrow(() -> new RuntimeException("File not found"));
        return new GetImage(file.getPath()).getResponse();
    }
    public byte[] getImage(Long fileId) {
        D_File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return new GetImage(file.getPath()).getResponse();


    }

    public byte[] getImage(String path) {
        return new GetImage(path).getResponse();
    }

    public void deleteFiles(List<Long> ids, String userId, String roles, String realm) {
        List<D_File> files = fileRepository.findAllByIdIn(ids);

        files.forEach(x -> {
            if(!roles.contains("Admin"))
                x.checkOwner(userId);
            x.delete();
        });
        fileRepository.saveAll(files);


        files.forEach(file -> new DeleteImage(file.getPath()).getResponse());


    }

    public void deleteImagesFromSource(Long sourceId, LocalDate from, LocalDate to, String userId, String roles, String realm) {
        log.info("Delete files in source {} by user {} in realm {}", sourceId, userId, realm);
        List<D_File> files = fileRepository.getAllBySourceId(sourceId, "image", from, to, realm);
        for (D_File file : files) {
            if(!roles.contains("Admin"))
                file.checkOwner(userId);
            file.delete();
            fileRepository.save(file);
            new DeleteImage(file.getPath()).getResponse();
        }
    }


    public Map<Long, String> map(List<SourceInternalShortResDTO> dtos) {
        return dtos.stream()
                .collect(Collectors.toMap(SourceInternalShortResDTO::getId, SourceInternalShortResDTO::getUserId));
    }

    public String getPreviewPhoto(Long imageId) {
        D_File file = fileRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        Integer imgSize = ImageSize.getSmallSize();
        String previewPhotoPath = file.getPath();
        return ImageUtil.getBaseWithConvert(new GetImage(previewPhotoPath).getResponse(), imgSize, 0);
    }


    public FilesSizeDTO getSizeByOwner(String userId, String realm, Long sourceId) {
        List<D_File> dFiles = fileRepository.findAllByOwnerAndRealmAndTypeAndSourceId(userId, realm, "image", sourceId);

        return new FilesSizeDTO((long) dFiles.size(), dFiles.stream().mapToLong(D_File::getSize).sum());
    }

    public List<Long> getSourcesIds(String realm, LocalDate dateFrom, LocalDate dateTo) {
        return fileRepository.getSourceIds("image", dateFrom, dateTo, realm);
    }

}
