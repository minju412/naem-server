package naem.server.domain.member.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String access_token;
    private String refresh_token;
    private Date accessTokenExp;
}
