package co.istad.ishop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.target}")
    private String uploadFolder;

    @Value("${file.media-url}")
    private String filePreviewUrl;

    public String uploadSingleFile(MultipartFile multipartFile) throws IOException {
        String extension = Objects.requireNonNull(multipartFile.getOriginalFilename())
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.') + 1);
        String newFileName = UUID.randomUUID() + "." + extension;
        File targetFile = new File(uploadFolder + File.separator + newFileName);

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(targetFile))) {
            stream.write(multipartFile.getBytes());
        }

        return filePreviewUrl + "/" + newFileName;
    }

    public List<String> uploadMultipleFiles(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadSingleFile(file));
        }
        return urls;
    }
}

