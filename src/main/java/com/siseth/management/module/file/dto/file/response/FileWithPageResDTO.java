package com.siseth.management.module.file.dto.file.response;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.component.paggingSorting.PaginationResDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class FileWithPageResDTO {

     private PaginationResDTO page;

     private List<FileWithSourceResDTO> files;

     public FileWithPageResDTO(Page<D_File> filePage) {
          this.page = new PaginationResDTO(filePage);
          this.files =  filePage.get()
                                      .map(FileWithSourceResDTO::new)
                                      .collect(Collectors.toList());
     }

     public FileWithPageResDTO(Page<D_File> filePage, List<FileWithSourceResDTO> files) {
          this.page = new PaginationResDTO(filePage);
          this.files =  files;
     }

}
