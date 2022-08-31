package naem.server.service;

import naem.server.domain.comment.dto.CommentSaveDto;

public interface CommentService {

    void save(Long postId, CommentSaveDto commentSaveDto);
}
