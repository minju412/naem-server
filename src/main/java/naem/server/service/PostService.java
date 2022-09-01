package naem.server.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.DetailedPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;

public interface PostService {

    void checkPrivileges(long postId, UserDetails userDetails);

    void save(Post post);

    Post save(PostSaveReqDto requestDto);

    DetailedPostInfoDto getDetailedPostInfoDto(Long id);

    Post getPost(Long id);

    Slice<BriefPostInfoDto> getPostList(Long cursor, PostReadCondition condition, Pageable pageRequest);

    void update(Long id, PostUpdateReqDto updateRequestDto);

    Long getAuthorId(Long id);

    Post delete(Long id);
}
