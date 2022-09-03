package naem.server.web;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.dto.MemberWithdrawDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.domain.member.dto.ProfileResDto;
import naem.server.service.MemberServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/member")
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;

    @ApiOperation(value = "내 정보 조회", notes = "내 정보 조회")
    @GetMapping("/profile")
    public ProfileResDto profile(@AuthenticationPrincipal UserDetails userDetails) {

        return memberService.getMyInfo(userDetails);
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보 수정")
    @PatchMapping()
    public Response memberPatch(@RequestBody PatchMemberDto patchMemberDto, @AuthenticationPrincipal UserDetails userDetails) {

        memberService.patch(patchMemberDto, userDetails);
        return new Response("OK", "정보 수정에 성공했습니다");
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴")
    @DeleteMapping()
    public Response withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto, @AuthenticationPrincipal UserDetails userDetails) {
        memberService.withdraw(memberWithdrawDto, userDetails);
        return new Response("OK", "회원 탈퇴에 성공하였습니다");
    }
}
