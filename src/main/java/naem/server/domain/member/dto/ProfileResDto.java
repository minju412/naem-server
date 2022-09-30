package naem.server.domain.member.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Tag;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberTag;

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
    private String recommenderCode;
    private List<Tag> tags = new ArrayList<>();

    public ProfileResDto(Member entity) {
        this.memberId = entity.getId();
        this.username = entity.getUsername();
        this.nickname = entity.getNickname();
        this.introduction = entity.getIntroduction();
        this.recommenderCode = entity.getRecommenderCode();
        for (MemberTag memberTag : entity.getMemberTags()) {
            this.tags.add(Tag.getTagFromMemberTag(memberTag));
        }
    }
}
