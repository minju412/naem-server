package naem.server.domain.member.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberConflictCheckDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Column(unique = true)
    private String username;
}
