package naem.server.domain.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberWithdrawDto {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String checkPassword;
}
