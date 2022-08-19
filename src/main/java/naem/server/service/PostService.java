package naem.server.service;

import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;

public interface PostService {

    void save(PostSaveReqDto requestDto);

    PostResDto getPost(Long id);
}
