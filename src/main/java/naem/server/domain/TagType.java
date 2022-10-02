package naem.server.domain;

import static naem.server.exception.ErrorCode.*;

import com.fasterxml.jackson.annotation.JsonCreator;

import naem.server.exception.CustomException;

public enum TagType {
    MENTAL, INTELLECTUAL, RETARDED,
    VISUAL, DEAFNESS, AUTISTIC,
    SPEECH, BRAIN_LESION, KIDNEY,
    RESPIRATORY, FACIAL, URETHRAL,
    HEART, RIVER, EPILEPSY, ETC;

    @JsonCreator
    public static TagType from(String str) {
        String res = "";
        switch (str.toUpperCase()) {
            case "MEN":
                res = "MENTAL";
                break;
            case "INT":
                res = "INTELLECTUAL";
                break;
            case "RET":
                res = "RETARDED";
                break;
            case "VIS":
                res = "VISUAL";
                break;
            case "DEA":
                res = "DEAFNESS";
                break;
            case "AUT":
                res = "AUTISTIC";
                break;
            case "SPE":
                res = "SPEECH";
                break;
            case "BRA":
                res = "BRAIN_LESION";
                break;
            case "KID":
                res = "KIDNEY";
                break;
            case "RES":
                res = "RESPIRATORY";
                break;
            case "FAC":
                res = "FACIAL";
                break;
            case "URE":
                res = "URETHRAL";
                break;
            case "HEA":
                res = "HEART";
                break;
            case "RIV":
                res = "RIVER";
                break;
            case "EPI":
                res = "EPILEPSY";
                break;
            case "ETC":
                res = "ETC";
                break;
            default:
                throw new CustomException(NO_MATCHING_ENUM_TYPE);
        }
        return TagType.valueOf(res.toUpperCase());
    }
}
