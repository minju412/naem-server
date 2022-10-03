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
        try {
            return TagType.valueOf(str.toUpperCase());
        } catch (Exception ex) {
            throw new CustomException(NO_MATCHING_ENUM_TYPE);
        }
    }
}
