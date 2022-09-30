package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Tag;
import naem.server.domain.member.DisabledMemberInfo;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberTag;
import naem.server.domain.member.dto.DisabledMemberInfoDto;
import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.MemberWithdrawDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.domain.member.dto.ProfileResDto;
import naem.server.exception.CustomException;
import naem.server.repository.DisabledMemberInfoRepository;
import naem.server.repository.MemberRepository;
import naem.server.repository.MemberTagRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final DisabledMemberInfoRepository disabledMemberInfoRepository;
    private final MemberRepository memberRepository;
    private final MemberTagRepository memberTagRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * memberId가 로그인한 본인인지 체크
     */
    @Override
    @Transactional
    public Member getLoginMember(UserDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return member;
    }

    /**
     * 내 정보 조회 로직
     */
    @Override
    @Transactional
    public ProfileResDto getMyInfo(UserDetails userDetails) {
        Member member = getLoginMember(userDetails);
        return new ProfileResDto(member);
    }

    /**
     * 회원 정보 수정 로직
     */
    @Override
    @Transactional
    public void patch(PatchMemberDto patchMemberDto, UserDetails userDetails) {

        Member member = getLoginMember(userDetails);
        if (StringUtils.isNotBlank(patchMemberDto.getNickname())) {
            member.setNickname(patchMemberDto.getNickname());
        }
        if (StringUtils.isNotBlank(patchMemberDto.getIntroduction())) {
            member.setIntroduction(patchMemberDto.getIntroduction());
        }

        List<MemberTag> newMemberTags = null;
        List<MemberTag> memberTags = member.getMemberTags();

        // 기존 멤버 태그 제거
        if (!memberTags.isEmpty()) {
            MemberTag.removeMemberTag(memberTags); // 해당 게시글의 MemberTag 목록에서 memberTag 삭제
        }

        // 새로운 멤버 태그 추가
        if (patchMemberDto.getTag() != null) {
            newMemberTags = new ArrayList<>();

            List<Tag> tags = new ArrayList<>(patchMemberDto.getTag());
            MemberTag memberTag = null;
            int tagListSize = 3;

            if (tags.size() > tagListSize) {
                throw new CustomException(TAG_LIST_SIZE_ERROR);
            }

            for (Tag tag : tags) {
                memberTag = MemberTag.createMemberTag(tag);
                newMemberTags.add(memberTag);
            }
        }
        member.updateMemberInfo(patchMemberDto.getNickname(), patchMemberDto.getIntroduction(), newMemberTags);
    }

    /**
     * 회원 탈퇴 로직
     */
    @Override
    public void withdraw(MemberWithdrawDto memberWithdrawDto, UserDetails userDetails) {

        Member member = getLoginMember(userDetails);
        if (!member.matchPassword(passwordEncoder, memberWithdrawDto.getCheckPassword())) {
            throw new CustomException(WRONG_PASSWORD);
        }
        memberRepository.delete(member);
    }

    /**
     * 가입된 회원 목록 조회 로직
     */
    @Override
    @Transactional
    public Slice<ProfileResDto> getMemberList(Long cursor, MemberReadCondition condition, Pageable pageRequest) {
        return memberRepository.getProfileResDtoScroll(cursor, condition, pageRequest);
    }

    /**
     * 장애인 인증 요청 정보 조회 로직
     */
    @Override
    @Transactional
    public DisabledMemberInfoDto getDisabledMemberInfoDto(Long id) {

        DisabledMemberInfo disabledMemberInfo = disabledMemberInfoRepository.findById(id)
            .orElseThrow(() -> new CustomException(DISABLED_MEMBER_INFO_NOT_FOUND));

        return new DisabledMemberInfoDto(disabledMemberInfo);
    }

    /**
     * 장애인 인증 요청 수락 로직
     */
    @Override
    @Transactional
    public DisabledMemberInfo grantDisabledReq(Long disabledMemberInfoId) {

        DisabledMemberInfo disabledMemberInfo = disabledMemberInfoRepository.findById(disabledMemberInfoId)
            .orElseThrow(() -> new CustomException(DISABLED_MEMBER_INFO_NOT_FOUND));
        Member member = memberRepository.findByUsername(disabledMemberInfo.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        // 멤버 타입 확인
        if (!member.getMemberType().toString().equals("IN_PERSON")) {
            throw new CustomException(INVALID_MEMBER_TYPE);
        }
        // 이미 인증된 회원인지 확인
        if (member.getIsAuthorized() == true) {
            throw new CustomException(ALREADY_AUTHORIZED_MEMBER);
        }

        String recommenderCode;
        do {
            recommenderCode = createRecommenderCode();
        } while (memberRepository.existsByRecommenderCode(recommenderCode)); // 추천인 코드 중복 방지

        member.grantDisabledAuthReq(recommenderCode);

        return disabledMemberInfo;
    }

    public String createRecommenderCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        return generatedString;
    }
}
