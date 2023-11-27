package com.example.demo.DTO;

import com.example.demo.entity.File;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private String filePath;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private Long boardId;

    public File toEntity(){
        return File.builder()
                .filePath(filePath)
                .fileName(fileName)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }

    public static FileDto toFileDto(File file){
        return new FileDto(
                file.getFilePath(),
                file.getFileName(),
                file.getFileType(),
                file.getFileSize(),
                file.getBoard().getId());
    }
}
