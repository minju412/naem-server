package naem.server.domain.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DisabledMemberAuthReq {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;
}
