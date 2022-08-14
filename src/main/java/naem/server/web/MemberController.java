package naem.server.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            .memberType(userDetail.getMemberType().toString())
            .role(userDetail.getRole().toString())
            .phoneNumber(userDetail.getPhoneNumber())
            .username(userDetail.getUsername())
            .nickname(userDetail.getNickname())
            .introduction(userDetail.getIntroduction())
            // .posts(userDetail.getPosts())
            .build();
    }

    // 특정 회원 정보 조회
    // @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/profile/{username}")
    public ProfileRes userProfile(@PathVariable String username) throws UserNotFoundException {
        Member user = memberService.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        return ProfileRes.builder()
            .memberType(user.getMemberType().toString())
            .role(user.getRole().toString())
            .phoneNumber(user.getPhoneNumber())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .introduction(user.getIntroduction())
            .build();
    }

    // @GetMapping("/members")
    // public List<Member> showUserList() {
    //     return memberService.findAll();
    // }

}
