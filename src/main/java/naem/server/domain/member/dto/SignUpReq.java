package naem.server.domain.member.dto;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.MemberType;

@Getter
@ToString
@NoArgsConstructor
public class SignUpReq {

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-\\d{3,4}-\\d{4}$", message = "휴대폰 번호는 xxx-xxx-xxxx 혹은 xxx-xxxx-xxxx 형식을 사용하세요.")
    private String phoneNumber;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).{5,20}$", message = "아이디는 5~20자의 영문 소문자 및 숫자를 사용하세요.")
    @Size(min = 5, max = 20)
    @Column(unique = true)
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%*?&].{8,}$", message = "비밀번호는 8자 이상의 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    // @Size(min = 8, max = 16) // 암호화
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$", message = "닉네임은 10자 이내의 한글, 영문, 숫자를 사용하세요.")
    @Size(min = 1, max = 10)
    @Column(unique = true)
    private String nickname;

    @Builder
    public SignUpReq(MemberType memberType, String phoneNumber, String username, String password, String nickname) {
        this.memberType = memberType;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    /**
     * Transform to User Entity
     * @return User Entity
     */
    public Member toUserEntity() {
        return Member.builder()
            .memberType(this.getMemberType())
            .phoneNumber(this.getPhoneNumber())
            .username(this.getUsername())
            .password(this.getPassword())
            .nickname(this.getNickname())
            .build();
    }
}
