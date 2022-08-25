package naem.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import naem.server.domain.post.dto.BriefPostInfoDto;

public interface CustomPostRepository {

    Slice<BriefPostInfoDto> getBriefPostInfoScroll(Long cursorId, Pageable pageable);

}
