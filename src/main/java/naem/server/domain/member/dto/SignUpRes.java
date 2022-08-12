package naem.server.domain.member.dto;

import lombok.Getter;

@Getter
public class SignUpRes extends CoreRes {

    public SignUpRes(boolean ok, String error) {
        super(ok, error);
    }
}
