package naem.server.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;

public interface PostService {

    Post save(PostSaveReqDto requestDto);

    PostResDto getPost(Long id);

    Slice<BriefPostInfoDto> getPostList(Long cursor, PostReadCondition condition, Pageable pageRequest);

    void update(Long id, PostUpdateReqDto updateRequestDto, UserDetails userDetails);

    Long getAuthorId(Long id);

    void delete(Long id, UserDetails userDetails);
}
