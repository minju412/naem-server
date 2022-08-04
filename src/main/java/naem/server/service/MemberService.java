package naem.server.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 정보 수정
    @Transactional
    public int patch(long id, PatchMemberDto patchMemberDto) {

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

            return 1;
        }
        return 0;
    }

}
