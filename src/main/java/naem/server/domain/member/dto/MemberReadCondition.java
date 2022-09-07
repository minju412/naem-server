package naem.server.domain.member.dto;

import lombok.Data;

@Data
public class MemberReadCondition {

    private Boolean isDeleted;

    // 가입된 회원 조회
    public MemberReadCondition() {
        this.isDeleted = false;
    }
}
