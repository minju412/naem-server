package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    /**
     * 회원 정보 수정 로직
     */
    @Transactional
    public void patch(long id, PatchMemberDto patchMemberDto) {

        Optional<Member> oMember = memberRepository.findById(id);

        if (oMember.isPresent()) {
            Member member = oMember.get();

            if (StringUtils.isNotBlank(patchMemberDto.getNickname())) {
                member.setNickname(patchMemberDto.getNickname());
            }
            if (StringUtils.isNotBlank(patchMemberDto.getIntroduction())) {
                member.setIntroduction(patchMemberDto.getIntroduction());
            }

            memberRepository.save(member);

        } else {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }
}
