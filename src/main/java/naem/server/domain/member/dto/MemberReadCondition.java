package naem.server.domain.member.dto;

import lombok.Data;
import naem.server.domain.member.MemberRole;

@Data
public class MemberReadCondition {

    private Boolean isDeleted;

    private MemberRole memberRole;

    // 가입된 회원 조회
    public MemberReadCondition(MemberRole memberRole) {
        this.isDeleted = false;
        this.memberRole = memberRole;
    }
}
