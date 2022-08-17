package naem.server.web;

import javax.validation.Valid;

import static naem.server.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.Member;

import naem.server.domain.member.dto.MemberWithdrawDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.domain.member.dto.ProfileDto.ProfileRes;
import naem.server.exception.CustomException;
import naem.server.service.MemberServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/member")
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;

    // 내 정보 조회
    @GetMapping("/profile")
    public ProfileRes profile(@AuthenticationPrincipal UserDetails userDetails) {

        Optional<Member> oUserDetail = memberService.findByUsername(userDetails.getUsername());
        if (oUserDetail.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member userDetail = oUserDetail.get();

        return ProfileRes.builder()
            .username(userDetail.getUsername())
            .nickname(userDetail.getNickname())
            .introduction(userDetail.getIntroduction())
            .build();
    }

    // 회원 정보 수정
    @PatchMapping("/{id}")
    public Response memberPatch(@PathVariable("id") long id, @RequestBody PatchMemberDto patchMemberDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Member> oUserDetail = memberService.findByUsername(userDetails.getUsername());
        if (oUserDetail.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member userDetail = oUserDetail.get();

        if (userDetail.getId() != id) {
            throw new CustomException(ACCESS_DENIED);
        }

        memberService.patch(id, patchMemberDto);
        return new Response("OK", "정보 수정에 성공했습니다");
    }
    
    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public Response withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.getCheckPassword());
        return new Response("OK", "회원 탈퇴에 성공하였습니다");
    }
}
