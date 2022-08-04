package naem.server.exhandler;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 401 UNAUTHORIZED : Resource 를 찾을 수 없음 */
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 가입한 회원입니다"),
    CONFLICT_ID(HttpStatus.CONFLICT, "이미 사용중인 id 입니다"),
    CONFLICT_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 nickname 입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
