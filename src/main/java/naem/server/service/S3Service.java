package naem.server.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import naem.server.domain.post.Image;
import naem.server.domain.post.Post;

public interface S3Service {

    List<String> uploadImage(List<MultipartFile> multipartFile, String dirName, Post post);

    void deleteImageList(List<Image> images);

}
