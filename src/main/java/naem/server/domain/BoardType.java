package naem.server.domain;

import static naem.server.exception.ErrorCode.*;

import com.fasterxml.jackson.annotation.JsonCreator;

import naem.server.exception.CustomException;

public enum BoardType {
    HOT, FREE, PROTECTOR, JOB, REHABILITATION, BENEFIT, TOGETHER;

    @JsonCreator
    public static BoardType from(String str) {
        try {
            return BoardType.valueOf(str.toUpperCase());
        } catch (Exception ex) {
            throw new CustomException(NO_MATCHING_ENUM_TYPE);
        }
    }
}
