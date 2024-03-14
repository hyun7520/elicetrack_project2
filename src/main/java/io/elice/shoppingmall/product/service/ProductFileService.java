package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductFileService {
    public List<Product> saveCategoryImages(List<MultipartFile> multipartFiles) {
        List<Product> fileList = new ArrayList<>();

        if (multipartFiles.isEmpty()) {
            return fileList;
        }

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                try {
                    String fileName = generateFileName(multipartFile);
                    String filePath = saveFile(multipartFile, fileName);

                    Product product = Product.builder()
                            .originalFileName(multipartFile.getOriginalFilename())
                            .storedFileName(filePath)
                            .fileSize(multipartFile.getSize())
                            .build();

                    fileList.add(product);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 파일 저장에 실패한 경우 예외 처리를 수행하거나 반환합니다.
                    throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
                }
            }
        }

        return fileList;
    }

    private String generateFileName(MultipartFile file) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = dateFormat.format(new Date());
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String randomString = UUID.randomUUID().toString().replaceAll("-", "");
        return dateString + "_" + randomString + extension;
    }

    private String saveFile(MultipartFile file, String fileName) throws IOException {
        String uploadDir = "C:\\Users\\spnamji\\Desktop\\dev";
        String filePath = uploadDir + File.separator + fileName;
        File dest = new File(filePath);

        dest.getParentFile().mkdirs();

        file.transferTo(dest);
        return filePath;
    }
}
