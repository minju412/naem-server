package naem.server.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.member.Member;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResDto {

    private Long memberId;
    private String username;
    private String nickname;
    private String introduction;

    public ProfileResDto(Member entity) {
        this.memberId = entity.getId();
        this.username = entity.getUsername();
        this.nickname = entity.getNickname();
        this.introduction = entity.getIntroduction();
    }
}
