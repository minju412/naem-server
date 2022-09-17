package naem.server.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import naem.server.domain.member.DisabledAuthImage;
import naem.server.domain.member.DisabledMemberInfo;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostImage;

public interface S3Service {

    List<String> uploadImage(List<MultipartFile> multipartFile, String dirName, Post post);

    List<String> uploadDisabledAuthImage(List<MultipartFile> multipartFile, String dirName, DisabledMemberInfo disabledMemberInfo);

    void deletePostImages(List<PostImage> images);

    void deleteDisabledAuthImages(List<DisabledAuthImage> images);
}
