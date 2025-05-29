package com.bsuir.storageservice.service;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DropBoxService {
    private final DbxClientV2 dropboxClient;

    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = generateNewFileName(file.getOriginalFilename());
        try (InputStream in = file.getInputStream()) {
            FileMetadata metadata = dropboxClient.files().uploadBuilder(fileName)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);
            return metadata.getPathLower();
        }
    }
    private String generateNewFileName(String originalFileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        String extension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";

        return "/" + timestamp + "_" + originalFileName + extension;
    }

    public byte[] downloadFileByPath(String filePath) throws Exception {
        if (filePath == null) {
            throw new IllegalArgumentException("File ID not found.");
        }
        try (InputStream in = dropboxClient.files().download(filePath).getInputStream()) {
            return in.readAllBytes();
        }
    }
}
