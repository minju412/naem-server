package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.MemberWithdrawDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.domain.member.dto.ProfileResDto;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 내 정보 조회 로직
     */
    @Override
    @Transactional
    public ProfileResDto getMyInfo(UserDetails userDetails) {

        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return new ProfileResDto(member);
    }

    /**
     * 회원 정보 수정 로직
     */
    @Override
    @Transactional
    public void patch(long memberId, PatchMemberDto patchMemberDto, UserDetails userDetails) {

        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (member.getId() != memberId) {
            throw new CustomException(ACCESS_DENIED);
        }

        if (StringUtils.isNotBlank(patchMemberDto.getNickname())) {
            member.setNickname(patchMemberDto.getNickname());
        }
        if (StringUtils.isNotBlank(patchMemberDto.getIntroduction())) {
            member.setIntroduction(patchMemberDto.getIntroduction());
        }

        memberRepository.save(member);
    }

    /**
     * 회원 탈퇴 로직
     */
    @Override
    public void withdraw(long memberId, MemberWithdrawDto memberWithdrawDto, UserDetails userDetails) {

        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (member.getId() != memberId) {
            throw new CustomException(ACCESS_DENIED);
        }

        if (!member.matchPassword(passwordEncoder, memberWithdrawDto.getCheckPassword())) {
            throw new CustomException(WRONG_PASSWORD);
        }

        memberRepository.delete(member);
    }
}
