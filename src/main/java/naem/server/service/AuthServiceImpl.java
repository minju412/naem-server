package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.Salt;
import naem.server.domain.member.dto.ResponseMemberDto;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;
import naem.server.service.util.SaltUtil;
import naem.server.service.util.SecurityUtil;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SaltUtil saltUtil;

    @Override
    @Transactional
    public void signUpMember(Member member) {

        Optional<Member> result = Optional.ofNullable(memberRepository.findByPhoneNumber(member.getPhoneNumber()));
        result.ifPresent(m -> {
            throw new CustomException(DUPLICATE_MEMBER);
        });

        result = Optional.ofNullable(memberRepository.findByUsername(member.getUsername()));
        result.ifPresent(m -> {
            throw new CustomException(CONFLICT_ID);
        });

        result = Optional.ofNullable(memberRepository.findByNickname(member.getNickname()));
        result.ifPresent(m -> {
            throw new CustomException(CONFLICT_NICKNAME);
        });

        String password = member.getPassword();
        String salt = saltUtil.genSalt();

        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt, password));
        memberRepository.save(member);
    }

    @Override
    public Member loginMember(String id, String password) {

        Member member = memberRepository.findByUsername(id);
        if (member == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        if (!member.getPassword().equals(password)) {
            throw new CustomException(WRONG_PASSWORD);
        }

        return member;
    }

    @Override
    @Transactional
    public ResponseMemberDto getMyUserWithAuthorities() {
        return ResponseMemberDto.from(
            SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByUsername).orElse(null));
    }

}
