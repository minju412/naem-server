package naem.server.web;

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

    // 회원 정보 조회
    @GetMapping("/profile/{username}")
    public ProfileRes userProfile(@PathVariable String username) throws UserNotFoundException {
        Member user = memberService.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        return ProfileRes.builder()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .build();
    }

    // @GetMapping("/members")
    // public List<Member> showUserList() {
    //     return memberService.findAll();
    // }

}
