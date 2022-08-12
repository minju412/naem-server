package naem.server.domain.member.dto;

import lombok.Builder;
import lombok.Data;

public class ProfileDto {

    @Data
    @Builder
    public static class ProfileReq {
        private String nickname;
    }

    @Data
    @Builder
    public static class ProfileRes {
        private String username;
        private String nickname;
    }

    // private MemberType memberType;
    // private MemberRole role;
    // private String username;
    // private String nickname;
    // private String introduction;
    // private List<Post> posts;
    // private List<Comment> comments;
    // private String filePath;
    //
    // public static ProfileDto from(Member member) {
    //
    //     if (member == null) {
    //         return null;
    //     }
    //
    //     return ProfileDto.builder()
    //         .memberType(member.getMemberType())
    //         .role(member.getRole())
    //         .username(member.getUsername())
    //         .nickname(member.getNickname())
    //         .introduction(member.getIntroduction())
    //         .filePath(member.getFilePath())
    //         // .authorityDtoSet(member.getAuthorities().stream()
    //         //     .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
    //         //     .collect(Collectors.toSet()))
    //         .build();
    // }
}
