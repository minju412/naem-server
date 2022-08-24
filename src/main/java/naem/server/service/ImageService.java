package naem.server.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;

import naem.server.domain.post.Image;
import naem.server.domain.post.Post;
import naem.server.repository.ImageRepository;
import naem.server.service.util.S3Uploader;

@Service
public class ImageService extends S3Uploader {

    private final ImageRepository imageRepository;

    public ImageService(AmazonS3Client amazonS3Client, ImageRepository imageRepository) {
        super(amazonS3Client);
        this.imageRepository = imageRepository;
    }

    public String saveImage(MultipartFile multipartFile, String dirName, Post post) throws IOException {

        String uri = super.upload(multipartFile, dirName);

        Image img = new Image();
        img.setImgurl(uri);
        img.setPost(post);

        imageRepository.save(img);
        return uri;
    }
}
