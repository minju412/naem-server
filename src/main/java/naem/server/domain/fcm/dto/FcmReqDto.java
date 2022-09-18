package naem.server.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FcmReqDto {

    private String targetToken;
    private String title;
    private String body;
}
