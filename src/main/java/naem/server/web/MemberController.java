package naem.server.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.ProfileDto.ProfileRes;
import naem.server.exception.UserNotFoundException;
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
    public ProfileRes profile(@AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        Member userDetail = memberService.findByUsername(userDetails.getUsername())
            .orElseThrow(UserNotFoundException::new);

        return ProfileRes.builder()
            .username(userDetail.getUsername())
            .nickname(userDetail.getNickname())
            .introduction(userDetail.getIntroduction())
            .build();
    }
}
