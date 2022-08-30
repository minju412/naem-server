package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.post.Image;
import naem.server.domain.post.Post;
import naem.server.exception.CustomException;
import naem.server.repository.ImageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    // 파일 업로드
    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFile, String dirName, Post post) {

        List<String> fileNameList = new ArrayList<>();
        List<String> imageUrl = new ArrayList<>();

        multipartFile.forEach(file -> {

            String fileName = createFileName(file.getOriginalFilename(), dirName);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            // s3에 업로드
            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            } catch (IOException e) {
                throw new CustomException(FILE_CAN_NOT_UPLOAD);
            }

            fileNameList.add(fileName);
            imageUrl.add(amazonS3Client.getUrl(bucket, fileName).toString());
        });

        try {
            storeInfoInDb(imageUrl, fileNameList, post); // db에 url 과 fileName 정보 저장
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNameList;
    }

    public void storeInfoInDb(List<String> imageUrls, List<String> fileNameList, Post post) throws IOException {
        for (int i = 0; i < imageUrls.size(); i++) {
            Image img = new Image();
            img.setImgUrl(imageUrls.get(i));
            img.setFileName(fileNameList.get(i));
            img.setPost(post);

            imageRepository.save(img);
        }
    }

    // 유니크한 파일의 이름을 생성하는 로직
    private String createFileName(String originalName, String dirName) {
        return dirName + "/" + UUID.randomUUID() + getFileExtension(originalName);
    }

    /**
     * 파일의 확장자명을 가져오는 로직
     * file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
     */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException(INVALID_FILE_ERROR);
        }
    }

    // 이미지 리스트 삭제
    @Override
    public void deleteImageList(List<Image> images) {
        for (Image image : images) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, image.getFileName()));
        }
    }

}
