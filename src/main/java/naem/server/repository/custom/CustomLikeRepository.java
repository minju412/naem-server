package naem.server.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;

public interface CustomLikeRepository {

    Slice<BriefPostInfoDto> getMyLikedPostScroll(Long cursorId, PostReadCondition condition, Pageable pageable);
}
