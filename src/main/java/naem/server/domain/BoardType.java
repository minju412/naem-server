package naem.server.domain;

import static naem.server.exception.ErrorCode.*;

import com.fasterxml.jackson.annotation.JsonCreator;

import naem.server.exception.CustomException;

public enum BoardType {
    HOT, FREE, PROTECTOR, JOB, REHABILITATION, BENEFIT, TOGETHER;

    @JsonCreator
    public static BoardType from(String str) {
        String res = "";
        switch (str.toUpperCase()) {
            case "HOT":
                res = "HOT";
                break;
            case "FRE":
                res = "FREE";
                break;
            case "PRO":
                res = "PROTECTOR";
                break;
            case "JOB":
                res = "JOB";
                break;
            case "REH":
                res = "REHABILITATION";
                break;
            case "BEN":
                res = "BENEFIT";
                break;
            case "TOG":
                res = "TOGETHER";
                break;
            default:
                throw new CustomException(NO_MATCHING_ENUM_TYPE);
        }
        return BoardType.valueOf(res.toUpperCase());
    }
}
