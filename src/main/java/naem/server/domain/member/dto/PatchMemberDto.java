package naem.server.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchMemberDto {

    private String nickname;
    private String introduction;
    private String filePath;
}
