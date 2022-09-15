package naem.server.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.dto.DisabledMemberInfoDto;
import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.ProfileResDto;
import naem.server.service.MemberService;
import naem.server.service.S3Service;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/admin")
@Slf4j
public class AdminController {

    private final MemberService memberService;

    @ApiOperation(value = "인증된 회원 목록 조회", notes = "인증된 회원 목록 조회")
    @GetMapping("/members/auth")
    public Slice<ProfileResDto> getPermittedMemberList(Long cursor, @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {
        return memberService.getMemberList(cursor, new MemberReadCondition(MemberRole.ROLE_USER), pageRequest);
    }

    @ApiOperation(value = "인증되지 않은 회원 목록 조회", notes = "인증되지 않은 회원 목록 조회")
    @GetMapping("/members/noauth")
    public Slice<ProfileResDto> getNotPermittedMemberList(Long cursor, @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {
        return memberService.getMemberList(cursor, new MemberReadCondition(MemberRole.ROLE_NOT_PERMITTED), pageRequest);
    }

    @ApiOperation(value = "장애인 인증 요청 확인", notes = "장애인 인증 요청 확인")
    @GetMapping("/disabled/info/{id}")
    public DisabledMemberInfoDto getPost(@PathVariable("id") long disabledMemberInfoId) {
        return memberService.getDisabledMemberInfoDto(disabledMemberInfoId);
    }

    @ApiOperation(value = "장애인 인증 요청 수락", notes = "장애인 인증 요청 수락")
    @PostMapping("/disabled/approval/{id}")
    public Response grantDisabledReq(@PathVariable("id") long disabledMemberInfoId) {
        memberService.grantDisabledReq(disabledMemberInfoId);
        return new Response("OK", "장애인 인증 요청 수락에 성공했습니다");
    }

}
