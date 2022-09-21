package naem.server.web;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.fcm.dto.FcmReqDto;
import naem.server.domain.member.DisabledMemberInfo;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.dto.DisabledMemberInfoDto;
import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.ProfileResDto;
import naem.server.service.FirebaseCloudMessageService;
import naem.server.service.MemberService;
import naem.server.service.S3Service;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;
    private final S3Service s3Service;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @ApiOperation(value = "인증된 회원 목록 조회", notes = "인증된 회원 목록 조회")
    @GetMapping("/members/auth")
    public Slice<ProfileResDto> getPermittedMemberList(Long cursor,
        @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {
        return memberService.getMemberList(cursor, new MemberReadCondition(MemberRole.ROLE_USER), pageRequest);
    }

    @ApiOperation(value = "인증되지 않은 회원 목록 조회", notes = "인증되지 않은 회원 목록 조회")
    @GetMapping("/members/noauth")
    public Slice<ProfileResDto> getNotPermittedMemberList(Long cursor,
        @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {
        return memberService.getMemberList(cursor, new MemberReadCondition(MemberRole.ROLE_NOT_PERMITTED), pageRequest);
    }

    @ApiOperation(value = "장애인 인증 요청 확인", notes = "장애인 인증 요청 확인")
    @GetMapping("/disabled/info/{id}")
    public DisabledMemberInfoDto getPost(@PathVariable("id") long disabledMemberInfoId) {
        return memberService.getDisabledMemberInfoDto(disabledMemberInfoId);
    }

    @ApiOperation(value = "장애인 인증 요청 수락", notes = "장애인 인증 요청 수락")
    @PatchMapping("/disabled/approval/{id}")
    public Response grantDisabledReq(@PathVariable("id") long disabledMemberInfoId,
        @RequestBody FcmReqDto fcmReqDto) throws IOException {

        DisabledMemberInfo disabledMemberInfo = memberService.grantDisabledReq(disabledMemberInfoId);
        s3Service.deleteDisabledAuthImages(disabledMemberInfo.getImg()); // S3 이미지 제거

        // 푸시 알림 전송
        firebaseCloudMessageService.sendMessageTo(
            fcmReqDto.getTargetToken(),
            fcmReqDto.getTitle(),
            fcmReqDto.getBody()
        );
        return new Response("OK", "장애인 인증 요청 수락에 성공했습니다");
    }

}
