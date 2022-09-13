package naem.server.domain.member.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.member.DisabledAuthImage;
import naem.server.domain.member.DisabledMemberInfo;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisabledMemberInfoDto {

    private String username;
    private List<String> imgUrls = new ArrayList<>();

    public DisabledMemberInfoDto(DisabledMemberInfo entity) {
        this.username = entity.getUsername();
        for (DisabledAuthImage image : entity.getImg()) {
            this.imgUrls.add(image.getImgUrl());
        }
    }
}
