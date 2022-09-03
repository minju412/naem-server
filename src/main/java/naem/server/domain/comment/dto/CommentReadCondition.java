package naem.server.domain.comment.dto;

import lombok.Data;
import naem.server.domain.member.Member;

@Data
public class CommentReadCondition {

    private Member member; // 작성자 아이디

    private Boolean isDeleted;

    // 내가 쓴 댓글 조회
    public CommentReadCondition(Member member) {
        this.member = member;
        this.isDeleted = false;
    }
}
