package naem.server.service;

import java.util.List;

import naem.server.domain.post.Post;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;

public interface PostService {

    Post save(PostSaveReqDto requestDto);

    PostResDto getPost(Long id);

    List<PostResDto> getPostList();
}
