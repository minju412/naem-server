package naem.server.web;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.fcm.dto.FcmReqDto;
import naem.server.service.FirebaseCloudMessageService;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/notification")
@Slf4j
public class NotificationController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/fcm")
    public ResponseEntity pushMessage(@RequestBody FcmReqDto fcmReqDto) throws IOException {
        System.out.println(fcmReqDto.getTargetToken() + " "
            + fcmReqDto.getTitle() + " " + fcmReqDto.getBody());

        firebaseCloudMessageService.sendMessageTo(
            fcmReqDto.getTargetToken(),
            fcmReqDto.getTitle(),
            fcmReqDto.getBody()
        );
        return ResponseEntity.ok().build();
    }
}
