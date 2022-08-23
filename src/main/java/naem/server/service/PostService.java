package naem.server.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;

public interface PostService {

    void save(PostSaveReqDto requestDto);

    PostResDto getPost(Long id);

    Long getAuthorId(Long id);

    void delete(Long id);
}
