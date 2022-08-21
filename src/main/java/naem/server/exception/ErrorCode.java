package naem.server.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "제약 조건을 위배한 요청입니다 (constraint violation)"),
    METHOD_ARG_NOT_VALID(HttpStatus.BAD_REQUEST, "제약 조건을 위배한 요청입니다 (method argument not valid)"),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "올바르지 않은 리프레시 토큰입니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다"),
    INVALID_CREDENTIAL(HttpStatus.BAD_REQUEST, "유효하지 않은 자격증명입니다"),
    TAG_LIST_SIZE_ERROR(HttpStatus.BAD_REQUEST, "최대 3개의 키워드를 선택할 수 있습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않음 */
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다"),

    /* 403 FORBIDDEN : 권한이 없음 */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, "이미 가입한 회원입니다"),
    CONFLICT_ID(HttpStatus.CONFLICT, "이미 사용중인 id 입니다"),
    CONFLICT_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 nickname 입니다"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    ACCESS_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "엑세스 토큰 오류입니다"),
    REFRESH_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "리프레시 토큰 오류입니다"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
