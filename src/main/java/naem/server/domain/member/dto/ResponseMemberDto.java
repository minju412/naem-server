package naem.server.domain.member.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Comment;
import naem.server.domain.Post;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.MemberType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberDto {

    private MemberType memberType;
    private MemberRole role;
    private String phoneNumber;
    private String username;
    private String nickname;
    private List<Post> posts;
    private List<Comment> comments;

    public static ResponseMemberDto from(Member member) {

        if (member == null) {
            return null;
        }

        return ResponseMemberDto.builder()
            .memberType(member.getMemberType())
            .role(member.getRole())
            .phoneNumber(member.getPhoneNumber())
            .username(member.getUsername())
            .nickname(member.getNickname())
            // .authorityDtoSet(member.getAuthorities().stream()
            //     .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
            //     .collect(Collectors.toSet()))
            .build();
    }
}
