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
        private String introduction;
    }
}
