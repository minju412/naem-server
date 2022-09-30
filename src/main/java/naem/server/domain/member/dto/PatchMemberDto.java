package naem.server.domain.member.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import naem.server.domain.Tag;

@Getter
@Setter
public class PatchMemberDto {

    private String nickname;
    private String introduction;
    private List<Tag> tag = null;
}
