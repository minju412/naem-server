package naem.server.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;

public interface PostService {

    void save(PostSaveReqDto requestDto);

    PostResDto getPost(Long id);

    void update(Long id, PostUpdateReqDto updateRequestDto);

    Long getAuthorId(Long id);

    void delete(Long id);
}
