package naem.server.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.ProfileResDto;
import naem.server.service.MemberService;

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
}
